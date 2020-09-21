/*
 * #%L
 * UltraCommerce Framework
 * %%
 * Copyright (C) 2009 - 2019 Ultra Commerce
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
package com.ultracommerce.core.checkout.service.workflow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.core.catalog.domain.Sku;
import com.ultracommerce.core.inventory.service.ContextualInventoryService;
import com.ultracommerce.core.order.domain.BundleOrderItem;
import com.ultracommerce.core.order.domain.DiscreteOrderItem;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.domain.OrderItem;
import com.ultracommerce.core.workflow.BaseActivity;
import com.ultracommerce.core.workflow.ProcessContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

/**
 * This will check the availability and quantities (if applicable) all order items in checkout request.
 * Very similar to the {@link CheckUpdateAvailabilityActivity} but in the ucCheckoutWorkflow instead.
 * This should prevent succeed checkout, in case, when Sku became unavailable in range
 * after it was adding to the cart and before completing the order.
 */
@Component("ucValidateAvailabilityActivity")
public class ValidateAvailabilityActivity extends BaseActivity<ProcessContext<CheckoutSeed>> {

    public static final int ORDER = 750;
    private static final Log LOG = LogFactory.getLog(ValidateAvailabilityActivity.class);

    @Resource(name = "ucInventoryService")
    protected ContextualInventoryService inventoryService;

    public ValidateAvailabilityActivity() {
        setOrder(ORDER);
    }

    @Override
    public ProcessContext<CheckoutSeed> execute(ProcessContext<CheckoutSeed> context) throws Exception {
        Order order = context.getSeedData().getOrder();
        if (order == null) {
            return context;
        }

        Map<Sku, Integer> skuItems = new HashMap<>();
        for (OrderItem orderItem : order.getOrderItems()) {
            Sku sku;
            if (orderItem instanceof DiscreteOrderItem) {
                sku = ((DiscreteOrderItem) orderItem).getSku();
            } else if (orderItem instanceof BundleOrderItem) {
                sku = ((BundleOrderItem) orderItem).getSku();
            } else {
                LOG.warn("Could not check availability; did not recognize passed-in item " + orderItem.getClass().getName());
                return context;
            }
            if (!sku.isActive()) {
                throw new IllegalArgumentException("The requested skuId (" + sku.getId() + ") is no longer active");
            }
            skuItems.merge(sku, orderItem.getQuantity(), (oldVal, newVal) -> oldVal + newVal);
        }
        for (Map.Entry<Sku, Integer> entry : skuItems.entrySet()) {
            inventoryService.checkSkuAvailability(order, entry.getKey(), entry.getValue());
        }

        return context;
    }
}
