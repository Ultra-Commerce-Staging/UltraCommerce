/*
 * #%L
 * UltraCommerce CMS Module
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
package com.ultracommerce.cms.web;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.cms.url.domain.URLHandler;
import com.ultracommerce.cms.url.service.URLHandlerService;
import com.ultracommerce.cms.url.type.URLRedirectType;
import com.ultracommerce.common.admin.condition.ConditionalOnNotAdmin;
import com.ultracommerce.common.util.UCSystemProperty;
import com.ultracommerce.common.util.UrlUtil;
import com.ultracommerce.common.web.filter.AbstractIgnorableOncePerRequestFilter;
import com.ultracommerce.common.web.filter.FilterOrdered;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Map;
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
@Component("ucURLHandlerFilter")
@ConditionalOnNotAdmin
public class URLHandlerFilter extends AbstractIgnorableOncePerRequestFilter {

    private static final Log LOG = LogFactory.getLog(URLHandlerFilter.class);

    @Autowired
    @Qualifier("ucURLHandlerService")
    private URLHandlerService urlHandlerService;

    @Autowired
    @Qualifier("ucURLHandlerFilterExtensionManager")
    private URLHandlerFilterExtensionManager extensionManager;

    @Value("${request.uri.encoding}")
    public String charEncoding;

    @Override
    protected void doFilterInternalUnlessIgnored(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String contextPath = request.getContextPath();
        String requestURIWithoutContext;
        if (request.getContextPath() != null) {
            requestURIWithoutContext = request.getRequestURI().substring(request.getContextPath().length());
        } else {
            requestURIWithoutContext = request.getRequestURI();
        }

        requestURIWithoutContext = URLDecoder.decode(requestURIWithoutContext, charEncoding);
        URLHandler handler = urlHandlerService.findURLHandlerByURI(requestURIWithoutContext);
        
        if (handler != null) {
            String url = UrlUtil.fixRedirectUrl(contextPath, handler.getNewURL());
            url = fixQueryString(request, url);
            extensionManager.getProxy().processPreRedirect(request, response, url);
            if (URLRedirectType.FORWARD == handler.getUrlRedirectType()) {
                request.getRequestDispatcher(handler.getNewURL()).forward(request, response);               
            } else if (URLRedirectType.REDIRECT_PERM == handler.getUrlRedirectType()) {
                response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
                response.setHeader( "Location", url);
                response.setHeader( "Connection", "close" );
            } else if (URLRedirectType.REDIRECT_TEMP == handler.getUrlRedirectType()) {
                response.sendRedirect(url);             
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
    
    /**
     * If the url does not include "//" then the system will ensure that the application context
     * is added to the start of the URL.
     * 
     * @param url
     * @return
     * @throws Exception
     */
    protected String fixQueryString(HttpServletRequest request, String url) {
        if (getPreserveQueryStringOnRedirect()) {
            try {
                Map parameterMap = request.getParameterMap();
                if (parameterMap != null && parameterMap.size() > 0) {
                    Set<String> queryParams = getExistingQueryParams(url);

                    String symbol = "?";
                    for (Object keyVal : parameterMap.keySet()) {
                        String key = (String) keyVal;
                        if (!queryParams.contains(key)) {
                            if (url.contains("?")) {
                                symbol = "&";
                            }
                            String param = URLEncoder.encode(key, "UTF-8");
                            String value = URLEncoder.encode(request.getParameter(key), "UTF-8");
                            url = url + symbol + param;
                            if (!StringUtils.isEmpty(value)) {
                                url = url + "=" + value;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                LOG.error("Error adjusting query string in URLHandlerFilter", e);
            }
            return url;
        }

        return url;
    }

    public static Set<String> getExistingQueryParams(String url) throws UnsupportedEncodingException {
        Set<String> query_params = new HashSet<String>();
        int pos = url.indexOf("?");
        if (pos > 0) {
            String query = url.substring(pos);
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                String param="";
                String value = null;
                if (idx > 0) {
                    param = pair.substring(0, idx);
                } else {
                    param=pair;
                }
                query_params.add(param);
            }
        }
        return query_params;
    }

    protected boolean getPreserveQueryStringOnRedirect() {
        return UCSystemProperty.resolveBooleanSystemProperty("preserveQueryStringOnRedirect");
    }

    @Override
    public int getOrder() {
        return FilterOrdered.POST_SECURITY_LOW;
    }
}
