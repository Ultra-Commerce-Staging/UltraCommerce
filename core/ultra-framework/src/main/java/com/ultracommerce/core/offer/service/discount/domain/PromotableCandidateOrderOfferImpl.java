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

import com.ultracommerce.common.currency.domain.UltraCurrency;
import com.ultracommerce.common.currency.util.UltraCurrencyUtils;
import com.ultracommerce.common.money.Money;
import com.ultracommerce.core.offer.domain.Offer;
import com.ultracommerce.core.offer.domain.OfferItemCriteria;
import com.ultracommerce.core.offer.service.type.OfferDiscountType;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public class PromotableCandidateOrderOfferImpl implements PromotableCandidateOrderOffer {

    private static final long serialVersionUID = 1L;
    
    protected HashMap<OfferItemCriteria, List<PromotableOrderItem>> candidateQualifiersMap = new HashMap<OfferItemCriteria, List<PromotableOrderItem>>();
    protected Offer offer;
    protected PromotableOrder promotableOrder;
    protected Money potentialSavings;
    
    public PromotableCandidateOrderOfferImpl(PromotableOrder promotableOrder, Offer offer) {
        assert(offer != null);
        assert(promotableOrder != null);
        this.promotableOrder = promotableOrder;
        this.offer = offer;
        calculatePotentialSavings();
    }
    
    /**
     * Instead of calculating the potential savings, you can specify an override of this value.   
     * This is currently coded only to work if the promotableOrder's isIncludeOrderAndItemAdjustments flag
     * is true.
     *  
     * @param promotableOrder
     * @param offer
     * @param potentialSavings
     */
    public PromotableCandidateOrderOfferImpl(PromotableOrder promotableOrder, Offer offer, Money potentialSavings) {
        this(promotableOrder, offer);
        if (promotableOrder.isIncludeOrderAndItemAdjustments()) {
            this.potentialSavings = potentialSavings;
        }
    }

    @Override
    public HashMap<OfferItemCriteria, List<PromotableOrderItem>> getCandidateQualifiersMap() {
        return candidateQualifiersMap;
    }
    
    protected void calculatePotentialSavings() {
        Money amountBeforeAdjustments = promotableOrder.calculateSubtotalWithoutAdjustments();
        potentialSavings = UltraCurrencyUtils.getMoney(BigDecimal.ZERO, getCurrency());
        if (getOffer().getDiscountType().equals(OfferDiscountType.AMOUNT_OFF)) {
            potentialSavings = UltraCurrencyUtils.getMoney(getOffer().getValue(), getCurrency());
        } else if (getOffer().getDiscountType().equals(OfferDiscountType.FIX_PRICE)) {
            potentialSavings = amountBeforeAdjustments.subtract(UltraCurrencyUtils.getMoney(getOffer().getValue(), getCurrency()));
        } else if (getOffer().getDiscountType().equals(OfferDiscountType.PERCENT_OFF)) {
            potentialSavings = amountBeforeAdjustments.multiply(getOffer().getValue().divide(new BigDecimal("100")));
        }

        if (potentialSavings.greaterThan(amountBeforeAdjustments)) {
            potentialSavings = amountBeforeAdjustments;
        }
    }
    
    @Override
    public Offer getOffer() {
        return this.offer;
    }
    
    @Override
    public PromotableOrder getPromotableOrder() {
        return this.promotableOrder;
    }
    
    public UltraCurrency getCurrency() {
        return promotableOrder.getOrderCurrency();
    }

    @Override
    public Money getPotentialSavings() {
        return potentialSavings;
    }

    @Override
    public boolean isCombinable() {
        Boolean combinable = offer.isCombinableWithOtherOffers();
        return (combinable != null && combinable);
    }

    @Override
    public boolean isTotalitarian() {
        Boolean totalitarian = offer.isTotalitarianOffer();
        return (totalitarian != null && totalitarian.booleanValue());
    }

    @Override
    public int getPriority() {
        return offer.getPriority();
    }
}
