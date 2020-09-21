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
import com.ultracommerce.core.offer.domain.OfferItemCriteria;
import com.ultracommerce.core.offer.service.discount.PromotionDiscount;
import com.ultracommerce.core.offer.service.discount.PromotionQualifier;

import java.util.List;

/**
 * Modules that extend offer engine behavior can benefit from a wrapped PromotableOrderItemPriceDetail.
 * 
 * @author bpolster
 *
 */
public class PromotableOrderItemPriceDetailWrapper implements PromotableOrderItemPriceDetail {

    private PromotableOrderItemPriceDetail detail;

    public PromotableOrderItemPriceDetailWrapper(PromotableOrderItemPriceDetail wrappedDetail) {
        detail = wrappedDetail;
    }

    public void addCandidateItemPriceDetailAdjustment(PromotableOrderItemPriceDetailAdjustment itemAdjustment) {
        detail.addCandidateItemPriceDetailAdjustment(itemAdjustment);
    }

    public List<PromotableOrderItemPriceDetailAdjustment> getCandidateItemAdjustments() {
        return detail.getCandidateItemAdjustments();
    }

    public boolean hasNonCombinableAdjustments() {
        return detail.hasNonCombinableAdjustments();
    }

    public boolean isTotalitarianOfferApplied() {
        return detail.isTotalitarianOfferApplied();
    }

    public boolean isNonCombinableOfferApplied() {
        return detail.isNonCombinableOfferApplied();
    }

    public void chooseSaleOrRetailAdjustments() {
        detail.chooseSaleOrRetailAdjustments();
    }

    public void removeAllAdjustments() {
        detail.removeAllAdjustments();
    }

    public List<PromotionDiscount> getPromotionDiscounts() {
        return detail.getPromotionDiscounts();
    }

    public List<PromotionQualifier> getPromotionQualifiers() {
        return detail.getPromotionQualifiers();
    }

    public int getQuantity() {
        return detail.getQuantity();
    }

    public void setQuantity(int quantity) {
        detail.setQuantity(quantity);
    }

    public PromotableOrderItem getPromotableOrderItem() {
        return detail.getPromotableOrderItem();
    }

    public int getQuantityAvailableToBeUsedAsQualifier(PromotableCandidateItemOffer itemOffer) {
        return detail.getQuantityAvailableToBeUsedAsQualifier(itemOffer);
    }

    public int getQuantityAvailableToBeUsedAsTarget(PromotableCandidateItemOffer itemOffer) {
        return detail.getQuantityAvailableToBeUsedAsTarget(itemOffer);
    }

    public PromotionQualifier addPromotionQualifier(PromotableCandidateItemOffer itemOffer, OfferItemCriteria itemCriteria, int qtyToMarkAsQualifier) {
        return detail.addPromotionQualifier(itemOffer, itemCriteria, qtyToMarkAsQualifier);
    }

    public void addPromotionDiscount(PromotableCandidateItemOffer itemOffer, OfferItemCriteria itemCriteria, int qtyToMarkAsTarget) {
        detail.addPromotionDiscount(itemOffer, itemCriteria, qtyToMarkAsTarget);
    }

    public Money calculateItemUnitPriceWithAdjustments(boolean allowSalePrice) {
        return detail.calculateItemUnitPriceWithAdjustments(allowSalePrice);
    }

    public void finalizeQuantities() {
        detail.finalizeQuantities();
    }

    public void clearAllNonFinalizedQuantities() {
        detail.clearAllNonFinalizedQuantities();
    }

    public String buildDetailKey() {
        return detail.buildDetailKey();
    }

    public Money getFinalizedTotalWithAdjustments() {
        return detail.getFinalizedTotalWithAdjustments();
    }

    public Money calculateTotalAdjustmentValue() {
        return detail.calculateTotalAdjustmentValue();
    }

    public PromotableOrderItemPriceDetail splitIfNecessary() {
        return detail.splitIfNecessary();
    }

    public boolean useSaleAdjustments() {
        return detail.useSaleAdjustments();
    }

    public boolean isAdjustmentsFinalized() {
        return detail.isAdjustmentsFinalized();
    }

    public void setAdjustmentsFinalized(boolean adjustmentsFinalized) {
        detail.setAdjustmentsFinalized(adjustmentsFinalized);
    }

    @Override
    public PromotableOrderItemPriceDetail shallowCopy() {
        return detail.shallowCopy();
    }

    @Override
    public PromotableOrderItemPriceDetail copyWithFinalizedData() {
        return detail.copyWithFinalizedData();
    }

}
