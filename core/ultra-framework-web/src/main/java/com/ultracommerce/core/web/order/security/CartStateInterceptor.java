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
package com.ultracommerce.core.web.order.security;

import com.ultracommerce.core.web.order.CartState;
import com.ultracommerce.profile.web.core.CustomerState;
import com.ultracommerce.profile.web.site.security.CustomerStateInterceptor;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

import javax.annotation.Resource;


/**
 * Interceptor responsible for putting the current cart on the request. Carts are defined in UC as an {@link Order} with
 * a status of IN_PROCESS. This interceptor should go after {@link CustomerStateInterceptor} since it relies on
 * {@link CustomerState}.
 * 
 * Note that in servlet applications you should be using {@link CartStateFilter}
 * 
 * @author Phillip Verheyden
 * @see {@link CartState}
 */
public class CartStateInterceptor implements WebRequestInterceptor {

    @Resource(name = "ucCartStateRequestProcessor")
    protected CartStateRequestProcessor cartStateProcessor;

    @Override
    public void preHandle(WebRequest request) throws Exception {
        cartStateProcessor.process(request);
    }

    @Override
    public void postHandle(WebRequest request, ModelMap model) throws Exception {
        // unimplemented
    }

    @Override
    public void afterCompletion(WebRequest request, Exception ex) throws Exception {
        // unimplemented
    }

}
