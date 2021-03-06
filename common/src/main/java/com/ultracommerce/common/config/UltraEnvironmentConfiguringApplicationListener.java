/*
 * #%L
 * UltraCommerce Common Libraries
 * %%
 * Copyright (C) 2009 - 2019 Ultra Commerce
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
package com.ultracommerce.common.config;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * <p>
 * In non-boot this class should be hooked up in your web.xml as shown below
 * 
 * <pre>
 * {@literal
 * <context-param>
 *   <param-name>contextInitializerClasses</param-name>
 *   <param-value>com.ultracommerce.common.config.UltraEnvironmentConfiguringApplicationListener</param-value>
 * </context-param>
 * }
 * </pre>
 * 
 * For Spring Boot deployments see {@link com.ultracommerce.common.config.UltraEnvironmentConfiguringPostProcessor}
 * 
 * @author Jeff Fischer
 * @author Phillip Verheyden (phillipuniverse)
 * @since 5.2
 * @see UltraEnvironmentConfigurer
 */
public class UltraEnvironmentConfiguringApplicationListener extends UltraEnvironmentConfigurer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        configure(applicationContext.getEnvironment());
    }

}
