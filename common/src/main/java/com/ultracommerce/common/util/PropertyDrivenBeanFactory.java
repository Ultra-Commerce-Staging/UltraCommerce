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
package com.ultracommerce.common.util;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.reflect.ConstructorUtils;
import com.ultracommerce.common.exception.ExceptionHelper;
import org.springframework.beans.BeanUtils;

/**
 * @author Jeff Fischer
 */
public class PropertyDrivenBeanFactory {

    public static Object createInstance(String className) {
        return createInstance(className, new String[]{});
    }

    public static Object createInstance(String className, String... constructorArgs) {
        try {
            Object bean;
            if (!ArrayUtils.isEmpty(constructorArgs)) {
                bean = ConstructorUtils.invokeExactConstructor(Class.forName(className), constructorArgs);
            } else {
                bean = BeanUtils.instantiateClass(Class.forName(className));
            }
            return bean;
        } catch (Exception e) {
            throw ExceptionHelper.refineException(e);
        }
    }
}
