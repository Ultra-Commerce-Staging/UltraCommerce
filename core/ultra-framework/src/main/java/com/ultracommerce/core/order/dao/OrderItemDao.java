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
package com.ultracommerce.core.order.dao;

import com.ultracommerce.core.order.domain.OrderItem;
import com.ultracommerce.core.order.domain.OrderItemPriceDetail;
import com.ultracommerce.core.order.domain.OrderItemQualifier;
import com.ultracommerce.core.order.domain.PersonalMessage;
import com.ultracommerce.core.order.service.type.OrderItemType;
import com.ultracommerce.core.order.service.type.OrderStatus;

import java.util.Date;
import java.util.List;

public interface OrderItemDao {

    OrderItem readOrderItemById(Long orderItemId);

    OrderItem save(OrderItem orderItem);

    void delete(OrderItem orderItem);

    OrderItem create(OrderItemType orderItemType);

    OrderItem saveOrderItem(OrderItem orderItem);
    
    PersonalMessage createPersonalMessage();

    OrderItemPriceDetail createOrderItemPriceDetail();

    OrderItemQualifier createOrderItemQualifier();

    /**
     * Sets the initial orderItemPriceDetail for the item.
     */
    OrderItemPriceDetail initializeOrderItemPriceDetails(OrderItem item);

    List<OrderItem> readOrderItemsForCustomersInDateRange(List<Long> customerIds, Date startDate, Date endDate);

    Long readNumberOfOrderItems();

    List<OrderItem> readBatchOrderItems(int start, int count, List<OrderStatus> statuses);
}
