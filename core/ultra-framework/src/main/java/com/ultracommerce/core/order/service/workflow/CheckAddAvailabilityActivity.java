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
 * This activity handles both adds and updates. In both cases, this will check the availability and quantities (if applicable)
 * of the passed in request. If this is an update request, this will use the {@link Sku} from {@link OrderItemRequestDTO#getOrderItemId()}.
 * If this is an add request, there is no order item yet so the {@link Sku} is looked up via the {@link OrderItemRequestDTO#getSkuId()}.
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
@Component("ucCheckAddAvailabilityActivity")
public class CheckAddAvailabilityActivity extends AbstractCheckAvailabilityActivity {

    private static final Log LOG = LogFactory.getLog(CheckAddAvailabilityActivity.class);

    public static final int ORDER = 2000;
    
    @Resource(name = "ucCatalogService")
    protected CatalogService catalogService;
    
    @Resource(name = "ucOrderItemService")
    protected OrderItemService orderItemService;
    
    public CheckAddAvailabilityActivity() {
        setOrder(ORDER);
    }
    
    @Override
    public ProcessContext<CartOperationRequest> execute(ProcessContext<CartOperationRequest> context) throws Exception {
        CartOperationRequest request = context.getSeedData();
        OrderItemRequestDTO orderItemRequestDTO = request.getItemRequest();
        if (orderItemRequestDTO instanceof NonDiscreteOrderItemRequestDTO){
            return context;
        }
        
        // No order item, this must be a new item add request
        Long skuId = request.getItemRequest().getSkuId();
        Sku sku = catalogService.findSkuById(skuId);

        Order order = context.getSeedData().getOrder();
        Integer requestedQuantity = request.getItemRequest().getQuantity();

        Map<Sku, Integer> skuItems = new HashMap<>();
        for (OrderItem orderItem : order.getOrderItems()) {
            Sku skuFromOrder = null;
            if (orderItem instanceof DiscreteOrderItem) {
                skuFromOrder = ((DiscreteOrderItem) orderItem).getSku();
            } else if (orderItem instanceof BundleOrderItem) {
                skuFromOrder = ((BundleOrderItem) orderItem).getSku();
            }
            if (skuFromOrder != null && skuFromOrder.equals(sku)) {
                skuItems.merge(sku, orderItem.getQuantity(), (oldVal, newVal) -> oldVal + newVal);
            }
        }
        skuItems.merge(sku, requestedQuantity, (oldVal, newVal) -> oldVal + newVal);
        for (Map.Entry<Sku, Integer> entry : skuItems.entrySet()) {
            checkSkuAvailability(order, entry.getKey(), entry.getValue());
        }

        return context;
    }

}
