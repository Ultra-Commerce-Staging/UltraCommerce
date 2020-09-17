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

import com.ultracommerce.common.currency.util.UltraCurrencyUtils;
import com.ultracommerce.common.money.Money;
import com.ultracommerce.core.offer.domain.Offer;
import com.ultracommerce.core.offer.domain.OfferItemCriteria;
import com.ultracommerce.core.offer.service.type.OfferDiscountType;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public class PromotableCandidateFulfillmentGroupOfferImpl extends AbstractPromotionRounding implements PromotableCandidateFulfillmentGroupOffer {

    private static final long serialVersionUID = 1L;
    
    protected HashMap<OfferItemCriteria, List<PromotableOrderItem>> candidateQualifiersMap = new HashMap<OfferItemCriteria, List<PromotableOrderItem>>();
    protected Offer offer;
    protected PromotableFulfillmentGroup promotableFulfillmentGroup;
    
    public PromotableCandidateFulfillmentGroupOfferImpl(PromotableFulfillmentGroup promotableFulfillmentGroup, Offer offer) {
        assert(offer != null);
        assert(promotableFulfillmentGroup != null);
        this.offer = offer;
        this.promotableFulfillmentGroup = promotableFulfillmentGroup;
    }
    
    @Override
    public HashMap<OfferItemCriteria, List<PromotableOrderItem>> getCandidateQualifiersMap() {
        return candidateQualifiersMap;
    }

    @Override
    public void setCandidateQualifiersMap(HashMap<OfferItemCriteria, List<PromotableOrderItem>> candidateItemsMap) {
        this.candidateQualifiersMap = candidateItemsMap;
    }
    
    protected Money getBasePrice() {
        Money priceToUse = null;
        if (promotableFulfillmentGroup.getFulfillmentGroup().getRetailFulfillmentPrice() != null) {
            priceToUse = promotableFulfillmentGroup.getFulfillmentGroup().getRetailFulfillmentPrice();
            if ((offer.getApplyDiscountToSalePrice()) && (promotableFulfillmentGroup.getFulfillmentGroup().getSaleFulfillmentPrice() != null)) {
                priceToUse = promotableFulfillmentGroup.getFulfillmentGroup().getSaleFulfillmentPrice();
            }
        }
        return priceToUse;
    }
    
    @Override
    public Money computeDiscountedAmount() {
        Money discountedAmount = new Money(0);
        Money priceToUse = getBasePrice();
        if (priceToUse != null) {
            if (offer.getDiscountType().equals(OfferDiscountType.AMOUNT_OFF)) {
                discountedAmount = UltraCurrencyUtils.getMoney(offer.getValue(), promotableFulfillmentGroup.getFulfillmentGroup().getOrder().getCurrency());
            } else if (offer.getDiscountType().equals(OfferDiscountType.FIX_PRICE)) {
                discountedAmount = priceToUse.subtract(UltraCurrencyUtils.getMoney(offer.getValue(), promotableFulfillmentGroup.getFulfillmentGroup().getOrder().getCurrency()));
            } else if (offer.getDiscountType().equals(OfferDiscountType.PERCENT_OFF)) {
                discountedAmount = priceToUse.multiply(offer.getValue().divide(new BigDecimal("100")));
            }
            if (discountedAmount.greaterThan(priceToUse)) {
                discountedAmount = priceToUse;
            }
        }

        return discountedAmount;
    }
    
    @Override
    public Money getDiscountedPrice() {
        return getBasePrice().subtract(computeDiscountedAmount());
    }

    @Override
    public Money getDiscountedAmount() {
        return computeDiscountedAmount();
    }

    @Override
    public Offer getOffer() {
        return offer;
    }
    
    @Override
    public PromotableFulfillmentGroup getFulfillmentGroup() {
        return promotableFulfillmentGroup;
    }

    public int getPriority() {
        return offer.getPriority();
    }
}
