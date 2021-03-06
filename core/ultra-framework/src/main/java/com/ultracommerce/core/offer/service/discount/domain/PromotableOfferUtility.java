/*
 * #%L
 * UltraCommerce Framework
 * %%
 * Copyright (C) 2009 - 2018 Ultra Commerce
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
import com.ultracommerce.core.offer.domain.OfferPriceData;

/**
 * @author Nick Crum ncrum
 */
public interface PromotableOfferUtility {
    boolean itemMatchesOfferPriceData(OfferPriceData offerPriceData, PromotableOrderItem promotableOrderItem);

    Money computeRetailAdjustmentValue(PromotableCandidateFulfillmentGroupOffer promotableCandidateFulfillmentGroupOffer, PromotableFulfillmentGroup promotableFulfillmentGroup);

    Money computeSalesAdjustmentValue(PromotableCandidateFulfillmentGroupOffer promotableCandidateFulfillmentGroupOffer, PromotableFulfillmentGroup promotableFulfillmentGroup);

    Money computeAdjustmentValue(PromotableCandidateFulfillmentGroupOffer promotableCandidateFulfillmentGroupOffer, PromotableFulfillmentGroup promotableFulfillmentGroup, boolean allowSalePrice);

    Money computeRetailAdjustmentValue(PromotableCandidateItemOffer promotableCandidateItemOffer, PromotableOrderItemPriceDetail orderItemPriceDetail);

    Money computeSalesAdjustmentValue(PromotableCandidateItemOffer promotableCandidateItemOffer, PromotableOrderItemPriceDetail orderItemPriceDetail);

    Money computeAdjustmentValue(PromotableCandidateItemOffer promotableCandidateItemOffer, PromotableOrderItemPriceDetail orderItemPriceDetail, boolean allowSalePrice);

    Money calculateSavingsForOrderItem(PromotableCandidateItemOffer promotableCandidateItemOffer, PromotableOrderItem orderItem, int qtyToReceiveSavings);
}
