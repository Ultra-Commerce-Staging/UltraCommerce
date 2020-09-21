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

import com.ultracommerce.core.offer.domain.Offer;

import java.io.Serializable;

public interface OrderItemQualifier extends Serializable {
    
    /**
     * Unique id of the item qualifier.
     * @return
     */
    Long getId();

    /**
     * Sets the id for this OrderItemQualifier
     * @param id
     */
    void setId(Long id);

    /**
     * The related order item.
     * @return
     */
    OrderItem getOrderItem();

    /**
     * Sets the related order item.
     * @param orderItem
     */
    void setOrderItem(OrderItem orderItem);

    /**
     * Sets the related offer.
     * @param offer
     */
    void setOffer(Offer offer);

    /**
     * Returns the related offer
     * @return
     */
    Offer getOffer();

    /**
     * Sets the quantity of the associated OrderItem that was used as a qualifier.
     * @param quantity
     */
    void setQuantity(Long quantity);

    /**
     * Returns the quantity of the associated OrderItem that was used as a qualifier.
     * @return
     */
    Long getQuantity();
}
