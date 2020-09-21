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
package com.ultracommerce.common.demo;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.ClassUtils;

/**
 * Condition to use if the Multi-Tenant module is available at runtime
 * 
 * @author Jeff Fischer
 * @author Phillip Verheyden (phillipuniverse)
 */
public class MTCondition implements Condition {
    
    public static final String[] CONDITION_CLASSES = new String[] {"com.ultracommerce.tenant.persistence.CatalogFilterEnabler"};
    
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        boolean present = false;
        int i = 0;
        while (!present && i < CONDITION_CLASSES.length) {
            present = ClassUtils.isPresent(CONDITION_CLASSES[i], context.getClassLoader());
            i++;
        }
        return present;
    }

}
