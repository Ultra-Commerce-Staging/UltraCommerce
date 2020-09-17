/*
 * #%L
 * UltraCommerce Framework
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
package com.ultracommerce.core.order.service.legacy;

import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.domain.OrderItem;
import com.ultracommerce.core.order.service.call.MergeCartResponse;
import com.ultracommerce.core.order.service.call.ReconstructCartResponse;
import com.ultracommerce.core.pricing.service.exception.PricingException;
import com.ultracommerce.profile.core.domain.Customer;

/**
 * This legacy interface should no longer be used as of 2.0
 * 
 * The new interface and implementation are OrderService and OrderServiceImpl
 * 
 * @deprecated
 */
@Deprecated
public interface LegacyCartService extends LegacyOrderService {

    Order addAllItemsToCartFromNamedOrder(Order namedOrder) throws PricingException;
    
    Order addAllItemsToCartFromNamedOrder(Order namedOrder, boolean priceOrder) throws PricingException;

    OrderItem moveItemToCartFromNamedOrder(Order order, OrderItem orderItem) throws PricingException;
    
    OrderItem moveItemToCartFromNamedOrder(Order order, OrderItem orderItem, boolean priceOrder) throws PricingException;

    OrderItem moveItemToCartFromNamedOrder(Long customerId, String orderName, Long orderItemId, Integer quantity) throws PricingException;
    
    OrderItem moveItemToCartFromNamedOrder(Long customerId, String orderName, Long orderItemId, Integer quantity, boolean priceOrder) throws PricingException;

    Order moveAllItemsToCartFromNamedOrder(Order namedOrder) throws PricingException;
    
    Order moveAllItemsToCartFromNamedOrder(Order namedOrder, boolean priceOrder) throws PricingException;

    /**
     * Merge the anonymous cart with the customer's cart taking into
     * consideration sku activation
     * @param customer the customer whose cart is to be merged
     * @param anonymousCartId the anonymous cart id
     * @return the response containing the cart, any items added to the cart,
     *         and any items removed from the cart
     */
    public MergeCartResponse mergeCart(Customer customer, Order anonymousCart, boolean priceOrder) throws PricingException;
    public MergeCartResponse mergeCart(Customer customer, Order anonymousCart) throws PricingException;
    
    /**
     * Reconstruct the cart using previous stored state taking into
     * consideration sku activation
     * @param customer the customer whose cart is to be reconstructed
     * @return the response containing the cart and any items removed from the
     *         cart
     */
    public ReconstructCartResponse reconstructCart(Customer customer, boolean priceOrder) throws PricingException;
    public ReconstructCartResponse reconstructCart(Customer customer) throws PricingException;

}
