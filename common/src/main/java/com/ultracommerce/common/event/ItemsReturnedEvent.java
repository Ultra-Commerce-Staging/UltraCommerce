/*
 * #%L
 * UltraCommerce Common Libraries
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
package com.ultracommerce.common.event;

import org.springframework.util.Assert;

import java.util.Collections;
import java.util.Map;

public class ItemsReturnedEvent extends UltraApplicationEvent {

    private static final long serialVersionUID = 1L;

    protected final Map<Long, Integer> itemsAndQuantitiesReturned;

    public ItemsReturnedEvent(Long orderId, Map<Long, Integer> returnedItems) {
        super(orderId);
        Assert.notNull(orderId);
        Assert.notEmpty(returnedItems);
        this.itemsAndQuantitiesReturned = Collections.unmodifiableMap(returnedItems);
    }

    public Long getOrderId() {
        return (Long) super.getSource();
    }

    public Map<Long, Integer> getItemsAndQuantitiesReturned() {
        return itemsAndQuantitiesReturned;
    }

}
