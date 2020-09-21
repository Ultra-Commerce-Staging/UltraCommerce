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
package com.ultracommerce.common.web.processor;

import com.ultracommerce.common.enumeration.domain.DataDrivenEnumeration;
import com.ultracommerce.common.enumeration.domain.DataDrivenEnumerationValue;
import com.ultracommerce.common.enumeration.service.DataDrivenEnumerationService;
import com.ultracommerce.common.web.expression.DataDrivenEnumVariableExpression;
import com.ultracommerce.presentation.condition.ConditionalOnTemplating;
import com.ultracommerce.presentation.dialect.AbstractUltraVariableModifierProcessor;
import com.ultracommerce.presentation.model.UltraTemplateContext;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;


/**
 * Processor that adds a list of {@link DataDriveEnumerationValue}s onto the model for a particular key.
 *  This will add a new variable on the model called 'enumValues'
 *
 * @param key (required) key for the {@link DataDrivenEnumeration} that the {@link DataDrivenEnumerationValue}s should be
 * apart of. This corresponds to {@link DataDrivenEnumeration#getKey()}.
 * 
 * @param sort (optional) <i>ASCENDING</i> or <i>DESCENDING</i> if the resulting values should be sorted by not. The sort will be on
 *          {@link DataDrivenEnumerationValue#getDisplay()}
 *
 * @author Phillip Verheyden (phillipuniverse)
 * @deprecated use {@link DataDrivenEnumVariableExpression} instead
 */
@Deprecated
@Component("ucDataDrivenEnumerationProcessor")
@ConditionalOnTemplating
public class DataDrivenEnumerationProcessor extends AbstractUltraVariableModifierProcessor {

    @Resource(name = "ucDataDrivenEnumerationService")
    protected DataDrivenEnumerationService enumService;
    
    @Resource
    protected DataDrivenEnumVariableExpression ddeVariableExpression;
    
    @Override
    public String getName() {
        return "enumeration";
    }
    
    @Override
    public int getPrecedence() {
        return 10000;
    }

    @Override
    public Map<String, Object> populateModelVariables(String tagName, Map<String, String> tagAttributes, UltraTemplateContext context) {
        List<DataDrivenEnumerationValue> enumValues = ddeVariableExpression.getEnumValues(tagAttributes.get("key"), tagAttributes.get("sort"));
        return ImmutableMap.of("enumValues", (Object) enumValues);
    }

}
