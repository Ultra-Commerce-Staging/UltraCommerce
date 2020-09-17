/*
 * #%L
 * UltraCommerce Common Libraries
 * %%
 * Copyright (C) 2009 - 2017 Ultra Commerce
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.util.UCRequestUtils;
import org.springframework.core.Ordered;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.context.request.ServletWebRequest;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * {@link OpenEntityManagerInViewFilter} implementation that can be "ignored" based on state set by {@link SecurityBasedIgnoreFilter}.
 *
 * @author Jeff Fischer
 */
public class IgnorableOpenEntityManagerInViewFilter extends OpenEntityManagerInViewFilter implements Ordered {

    private static final Log LOG = LogFactory.getLog(IgnorableOpenEntityManagerInViewFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (isIgnored(request, response)) {
            if (LOG.isTraceEnabled()) {
                LOG.trace(String.format("%s filtering is disabled for %s", this.getClass().getName(), request.getRequestURI()));
            }
            filterChain.doFilter(request, response);
        } else {
            if (LOG.isTraceEnabled()) {
                LOG.trace(String.format("%s filtering is enabled for %s", this.getClass().getName(), request.getRequestURI()));
            }
            super.doFilterInternal(request, response, filterChain);
        }
    }

    protected boolean isIgnored(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        boolean isUriSecurityIgnored = UCRequestUtils.isFilteringIgnoredForUri(new ServletWebRequest(httpServletRequest, httpServletResponse));
        return isUriSecurityIgnored;
    }

    @Override
    public int getOrder() {
        return FilterOrdered.PRE_SECURITY_HIGH;
    }
}
