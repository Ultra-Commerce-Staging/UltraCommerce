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

import com.ultracommerce.common.security.MergeCartProcessor;
import com.ultracommerce.common.util.UCRequestUtils;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.service.MergeCartService;
import com.ultracommerce.core.order.service.OrderService;
import com.ultracommerce.core.order.service.call.MergeCartResponse;
import com.ultracommerce.core.order.service.exception.RemoveFromCartException;
import com.ultracommerce.core.pricing.service.exception.PricingException;
import com.ultracommerce.profile.core.domain.Customer;
import com.ultracommerce.profile.core.service.CustomerService;
import com.ultracommerce.profile.web.core.security.CustomerStateRequestProcessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @deprecated this has been replaced by invoking {@link MergeCartService} explicitly within the
 * {@link CartStateRequestProcessor}
 */
@Deprecated
@Component("ucMergeCartProcessor")
public class MergeCartProcessorImpl implements MergeCartProcessor {

    protected String mergeCartResponseKey = "bl_merge_cart_response";

    @Resource(name="ucCustomerService")
    protected CustomerService customerService;

    @Resource(name="ucOrderService")
    protected OrderService orderService;
    
    @Resource(name="ucMergeCartService")
    protected MergeCartService mergeCartService;
    
    @Resource(name = "ucCustomerStateRequestProcessor")
    protected CustomerStateRequestProcessor customerStateRequestProcessor;
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, Authentication authResult) {
        execute(new ServletWebRequest(request, response), authResult);
    }
    
    @Override
    public void execute(WebRequest request, Authentication authResult) {
        Customer loggedInCustomer = customerService.readCustomerByUsername(authResult.getName());
        Customer anonymousCustomer = customerStateRequestProcessor.getAnonymousCustomer(request);
        
        Order cart = null;
        if (anonymousCustomer != null) {
            cart = orderService.findCartForCustomer(anonymousCustomer);
        }
        MergeCartResponse mergeCartResponse;
        try {
            mergeCartResponse = mergeCartService.mergeCart(loggedInCustomer, cart);
        } catch (PricingException e) {
            throw new RuntimeException(e);
        } catch (RemoveFromCartException e) {
            throw new RuntimeException(e);
        }

        if (UCRequestUtils.isOKtoUseSession(request)) {
            request.setAttribute(mergeCartResponseKey, mergeCartResponse, WebRequest.SCOPE_SESSION);
        }
    }

    public String getMergeCartResponseKey() {
        return mergeCartResponseKey;
    }

    public void setMergeCartResponseKey(String mergeCartResponseKey) {
        this.mergeCartResponseKey = mergeCartResponseKey;
    }

}
