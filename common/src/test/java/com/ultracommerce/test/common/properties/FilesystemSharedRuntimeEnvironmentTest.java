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
package com.ultracommerce.test.common.properties;

import com.ultracommerce.common.config.UltraEnvironmentConfiguringApplicationListener;
import com.ultracommerce.common.config.UltraEnvironmentConfigurer;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = UltraEnvironmentConfiguringApplicationListener.class)
@ActiveProfiles("production")
@DirtiesContext
public class FilesystemSharedRuntimeEnvironmentTest {

    @Autowired
    protected Environment env;
    
    // Inside of a static @BeforeClass to ensure this code executes before Spring starts the appctx
    @BeforeClass
    public static void setOverrideProperty() {
        String sharedOverridePropertiesPath = FilesystemSharedRuntimeEnvironmentTest.class.getClassLoader().getResource("sharedoverridestest.properties").getFile();
        sharedOverridePropertiesPath = sharedOverridePropertiesPath.replace("%20", " ");
        sharedOverridePropertiesPath = sharedOverridePropertiesPath.replace("%40", "@");
        System.setProperty(UltraEnvironmentConfigurer.PROPERTY_SHARED_OVERRIDES_PROPERTY, sharedOverridePropertiesPath);
    }
    
    // don't impact other tests with my property override
    @AfterClass
    public static void clearOverrideProperty() {
        System.clearProperty(UltraEnvironmentConfigurer.PROPERTY_SHARED_OVERRIDES_PROPERTY);
    }
    
    @Test
    // this is a customized version of the Spring appctx with an overridden Environment, make sure it gets reset
    // by dirtying the context
    @DirtiesContext
    public void testPropertiesWereOverridden() {
        Assert.assertEquals("sharedoverridevalue", env.getProperty(DefaultDevelopmentOverridePropertiesTest.TEST_PROPERTY));
        Assert.assertTrue(((ConfigurableEnvironment) env).getPropertySources().contains(UltraEnvironmentConfigurer.SHARED_OVERRIDE_SOURCES_NAME));
    }
}
