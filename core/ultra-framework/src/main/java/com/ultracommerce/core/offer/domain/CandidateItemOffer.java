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
package com.ultracommerce.core.offer.domain;

import com.ultracommerce.common.copy.MultiTenantCloneable;
import com.ultracommerce.common.money.Money;
import com.ultracommerce.core.order.domain.OrderItem;

import java.io.Serializable;

/**
 * OrderItem level offer that has been qualified for an order,
 * but may still be ejected based on additional pricing
 * and stackability concerns once the order has been processed
 * through the promotion engine.
 */
public interface CandidateItemOffer extends Serializable, MultiTenantCloneable<CandidateItemOffer> {
    
    public Long getId();

    public void setId(Long id);

    public OrderItem getOrderItem();

    public void setOrderItem(OrderItem orderItem);
    
    public CandidateItemOffer clone();
    
    public void setOffer(Offer offer);

    public int getPriority();

    public Offer getOffer();
    
    public Money getDiscountedPrice();
    
    public void setDiscountedPrice(Money discountedPrice);
    
}
