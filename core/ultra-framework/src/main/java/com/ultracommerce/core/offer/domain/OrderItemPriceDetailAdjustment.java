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
import com.ultracommerce.core.order.domain.OrderItemPriceDetail;

/**
 * Records the actual adjustments that were made to an OrderItemPriceDetail.
 * 
 * @author bpolster
 *
 */
public interface OrderItemPriceDetailAdjustment extends Adjustment, MultiTenantCloneable<OrderItemPriceDetailAdjustment> {

    /**
     * Stores the offer name at the time the adjustment was made.   Primarily to simplify display 
     * within the admin.
     * 
     * @return
     */
    public String getOfferName();

    /**
     * Returns the name of the offer at the time the adjustment was made.
     * @param offerName
     */
    public void setOfferName(String offerName);

    public OrderItemPriceDetail getOrderItemPriceDetail();

    public void init(OrderItemPriceDetail orderItemPriceDetail, Offer offer, String reason);

    public void setOrderItemPriceDetail(OrderItemPriceDetail orderItemPriceDetail);

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

    /**
     * Future credit means that the associated adjustment will be discounted at a later time to the customer 
     * via a credit. It is up to the implementor to decide how to achieve this. This field is used to determine 
     * if the adjustment originated from an offer marked as FUTURE_CREDIT.
     *
     * See {@link Offer#getAdjustmentType()} for more info
     *
     * @return 
     */
    boolean isFutureCredit();

    /**
     * Future credit means that the associated adjustment will be discounted at a later time to the customer 
     * via a credit. It is up to the implementor to decide how to achieve this. This field is used to determine 
     * if the adjustment originated from an offer marked as FUTURE_CREDIT.
     *
     * See {@link Offer#getAdjustmentType()} for more info
     *
     * @param futureCredit
     */
    void setFutureCredit(boolean futureCredit);
}
