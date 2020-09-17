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
package com.ultracommerce.api.common.web.filter;

import com.ultracommerce.common.util.UCRequestUtils;
import com.ultracommerce.common.web.filter.AbstractIgnorableFilter;
import com.ultracommerce.common.web.filter.FilterOrdered;
import com.ultracommerce.common.web.filter.SessionlessHttpServletRequestWrapper;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Sets a request attribute that informs all Ultra Filters that follow NOT to use the HTTP Session.
 * 
 * Intended for use by REST api requests.
 * 
 * @author bpolster
 */
@Component("ucStatelessSessionFilter")
public class StatelessSessionFilter extends AbstractIgnorableFilter {

    @Override
    public void doFilterUnlessIgnored(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        UCRequestUtils.setOKtoUseSession(new ServletWebRequest((HttpServletRequest) request, (HttpServletResponse) response), Boolean.FALSE);
        SessionlessHttpServletRequestWrapper wrapper = new SessionlessHttpServletRequestWrapper((HttpServletRequest) request);
        filterChain.doFilter(wrapper, response);
    }

    @Override
    public int getOrder() {
        return FilterOrdered.PRE_SECURITY_HIGH;
    }
}
