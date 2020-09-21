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
package com.ultracommerce.test.common.web.controller.annotation;

import com.ultracommerce.common.web.controller.annotation.EnableFrameworkRestControllers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Tests for {@link EnableFrameworkRestControllers}
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
@RunWith(SpringRunner.class)
public class FrameworkRestControllerTest {

    @Configuration
    @EnableFrameworkRestControllers
    static class Config {}
    
    @Autowired
    private ApplicationContext appctx;
    
    @Test
    public void frameworkControllerPrefixed() {
        Assert.assertEquals("ucTestFrameworkRestController", appctx.getBeanNamesForType(TestFrameworkRestController.class)[0]);
    }
    
    @Test(expected = NoSuchBeanDefinitionException.class)
    public void noControllerActivated() {
        appctx.getBean(TestFrameworkController.class);
    }
}
