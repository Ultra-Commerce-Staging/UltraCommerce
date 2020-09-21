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

import com.ultracommerce.common.currency.util.UltraCurrencyUtils;
import com.ultracommerce.common.money.Money;
import com.ultracommerce.core.order.domain.FulfillmentGroup;
import com.ultracommerce.core.order.domain.FulfillmentOption;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.pricing.service.FulfillmentPricingService;
import com.ultracommerce.core.pricing.service.ShippingService;
import com.ultracommerce.core.workflow.BaseActivity;
import com.ultracommerce.core.workflow.ProcessContext;

import java.math.BigDecimal;

/**
 * @deprecated Should use the {@link FulfillmentOption} paradigm, implemented in {@link FulfillmentPricingService}.
 * This activity was replaced by {@link FulfillmentGroupPricingActivity}.
 * 
 * @see {@link FulfillmentPricingActivity}, {@link FulfillmentPricingService}, {@link FulfillmentOption}
 */
@Deprecated
public class ShippingActivity extends BaseActivity<ProcessContext<Order>> {

    private ShippingService shippingService;

    public void setShippingService(ShippingService shippingService) {
        this.shippingService = shippingService;
    }

    @Override
    public ProcessContext<Order> execute(ProcessContext<Order> context) throws Exception {
        Order order = context.getSeedData();

        /*
         * 1. Get FGs from Order
         * 2. take each FG and call shipping module with the shipping svc
         * 3. add FG back to order
         */

        Money totalShipping = UltraCurrencyUtils.getMoney(BigDecimal.ZERO, order.getCurrency());
        for (FulfillmentGroup fulfillmentGroup : order.getFulfillmentGroups()) {
            fulfillmentGroup = shippingService.calculateShippingForFulfillmentGroup(fulfillmentGroup);
            totalShipping = totalShipping.add(fulfillmentGroup.getShippingPrice());
        }
        order.setTotalShipping(totalShipping);
        context.setSeedData(order);
        return context;
    }

}
