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
import com.ultracommerce.core.order.domain.FulfillmentGroup;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.domain.OrderItem;

public interface PromotableItemFactory {

    PromotableOrder createPromotableOrder(Order order, boolean includeOrderAndItemAdjustments);

    PromotableCandidateOrderOffer createPromotableCandidateOrderOffer(PromotableOrder promotableOrder, Offer offer);

    PromotableCandidateOrderOffer createPromotableCandidateOrderOffer(PromotableOrder promotableOrder,
            Offer offer, Money potentialSavings);

    PromotableOrderAdjustment createPromotableOrderAdjustment(
            PromotableCandidateOrderOffer promotableCandidateOrderOffer,
            PromotableOrder order);

    PromotableOrderAdjustment createPromotableOrderAdjustment(
            PromotableCandidateOrderOffer promotableCandidateOrderOffer,
            PromotableOrder order, Money value);

    PromotableOrderItem createPromotableOrderItem(OrderItem orderItem, PromotableOrder order,
            boolean includeAdjustments);

    PromotableOrderItemPriceDetail createPromotableOrderItemPriceDetail(PromotableOrderItem promotableOrderItem,
            int quantity);

    PromotableCandidateItemOffer createPromotableCandidateItemOffer(PromotableOrder promotableOrder, Offer offer);

    PromotableOrderItemPriceDetailAdjustment createPromotableOrderItemPriceDetailAdjustment(
            PromotableCandidateItemOffer promotableCandidateItemOffer,
            PromotableOrderItemPriceDetail promotableOrderItemPriceDetail);

    PromotableFulfillmentGroup createPromotableFulfillmentGroup(FulfillmentGroup fulfillmentGroup, PromotableOrder order);

    PromotableCandidateFulfillmentGroupOffer createPromotableCandidateFulfillmentGroupOffer(
            PromotableFulfillmentGroup fulfillmentGroup,
            Offer offer);
    
    PromotableFulfillmentGroupAdjustment createPromotableFulfillmentGroupAdjustment(
            PromotableCandidateFulfillmentGroupOffer promotableCandidateFulfillmentGroupOffer,
            PromotableFulfillmentGroup fulfillmentGroup);
}
