/*
 * #%L
 * UltraCommerce Profile Web
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
package com.ultracommerce.profile.web.core;

import com.ultracommerce.common.web.UltraRequestContext;
import com.ultracommerce.common.web.UltraRequestCustomerResolverImpl;
import com.ultracommerce.profile.core.domain.Customer;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * Convenient class to get the active customer from the current request. This state is kept up-to-date in regards to the database
 * throughout the lifetime of the request via the {@link CustomerStateRefresher}.
 *
 * @author Jeff Fischer
 * @author Phillip Verheyden (phillipuniverse)
 */
@Component("ucCustomerState")
public class CustomerState {
    
    public static Customer getCustomer(HttpServletRequest request) {
        return (Customer) UltraRequestCustomerResolverImpl.getRequestCustomerResolver().getCustomer(request);
    }
    
    public static Customer getCustomer(WebRequest request) {
        return (Customer) UltraRequestCustomerResolverImpl.getRequestCustomerResolver().getCustomer(request);
    }
    
    public static Customer getCustomer() {
        if (UltraRequestContext.getUltraRequestContext() == null
                || UltraRequestContext.getUltraRequestContext().getWebRequest() == null) {
            return null;
        }
        return (Customer) UltraRequestCustomerResolverImpl.getRequestCustomerResolver().getCustomer();
    }
    
    public static void setCustomer(Customer customer) {
        UltraRequestCustomerResolverImpl.getRequestCustomerResolver().setCustomer(customer);
    }

}
