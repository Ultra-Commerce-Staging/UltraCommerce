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
/**
 * 
 */
package com.ultracommerce.test.common.context.override.reader;

import com.ultracommerce.common.extensibility.FrameworkXmlBeanDefinitionReader;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * Tests whether or not bean overriding works when beans are read via the {@link FrameworkXmlBeanDefinitionReader} and then overridden
 * via an {@literal @}Configuration class.
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = FrameworkXmlBeanDefinitionTestConfiguration.class)
public class FrameworkXmlBeanDefinitionReaderTest {

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected ObjectMapper objectMapper;

    @Test
    public void testOverrideInjection() {
        Assert.assertEquals(BCryptPasswordEncoder.class.getName(), passwordEncoder.getClass().getName());

        Assert.assertEquals(XmlMapper.class.getName(), objectMapper.getClass().getName());
    }
}
