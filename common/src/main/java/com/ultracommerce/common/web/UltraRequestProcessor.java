/*
 * #%L
 * UltraCommerce Common Libraries
 * %%
 * Copyright (C) 2009 - 2016 Ultra Commerce
 * %%
 * Licensed under the Ultra Fair Use License Agreement, Version 1.0
 * (the "Fair Use License" located  at http://license.ultracommerce.org/fair_use_license-1.0.txt)
 * unless the restrictions on use therein are violated and require payment to Ultra in which case
 * the Ultra End User License Agreement (EULA), Version 1.1
 * (the "Commercial License" located at http://license.ultracommerce.org/commercial_license-1.1.txt)
 * shall apply.
 * 
 * Alternatively, the Commercial License may be replaced with a mutually agreed upon license (the "Custom License")
 * between you and Ultra Commerce. You may not use this file except in compliance with the applicable license.
 * #L%
 */
package com.ultracommerce.common.web;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.RequestDTO;
import com.ultracommerce.common.RequestDTOImpl;
import com.ultracommerce.common.classloader.release.ThreadLocalManager;
import com.ultracommerce.common.currency.domain.UltraRequestedCurrencyDto;
import com.ultracommerce.common.extension.ExtensionManager;
import com.ultracommerce.common.locale.domain.Locale;
import com.ultracommerce.common.sandbox.domain.SandBox;
import com.ultracommerce.common.sandbox.service.SandBoxService;
import com.ultracommerce.common.site.domain.Site;
import com.ultracommerce.common.site.domain.Theme;
import com.ultracommerce.common.util.UCRequestUtils;
import com.ultracommerce.common.util.DeployBehaviorUtil;
import com.ultracommerce.common.util.StringUtil;
import com.ultracommerce.common.web.exception.HaltFilterChainException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 
 * @author Phillip Verheyden
 * @see {@link UltraRequestFilter}
 */
@Component("ucRequestProcessor")
public class UltraRequestProcessor extends AbstractUltraWebRequestProcessor {

    protected final Log LOG = LogFactory.getLog(getClass());

    private static String REQUEST_DTO_PARAM_NAME = UltraRequestFilter.REQUEST_DTO_PARAM_NAME;
    public static String REPROCESS_PARAM_NAME = "REPROCESS_UC_REQUEST";
    
    private static final String SITE_STRICT_VALIDATE_PRODUCTION_CHANGES_KEY = "site.strict.validate.production.changes";
    public static final String SITE_DISABLE_SANDBOX_PREVIEW = "site.disable.sandbox.preview";

    private static final String SANDBOX_ID_PARAM = "ucSandboxId";

    @Resource(name = "ucSiteResolver")
    protected UltraSiteResolver siteResolver;

    @Resource(name = "ucLocaleResolver")
    protected UltraLocaleResolver localeResolver;

    @Resource(name = "ucCurrencyResolver")
    protected UltraCurrencyResolver currencyResolver;

    @Resource(name = "ucSandBoxResolver")
    protected UltraSandBoxResolver sandboxResolver;

    @Resource(name = "ucThemeResolver")
    protected UltraThemeResolver themeResolver;

    @Resource(name = "messageSource")
    protected MessageSource messageSource;

    @Resource(name = "ucTimeZoneResolver")
    protected UltraTimeZoneResolver ultraTimeZoneResolver;

    @Resource(name = "ucBaseUrlResolver")
    protected BaseUrlResolver baseUrlResolver;

    @Resource(name = "ucSandBoxService")
    protected SandBoxService sandBoxService;
    
    @Value("${thymeleaf.threadLocalCleanup.enabled}")
    protected boolean thymeleafThreadLocalCleanupEnabled = true;

    @Value("${" + SITE_STRICT_VALIDATE_PRODUCTION_CHANGES_KEY + ":false}")
    protected boolean siteStrictValidateProductionChanges = false;

    @Value("${" + SITE_DISABLE_SANDBOX_PREVIEW + ":false}")
    protected boolean siteDisableSandboxPreview = false;

    @Resource(name = "ucDeployBehaviorUtil")
    protected DeployBehaviorUtil deployBehaviorUtil;
    
    @Resource(name="ucEntityExtensionManagers")
    protected Map<String, ExtensionManager> entityExtensionManagers;
    
    @Override
    public void process(WebRequest request) {
        UltraRequestContext brc = new UltraRequestContext();
        brc.getAdditionalProperties().putAll(entityExtensionManagers);
        
        Site site = siteResolver.resolveSite(request);
        
        brc.setNonPersistentSite(site);
        brc.setWebRequest(request);
        if (site == null) {
            brc.setIgnoreSite(true);
        }
        brc.setAdmin(false);

        if (siteStrictValidateProductionChanges) {
            brc.setValidateProductionChangesState(ValidateProductionChangesState.SITE);
        } else {
            brc.setValidateProductionChangesState(ValidateProductionChangesState.UNDEFINED);
        }

        UltraRequestContext.setUltraRequestContext(brc);

        Locale locale = localeResolver.resolveLocale(request);
        brc.setLocale(locale);
        TimeZone timeZone = ultraTimeZoneResolver.resolveTimeZone(request);
        UltraRequestedCurrencyDto currencyDto = currencyResolver.resolveCurrency(request);
        // Assumes UltraProcess
        RequestDTO requestDTO = (RequestDTO) request.getAttribute(REQUEST_DTO_PARAM_NAME, WebRequest.SCOPE_REQUEST);
        if (requestDTO == null) {
            requestDTO = new RequestDTOImpl(request);
        }

        SandBox currentSandbox = sandboxResolver.resolveSandBox(request, site);
        
        // When a user elects to switch his sandbox, we want to invalidate the current session. We'll then redirect the 
        // user to the current URL so that the configured filters trigger again appropriately.
        Boolean reprocessRequest = (Boolean) request.getAttribute(UltraRequestProcessor.REPROCESS_PARAM_NAME, WebRequest.SCOPE_REQUEST);
        if (reprocessRequest != null && reprocessRequest) {
            LOG.debug("Reprocessing request");
            if (request instanceof ServletWebRequest) {
                HttpServletRequest hsr = ((ServletWebRequest) request).getRequest();
                
                clearUltraSessionAttrs(request);
                
                StringBuffer url = hsr.getRequestURL();
                HttpServletResponse response = ((ServletWebRequest) request).getResponse();

                try {
                    if (!isUrlValid(url.toString())) {
                        LOG.error("SECURITY FAILURE Bad redirect location: " + StringUtil.sanitize(url.toString()));
                        response.sendError(403);
                        return;
                    }

                    String sandboxId = hsr.getParameter(SANDBOX_ID_PARAM);

                    if (isSandboxIdValid(sandboxId)) {
                        String queryString = "?" + SANDBOX_ID_PARAM + "=" + sandboxId;
                        url.append(queryString);
                    }

                    response.sendRedirect(url.toString());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                throw new HaltFilterChainException("Reprocess required, redirecting user");
            }
        }
        
        
        if (!siteDisableSandboxPreview && currentSandbox != null) {
            SandBoxContext previewSandBoxContext = new SandBoxContext();
            previewSandBoxContext.setSandBoxId(currentSandbox.getId());
            previewSandBoxContext.setPreviewMode(true);
            SandBoxContext.setSandBoxContext(previewSandBoxContext);
        }
        if (currencyDto != null) {
            brc.setUltraCurrency(currencyDto.getCurrencyToUse());
            brc.setRequestedUltraCurrency(currencyDto.getRequestedCurrency());
        }
        //We do this to prevent lazy init exceptions when this context/sandbox combination
        // is used in a different session that it was initiated in. see QA#2576
        if(currentSandbox != null && currentSandbox.getChildSandBoxes() != null) {
            currentSandbox.getChildSandBoxes().size();
        }

        brc.setSandBox(currentSandbox);
        brc.setDeployBehavior(deployBehaviorUtil.isProductionSandBoxMode() ? DeployBehavior.CLONE_PARENT : DeployBehavior.OVERWRITE_PARENT);
        brc.setRequestDTO(requestDTO);

        // Note that this must happen after the request context is set up as resolving a theme is dependent on site
        Theme theme = themeResolver.resolveTheme(request);
        brc.setTheme(theme);

        brc.setMessageSource(messageSource);
        brc.setTimeZone(timeZone);
        Map<String, Object> ruleMap = (Map<String, Object>) request.getAttribute("ucRuleMap", WebRequest.SCOPE_REQUEST);
        if (ruleMap == null) {
            LOG.trace("Creating ruleMap and adding in Locale.");
            ruleMap = new HashMap<String, Object>();
            request.setAttribute("ucRuleMap", ruleMap, WebRequest.SCOPE_REQUEST);
        } else {
            LOG.trace("Using pre-existing ruleMap - added by non standard UC process.");
        }
        ruleMap.put("locale", locale);

        String adminUserId = request.getParameter(UltraRequestFilter.ADMIN_USER_ID_PARAM_NAME);
        if (StringUtils.isNotBlank(adminUserId)) {
            //TODO: Add token logic to secure the admin user id
            brc.setAdminUserId(Long.parseLong(adminUserId));
        }

    }

    protected boolean isUrlValid(String url) {
        boolean isValid = false;
        String siteBaseUrl = baseUrlResolver.getSiteBaseUrl() + "/";

        if (StringUtils.equals(url, siteBaseUrl)) {
            isValid = true;
        }

        return isValid;
    }

    protected boolean isSandboxIdValid(String sandboxId) {
        boolean isValid = false;

        if (StringUtils.isNotEmpty(sandboxId)) {
            Long id = Long.valueOf(sandboxId);

            SandBox sandbox = sandBoxService.retrieveSandBoxById(id);

            if (sandbox != null) {
                isValid = true;
            }
        }

        return isValid;
    }

    @Override
    public void postProcess(WebRequest request) {
        ThreadLocalManager.remove();
    }
    
    protected void clearUltraSessionAttrs(WebRequest request) {
        if (UCRequestUtils.isOKtoUseSession(request)) {
            request.removeAttribute(UltraLocaleResolverImpl.LOCALE_VAR, WebRequest.SCOPE_SESSION);
            request.removeAttribute(UltraCurrencyResolverImpl.CURRENCY_VAR, WebRequest.SCOPE_SESSION);
            request.removeAttribute(UltraTimeZoneResolverImpl.TIMEZONE_VAR, WebRequest.SCOPE_SESSION);
            request.removeAttribute(UltraSandBoxResolver.SANDBOX_ID_VAR, WebRequest.SCOPE_SESSION);

            // From CustomerStateRequestProcessorImpl, using explicit String because it's out of module
            request.removeAttribute("_uc_anonymousCustomer", WebRequest.SCOPE_SESSION);
            request.removeAttribute("_uc_anonymousCustomerId", WebRequest.SCOPE_SESSION);
        }
    }
}
