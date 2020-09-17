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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.RequestDTOImpl;
import com.ultracommerce.common.admin.condition.ConditionalOnNotAdmin;
import com.ultracommerce.common.exception.SiteNotFoundException;
import com.ultracommerce.common.module.UltraModuleRegistration.UltraModuleEnum;
import com.ultracommerce.common.module.ModulePresentUtil;
import com.ultracommerce.common.util.UCRequestUtils;
import com.ultracommerce.common.web.exception.HaltFilterChainException;
import com.ultracommerce.common.web.filter.AbstractIgnorableOncePerRequestFilter;
import com.ultracommerce.common.web.filter.FilterOrdered;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Responsible for setting up the site and locale used by Ultra Commerce components.
 * 
 * @author bpolster
 */
@Component("ucRequestFilter")
@ConditionalOnNotAdmin
public class UltraRequestFilter extends AbstractIgnorableOncePerRequestFilter {

    private final Log LOG = LogFactory.getLog(getClass());

    /**
     * Parameter/Attribute name for the current language
     */
    public static String REQUEST_DTO_PARAM_NAME = "ucRequestDTO";

    public static final String ADMIN_USER_ID_PARAM_NAME = "ucAdminUserId";

    // Properties to manage URLs that will not be processed by this filter.
    private static final String UC_ADMIN_GWT = "com.ultracommerce.admin";
    private static final String UC_ADMIN_PREFIX = "ucadmin";
    private static final String UC_ADMIN_SERVICE = ".service";

    private Set<String> ignoreSuffixes;

    @Autowired
    @Qualifier("ucRequestProcessor")
    protected UltraRequestProcessor requestProcessor;

    @Override
    protected void doFilterInternalUnlessIgnored(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        if (!shouldProcessURL(request, request.getRequestURI())) {
            if (LOG.isTraceEnabled()) {
                LOG.trace(String.format("%s not processing URL %s", getClass().getName(), request.getRequestURI()));
            }
            filterChain.doFilter(request, response);
            return;
        }
        
        if (LOG.isTraceEnabled()) {
            String requestURIWithoutContext;

            if (request.getContextPath() != null) {
                requestURIWithoutContext = request.getRequestURI().substring(request.getContextPath().length());
            } else {
                requestURIWithoutContext = request.getRequestURI();
            }

            // Remove JSESSION-ID or other modifiers
            int pos = requestURIWithoutContext.indexOf(";");
            if (pos >= 0) {
                requestURIWithoutContext = requestURIWithoutContext.substring(0, pos);
            }

            LOG.trace("Process URL Filter Begin " + requestURIWithoutContext);
        }

        if (request.getAttribute(REQUEST_DTO_PARAM_NAME) == null) {
            request.setAttribute(REQUEST_DTO_PARAM_NAME, new RequestDTOImpl(request));
        }

        try {
            requestProcessor.process(new ServletWebRequest(request, response));
            filterChain.doFilter(request, response);
        } catch (HaltFilterChainException e) {
            return;
        } catch (SiteNotFoundException e) {
            LOG.warn("Could not resolve a site for the given request, returning not found");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } finally {
            requestProcessor.postProcess(new ServletWebRequest(request, response));
        }
    }

    /**
     * Determines if the passed in URL should be processed by the content management system.
     * <p/>
     * By default, this method returns false for any UC-Admin URLs and service calls and for all common image/digital mime-types (as determined by an internal call to {@code getIgnoreSuffixes}.
     * <p/>
     * This check is called with the {@code doFilterInternal} method to short-circuit the content processing which can be expensive for requests that do not require it.
     * 
     * @param requestURI
     *            - the HttpServletRequest.getRequestURI
     * @return true if the {@code HttpServletRequest} should be processed
     */
    protected boolean shouldProcessURL(HttpServletRequest request, String requestURI) {
        return shouldProcessURL(request, requestURI, false);
    }

    protected boolean shouldProcessURL(HttpServletRequest request, String requestURI, boolean ignoreSessionCheck) {
        if (requestURI.contains(UC_ADMIN_GWT) || requestURI.endsWith(UC_ADMIN_SERVICE) || requestURI.contains(UC_ADMIN_PREFIX)) {
            return false;
        } else if (!ignoreSessionCheck && UCRequestUtils.isOKtoUseSession(new ServletWebRequest(request))
            && ModulePresentUtil.isPresent(UltraModuleEnum.ENTERPRISE)) {
            //if session usage is enabled and enterprise is in play - disable to allow the enterprise request filters
            //if session usage is disallowed - allow this filter regardless of enterprise (i.e. rest api)
            return false;
        }
        return true;
    }

    /**
     * Returns a set of suffixes that can be ignored by content processing. The following are returned:
     * <p/>
     * <B>List of suffixes ignored:</B>
     * 
     * ".aif", ".aiff", ".asf", ".avi", ".bin", ".bmp", ".doc", ".eps", ".gif", ".hqx", ".jpg", ".jpeg", ".mid", ".midi", ".mov", ".mp3", ".mpg", ".mpeg", ".p65", ".pdf", ".pic", ".pict", ".png", ".ppt", ".psd", ".qxd", ".ram", ".ra", ".rm", ".sea", ".sit", ".stk", ".swf", ".tif", ".tiff", ".txt", ".rtf", ".vob", ".wav", ".wmf", ".xls", ".zip";
     * 
     * @return set of suffixes to ignore.
     */
    protected Set getIgnoreSuffixes() {
        if (ignoreSuffixes == null || ignoreSuffixes.isEmpty()) {
            String[] ignoreSuffixList = { ".aif", ".aiff", ".asf", ".avi", ".bin", ".bmp", ".css", ".doc", ".eps", ".gif", ".hqx", ".js", ".jpg", ".jpeg", ".mid", ".midi", ".mov", ".mp3", ".mpg", ".mpeg", ".p65", ".pdf", ".pic", ".pict", ".png", ".ppt", ".psd", ".qxd", ".ram", ".ra", ".rm", ".sea", ".sit", ".stk", ".swf", ".tif", ".tiff", ".txt", ".rtf", ".vob", ".wav", ".wmf", ".xls", ".zip" };
            ignoreSuffixes = new HashSet<>(Arrays.asList(ignoreSuffixList));
        }
        return ignoreSuffixes;
    }

    @Override
    protected boolean shouldNotFilterErrorDispatch() {
        return false;
    }

    @Override
    public int getOrder() {
        return FilterOrdered.PRE_SECURITY_LOW;
    }
}
