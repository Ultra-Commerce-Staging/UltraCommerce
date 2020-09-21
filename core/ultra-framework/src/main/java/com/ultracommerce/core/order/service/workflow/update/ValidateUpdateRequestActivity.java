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
package com.ultracommerce.core.order.service.workflow.update;

import com.ultracommerce.core.order.domain.DiscreteOrderItem;
import com.ultracommerce.core.order.domain.OrderItem;
import com.ultracommerce.core.order.domain.OrderItemImpl;
import com.ultracommerce.core.order.service.OrderItemService;
import com.ultracommerce.core.order.service.call.OrderItemRequestDTO;
import com.ultracommerce.core.order.service.exception.MinQuantityNotFulfilledException;
import com.ultracommerce.core.order.service.workflow.CartOperationRequest;
import com.ultracommerce.core.order.service.workflow.service.OrderItemRequestValidationService;
import com.ultracommerce.core.workflow.BaseActivity;
import com.ultracommerce.core.workflow.ProcessContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component("ucValidateUpdateRequestActivity")
public class ValidateUpdateRequestActivity extends BaseActivity<ProcessContext<CartOperationRequest>> {
    
    public static final int ORDER = 1000;
    
    @Resource(name = "ucOrderItemService")
    protected OrderItemService orderItemService;

    @Resource(name = "ucOrderItemRequestValidationService")
    protected OrderItemRequestValidationService orderItemRequestValidationService;
    
    public ValidateUpdateRequestActivity() {
        setOrder(ORDER);
    }
    
    @Override
    public ProcessContext<CartOperationRequest> execute(ProcessContext<CartOperationRequest> context) throws Exception {
        CartOperationRequest request = context.getSeedData();
        OrderItemRequestDTO orderItemRequestDTO = request.getItemRequest();
        
        Map<String, String> attributes = new HashMap<>();
        OrderItem requestedOrderItem = new OrderItemImpl();
        for (OrderItem oi : request.getOrder().getOrderItems()) {
            if (oi.getId() == orderItemRequestDTO.getOrderItemId()){
                requestedOrderItem = oi;
            }
        }

        for (String key : requestedOrderItem.getOrderItemAttributes().keySet()) {
            attributes.put(key, requestedOrderItem.getOrderItemAttributes().get(key).getValue());
        }

        orderItemRequestDTO.setItemAttributes(attributes);
        
        // Throw an exception if the user did not specify an orderItemId
        if (orderItemRequestDTO.getOrderItemId() == null) {
            throw new IllegalArgumentException("OrderItemId must be specified when removing from order");
        }

        // Throw an exception if the user tried to update an item to a negative quantity
        if (orderItemRequestDTO.getQuantity() < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        if (!orderItemRequestValidationService.satisfiesMinQuantityCondition(orderItemRequestDTO, context)) {
            Integer minQuantity = orderItemRequestValidationService.getMinQuantity(orderItemRequestDTO, context);
            Long productId = orderItemRequestDTO.getProductId();

            throw new MinQuantityNotFulfilledException("This item requires a minimum quantity of " + minQuantity, productId);
        }

        // Throw an exception if the user did not specify an order to add the item to
        if (request.getOrder() == null) {
            throw new IllegalArgumentException("Order is required when updating item quantities");
        }
        
        // Throw an exception if the user is trying to update an order item that is part of a bundle
        OrderItem orderItem = orderItemService.readOrderItemById(orderItemRequestDTO.getOrderItemId());
        if (orderItem != null && orderItem instanceof DiscreteOrderItem) {
            DiscreteOrderItem doi = (DiscreteOrderItem) orderItem;
            if (doi.getBundleOrderItem() != null) {
                throw new IllegalArgumentException("Cannot update an item that is part of a bundle");
            }
        }
        
        return context;
    }
    
}
