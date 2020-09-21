/*
 * #%L
 * UltraCommerce Admin Module
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
package com.ultracommerce.admin.web.rulebuilder.service.options;

import org.apache.commons.lang3.reflect.FieldUtils;
import com.ultracommerce.common.UltraEnumerationType;
import com.ultracommerce.common.time.HourOfDayType;
import com.ultracommerce.core.inventory.service.type.InventoryType;
import com.ultracommerce.openadmin.web.rulebuilder.enums.AbstractRuleBuilderEnumOptionsExtensionListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Rule Builder enum options provider for {@link HourOfDayType}
 * 
 * @author Andre Azzolini (apazzolini)
 */
@Component("ucInventoryTypeOptionsExtensionListener")
public class InventoryTypeEnumOptionsExtensionListener extends AbstractRuleBuilderEnumOptionsExtensionListener {

    /**
     * Overridden to remove deprecated options
     */
    @Override
    protected Map<String, ? extends UltraEnumerationType> getTypes(Class<? extends UltraEnumerationType> clazz) {
        
        try {
            Map<String, ? extends UltraEnumerationType> options =
                    (Map<String, ? extends UltraEnumerationType>) FieldUtils.readStaticField(clazz, "TYPES", true);
            options.remove("NONE");
            options.remove("BASIC");
            return options;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    protected Map<String, Class<? extends UltraEnumerationType>> getValuesToGenerate() {
        Map<String, Class<? extends UltraEnumerationType>> map = 
                new HashMap<String, Class<? extends UltraEnumerationType>>();
        
        map.put("ucOptions_InventoryType", InventoryType.class);
        
        return map;
    }

}
