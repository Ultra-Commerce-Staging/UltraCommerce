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
/**
 * @author Austin Rooke (austinrooke)
 */
package com.ultracommerce.core.spec.order.service

import com.ultracommerce.common.money.Money
import com.ultracommerce.core.order.domain.Order
import com.ultracommerce.core.order.domain.OrderImpl
import com.ultracommerce.core.order.service.OrderItemService
import com.ultracommerce.core.order.service.call.OrderItemRequestDTO
import com.ultracommerce.core.order.service.workflow.CartOperationRequest
import com.ultracommerce.core.workflow.DefaultProcessContextImpl
import com.ultracommerce.core.workflow.ProcessContext
import com.ultracommerce.profile.core.domain.Customer
import com.ultracommerce.profile.core.domain.CustomerImpl
import spock.lang.Specification

class BaseBuildOrderItemFromDTOSpec extends Specification {

    OrderItemService orderItemService
    ProcessContext<CartOperationRequest> context
    def setup() {
        context = new DefaultProcessContextImpl<CartOperationRequest>().with() {
            Customer customer = new CustomerImpl()
            customer.id = 1
            Order order = new OrderImpl()
            order.id = 1
            order.customer = customer
            OrderItemRequestDTO itemRequest = Spy(OrderItemRequestDTO).with {
                skuId = 1
                productId = 1
                categoryId = 1
                quantity = 1
                overrideSalePrice = new Money("1.00")
                overrideRetailPrice = new Money("1.50")
                it
            }
            seedData = new CartOperationRequest(order,itemRequest,true)
            it
        }
    }
}
