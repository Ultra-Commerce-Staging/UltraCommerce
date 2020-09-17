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
/**
 * 
 */
package com.ultracommerce.common.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.util.StringUtils;


/**
 * <p>
 * Prefixes a default Spring-generated bean name with 'bl', and also uppercases the first character of the default bean name.
 * If the bean name is already prefixed with {@code bl}|, this does nothing.
 * 
 * <p>
 * Example: {@code catalogEndpoint -> ucCatalogEndpoint}, {@code ucCatalogService -> ucCatalogService}
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
public class UltraBeanNameGenerator extends AnnotationBeanNameGenerator {

    public static final String ULTRA_BEAN_PREFIX = "uc";
    
    @Override
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        String beanName = super.generateBeanName(definition, registry);
        if (!beanName.startsWith(ULTRA_BEAN_PREFIX)) {
            beanName = ULTRA_BEAN_PREFIX + StringUtils.capitalize(beanName);
        }
        
        return beanName;
    }
}
