/*
 * #%L
 * UltraCommerce Framework
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
/**
 * 
 */
package com.ultracommerce.core.order.service.workflow;

import com.ultracommerce.core.catalog.domain.Sku;
import com.ultracommerce.core.inventory.service.ContextualInventoryService;
import com.ultracommerce.core.inventory.service.InventoryUnavailableException;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.service.type.OrderStatus;
import com.ultracommerce.core.workflow.BaseActivity;
import com.ultracommerce.core.workflow.ProcessContext;

import java.util.Objects;

import javax.annotation.Resource;

/**
 * Common functionality between checking availability between adds and updates
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
public abstract class AbstractCheckAvailabilityActivity extends BaseActivity<ProcessContext<CartOperationRequest>> {

    @Resource(name = "ucInventoryService")
    protected ContextualInventoryService inventoryService;

    @Override
    public boolean shouldExecute(ProcessContext<CartOperationRequest> context) {
        Order order = context.getSeedData().getOrder();
        return order != null && !Objects.equals(order.getStatus(), OrderStatus.NAMED);
    }
    
    protected void checkSkuAvailability(Order order, Sku sku, Integer requestedQuantity) throws InventoryUnavailableException {
        inventoryService.checkSkuAvailability(order, sku, requestedQuantity);
    }
}
