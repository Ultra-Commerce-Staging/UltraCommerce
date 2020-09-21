/*
 * #%L
 * UltraCommerce Framework Web
 * %%
 * Copyright (C) 2009 - 2017 Ultra Commerce
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
package com.ultracommerce.core.web.expression.checkout;

import com.ultracommerce.common.vendor.service.exception.FulfillmentPriceException;
import com.ultracommerce.common.web.expression.UltraVariableExpression;
import com.ultracommerce.core.order.domain.FulfillmentGroup;
import com.ultracommerce.core.order.domain.FulfillmentOption;
import com.ultracommerce.core.order.domain.NullOrderImpl;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.domain.OrderMultishipOption;
import com.ultracommerce.core.order.service.FulfillmentGroupService;
import com.ultracommerce.core.order.service.FulfillmentOptionService;
import com.ultracommerce.core.order.service.OrderMultishipOptionService;
import com.ultracommerce.core.pricing.service.FulfillmentPricingService;
import com.ultracommerce.core.pricing.service.fulfillment.provider.FulfillmentEstimationResponse;
import com.ultracommerce.core.web.order.CartState;
import com.ultracommerce.core.web.order.service.CartStateService;
import com.ultracommerce.presentation.condition.ConditionalOnTemplating;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;

/**
 * @author Chris Kittrell (ckittrell)
 */
@Component("ucFulfillmentVariableExpression")
@ConditionalOnTemplating
public class FulfillmentVariableExpression implements UltraVariableExpression {

    @Resource(name = "ucCartStateService")
    protected CartStateService cartStateService;

    @Resource(name = "ucFulfillmentGroupService")
    protected FulfillmentGroupService fulfillmentGroupService;

    @Resource(name = "ucFulfillmentOptionService")
    protected FulfillmentOptionService fulfillmentOptionService;

    @Resource(name = "ucFulfillmentPricingService")
    protected FulfillmentPricingService fulfillmentPricingService;

    @Resource(name = "ucOrderMultishipOptionService")
    protected OrderMultishipOptionService orderMultishipOptionService;

    @Override
    public String getName() {
        return "fulfillment";
    }

    public int getNumShippableFulfillmentGroups() {
        Order cart = CartState.getCart();

        return fulfillmentGroupService.calculateNumShippableFulfillmentGroups(cart);
    }

    public List<FulfillmentOption> getFulfillmentOptions() {
        return fulfillmentOptionService.readAllFulfillmentOptions();
    }

    public List<OrderMultishipOption> getMultiShipOptions() {
        Order cart = CartState.getCart();

        if (!isNullOrder(cart)) {
            return orderMultishipOptionService.getOrGenerateOrderMultishipOptions(cart);
        }
        return new ArrayList<>();
    }

    public FulfillmentEstimationResponse getFulfillmentEstimateResponse() {
        Order cart = CartState.getCart();

        if (!isNullOrder(cart) && cart.getFulfillmentGroups().size() > 0 && cartStateService.cartHasPopulatedShippingAddress()) {
            try {
                List<FulfillmentOption> fulfillmentOptions = fulfillmentOptionService.readAllFulfillmentOptions();
                FulfillmentGroup firstShippableFulfillmentGroup = fulfillmentGroupService.getFirstShippableFulfillmentGroup(cart);
                return fulfillmentPricingService.estimateCostForFulfillmentGroup(firstShippableFulfillmentGroup, new HashSet<>(fulfillmentOptions));
            } catch (FulfillmentPriceException e) {
                // do nothing
            }
        }
        return null;
    }

    protected boolean isNullOrder(Order cart) {
        return cart == null || (cart instanceof NullOrderImpl);
    }


}
