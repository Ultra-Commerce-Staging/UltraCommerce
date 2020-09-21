/*
 * #%L
 * UltraCommerce Framework Web
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
package com.ultracommerce.core.web.order.security;

import org.apache.commons.lang.StringUtils;
import com.ultracommerce.common.util.UCRequestUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.web.context.request.ServletWebRequest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Designed to be manually instantiated in client-specific security settings
 * 
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
public class UltraAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    protected static final String SESSION_ATTR = "SFP-ActiveID";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {
        
        String targetUrl = request.getParameter(getTargetUrlParameter());
        if (UCRequestUtils.isOKtoUseSession(new ServletWebRequest(request))) {
            request.getSession().removeAttribute(SESSION_ATTR);
        }
        if (StringUtils.isNotBlank(targetUrl) && targetUrl.contains(":")) {
            getRedirectStrategy().sendRedirect(request, response, getDefaultTargetUrl());
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}
