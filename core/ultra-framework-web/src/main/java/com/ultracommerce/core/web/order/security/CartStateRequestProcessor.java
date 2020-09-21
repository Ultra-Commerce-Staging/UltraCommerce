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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.crossapp.service.CrossAppAuthService;
import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.util.UCRequestUtils;
import com.ultracommerce.common.web.AbstractUltraWebRequestProcessor;
import com.ultracommerce.common.web.UltraWebRequestProcessor;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.service.MergeCartService;
import com.ultracommerce.core.order.service.OrderService;
import com.ultracommerce.core.order.service.call.MergeCartResponse;
import com.ultracommerce.core.order.service.exception.RemoveFromCartException;
import com.ultracommerce.core.order.service.type.OrderStatus;
import com.ultracommerce.core.pricing.service.exception.PricingException;
import com.ultracommerce.core.web.service.UpdateCartService;
import com.ultracommerce.profile.core.domain.Customer;
import com.ultracommerce.profile.web.core.CustomerState;
import com.ultracommerce.profile.web.core.security.CustomerStateRequestProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

/**
 * Ensures that the customer's current cart is available to the request.  
 * 
 * Also invokes ucMergeCartProcessor" if the user has just logged in.   
 * 
 * Genericized version of the CartStateFilter. This was made to facilitate reuse between Servlet Filters, Portlet Filters 
 * and Spring MVC interceptors. Spring has an easy way of converting HttpRequests and PortletRequests into WebRequests 
 * via <br />
 * new ServletWebRequest(httpServletRequest); new PortletWebRequest(portletRequest); <br />
 * For the interceptor pattern, you can simply implement a WebRequestInterceptor to invoke from there.
 * 
 * @author Phillip Verheyden
 * @see {@link CartStateFilter}
 * @see {@link UltraWebRequestProcessor}
 * @see {@link ServletWebRequest}
 * @see {@link org.springframework.web.portlet.context.PortletWebRequest}
 */
@Component("ucCartStateRequestProcessor")
public class CartStateRequestProcessor extends AbstractUltraWebRequestProcessor {

    /** Logger for this class and subclasses */
    protected final Log LOG = LogFactory.getLog(getClass());

    public static final String UC_RULE_MAP_PARAM = "ucRuleMap";

    @Resource(name = "ucCartStateRequestProcessorExtensionManager")
    protected CartStateRequestProcessorExtensionManager extensionManager;

    @Resource(name = "ucOrderService")
    protected OrderService orderService;

    @Resource(name = "ucUpdateCartService")
    protected UpdateCartService updateCartService;

    @Resource(name = "ucMergeCartService")
    protected MergeCartService mergeCartService;
    
    @Resource(name = "ucCustomerStateRequestProcessor")
    protected CustomerStateRequestProcessor customerStateRequestProcessor;

    @Autowired(required = false)
    @Qualifier("ucCrossAppAuthService")
    protected CrossAppAuthService crossAppAuthService;

    protected static String cartRequestAttributeName = "cart";
    
    protected static String anonymousCartSessionAttributeName = "anonymousCart";

    public static final String OVERRIDE_CART_ATTR_NAME = "_uc_overrideCartId";
        
    @Override
    public void process(WebRequest request) {
        Customer customer = CustomerState.getCustomer();

        if (customer == null) {
            LOG.info("No customer was found on the current request, no cart will be added to the current request. Ensure that the"
                    + " ucCustomerStateFilter occurs prior to the ucCartStateFilter");
            return;
        }

        ExtensionResultHolder<Order> erh = new ExtensionResultHolder<Order>();
        extensionManager.getProxy().lookupOrCreateCart(request, customer, erh);

        Order cart;
        if (erh.getResult() != null) {
            cart = orderService.findCartForCustomerWithEnhancements(customer, erh.getResult());
        } else {
            cart = getOverrideCart(request);
            if (cart == null && mergeCartNeeded(customer, request)) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Merge cart required, calling mergeCart " + customer.getId());
                }
                cart = mergeCart(customer, request);
            } else if (cart == null) {
                cart = orderService.findCartForCustomerWithEnhancements(customer);
            }

            if (cart == null) {
                cart = orderService.getNullOrder();
            } else {
                updateCartService.updateAndValidateCart(cart);
            }
        }

        updateCartRequestAttributes(request, cart);

    }

    protected void updateCartRequestAttributes(WebRequest request, Order cart) {
        request.setAttribute(cartRequestAttributeName, cart, WebRequest.SCOPE_REQUEST);

        // Setup cart for content rule processing
        @SuppressWarnings("unchecked")
        Map<String, Object> ruleMap = (Map<String, Object>) request.getAttribute(UC_RULE_MAP_PARAM, WebRequest.SCOPE_REQUEST);
        if (ruleMap == null) {
            ruleMap = new HashMap<String, Object>();
        }
        ruleMap.put("order", cart);

        // Leaving the following line in for backwards compatibility, but all rules should use order as the
        // variable name.
        ruleMap.put("cart", cart);
        request.setAttribute(UC_RULE_MAP_PARAM, ruleMap, WebRequest.SCOPE_REQUEST);
    }

    public Order getOverrideCart(WebRequest request) {
        Long orderId = null;
        if (UCRequestUtils.isOKtoUseSession(request)) {
            orderId = (Long) request.getAttribute(OVERRIDE_CART_ATTR_NAME, WebRequest.SCOPE_SESSION);
        }
        Order cart = null;
        if (orderId != null) {
            cart = orderService.findOrderById(orderId);
    
            if (cart == null || 
                    cart.getStatus().equals(OrderStatus.SUBMITTED) || 
                    cart.getStatus().equals(OrderStatus.CANCELLED)) {
                return null;
            }
        }

        return cart;
    }
    
    /**
     * Returns true if the given <b>customer</b> is different than the previous anonymous customer, implying that this is
     * the logged in customer and we need to merge the carts
     */
    public boolean mergeCartNeeded(Customer customer, WebRequest request) {
        // When the user is a CSR, we want to disable cart merging
        if (crossAppAuthService != null && crossAppAuthService.isAuthedFromAdmin()) {
            return false;
        }

        Customer anonymousCustomer = customerStateRequestProcessor.getAnonymousCustomer(request);
        return (anonymousCustomer != null && customer.getId() != null && !customer.getId().equals(anonymousCustomer.getId()));
    }

    /**
     * Looks up the anonymous customer and merges that cart with the cart from the given logged in <b>customer</b>. This
     * will also remove the customer from session after it has finished since it is no longer needed
     */
    public Order mergeCart(Customer customer, WebRequest request) {
        Customer anonymousCustomer = customerStateRequestProcessor.getAnonymousCustomer(request);
        MergeCartResponse mergeCartResponse;
        try {
            Order cart = orderService.findCartForCustomer(anonymousCustomer);
            mergeCartResponse = mergeCartService.mergeCart(customer, cart);
        } catch (PricingException e) {
            throw new RuntimeException(e);
        } catch (RemoveFromCartException e) {
            throw new RuntimeException(e);
        }
        
        if (UCRequestUtils.isOKtoUseSession(request)) {
            // The anonymous customer from session is no longer needed; it can be safely removed
            request.removeAttribute(CustomerStateRequestProcessor.getAnonymousCustomerSessionAttributeName(),
                    WebRequest.SCOPE_SESSION);
            request.removeAttribute(CustomerStateRequestProcessor.getAnonymousCustomerIdSessionAttributeName(),
                    WebRequest.SCOPE_SESSION);
        }
        return mergeCartResponse.getOrder();
    }

    public static String getCartRequestAttributeName() {
        return cartRequestAttributeName;
    }

    public static void setCartRequestAttributeName(String cartRequestAttributeName) {
        CartStateRequestProcessor.cartRequestAttributeName = cartRequestAttributeName;
    }
}