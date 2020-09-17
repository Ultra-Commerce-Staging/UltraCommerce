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
package com.ultracommerce.core.order.service.workflow.update.options;

import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.service.OrderItemService;
import com.ultracommerce.core.order.service.OrderService;
import com.ultracommerce.core.order.service.call.DiscreteOrderItemRequest;
import com.ultracommerce.core.order.service.call.OrderItemRequestDTO;
import com.ultracommerce.core.order.service.workflow.CartOperationRequest;
import com.ultracommerce.core.workflow.BaseActivity;
import com.ultracommerce.core.workflow.ProcessContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component("ucUpdateProductOptionsOrderItemActivity")
public class UpdateProductOptionsOrderItemActivity extends BaseActivity<ProcessContext<CartOperationRequest>> {
    
    public static final int ORDER = 2000;
    
    @Resource(name = "ucOrderService")
    protected OrderService orderService;

    @Resource(name = "ucOrderItemService")
    protected OrderItemService orderItemService;
    
    public UpdateProductOptionsOrderItemActivity() {
        setOrder(ORDER);
    }

    @Override
    public ProcessContext<CartOperationRequest> execute(ProcessContext<CartOperationRequest> context) throws Exception {
        CartOperationRequest request = context.getSeedData();
        OrderItemRequestDTO orderItemRequestDTO = request.getItemRequest();
        Order order = request.getOrder();
        
        if (orderItemService.readOrderItemById(Long.valueOf(orderItemRequestDTO.getOrderItemId())) != null) {
            DiscreteOrderItemRequest itemRequest = new DiscreteOrderItemRequest();
            itemRequest.setItemAttributes(orderItemRequestDTO.getItemAttributes());
            orderItemService.updateDiscreteOrderItem(orderItemService.readOrderItemById(Long.valueOf(orderItemRequestDTO.getOrderItemId())), itemRequest);

        }

        order = orderService.save(order, false);
        request.setOrder(order);

        return context;
    }

}
