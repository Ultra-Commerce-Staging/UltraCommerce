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
package com.ultracommerce.test.common.boot.properties;

import com.ultracommerce.common.config.UltraEnvironmentConfiguringApplicationListener;
import com.ultracommerce.common.config.UltraEnvironmentConfigurer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Test that ensures our {@link UltraEnvironmentConfiguringApplicationListener} is applied automatically in a Spring Boot environment, and that
 * any application.properties files overrides any of the Ultra sources
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringBootPropertiesTest {

    @Configuration
    public static class PropertiesConfig{}
    
    public static final String TEST_PROPERTY = "test.property.source";
    
    @Autowired
    protected ConfigurableEnvironment env;
    
    @Test
    public void testProfileSourcesRegisteredOverridesCommon() {
        Assert.assertTrue(env.getPropertySources().contains(UltraEnvironmentConfigurer.FRAMEWORK_SOURCES_NAME));
        Assert.assertTrue(env.getPropertySources().contains(UltraEnvironmentConfigurer.PROFILE_AWARE_SOURCES_NAME));
        
        String developmentSourceName = new ClassPathResource("common-test-properties/profile-aware-properties/development.properties").getDescription();
        CompositePropertySource profileAwareSource = (CompositePropertySource) env.getPropertySources().get(UltraEnvironmentConfigurer.PROFILE_AWARE_SOURCES_NAME);
        
        Assert.assertTrue(profileAwareSource.getPropertySources().contains(PropertySource.named(developmentSourceName)));
    }
    
    @Test
    public void testBootPropertiesOverrideUltra() {
        Assert.assertEquals("boot.property.value", env.getProperty(TEST_PROPERTY));
    }
    
}
