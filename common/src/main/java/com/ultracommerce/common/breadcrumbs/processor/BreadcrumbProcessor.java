/*
 * #%L
 * UltraCommerce Common Libraries
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

package com.ultracommerce.common.breadcrumbs.processor;

import com.ultracommerce.common.breadcrumbs.dto.BreadcrumbDTO;
import com.ultracommerce.common.web.expression.BreadcrumbVariableExpression;
import com.ultracommerce.presentation.condition.ConditionalOnTemplating;
import com.ultracommerce.presentation.dialect.AbstractUltraVariableModifierProcessor;
import com.ultracommerce.presentation.model.UltraTemplateContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * A Thymeleaf processor that will add a list of BreadcrumbDTOs to the model.
 *
 * @author bpolster
 */
@Component("ucBreadcrumbProcessor")
@ConditionalOnTemplating
public class BreadcrumbProcessor extends AbstractUltraVariableModifierProcessor {

    @Resource
    protected BreadcrumbVariableExpression breadcrumbVariableExpression;
    
    @Override
    public String getName() {
        return "breadcrumbs";
    }
    
    @Override
    public int getPrecedence() {
        return 10000;
    }

    @Override
    public Map<String, Object> populateModelVariables(String tagName, Map<String, String> tagAttributes, UltraTemplateContext context) {
        List<BreadcrumbDTO> dtos = breadcrumbVariableExpression.getBreadcrumbs();
        
        String resultVar = tagAttributes.get("resultVar");

        if (resultVar == null) {
            resultVar = "breadcrumbs";
        }

        if (!CollectionUtils.isEmpty(dtos)) {
            return ImmutableMap.of(resultVar, (Object) dtos);
        }
        return null;
    }

}
