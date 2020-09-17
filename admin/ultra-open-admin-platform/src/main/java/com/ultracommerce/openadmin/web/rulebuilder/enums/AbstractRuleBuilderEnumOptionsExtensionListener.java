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
package com.ultracommerce.openadmin.web.rulebuilder.enums;

import org.apache.commons.lang3.reflect.FieldUtils;
import com.ultracommerce.common.UltraEnumerationType;

import java.util.Map;
import java.util.Map.Entry;

/**
 * Abstract extension listener for rule builder enum options that handles the boilerplate code required for setting up
 * the response to the client. This class provides two abstract methods that must be implemented, {@link #getVariableName()}
 * and {@link #getEnumClass()}. Generates a String with the following pattern:
 * 
 * var variableName = [
 *     {"enumType": "enumFriendlyType"},
 *     {"enumType2": "enumFriendlyType2"},
 *     ...
 *     {"enumTypeN": "enumFriendlyTypeN"}
 * ];
 * 
 * @author Andre Azzolini (apazzolini)
 */
public abstract class AbstractRuleBuilderEnumOptionsExtensionListener implements RuleBuilderEnumOptionsExtensionListener {
    
    public String getOptionValues() {
        StringBuilder sb = new StringBuilder();
        for (Entry<String, Class<? extends UltraEnumerationType>> entry : getValuesToGenerate().entrySet()) {
            try {
                sb.append("var ").append(entry.getKey()).append(" = [");
                
                int i = 0;
                Map<String, ? extends UltraEnumerationType> types = getTypes(entry.getValue());
                for (Entry<String, ? extends UltraEnumerationType> entry2 : types.entrySet()) {
                    sb.append("{\"" + entry2.getValue().getType() + "\": \"" + entry2.getValue().getFriendlyType() + "\"}");
                    if (++i < types.size()) {
                        sb.append(", ");
                    }
                }
                sb.append("]; \r\n");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return sb.toString();
    }
    
    @SuppressWarnings("unchecked")
    protected Map<String, ? extends UltraEnumerationType> getTypes(Class<? extends UltraEnumerationType> clazz) {
        try {
            return (Map<String, ? extends UltraEnumerationType>) FieldUtils.readStaticField(clazz, "TYPES", true);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * @return a map representing the various values that this extension listener should generate
     */
    protected abstract Map<String, Class<? extends UltraEnumerationType>> getValuesToGenerate();

}
