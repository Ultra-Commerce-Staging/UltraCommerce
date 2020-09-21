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

package com.ultracommerce.site.common.web.filter;

import com.ultracommerce.common.util.UCRequestUtils;
import com.ultracommerce.common.web.filter.AbstractIgnorableFilter;
import com.ultracommerce.common.web.filter.FilterOrdered;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component("ucEstablishSessionFilter")
public class EstablishSessionFilter extends AbstractIgnorableFilter {

    @Override
    public void doFilterUnlessIgnored(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if (HttpServletRequest.class.isAssignableFrom(request.getClass())) {
            ((HttpServletRequest) request).getSession();
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean isIgnored(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        boolean response = super.isIgnored(httpServletRequest, httpServletResponse);
        if (!response) {
            //ignore for stateless requests (i.e. rest api)
            response = !UCRequestUtils.isOKtoUseSession(new ServletWebRequest(httpServletRequest));
        }
        return response;
    }

    @Override
    public int getOrder() {
        return FilterOrdered.PRE_SECURITY_LOW;
    }
}
