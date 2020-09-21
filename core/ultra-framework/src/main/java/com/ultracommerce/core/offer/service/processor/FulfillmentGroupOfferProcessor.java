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
package com.ultracommerce.core.offer.service.processor;

import com.ultracommerce.core.offer.domain.Offer;
import com.ultracommerce.core.offer.service.discount.FulfillmentGroupOfferPotential;
import com.ultracommerce.core.offer.service.discount.domain.PromotableCandidateFulfillmentGroupOffer;
import com.ultracommerce.core.offer.service.discount.domain.PromotableOrder;

import java.util.List;

/**
 * 
 * @author jfischer
 *
 */
public interface FulfillmentGroupOfferProcessor extends OrderOfferProcessor {

    public void filterFulfillmentGroupLevelOffer(PromotableOrder order, List<PromotableCandidateFulfillmentGroupOffer> qualifiedFGOffers, Offer offer);

    public void calculateFulfillmentGroupTotal(PromotableOrder order);
    
    /**
     * Private method that takes a list of sorted CandidateOrderOffers and determines if each offer can be
     * applied based on the restrictions (stackable and/or combinable) on that offer.  OrderAdjustments
     * are create on the Order for each applied CandidateOrderOffer.  An offer with stackable equals false
     * cannot be applied to an Order that already contains an OrderAdjustment.  An offer with combinable
     * equals false cannot be applied to the Order if the Order already contains an OrderAdjustment.
     *
     * @param qualifiedFGOffers a sorted list of CandidateOrderOffer
     * @param order the Order to apply the CandidateOrderOffers
     * @return true if order offer applied; otherwise false
     */
    public boolean applyAllFulfillmentGroupOffers(List<PromotableCandidateFulfillmentGroupOffer> qualifiedFGOffers, PromotableOrder order);
    
    public List<FulfillmentGroupOfferPotential> removeTrailingNotCombinableFulfillmentGroupOffers(List<FulfillmentGroupOfferPotential> candidateOffers);
    
}
