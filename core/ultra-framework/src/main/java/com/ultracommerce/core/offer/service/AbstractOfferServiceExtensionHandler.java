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
package com.ultracommerce.core.offer.service;

import com.ultracommerce.common.extension.AbstractExtensionHandler;
import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.core.offer.domain.Offer;
import com.ultracommerce.core.offer.domain.OfferCode;
import com.ultracommerce.core.offer.service.discount.domain.PromotableCandidateItemOffer;
import com.ultracommerce.core.offer.service.discount.domain.PromotableOrder;
import com.ultracommerce.core.offer.service.discount.domain.PromotableOrderItem;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.domain.OrderItemPriceDetail;
import com.ultracommerce.profile.core.domain.Customer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Andre Azzolini (apazzolini), bpolster
 */
public class AbstractOfferServiceExtensionHandler extends AbstractExtensionHandler implements OfferServiceExtensionHandler {
    
    @Override
    public ExtensionResultStatusType applyAdditionalFilters(List<Offer> offers, Order order) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType buildOfferCodeListForCustomer(Customer customer, List<OfferCode> offerCodes) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType calculatePotentialSavings(PromotableCandidateItemOffer itemOffer,
            PromotableOrderItem item, int quantity, Map<String, Object> contextMap) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType resetPriceDetails(PromotableOrderItem item) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType applyItemOffer(PromotableOrder order, PromotableCandidateItemOffer itemOffer,
            Map<String, Object> contextMap) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType synchronizeAdjustmentsAndPrices(PromotableOrder order) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType chooseSaleOrRetailAdjustments(PromotableOrder order) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType createOrderItemPriceDetailAdjustment(ExtensionResultHolder<?> resultHolder,
            OrderItemPriceDetail itemDetail) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType applyAdditionalRuleVariablesForItemOfferEvaluation(PromotableOrderItem orderItem, HashMap<String, Object> vars) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType addAdditionalOffersForCode(List<Offer> offers, OfferCode offerCode) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }



}
