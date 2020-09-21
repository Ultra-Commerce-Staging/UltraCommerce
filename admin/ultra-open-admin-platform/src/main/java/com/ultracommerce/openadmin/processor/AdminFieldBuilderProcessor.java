/*
 * #%L
 * UltraCommerce Open Admin Platform
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
package com.ultracommerce.openadmin.processor;

import com.ultracommerce.openadmin.web.rulebuilder.dto.FieldWrapper;
import com.ultracommerce.openadmin.web.rulebuilder.service.RuleBuilderFieldService;
import com.ultracommerce.openadmin.web.rulebuilder.service.RuleBuilderFieldServiceFactory;
import com.ultracommerce.openadmin.web.service.AdminFieldBuilderProcessorExtensionManager;
import com.ultracommerce.presentation.condition.ConditionalOnTemplating;
import com.ultracommerce.presentation.dialect.AbstractUltraVariableModifierProcessor;
import com.ultracommerce.presentation.dialect.UltraDialectPrefix;
import com.ultracommerce.presentation.model.UltraTemplateContext;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

import javax.annotation.Resource;

/**
 * @author Elbert Bautista (elbertbautista)
 */
@Component("ucAdminFieldBuilderProcessor")
@ConditionalOnTemplating
public class AdminFieldBuilderProcessor extends AbstractUltraVariableModifierProcessor {

    @Resource(name = "ucRuleBuilderFieldServiceFactory")
    protected RuleBuilderFieldServiceFactory ruleBuilderFieldServiceFactory;
    
    @Resource(name="ucAdminFieldBuilderProcessorExtensionManager")
    protected AdminFieldBuilderProcessorExtensionManager extensionManager;

    @Override
    public String getName() {
        return "admin_field_builder";
    }
    
    @Override
    public String getPrefix() {
        return UltraDialectPrefix.UC_ADMIN;
    }
    
    @Override
    public int getPrecedence() {
        return 100;
    }

    @Override
    public Map<String, Object> populateModelVariables(String tagName, Map<String, String> tagAttributes, UltraTemplateContext context) {
        FieldWrapper fieldWrapper = new FieldWrapper();
        String fieldBuilder = context.parseExpression(tagAttributes.get("fieldBuilder"));
        String ceilingEntity = context.parseExpression(tagAttributes.get("ceilingEntity"));

        if (fieldBuilder != null) {
            RuleBuilderFieldService ruleBuilderFieldService = ruleBuilderFieldServiceFactory.createInstance(fieldBuilder);
            if (ruleBuilderFieldService != null) {
                fieldWrapper = ruleBuilderFieldService.buildFields();
            }
        }
        
        if (extensionManager != null) {
            extensionManager.getProxy().modifyRuleBuilderFields(fieldBuilder, ceilingEntity, fieldWrapper);
        }
        
        return ImmutableMap.of("fieldWrapper", (Object) fieldWrapper);
    }
    
    @Override
    public boolean useGlobalScope() {
        return false;
    }


}
