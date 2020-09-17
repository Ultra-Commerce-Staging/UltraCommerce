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
package com.ultracommerce.core.order.domain;

import com.ultracommerce.common.copy.MultiTenantCloneable;
import com.ultracommerce.common.value.ValueAssignable;

/**
 * The Interface OrderItemAttribute.   Allows for arbitrary data to be
 * persisted with the orderItem.  This can be used to store additional
 * items that are required during order entry.
 *
 * Examples:
 *   Engravement Message for a jewelry item
 *   TestDate for someone purchasing an online exam
 *   Number of minutes for someone purchasing a rate plan.
 *
 */
public interface OrderItemAttribute extends ValueAssignable<String>, MultiTenantCloneable<OrderItemAttribute> {

    /**
     * Gets the id.
     * 
     * @return the id
     */
    Long getId();

    /**
     * Sets the id.
     * 
     * @param id the new id
     */
    void setId(Long id);

    /**
     * Gets the parent orderItem
     * 
     * @return the orderItem
     */
    OrderItem getOrderItem();

    /**
     * Sets the orderItem.
     * 
     * @param orderItem the associated orderItem
     */
    void setOrderItem(OrderItem orderItem);

    /**
     * Provide support for a deep copy of an order item.
     * @return
     */
    public OrderItemAttribute clone();
}
