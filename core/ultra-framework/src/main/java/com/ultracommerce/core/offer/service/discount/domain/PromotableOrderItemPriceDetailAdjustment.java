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
package com.ultracommerce.core.offer.service.discount.domain;

import com.ultracommerce.common.money.Money;
import com.ultracommerce.core.offer.domain.Offer;

import java.io.Serializable;

/**
 * This class holds adjustment records during the discount calculation 
 * processing.  This and other disposable objects avoid churn on the database while the 
 * offer engine determines the best offer(s) for the order being priced.
 * 
 * @author bpolster
 */
public interface PromotableOrderItemPriceDetailAdjustment extends Serializable {

    /**
     * Returns the associated promotableOrderItemPriceDetail
     * @return
     */
    PromotableOrderItemPriceDetail getPromotableOrderItemPriceDetail();

    /**
     * Returns the associated promotableCandidateItemOffer
     * @return
     */
    Offer getOffer();

    /**
     * Returns the value of this adjustment if only retail prices
     * can be used.
     * @return
     */
    Money getRetailAdjustmentValue();

    /**
     * Returns the value of this adjustment if sale prices
     * can be used.
     * @return
     */
    Money getSaleAdjustmentValue();

    /**
     * Returns the value of this adjustment.
     * can be used.
     * @return
     */
    Money getAdjustmentValue();

    /**
     * Returns true if the value was applied to the sale price.
     * @return
     */
    boolean isAppliedToSalePrice();

    /**
     * Returns true if this adjustment represents a combinable offer.
     */
    boolean isCombinable();

    /**
     * Returns true if this adjustment represents a totalitarian offer.   
     */
    boolean isTotalitarian();

    /**
     * Returns the id of the contained offer.
     * @return
     */
    Long getOfferId();

    /**
     * Sets the adjustment price based on the passed in parameter.
     */
    void finalizeAdjustment(boolean useSalePrice);

    /**
     * Copy this adjustment.   Used when a detail that contains this adjustment needs to be split.
     * @param discountQty
     * @param copy
     * @return
     */
    public PromotableOrderItemPriceDetailAdjustment copy();

}
