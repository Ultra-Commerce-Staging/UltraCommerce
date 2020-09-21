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
package com.ultracommerce.common.web.security;

import com.ultracommerce.common.web.controller.UltraControllerUtility;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Component;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Extends the Spring DefaultRedirectStrategy with support for ajax redirects.
 * 
 * Designed for use with SpringSecurity when errors are present.
 * 
 * Tacks on the UC_AJAX_PARAMETER=true to the redirect request if the request is an ajax request.   This will cause the
 * resulting controller (e.g. LoginController) to treat the request as if it is coming from Ajax and 
 * return the related page fragment rather than returning the full view of the page.
 * 
 * @author bpolster
 *
 */
@Component("ucAuthenticationFailureRedirectStrategy")
public class UltraAuthenticationFailureRedirectStrategy implements RedirectStrategy {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
        if (UltraControllerUtility.isAjaxRequest(request)) {
             url = updateUrlForAjax(url);
        }
        redirectStrategy.sendRedirect(request, response, url);
    }

    public String updateUrlForAjax(String url) {
        String ucAjax = UltraControllerUtility.UC_AJAX_PARAMETER;
        if (url != null && url.indexOf("?") > 0) {
            url = url + "&" + ucAjax + "=true";
        } else {
            url = url + "?" + ucAjax + "=true";
        }
        return url;
    }
    
    public RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }

    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }
}
