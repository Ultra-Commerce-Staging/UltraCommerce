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

import com.ultracommerce.common.util.UCSystemProperty;
import com.ultracommerce.common.web.expression.PropertiesVariableExpression;
import com.ultracommerce.presentation.condition.ConditionalOnTemplating;
import com.ultracommerce.presentation.dialect.AbstractUltraVariableModifierProcessor;
import com.ultracommerce.presentation.model.UltraTemplateContext;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * <p>
 * Looks up the value of a configuration variable and adds the value to the model.
 * 
 * <p>
 * While this adds the configuration value onto the model, you might want to use the value of this in larger expression. In
 * that instance you may want to use {@link PropertiesVariableExpression} instead with {@code #props.get('property')}.
 * 
 * @parameter name (required) the name of the system property to look up
 * @parameter resultVar (optional) what model variable the system property value is added to, defaults to <b>value</b>
 * 
 * @author bpolster
 * @see {@link PropertiesVariableExpression}
 * @deprecated use {@link PropertiesVariableExpression} instead
 */
@Deprecated
@Component("ucConfigVariableProcessor")
@ConditionalOnTemplating
public class ConfigVariableProcessor extends AbstractUltraVariableModifierProcessor {

    @Override
    public String getName() {
        return "config";
    }
    
    @Override
    public int getPrecedence() {
        return 10000;
    }

    /* (non-Javadoc)
     * @see com.ultracommerce.presentation.dialect.AbstractModelVariableModifierProcessor#populateModelVariables(java.lang.String, java.util.Map, java.util.Map)
     */
    @Override
    public Map<String, Object> populateModelVariables(String tagName, Map<String, String> tagAttributes, UltraTemplateContext context) {
        String resultVar = tagAttributes.get("resultVar");
        if (resultVar == null) {
            resultVar = "value";
        }

        String attributeName = tagAttributes.get("name");
        String attributeValue = UCSystemProperty.resolveSystemProperty(attributeName);
        
        return ImmutableMap.of(resultVar, (Object) attributeValue);
    }

}
