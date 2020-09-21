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
package com.ultracommerce.core.order.service.workflow.remove;

import com.ultracommerce.core.order.domain.DiscreteOrderItem;
import com.ultracommerce.core.order.domain.OrderItem;
import com.ultracommerce.core.order.service.OrderItemService;
import com.ultracommerce.core.order.service.call.OrderItemRequestDTO;
import com.ultracommerce.core.order.service.workflow.CartOperationRequest;
import com.ultracommerce.core.workflow.BaseActivity;
import com.ultracommerce.core.workflow.ProcessContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component("ucValidateRemoveRequestActivity")
public class ValidateRemoveRequestActivity extends BaseActivity<ProcessContext<CartOperationRequest>> {
    
    public static final int ORDER = 1000;
    
    @Resource(name = "ucOrderItemService")
    protected OrderItemService orderItemService;
    
    public ValidateRemoveRequestActivity() {
        setOrder(ORDER);
    }

    @Override
    public ProcessContext<CartOperationRequest> execute(ProcessContext<CartOperationRequest> context) throws Exception {
        CartOperationRequest request = context.getSeedData();
        OrderItemRequestDTO orderItemRequestDTO = request.getItemRequest();

        // Throw an exception if the user did not specify an orderItemId
        if (orderItemRequestDTO.getOrderItemId() == null) {
            throw new IllegalArgumentException("OrderItemId must be specified when removing from order");
        }

        // Throw an exception if the user did not specify an order to add the item to
        if (request.getOrder() == null) {
            throw new IllegalArgumentException("Order is required when updating item quantities");
        }
        
        // Throw an exception if the user is trying to remove an order item that is part of a bundle
        OrderItem orderItem = null;
        for (OrderItem oi : request.getOrder().getOrderItems()) {
            if (oi.getId().equals(orderItemRequestDTO.getOrderItemId())) {
                orderItem = oi;
            }
        }
        
        if (orderItem == null) {
            throw new IllegalArgumentException("Could not find order item to remove");
        }
        
        if (orderItem != null && orderItem instanceof DiscreteOrderItem) {
            DiscreteOrderItem doi = (DiscreteOrderItem) orderItem;
            if (doi.getBundleOrderItem() != null) {
                throw new IllegalArgumentException("Cannot remove an item that is part of a bundle");
            }
        }
        request.setOrderItem(orderItem);
        
        return context;
    }
    
}
