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

import java.io.Serializable;

/**
 * This class holds adjustment records during the discount calculation 
 * processing.  This and other disposable objects avoid churn on the database while the 
 * offer engine determines the best offer(s) for the order being priced.
 * 
 * @author bpolster
 */
public interface PromotableFulfillmentGroupAdjustment extends PromotionRounding, Serializable {

    /**
     * Returns the associated promotableFulfillmentGroup
     * @return
     */
    public PromotableFulfillmentGroup getPromotableFulfillmentGroup();

    /**
     * Returns the associated promotableCandidateOrderOffer
     * @return
     */
    public PromotableCandidateFulfillmentGroupOffer getPromotableCandidateFulfillmentGroupOffer();

    /**
     * Returns the value of this adjustment 
     * @return
     */
    public Money getSaleAdjustmentValue();

    /**
     * Returns the value of this adjustment 
     * @return
     */
    public Money getRetailAdjustmentValue();

    /**
     * Returns the value of this adjustment 
     * @return
     */
    public Money getAdjustmentValue();

    /**
     * Returns true if this adjustment represents a combinable offer.
     */
    boolean isCombinable();

    /**
     * Returns true if this adjustment represents a totalitarian offer.   
     */
    boolean isTotalitarian();
    
    /**
     * Updates the adjustmentValue to the sales or retail value based on the passed in param
     */
    void finalizeAdjustment(boolean useSaleAdjustments);

    boolean isAppliedToSalePrice();
}
