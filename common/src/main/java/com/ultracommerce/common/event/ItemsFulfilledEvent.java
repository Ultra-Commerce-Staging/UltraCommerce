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

/**
 * Event that may be raised to indicate that items have been fulfilled.
 * 
 * @author Kelly Tisdell
 *
 */
public class ItemsFulfilledEvent extends UltraApplicationEvent {

    private static final long serialVersionUID = 1L;

    protected final Map<Long, Integer> itemsAndQuantitiesFulfilled;

    public ItemsFulfilledEvent(Long fulfillmentGroupId, Map<Long, Integer> fulfilled) {
        super(fulfillmentGroupId);
        Assert.notNull(fulfillmentGroupId);
        Assert.notEmpty(fulfilled);
        this.itemsAndQuantitiesFulfilled = Collections.unmodifiableMap(fulfilled);
    }

    public Long getFulfillmentGroupId() {
        return (Long) super.getSource();
    }

    public Map<Long, Integer> getItemsAndQuantitiesFulfilled() {
        return itemsAndQuantitiesFulfilled;
    }
}
