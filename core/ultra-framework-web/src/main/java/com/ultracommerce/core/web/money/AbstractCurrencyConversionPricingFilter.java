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
package com.ultracommerce.core.web.money;

import com.ultracommerce.common.money.CurrencyConversionContext;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public abstract class AbstractCurrencyConversionPricingFilter implements CurrencyConversionPricingFilter {
    
    public void destroy() {
        //do nothing
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        CurrencyConversionContext.setCurrencyConversionContext(getCurrencyConversionContext(request));
        CurrencyConversionContext.setCurrencyConversionService(getCurrencyConversionService(request));
        try {
            filterChain.doFilter(request, response);
        } finally {
            CurrencyConversionContext.setCurrencyConversionContext(null);
            CurrencyConversionContext.setCurrencyConversionService(null);
        }
    }

    public void init(FilterConfig arg0) throws ServletException {
        //do nothing
    }
    
}
