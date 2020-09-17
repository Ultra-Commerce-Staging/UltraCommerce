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

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * <p>
 * This is by default added into {@code META-INF/spring.factories} with the {@link org.springframework.boot.env.EnvironmentPostProcessor} key. 
 * In non-boot applications refer to {@link com.ultracommerce.common.config.UltraEnvironmentConfiguringApplicationListener}
 * 
 * @author Jay Aisenbrey (cja769)
 * @since 6.1
 * @see UltraEnvironmentConfigurer
 *
 */
public class UltraEnvironmentConfiguringPostProcessor extends UltraEnvironmentConfigurer implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        configure(environment);
    }

}
