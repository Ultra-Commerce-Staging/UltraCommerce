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

import org.apache.commons.lang3.StringUtils;
import com.ultracommerce.core.catalog.domain.ProductOption;
import com.ultracommerce.core.order.domain.DiscreteOrderItem;
import com.ultracommerce.presentation.condition.ConditionalOnTemplating;
import com.ultracommerce.presentation.dialect.AbstractUltraVariableModifierProcessor;
import com.ultracommerce.presentation.model.UltraTemplateContext;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Priyesh Patel
 */
@Component("ucProductOptionDisplayProcessor")
@ConditionalOnTemplating
public class ProductOptionDisplayProcessor extends AbstractUltraVariableModifierProcessor {

    @Override
    public String getName() {
        return "product_option_display";
    }
    
    @Override
    public int getPrecedence() {
        return 100;
    }
    
    @Override
    public boolean useGlobalScope() {
        return false;
    }

    @Override
    public Map<String, Object> populateModelVariables(String tagName, Map<String, String> tagAttributes, UltraTemplateContext context) {
        HashMap<String, String> productOptionDisplayValues = new HashMap<>();
        Object item = context.parseExpression(tagAttributes.get("orderItem"));
        if (item instanceof DiscreteOrderItem) {
            DiscreteOrderItem orderItem = (DiscreteOrderItem) item;

            for (String i : orderItem.getOrderItemAttributes().keySet()) {
                for (ProductOption option : orderItem.getProduct().getProductOptions()) {
                    if (option.getAttributeName().equals(i) && !StringUtils.isEmpty(orderItem.getOrderItemAttributes().get(i).toString())) {
                        productOptionDisplayValues.put(option.getLabel(), orderItem.getOrderItemAttributes().get(i).toString());
                    }
                }
            }
        }
        return ImmutableMap.of("productOptionDisplayValues", (Object) productOptionDisplayValues);
    }

}
