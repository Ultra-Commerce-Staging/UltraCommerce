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
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.domain.OrderItem;
import com.ultracommerce.core.order.service.OrderService;
import com.ultracommerce.core.order.service.call.OrderItemRequestDTO;
import com.ultracommerce.core.order.service.exception.ItemNotFoundException;
import com.ultracommerce.core.order.service.workflow.CartOperationRequest;
import com.ultracommerce.core.workflow.BaseActivity;
import com.ultracommerce.core.workflow.ProcessContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component("ucUpdateOrderItemActivity")
public class UpdateOrderItemActivity extends BaseActivity<ProcessContext<CartOperationRequest>> {

    public static final int ORDER = 3000;
    
    @Resource(name = "ucOrderService")
    protected OrderService orderService;
    
    public UpdateOrderItemActivity() {
        setOrder(ORDER);
    }
    
    @Override
    public ProcessContext<CartOperationRequest> execute(ProcessContext<CartOperationRequest> context) throws Exception {
        CartOperationRequest request = context.getSeedData();
        OrderItemRequestDTO orderItemRequestDTO = request.getItemRequest();
        Order order = request.getOrder();
        
        OrderItem orderItem = null;
        for (OrderItem oi : order.getOrderItems()) {
            if (oi.getId().equals(orderItemRequestDTO.getOrderItemId())) {
                orderItem = oi;
            }
        }
        
        if (orderItem == null || !order.getOrderItems().contains(orderItem)) {
            throw new ItemNotFoundException("Order Item (" + orderItemRequestDTO.getOrderItemId() + ") not found in Order (" + order.getId() + ")");
        }
        
        OrderItem itemFromOrder = order.getOrderItems().get(order.getOrderItems().indexOf(orderItem));
        if (orderItemRequestDTO.getQuantity() >= 0) {
            int previousQty = itemFromOrder.getQuantity();
            request.setOrderItemQuantityDelta(orderItemRequestDTO.getQuantity() - itemFromOrder.getQuantity());
            itemFromOrder.setQuantity(orderItemRequestDTO.getQuantity());

            for (OrderItem child : itemFromOrder.getChildOrderItems()) {
                int childQuantity = child.getQuantity();
                childQuantity = childQuantity / previousQty;
                child.setQuantity(childQuantity * orderItemRequestDTO.getQuantity());
            }

            // Update any additional attributes of the passed in request
            if (itemFromOrder instanceof DiscreteOrderItem) {
                DiscreteOrderItem discreteOrderItem = (DiscreteOrderItem) itemFromOrder;
                discreteOrderItem.getAdditionalAttributes().putAll(orderItemRequestDTO.getAdditionalAttributes());
            }

            request.setOrderItem(itemFromOrder);
        }

        return context;
    }

}
