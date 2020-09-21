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
package com.ultracommerce.core.web.order;

import com.ultracommerce.common.web.UltraRequestContext;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.web.order.security.CartStateRequestProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

@Component("ucCartState")
public class CartState {

    /**
     * Gets the current cart based on the current request
     * 
     * @return the current customer's cart
     */
    public static Order getCart() {
        if (UltraRequestContext.getUltraRequestContext() == null ||
                UltraRequestContext.getUltraRequestContext().getWebRequest() == null) {
            return null;
        }

        WebRequest request = UltraRequestContext.getUltraRequestContext().getWebRequest();
        return (Order) request.getAttribute(CartStateRequestProcessor.getCartRequestAttributeName(), WebRequest.SCOPE_REQUEST);
    }
    
    /**
     * Sets the current cart on the current request
     * 
     * @param cart the new cart to set
     */
    public static void setCart(Order cart) {
        WebRequest request = UltraRequestContext.getUltraRequestContext().getWebRequest();
        request.setAttribute(CartStateRequestProcessor.getCartRequestAttributeName(), cart, WebRequest.SCOPE_REQUEST);
    }

}
