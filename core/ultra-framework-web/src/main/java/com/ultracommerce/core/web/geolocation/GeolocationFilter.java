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
package com.ultracommerce.core.web.geolocation;

import com.ultracommerce.common.admin.condition.ConditionalOnNotAdmin;
import com.ultracommerce.common.web.filter.AbstractIgnorableOncePerRequestFilter;
import com.ultracommerce.common.web.filter.FilterOrdered;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component("ucGeolocationFilter")
@ConditionalOnNotAdmin
public class GeolocationFilter extends AbstractIgnorableOncePerRequestFilter {

    @Autowired
    @Qualifier("ucGeolocationRequestProcessor")
    protected GeolocationRequestProcessor geolocationRequestProcessor;

    @Override
    protected void doFilterInternalUnlessIgnored(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        ServletWebRequest request = new ServletWebRequest(httpServletRequest, httpServletResponse);
        try {
            geolocationRequestProcessor.process(request);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } finally {
            geolocationRequestProcessor.postProcess(request);
        }
    }

    @Override
    public int getOrder() {
        return FilterOrdered.POST_SECURITY_LOW;
    }
}

