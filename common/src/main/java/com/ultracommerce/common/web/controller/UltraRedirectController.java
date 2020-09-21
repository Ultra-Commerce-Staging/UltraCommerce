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
package com.ultracommerce.common.web.controller;

import com.ultracommerce.common.util.UCRequestUtils;
import org.springframework.ui.Model;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This controller works in conjunction with the ultra-ajax style redirect.
 * 
 * The logic is quite complex but solves a problem related to redirects and
 * an Ajax form.
 * 
 * It is intended to solve a problem with using an Ajax style login modal 
 * along with Spring Security.
 * 
 * Spring Security wants to redirect after a successful login.   Unfortunately,
 * we can reliably redirect from Spring Security to a page within the UC 
 * system when the login modal is presented in Ajax.
 * 
 * To solve this problem, Spring Security can be configured to use 
 * the UltraWindowLocationRedirectStrategy.   That strategy will add an attribute to 
 * session for the page you want to redirect to if the request is coming in
 * from an Ajax call.    It will then cause a redirect that should be picked 
 * up by this controller.   This controller will then render a page with the
 * uc-redirect-div.    The client-side javaScript (UC.js) will intercept
 * this code and force the browser to load the new page (e.g. via window.location)
 * 
 * @see UltraRedirectStrategy
 * 
 * @author bpolster
 */
public class UltraRedirectController {
    
    public String redirect(HttpServletRequest request, HttpServletResponse response, Model model) {
        String path = null;
        if (UCRequestUtils.isOKtoUseSession(new ServletWebRequest(request))) {
            path = (String) request.getSession().getAttribute("UC_REDIRECT_URL");
        }

        if (path == null) {
            path = request.getContextPath();
        }
        return "ajaxredirect:" + path;
    }
}
