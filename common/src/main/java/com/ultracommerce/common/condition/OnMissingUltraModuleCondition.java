/*
 * #%L
 * UltraCommerce Common Libraries
 * %%
 * Copyright (C) 2009 - 2017 Ultra Commerce
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
package com.ultracommerce.common.condition;

import org.apache.commons.lang3.StringUtils;
import com.ultracommerce.common.module.UltraModuleRegistration.UltraModuleEnum;
import com.ultracommerce.common.module.ModulePresentUtil;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

/**
 * @author Brandon Hines
 */
public class OnMissingUltraModuleCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Map<String, Object> attributes = metadata.getAnnotationAttributes(ConditionalOnMissingUltraModule.class.getName());
        UltraModuleEnum module = (UltraModuleEnum) attributes.get("value");
        String moduleName = (UltraModuleEnum.IGNORED != module) ? module.getName() : (String) attributes.get("moduleName");

        return StringUtils.isNotEmpty(moduleName) && !ModulePresentUtil.isPresent(moduleName);
    }
}
