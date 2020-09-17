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
package com.ultracommerce.core.pricing.service.workflow;

import org.apache.commons.collections4.CollectionUtils;
import com.ultracommerce.core.offer.domain.Offer;
import com.ultracommerce.core.offer.domain.OfferCode;
import com.ultracommerce.core.offer.service.OfferService;
import com.ultracommerce.core.offer.service.OfferValueModifierExtensionManager;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.service.OrderService;
import com.ultracommerce.core.workflow.BaseActivity;
import com.ultracommerce.core.workflow.ProcessContext;
import org.springframework.stereotype.Component;

import java.util.List;

import javax.annotation.Resource;

@Component("ucOfferActivity")
public class OfferActivity extends BaseActivity<ProcessContext<Order>> {

    public static final int ORDER = 1000;
    
    @Resource(name="ucOfferService")
    protected OfferService offerService;

    @Resource(name = "ucOrderService")
    protected OrderService orderService;

    @Resource(name = "ucOfferValueModifierExtensionManager")
    protected OfferValueModifierExtensionManager offerModifierExtensionManager;
    
    public OfferActivity() {
        setOrder(ORDER);
    }

    @Override
    public ProcessContext<Order> execute(ProcessContext<Order> context) throws Exception {
        Order order = context.getSeedData();
        List<OfferCode> offerCodes = getNewOfferCodesFromCustomer(order);

        if (offerCodes != null && !offerCodes.isEmpty()) {
            order = orderService.addOfferCodes(order, offerCodes, false);
        }

        List<Offer> offers = offerService.buildOfferListForOrder(order);

        if (CollectionUtils.isNotEmpty(offers) && offerModifierExtensionManager != null) {
            offerModifierExtensionManager.getProxy().modifyOfferValues(offers, order);
        }

        order = offerService.applyAndSaveOffersToOrder(offers, order);
        context.setSeedData(order);

        return context;
    }

    protected List<OfferCode> getNewOfferCodesFromCustomer(Order order) {
        List<OfferCode> offerCodesFromCustomer = offerService.buildOfferCodeListForCustomer(order);
        List<OfferCode> offerCodesFromOrder = order.getAddedOfferCodes();

        offerCodesFromCustomer.removeAll(offerCodesFromOrder);

        return offerCodesFromCustomer;
    }

}
