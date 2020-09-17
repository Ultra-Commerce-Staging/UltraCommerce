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

import com.ultracommerce.common.event.UltraApplicationEvent;


/**
 * An event for whenever an {@link OrderImpl} has been persisted
 *
 * @author Phillip Verheyden (phillipuniverse)
 * 
 * @see {@link OrderPersistedEntityListener}
 */
public class OrderPersistedEvent extends UltraApplicationEvent {

    private static final long serialVersionUID = 1L;

    /**
     * @param order the newly persisted customer
     */
    public OrderPersistedEvent(Order order) {
        super(order);
    }
    
    /**
     * Gets the newly-persisted {@link Order} set by the {@link OrderPersistedEntityListener}
     * 
     * @return
     */
    public Order getOrder() {
        return (Order)source;
    }

}
