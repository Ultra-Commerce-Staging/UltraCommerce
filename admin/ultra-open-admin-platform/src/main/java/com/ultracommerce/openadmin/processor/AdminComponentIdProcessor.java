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

import org.apache.commons.lang3.StringUtils;
import com.ultracommerce.openadmin.web.form.component.ListGrid;
import com.ultracommerce.openadmin.web.form.entity.Field;
import com.ultracommerce.presentation.condition.ConditionalOnTemplating;
import com.ultracommerce.presentation.dialect.AbstractUltraAttributeModifierProcessor;
import com.ultracommerce.presentation.dialect.UltraDialectPrefix;
import com.ultracommerce.presentation.model.UltraAttributeModifier;
import com.ultracommerce.presentation.model.UltraTemplateContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * A Thymeleaf processor that will generate the appropriate ID for a given admin component.
 * 
 * @author Andre Azzolini (apazzolini)
 */
@Component("ucAdminComponentIdProcessor")
@ConditionalOnTemplating
public class AdminComponentIdProcessor extends AbstractUltraAttributeModifierProcessor {

    @Override
    public String getName() {
        return "component_id";
    }
    
    @Override
    public String getPrefix() {
        return UltraDialectPrefix.UC_ADMIN;
    }
    
    @Override
    public int getPrecedence() {
        return 10002;
    }
    
    @Override
    public UltraAttributeModifier getModifiedAttributes(String tagName, Map<String, String> tagAttributes, String attributeName, String attributeValue, UltraTemplateContext context) {
        Object component = context.parseExpression(attributeValue);

        String fieldName = "";
        String id = "";
        
        if (component instanceof ListGrid) {
            ListGrid lg = (ListGrid) component;
            
            fieldName = "listGrid-" + lg.getListGridType();
            if (StringUtils.isNotBlank(lg.getSubCollectionFieldName())) {
                fieldName += "-" + lg.getSubCollectionFieldName();
            }
        } else if (component instanceof Field) {
            Field field = (Field) component;
            fieldName = "field-" + field.getName();
        }
        
        if (StringUtils.isNotBlank(fieldName)) {
            id = cleanCssIdString(fieldName);
        }
        
        Map<String, String> attrs = new HashMap<>();
        attrs.put("id", id);
        return new UltraAttributeModifier(attrs);
    }
    
    protected String cleanCssIdString(String in) {
        in = in.replaceAll("[^a-zA-Z0-9-]", "-");
        return in;
    }

}
