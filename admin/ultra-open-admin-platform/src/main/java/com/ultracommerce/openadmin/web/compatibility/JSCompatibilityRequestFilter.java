/*
 * #%L
 * UltraCommerce Open Admin Platform
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
package com.ultracommerce.openadmin.web.compatibility;

import com.ultracommerce.common.web.filter.AbstractIgnorableOncePerRequestFilter;
import com.ultracommerce.common.web.filter.FilterOrdered;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Jeff Fischer
 */
@Component("ucJSCompatibilityRequestFilter")
public class JSCompatibilityRequestFilter extends AbstractIgnorableOncePerRequestFilter {

    @Override
    protected void doFilterInternalUnlessIgnored(HttpServletRequest request, HttpServletResponse response, FilterChain
            filterChain) throws ServletException, IOException {
        filterChain.doFilter(new JSCompatibilityRequestWrapper(request), response);
    }

    @Override
    public int getOrder() {
        return FilterOrdered.POST_SECURITY_LOW;
    }
}
