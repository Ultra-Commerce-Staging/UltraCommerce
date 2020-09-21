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
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Tests that our property configurations work with other environments that aren't standard
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = UltraEnvironmentConfiguringApplicationListener.class)
@ActiveProfiles("otherenvironment")
@DirtiesContext
public class OtherEnvironmentOverridePropertiesTest {

    @Autowired
    protected Environment env;
    
    @Test
    public void testOtherEnvironmentOverride() {
        Assert.assertEquals("otherenvironmentvalue", env.getProperty(DefaultDevelopmentOverridePropertiesTest.TEST_PROPERTY));
    }
}
