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

package com.ultracommerce.core.web.service;

import com.ultracommerce.common.extension.ExtensionHandler;
import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.service.call.OrderItemRequestDTO;


/**
 * @author Andre Azzolini (apazzolini), bpolster
 */
public interface UpdateCartServiceExtensionHandler extends ExtensionHandler {
    
    /**
     * Throws an exception if cart is invalid.
     * 
     * @param cart
     * @param resultHolder
     * @return
     */
    public ExtensionResultStatusType updateAndValidateCart(Order cart, ExtensionResultHolder resultHolder);

    /**
     * Validates the item request just before it actually goes through the add or update workflow to get it into the cart
     * @param addToCartItem
     * @return
     * @throws Exception
     */
    public ExtensionResultStatusType validateAddToCartItem(OrderItemRequestDTO itemRequest, Order cart) throws IllegalArgumentException;
    
}
