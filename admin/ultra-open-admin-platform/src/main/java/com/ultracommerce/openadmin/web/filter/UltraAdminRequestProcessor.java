/*
 * #%L
 * UltraCommerce Open Admin Platform
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
package com.ultracommerce.openadmin.web.filter;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.classloader.release.ThreadLocalManager;
import com.ultracommerce.common.currency.domain.UltraRequestedCurrencyDto;
import com.ultracommerce.common.exception.SiteNotFoundException;
import com.ultracommerce.common.extension.ExtensionManager;
import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.locale.domain.Locale;
import com.ultracommerce.common.sandbox.domain.SandBox;
import com.ultracommerce.common.sandbox.domain.SandBoxType;
import com.ultracommerce.common.sandbox.service.SandBoxService;
import com.ultracommerce.common.security.service.StaleStateProtectionService;
import com.ultracommerce.common.site.domain.Catalog;
import com.ultracommerce.common.site.domain.Site;
import com.ultracommerce.common.site.service.SiteService;
import com.ultracommerce.common.util.UCRequestUtils;
import com.ultracommerce.common.util.DeployBehaviorUtil;
import com.ultracommerce.common.web.AbstractUltraWebRequestProcessor;
import com.ultracommerce.common.web.UltraCurrencyResolver;
import com.ultracommerce.common.web.UltraLocaleResolver;
import com.ultracommerce.common.web.UltraRequestContext;
import com.ultracommerce.common.web.UltraSandBoxResolver;
import com.ultracommerce.common.web.UltraSiteResolver;
import com.ultracommerce.common.web.UltraTimeZoneResolver;
import com.ultracommerce.common.web.DeployBehavior;
import com.ultracommerce.common.web.ValidateProductionChangesState;
import com.ultracommerce.openadmin.server.security.domain.AdminUser;
import com.ultracommerce.openadmin.server.security.remote.SecurityVerifier;
import com.ultracommerce.openadmin.server.security.service.AdminSecurityService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.annotation.Resource;


/**
 * 
 * @author Phillip Verheyden
 * @see {@link com.ultracommerce.common.web.UltraRequestFilter}
 */
@Component("ucAdminRequestProcessor")
public class UltraAdminRequestProcessor extends AbstractUltraWebRequestProcessor {

    public static final String SANDBOX_REQ_PARAM = "ucSandBoxId";
    public static final String PROFILE_REQ_PARAM = "ucProfileId";
    public static final String CATALOG_REQ_PARAM = "ucCatalogId";

    private static final String ADMIN_STRICT_VALIDATE_PRODUCTION_CHANGES_KEY = "admin.strict.validate.production.changes";

    protected final Log LOG = LogFactory.getLog(getClass());

    @Resource(name = "ucSiteResolver")
    protected UltraSiteResolver siteResolver;

    @Resource(name = "messageSource")
    protected MessageSource messageSource;
    
    @Resource(name = "ucLocaleResolver")
    protected UltraLocaleResolver localeResolver;
    
    @Resource(name = "ucAdminTimeZoneResolver")
    protected UltraTimeZoneResolver ultraTimeZoneResolver;

    @Resource(name = "ucCurrencyResolver")
    protected UltraCurrencyResolver currencyResolver;

    @Resource(name = "ucSandBoxService")
    protected SandBoxService sandBoxService;

    @Resource(name = "ucSiteService")
    protected SiteService siteService;

    @Resource(name = "ucAdminSecurityRemoteService")
    protected SecurityVerifier adminRemoteSecurityService;
    
    @Resource(name = "ucAdminSecurityService")
    protected AdminSecurityService adminSecurityService;

    @Resource(name = "ucDeployBehaviorUtil")
    protected DeployBehaviorUtil deployBehaviorUtil;
    
    @Value("${" + ADMIN_STRICT_VALIDATE_PRODUCTION_CHANGES_KEY + ":true}")
    protected boolean adminStrictValidateProductionChanges = true;
    
    @Resource(name="ucEntityExtensionManagers")
    protected Map<String, ExtensionManager<?>> entityExtensionManagers;

    @Resource(name = "ucAdminRequestProcessorExtensionManager")
    protected AdminRequestProcessorExtensionManager extensionManager;

    @Resource(name = "ucStaleStateProtectionService")
    protected StaleStateProtectionService staleStateProtectionService;

    @Override
    public void process(WebRequest request) throws SiteNotFoundException {
        UltraRequestContext brc = UltraRequestContext.getUltraRequestContext();
        if (brc == null) {
            brc = new UltraRequestContext();
            UltraRequestContext.setUltraRequestContext(brc);
        }

        brc.getAdditionalProperties().putAll(entityExtensionManagers);

        if (brc.getSite() == null) {
            Site site = siteResolver.resolveSite(request);
            brc.setSite(site);
        }
        brc.setWebRequest(request);
        brc.setIgnoreSite(brc.getSite() == null);
        brc.setAdmin(true);

        if (adminStrictValidateProductionChanges) {
            brc.setValidateProductionChangesState(ValidateProductionChangesState.ADMIN);
        } else {
            brc.setValidateProductionChangesState(ValidateProductionChangesState.UNDEFINED);
        }
        
        Locale locale = localeResolver.resolveLocale(request);
        brc.setLocale(locale);
        
        brc.setMessageSource(messageSource);
        
        TimeZone timeZone = ultraTimeZoneResolver.resolveTimeZone(request);
        brc.setTimeZone(timeZone);

        // Note: The currencyResolver will set the currency on the UltraRequestContext but 
        // later modules (specifically PriceListRequestProcessor in UC enterprise) may override based
        // on the desired currency.
        UltraRequestedCurrencyDto dto = currencyResolver.resolveCurrency(request);
        if (dto != null) {
            brc.setUltraCurrency(dto.getCurrencyToUse());
            brc.setRequestedUltraCurrency(dto.getRequestedCurrency());
        }

        AdminUser adminUser = adminRemoteSecurityService.getPersistentAdminUser();
        if (adminUser != null) {
            brc.setAdminUserId(adminUser.getId());
        }

        prepareSandBox(request, brc);
        prepareProfile(request, brc);
        prepareCatalog(request, brc);

        brc.getAdditionalProperties().put(staleStateProtectionService.getStateVersionTokenParameter(), staleStateProtectionService.getStateVersionToken());
    }

    protected void prepareProfile(WebRequest request, UltraRequestContext brc) {
        AdminUser adminUser = adminRemoteSecurityService.getPersistentAdminUser();
        if (adminUser == null) {
            //clear any profile
            if (UCRequestUtils.isOKtoUseSession(request)) {
                request.removeAttribute(PROFILE_REQ_PARAM, WebRequest.SCOPE_SESSION);
            }
        } else {
            Site profile = null;
            if (StringUtils.isNotBlank(request.getParameter(PROFILE_REQ_PARAM))) {
                Long profileId = Long.parseLong(request.getParameter(PROFILE_REQ_PARAM));
                profile = siteService.retrievePersistentSiteById(profileId);
                if (profile == null) {
                    throw new IllegalArgumentException(String.format("Unable to find the requested profile: %s", profileId));
                }
                String token = request.getParameter(staleStateProtectionService.getStateVersionTokenParameter());
                staleStateProtectionService.compareToken(token);
                staleStateProtectionService.invalidateState(true);
            }

            if (profile == null) {
                Long previouslySetProfileId = null;
                if (UCRequestUtils.isOKtoUseSession(request)) {
                    previouslySetProfileId = (Long) request.getAttribute(PROFILE_REQ_PARAM,
                        WebRequest.SCOPE_SESSION);
                }
                if (previouslySetProfileId != null) {
                    profile = siteService.retrievePersistentSiteById(previouslySetProfileId);
                }
            }

            if (profile == null) {
                List<Site> profiles = new ArrayList<Site>();
                if (brc.getNonPersistentSite() != null) {
                    Site currentSite = siteService.retrievePersistentSiteById(brc.getNonPersistentSite().getId());
                    if (extensionManager != null) {
                        ExtensionResultHolder<Set<Site>> profilesResult = new ExtensionResultHolder<Set<Site>>();
                        extensionManager.retrieveProfiles(currentSite, profilesResult);
                        if (!CollectionUtils.isEmpty(profilesResult.getResult())) {
                            profiles.addAll(profilesResult.getResult());
                        }
                    }
                }
                if (profiles.size() > 0) {
                    profile = profiles.get(0);
                }
            }

            if (profile != null) {
                if (UCRequestUtils.isOKtoUseSession(request)) {
                    request.setAttribute(PROFILE_REQ_PARAM, profile.getId(), WebRequest.SCOPE_SESSION);
                }
                brc.setCurrentProfile(profile);
            }
        }
    }

    protected void prepareCatalog(WebRequest request, UltraRequestContext brc) {
        AdminUser adminUser = adminRemoteSecurityService.getPersistentAdminUser();
        if (adminUser == null) {
            //clear any catalog
            if (UCRequestUtils.isOKtoUseSession(request)) {
                request.removeAttribute(CATALOG_REQ_PARAM, WebRequest.SCOPE_SESSION);
            }
        } else {
            Catalog catalog = null;
            if (StringUtils.isNotBlank(request.getParameter(CATALOG_REQ_PARAM))) {
                Long catalogId = Long.parseLong(request.getParameter(CATALOG_REQ_PARAM));
                catalog = siteService.findCatalogById(catalogId);
                if (catalog == null) {
                    throw new IllegalArgumentException(String.format("Unable to find the requested catalog: %s", catalogId));
                }
                String token = request.getParameter(staleStateProtectionService.getStateVersionTokenParameter());
                staleStateProtectionService.compareToken(token);
                staleStateProtectionService.invalidateState(true);
            }

            if (catalog == null) {
                Long previouslySetCatalogId = null;
                if (UCRequestUtils.isOKtoUseSession(request)) {
                    previouslySetCatalogId = (Long) request.getAttribute(CATALOG_REQ_PARAM,
                        WebRequest.SCOPE_SESSION);
                }
                if (previouslySetCatalogId != null) {
                    catalog = siteService.findCatalogById(previouslySetCatalogId);
                }
            }

            if (catalog == null) {
                List<Catalog> catalogs = new ArrayList<Catalog>();
                if (brc.getNonPersistentSite() != null) {
                    Site currentSite = siteService.retrievePersistentSiteById(brc.getNonPersistentSite().getId());
                    if (extensionManager != null) {
                        ExtensionResultHolder<Set<Catalog>> catalogResult = new ExtensionResultHolder<Set<Catalog>>();
                        extensionManager.retrieveCatalogs(currentSite, catalogResult);
                        if (!CollectionUtils.isEmpty(catalogResult.getResult())) {
                            catalogs.addAll(catalogResult.getResult());
                        }
                    }
                }
                if (catalogs.size() > 0) {
                    catalog = catalogs.get(0);
                }
            }

            if (catalog != null) {
                if (UCRequestUtils.isOKtoUseSession(request)) {
                    request.setAttribute(CATALOG_REQ_PARAM, catalog.getId(), WebRequest.SCOPE_SESSION);
                }
                brc.setCurrentCatalog(catalog);
            }
            if (extensionManager != null) {
                if (brc.getNonPersistentSite() != null) {
                    Site currentSite = siteService.retrievePersistentSiteById(brc.getNonPersistentSite().getId());
                    ExtensionResultHolder<Catalog> catalogResult = new ExtensionResultHolder<Catalog>();
                    extensionManager.overrideCurrentCatalog(request, currentSite, catalogResult);
                    if (catalogResult.getResult() != null) {
                        brc.setCurrentCatalog(catalogResult.getResult());
                    }

                    ExtensionResultHolder<Site> profileResult = new ExtensionResultHolder<Site>();
                    extensionManager.overrideCurrentProfile(request, currentSite, profileResult);
                    if (profileResult.getResult() != null) {
                        brc.setCurrentProfile(profileResult.getResult());
                    }
                }
            }
        }
    }

    protected void prepareSandBox(WebRequest request, UltraRequestContext brc) {
        AdminUser adminUser = adminRemoteSecurityService.getPersistentAdminUser();
        if (adminUser == null) {
            //clear any sandbox
            if (UCRequestUtils.isOKtoUseSession(request)) {
                request.removeAttribute(UltraSandBoxResolver.SANDBOX_ID_VAR, WebRequest.SCOPE_SESSION);
            }
        } else {
            SandBox sandBox = null;
            if (StringUtils.isNotBlank(request.getParameter(SANDBOX_REQ_PARAM))) {
                Long sandBoxId = Long.parseLong(request.getParameter(SANDBOX_REQ_PARAM));
                sandBox = sandBoxService.retrieveUserSandBoxForParent(adminUser.getId(), sandBoxId);
                if (sandBox == null) {
                    SandBox approvalOrUserSandBox = sandBoxService.retrieveSandBoxManagementById(sandBoxId);
                    if (approvalOrUserSandBox != null) {
                        if (approvalOrUserSandBox.getSandBoxType().equals(SandBoxType.USER)) {
                            sandBox = approvalOrUserSandBox;
                        } else {
                            sandBox = sandBoxService.createUserSandBox(adminUser.getId(), approvalOrUserSandBox);
                        }
                    }
                }
                if (UCRequestUtils.isOKtoUseSession(request)) {
                    String token = request.getParameter(staleStateProtectionService.getStateVersionTokenParameter());
                    staleStateProtectionService.compareToken(token);
                    staleStateProtectionService.invalidateState(true);
                }
            }

            if (sandBox == null) {
                Long previouslySetSandBoxId = null;
                if (UCRequestUtils.isOKtoUseSession(request)) {
                    previouslySetSandBoxId = (Long) request.getAttribute(UltraSandBoxResolver.SANDBOX_ID_VAR,
                        WebRequest.SCOPE_SESSION);
                }
                if (previouslySetSandBoxId != null) {
                    sandBox = sandBoxService.retrieveSandBoxManagementById(previouslySetSandBoxId);
                }
            }

            if (sandBox == null) {
                List<SandBox> defaultSandBoxes = sandBoxService.retrieveSandBoxesByType(SandBoxType.DEFAULT);
                if (defaultSandBoxes.size() > 1) {
                    throw new IllegalStateException("Only one sandbox should be configured as default");
                }

                SandBox defaultSandBox;
                if (defaultSandBoxes.size() == 1) {
                    defaultSandBox = defaultSandBoxes.get(0);
                } else {
                    defaultSandBox = sandBoxService.createDefaultSandBox();
                }

                sandBox = sandBoxService.retrieveUserSandBoxForParent(adminUser.getId(), defaultSandBox.getId());
                if (sandBox == null) {
                    sandBox = sandBoxService.createUserSandBox(adminUser.getId(), defaultSandBox);
                }
            }

            // If the user just changed sandboxes, we want to update the database record.
            Long previouslySetSandBoxId = null;
            if (UCRequestUtils.isOKtoUseSession(request)) {
                previouslySetSandBoxId = (Long) request.getAttribute(UltraSandBoxResolver.SANDBOX_ID_VAR, WebRequest.SCOPE_SESSION);
            }
            if (previouslySetSandBoxId != null && !sandBox.getId().equals(previouslySetSandBoxId)) {
                adminUser.setLastUsedSandBoxId(sandBox.getId());
                adminUser = adminSecurityService.saveAdminUser(adminUser);
            }

            if (UCRequestUtils.isOKtoUseSession(request)) {
                request.setAttribute(UltraSandBoxResolver.SANDBOX_ID_VAR, sandBox.getId(), WebRequest.SCOPE_SESSION);
            }
            //We do this to prevent lazy init exceptions when this context/sandbox combination
            // is used in a different session that it was initiated in. see QA#2576
            if(sandBox != null && sandBox.getChildSandBoxes() != null) {
                sandBox.getChildSandBoxes().size();
            }
            brc.setSandBox(sandBox);
            brc.setDeployBehavior(deployBehaviorUtil.isProductionSandBoxMode() ? DeployBehavior.CLONE_PARENT : DeployBehavior.OVERWRITE_PARENT);
            brc.getAdditionalProperties().put("adminUser", adminUser);
        }
    }

    @Override
    public void postProcess(WebRequest request) {
        ThreadLocalManager.remove();
    }

}
