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
package com.ultracommerce.common.web;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * By default, we'll resolve the customer from the "customer" attribute on the request.
 * 
 * @author Andre Azzolini (apazzolini)
 */
@Service("ucRequestCustomerResolver")
public class UltraRequestCustomerResolverImpl implements ApplicationContextAware, UltraRequestCustomerResolver {
    
    private static ApplicationContext applicationContext;

    protected static String customerRequestAttributeName = "customer";

    @Override
    public Object getCustomer(HttpServletRequest request) {
        return getCustomer(new ServletWebRequest(request));
    }
    
    @Override
    public Object getCustomer() {
        WebRequest request = UltraRequestContext.getUltraRequestContext().getWebRequest();
        return getCustomer(request);
    }

    @Override
    public Object getCustomer(WebRequest request) {
        return request.getAttribute(getCustomerRequestAttributeName(), WebRequest.SCOPE_REQUEST);
    }

    @Override
    public void setCustomer(Object customer) {
        WebRequest request = UltraRequestContext.getUltraRequestContext().getWebRequest();
        request.setAttribute(getCustomerRequestAttributeName(), customer, WebRequest.SCOPE_REQUEST);
    }

    @Override
    public String getCustomerRequestAttributeName() {
        return customerRequestAttributeName;
    }

    @Override
    public void setCustomerRequestAttributeName(String customerRequestAttributeName) {
        UltraRequestCustomerResolverImpl.customerRequestAttributeName = customerRequestAttributeName;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        UltraRequestCustomerResolverImpl.applicationContext = applicationContext;
    }
    
    public static UltraRequestCustomerResolver getRequestCustomerResolver() {
        return (UltraRequestCustomerResolver) applicationContext.getBean("ucRequestCustomerResolver");
    }
    
}
