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
package com.ultracommerce.test.common.context.enable;

import com.ultracommerce.common.config.UltraEnvironmentConfiguringApplicationListener;
import com.ultracommerce.common.config.EnableUltraAdminAutoConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * 
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = UltraEnvironmentConfiguringApplicationListener.class)
@WebAppConfiguration
@ActiveProfiles("mbeansdisabled")
public class EnableUltraAdminAutoConfigurationTest {

    @Configuration
    @EnableUltraAdminAutoConfiguration
    public static class AdminConfig {}
    
    @Autowired
    protected ApplicationContext ctx;
    
    @Test
    public void adminBeanOverridesRoot() {
        Assert.assertEquals("admin", ctx.getBean("override"));
    }
    
    @Test
    public void cannotFindSiteBean() {
        Assert.assertFalse(ctx.containsBean("site"));
    }
    
    @Test
    public void canFindAdminBean() {
        Assert.assertTrue(ctx.containsBean("admin"));
        Assert.assertEquals("admin", ctx.getBean("admin"));
    }
    
    @Test
    public void canFindRootBean() {
        Assert.assertTrue(ctx.containsBean("root"));
        Assert.assertEquals("root", ctx.getBean("root"));
    }
}
