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
package com.ultracommerce.core.order.service.call;

import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.domain.OrderItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MergeCartResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Order order;

    private List<OrderItem> addedItems = new ArrayList<OrderItem>();;

    private List<OrderItem> removedItems = new ArrayList<OrderItem>();;

    private boolean merged;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<OrderItem> getAddedItems() {
        return addedItems;
    }

    public void setAddedItems(List<OrderItem> addedItems) {
        this.addedItems = addedItems;
    }

    public List<OrderItem> getRemovedItems() {
        return removedItems;
    }

    public void setRemovedItems(List<OrderItem> removedItems) {
        this.removedItems = removedItems;
    }

    public boolean isMerged() {
        return merged;
    }

    public void setMerged(boolean merged) {
        this.merged = merged;
    }

}
