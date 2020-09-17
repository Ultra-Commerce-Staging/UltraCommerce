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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.core.catalog.domain.Sku;
import com.ultracommerce.core.catalog.service.CatalogService;
import com.ultracommerce.core.order.domain.BundleOrderItem;
import com.ultracommerce.core.order.domain.DiscreteOrderItem;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.domain.OrderItem;
import com.ultracommerce.core.order.service.OrderItemService;
import com.ultracommerce.core.order.service.call.NonDiscreteOrderItemRequestDTO;
import com.ultracommerce.core.order.service.call.OrderItemRequestDTO;
import com.ultracommerce.core.workflow.ProcessContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

/**
 * Very similar to the {@link CheckAddAvailabilityActivity} but in the ucUpdateItemWorkflow instead
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
@Component("ucCheckUpdateAvailabilityActivity")
public class CheckUpdateAvailabilityActivity extends AbstractCheckAvailabilityActivity {

    private static final Log LOG = LogFactory.getLog(CheckUpdateAvailabilityActivity.class);
    
    public static final int ORDER = 2000;
    
    @Resource(name = "ucCatalogService")
    protected CatalogService catalogService;
    
    @Resource(name = "ucOrderItemService")
    protected OrderItemService orderItemService;
    
    public CheckUpdateAvailabilityActivity() {
        setOrder(ORDER);
    }
    
    @Override
    public ProcessContext<CartOperationRequest> execute(ProcessContext<CartOperationRequest> context) throws Exception {
        CartOperationRequest request = context.getSeedData();
        OrderItemRequestDTO orderItemRequestDTO = request.getItemRequest();
        if (orderItemRequestDTO instanceof NonDiscreteOrderItemRequestDTO) {
            return context;
        }

        Sku sku;
        Long orderItemId = request.getItemRequest().getOrderItemId();
        OrderItem orderItem = orderItemService.readOrderItemById(orderItemId);
        if (orderItem instanceof DiscreteOrderItem) {
            sku = ((DiscreteOrderItem) orderItem).getSku();
        } else if (orderItem instanceof BundleOrderItem) {
            sku = ((BundleOrderItem) orderItem).getSku();
        } else {
            LOG.warn("Could not check availability; did not recognize passed-in item " + orderItem.getClass().getName());
            return context;
        }

        Order order = context.getSeedData().getOrder();
        Integer requestedQuantity = request.getItemRequest().getQuantity();
        Map<Sku, Integer> skuItems = new HashMap<>();
        for (OrderItem orderItemFromOrder : order.getOrderItems()) {
            Sku skuFromOrder = null;
            if (orderItemFromOrder instanceof DiscreteOrderItem) {
                skuFromOrder = ((DiscreteOrderItem) orderItemFromOrder).getSku();
            } else if (orderItemFromOrder instanceof BundleOrderItem) {
                skuFromOrder = ((BundleOrderItem) orderItemFromOrder).getSku();
            }
            if (skuFromOrder != null && skuFromOrder.equals(sku) && !orderItemFromOrder.equals(orderItem)) {
                skuItems.merge(sku, orderItemFromOrder.getQuantity(), (oldVal, newVal) -> oldVal + newVal);
            }
        }
        skuItems.merge(sku, requestedQuantity, (oldVal, newVal) -> oldVal + newVal);
        for (Map.Entry<Sku, Integer> entry : skuItems.entrySet()) {
            checkSkuAvailability(order, entry.getKey(), entry.getValue());
        }

        Integer previousQty = orderItem.getQuantity();
        for (OrderItem child : orderItem.getChildOrderItems()) {
            Sku childSku = ((DiscreteOrderItem) child).getSku();
            Integer childQuantity = child.getQuantity();
            childQuantity = childQuantity / previousQty;
            checkSkuAvailability(order, childSku, childQuantity * requestedQuantity);
        }

        return context;
    }
}
