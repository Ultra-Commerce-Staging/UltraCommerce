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
package com.ultracommerce.core.order.service.workflow.add;

import com.ultracommerce.common.dao.GenericEntityDao;
import com.ultracommerce.core.catalog.service.CatalogService;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.domain.OrderItem;
import com.ultracommerce.core.order.service.OrderItemService;
import com.ultracommerce.core.order.service.OrderService;
import com.ultracommerce.core.order.service.call.OrderItemRequestDTO;
import com.ultracommerce.core.order.service.workflow.CartOperationRequest;
import com.ultracommerce.core.workflow.BaseActivity;
import com.ultracommerce.core.workflow.ProcessContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component("ucAddOrderItemActivity")
public class AddOrderItemActivity extends BaseActivity<ProcessContext<CartOperationRequest>> {
    
    public static final int ORDER = 3000;
    
    @Resource(name = "ucOrderService")
    protected OrderService orderService;
    
    @Resource(name = "ucOrderItemService")
    protected OrderItemService orderItemService;
    
    @Resource(name = "ucCatalogService")
    protected CatalogService catalogService;

    @Resource(name = "ucGenericEntityDao")
    protected GenericEntityDao genericEntityDao;
    
    public AddOrderItemActivity() {
        setOrder(ORDER);
    }

    @Override
    public ProcessContext<CartOperationRequest> execute(ProcessContext<CartOperationRequest> context) throws Exception {
        CartOperationRequest request = context.getSeedData();
        OrderItemRequestDTO orderItemRequestDTO = request.getItemRequest();

        // Order has been verified in a previous activity -- the values in the request can be trusted
        Order order = request.getOrder();

        // Build the order item
        OrderItem item = orderItemService.buildOrderItemFromDTO(order, orderItemRequestDTO);

        // Check for special pricing
        orderItemService.priceOrderItem(item);

        order.getOrderItems().add(item);
        request.setOrderItem(item);


        genericEntityDao.persist(item);

        return context;
    }

}
