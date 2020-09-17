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
package com.ultracommerce.core.order.service;

import com.ultracommerce.core.catalog.domain.Product;
import com.ultracommerce.core.order.domain.BundleOrderItem;
import com.ultracommerce.core.order.domain.DiscreteOrderItem;
import com.ultracommerce.core.order.domain.GiftWrapOrderItem;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.domain.OrderItem;
import com.ultracommerce.core.order.domain.PersonalMessage;
import com.ultracommerce.core.order.service.call.BundleOrderItemRequest;
import com.ultracommerce.core.order.service.call.ConfigurableOrderItemRequest;
import com.ultracommerce.core.order.service.call.DiscreteOrderItemRequest;
import com.ultracommerce.core.order.service.call.GiftWrapOrderItemRequest;
import com.ultracommerce.core.order.service.call.OrderItemRequest;
import com.ultracommerce.core.order.service.call.OrderItemRequestDTO;
import com.ultracommerce.core.order.service.call.ProductBundleOrderItemRequest;
import com.ultracommerce.core.order.service.type.OrderStatus;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public interface OrderItemService {
    
    public OrderItem readOrderItemById(Long orderItemId);

    public OrderItem saveOrderItem(OrderItem orderItem);
    
    public void delete(OrderItem item);
    
    public PersonalMessage createPersonalMessage();

    public DiscreteOrderItem createDiscreteOrderItem(DiscreteOrderItemRequest itemRequest);
    
    public DiscreteOrderItem createDynamicPriceDiscreteOrderItem(final DiscreteOrderItemRequest itemRequest, @SuppressWarnings("rawtypes") HashMap skuPricingConsiderations);

    public GiftWrapOrderItem createGiftWrapOrderItem(GiftWrapOrderItemRequest itemRequest);

    /**
     * Used to create "manual" product bundles.   Manual product bundles are primarily designed
     * for grouping items in the cart display.    Typically ProductBundle will be used to
     * achieve non programmer related bundles.
     *
     *
     * @param itemRequest
     * @return
     */
    public BundleOrderItem createBundleOrderItem(BundleOrderItemRequest itemRequest);

    public BundleOrderItem createBundleOrderItem(ProductBundleOrderItemRequest itemRequest);

    public BundleOrderItem createBundleOrderItem(ProductBundleOrderItemRequest itemRequest, boolean saveItem);

    /**
     * Creates an OrderItemRequestDTO object that most closely resembles the given OrderItem.
     * That is, it will copy the SKU and quantity and attempt to copy the product and category
     * if they exist.
     * 
     * @param item the item to copy
     * @return the OrderItemRequestDTO that mirrors the item
     */
    public OrderItemRequestDTO buildOrderItemRequestDTOFromOrderItem(OrderItem item);

    public OrderItem updateDiscreteOrderItem(OrderItem orderItem, DiscreteOrderItemRequest itemRequest);

    public OrderItem createOrderItem(OrderItemRequest itemRequest);

    public OrderItem buildOrderItemFromDTO(Order order, OrderItemRequestDTO orderItemRequestDTO);

    public void priceOrderItem(OrderItem item);

    public Set<Product> findAllProductsInRequest(ConfigurableOrderItemRequest itemRequest);

    public void applyAdditionalOrderItemProperties(OrderItem orderItem);

    public ConfigurableOrderItemRequest createConfigurableOrderItemRequestFromProduct(Product product);

    public void modifyOrderItemRequest(ConfigurableOrderItemRequest itemRequest);

    public void mergeOrderItemRequest(ConfigurableOrderItemRequest itemRequest, OrderItem orderItem);

    public List<OrderItem> findOrderItemsForCustomersInDateRange(List<Long> customerIds, Date startDate, Date endDate);

    List<OrderItem> readBatchOrderItems(int start, int count, List<OrderStatus>orderStatusList);

    Long readNumberOfOrderItems();
}
