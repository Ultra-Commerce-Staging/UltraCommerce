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
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * Test to verify that properties are added to the environment early enough via the ApplicationListener so that @ConditionalOnProperty works
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = UltraEnvironmentConfiguringApplicationListener.class)
public class ConditionalOnPropertyTest {

    @Configuration
    public static class ConditionalOnPropertyConfig {
        
        @Bean
        @ConditionalOnProperty("test.property.source")
        public String shouldFind() {
            return "shouldFind";
        }
        
        @Bean
        @ConditionalOnProperty("property.that.does.not.exist")
        public String shouldNotFind() {
            return "shouldNotFind";
        }
    }
    
    @Autowired
    ApplicationContext ctx;
    
    @Test
    public void foundPropertyInstantiatesBean() {
        Assert.assertTrue(ctx.containsBean("shouldFind"));
    }
    
    @Test
    public void missingPropertyPreventsBean() {
        Assert.assertFalse(ctx.containsBean("shouldNotFind"));
    }
}
