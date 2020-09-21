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

import com.ultracommerce.common.money.Money;
import com.ultracommerce.core.order.domain.OrderItem;

public interface OrderItemAdjustment extends Adjustment {

    public OrderItem getOrderItem();

    public void init(OrderItem orderItem, Offer offer, String reason);

    public void setOrderItem(OrderItem orderItem);

    /**
     * Even for items that are on sale, it is possible that an adjustment was made
     * to the retail price that gave the customer a better offer.
     *
     * Since some offers can be applied to the sale price and some only to the
     * retail price, this setting provides the required value.
     *
     * @return true if this adjustment was applied to the sale price
     */
    public boolean isAppliedToSalePrice();

    public void setAppliedToSalePrice(boolean appliedToSalePrice);

    /**
     * Value of this adjustment relative to the retail price.
     * @return
     */
    public Money getRetailPriceValue();

    public void setRetailPriceValue(Money retailPriceValue);

    /**
     * Value of this adjustment relative to the sale price.
     *
     * @return
     */
    public Money getSalesPriceValue();

    public void setSalesPriceValue(Money salesPriceValue);
}
