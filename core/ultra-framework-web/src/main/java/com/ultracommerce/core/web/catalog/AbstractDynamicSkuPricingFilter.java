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
package com.ultracommerce.core.web.catalog;

import com.ultracommerce.core.catalog.service.dynamic.SkuPricingConsiderationContext;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * 
 * @author jfischer
 * @see {@link DefaultDynamicSkuPricingFilter}
 */
public abstract class AbstractDynamicSkuPricingFilter implements DynamicSkuPricingFilter {

    public void destroy() {
        //do nothing
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        SkuPricingConsiderationContext.setSkuPricingConsiderationContext(getPricingConsiderations(request));
        SkuPricingConsiderationContext.setSkuPricingService(getDynamicSkuPricingService(request));
        filterChain.doFilter(request, response);
    }

    public void init(FilterConfig config) throws ServletException {
        //do nothing
    }

}
