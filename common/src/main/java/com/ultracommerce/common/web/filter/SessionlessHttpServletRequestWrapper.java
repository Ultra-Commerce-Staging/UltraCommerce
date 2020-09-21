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
package com.ultracommerce.common.web.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;


public class SessionlessHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public SessionlessHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }
    
    @Override
    public String getRequestedSessionId() {
        return null;
    }

    @Override
    public HttpSession getSession(boolean create) {
        if (!create) {
            return null;
        }
        throw new UnsupportedOperationException("You are in a sessionless environment and cannot get/create a HttpSession.");
    }

    @Override
    public HttpSession getSession() {
        throw new UnsupportedOperationException("You are in a sessionless environment and cannot get/create a HttpSession.");
    }

}
