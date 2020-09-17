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

import com.ultracommerce.common.currency.domain.UltraCurrency;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.service.call.OrderItemRequestDTO;
import com.ultracommerce.core.order.service.call.UpdateCartResponse;

/**
 * Provides methods to facilitate order repricing.
 *
 * Author: jerryocanas
 * Date: 9/26/12
 */
public interface UpdateCartService {

    /**
     * Sets the currency that was set as active on last pass through.
     *
     * @param savedCurrency
     */
    public void setSavedCurrency(UltraCurrency savedCurrency);

    /**
     * Gets the currency that was set as active on last pass through.
     *
     * @return
     */
    public UltraCurrency getSavedCurrency();

    /**
     *  Compares the currency set in the UltraRequestContext with the savedCurrency.
     *  If different, returns TRUE
     *
     * @return
     */
    public boolean currencyHasChanged();

    /**
     * Reprices the order by removing all items and recreating the cart calling for a reprice on the new cart.
     *
     * @return
     */
    public UpdateCartResponse copyCartToCurrentContext(Order currentCart);

    /**
     * Validates the given add item that will be added to the given cart. This occurs prior to the item actually being added
     *
     * @param cart
     * @throws IllegalArgumentException
     */
    public void validateAddToCartRequest(OrderItemRequestDTO itemRequest, Order cart) throws IllegalArgumentException;

    /**
     * Updates the cart (locale, pricing) and performs validation.
     *
     * @param cart
     * @throws IllegalArgumentException
     */
    public void updateAndValidateCart(Order cart);

}
