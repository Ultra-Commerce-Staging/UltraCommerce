/*
 * #%L
 * UltraCommerce Framework Web
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
 * 
 */
package com.ultracommerce.core.web.expression;

import com.ultracommerce.common.web.expression.UltraVariableExpression;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.service.OrderService;
import com.ultracommerce.profile.core.domain.Customer;
import com.ultracommerce.profile.web.core.CustomerState;

import javax.annotation.Resource;


/**
 * 
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
public class OrderVariableExpression implements UltraVariableExpression {

    @Resource(name = "ucOrderService")
    protected OrderService orderService;
    
    @Override
    public String getName() {
        return "orders";
    }

    public Order getNamedOrderForCurrentCustomer(String orderName) {
        return getNamedOrderForCustomer(orderName, CustomerState.getCustomer());
    }
    
    public Order getNamedOrderForCustomer(String orderName, Customer customer) {
        return orderService.findNamedOrderForCustomer(orderName, customer);
    }
}
