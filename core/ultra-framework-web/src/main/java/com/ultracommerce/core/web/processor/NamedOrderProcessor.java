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

package com.ultracommerce.core.web.processor;

import com.ultracommerce.core.order.domain.NullOrderImpl;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.service.OrderService;
import com.ultracommerce.core.web.expression.OrderVariableExpression;
import com.ultracommerce.presentation.condition.ConditionalOnTemplating;
import com.ultracommerce.presentation.dialect.AbstractUltraVariableModifierProcessor;
import com.ultracommerce.presentation.model.UltraTemplateContext;
import com.ultracommerce.profile.core.domain.Customer;
import com.ultracommerce.profile.web.core.CustomerState;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

/**
 * <p>
 * A Thymeleaf processor that will add the desired named order to the model
 *
 * <p>
 * Example:
 * 
 * <pre>
 *  &lt;blc:named_order orderVar="wishlist" orderName="wishlist" /&gt;
 *  &lt;span th:text="${wishlist.customer.name}" /&gt; 
 * </pre>
 *
 * @param orderVar the value that the order will be assigned to
 * @param orderName the name of the order, {@link Order#getName()}
 * 
 * @see {@link Order#getName()}
 * @author elbertbautista
 * @deprecated use {@link OrderVariableExpression#getNamedOrderForCurrentCustomer(String)} instead
 */
@Deprecated
@Component("ucNamedOrderProcessor")
@ConditionalOnTemplating
public class NamedOrderProcessor extends AbstractUltraVariableModifierProcessor {

    @Resource(name = "ucOrderService")
    protected OrderService orderService;
    
    @Override
    public String getName() {
        return "named_order";
    }
    
    @Override
    public int getPrecedence() {
        return 10000;
    }
    
    @Override
    public Map<String, Object> populateModelVariables(String tagName, Map<String, String> tagAttributes, UltraTemplateContext context) {
        Customer customer = CustomerState.getCustomer();

        String orderVar = tagAttributes.get("orderVar");
        String orderName = tagAttributes.get("orderName");

        Order order = orderService.findNamedOrderForCustomer(orderName, customer);
        Map<String, Object> newModelVars = new HashMap<>();
        if (order != null) {
            newModelVars.put(orderVar, order);
        } else {
            newModelVars.put(orderVar, new NullOrderImpl());
        }
        return newModelVars;
    }
}
