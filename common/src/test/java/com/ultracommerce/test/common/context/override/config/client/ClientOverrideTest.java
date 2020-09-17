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
package com.ultracommerce.test.common.context.override.config.client;

import com.ultracommerce.common.email.service.info.EmailInfo;
import com.ultracommerce.common.extensibility.FrameworkXmlBeanDefinitionReader;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Nick Crum ncrum
 */
@RunWith(SpringRunner.class)
public class ClientOverrideTest {

    @Configuration
    @Import(MainRootConfig.class)
    public static class MainConfiguration {

        @Configuration
        @ImportResource(value = "classpath:context/config/client-override.xml", reader = FrameworkXmlBeanDefinitionReader.class)
        public static class FrameworkConfig {}
    }

    @Autowired
    protected EmailInfo emailInfo;

    @Test
    public void testOverride() {
        Assert.assertEquals("client", emailInfo.getFromAddress());
    }
}
