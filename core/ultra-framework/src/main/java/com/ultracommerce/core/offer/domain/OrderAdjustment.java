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

import com.ultracommerce.core.order.domain.Order;

public interface OrderAdjustment extends Adjustment {

    public Order getOrder();

    public void init(Order order, Offer offer, String reason);

    public void setOrder(Order order);

    /**
     * Future credit means that the associated adjustment will be discounted at a later time to the customer 
     * via a credit. It is up to the implementor to decide how to achieve this. This field is used to determine 
     * if the adjustment originated from an offer marked as FUTURE_CREDIT.
     *
     * See {@link Offer#getAdjustmentType()} for more info
     *
     * @return
     */
    Boolean isFutureCredit();

    /**
     * Future credit means that the associated adjustment will be discounted at a later time to the customer 
     * via a credit. It is up to the implementor to decide how to achieve this. This field is used to determine 
     * if the adjustment originated from an offer marked as FUTURE_CREDIT.
     *
     * See {@link Offer#getAdjustmentType()} for more info
     *
     * @param futureCredit
     */
    void setFutureCredit(Boolean futureCredit);
}
