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
package com.ultracommerce.test.common.context.merge.reader;

import com.ultracommerce.common.extensibility.MergeXmlBeanDefinitionReader;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import lombok.Data;

/**
 * Spring integratino tests to verify that the MergeXmlBeanDefinitionReader is working as expected
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
@RunWith(SpringRunner.class)
public class MergeBeanDefinitionTest {

    @Configuration
    @ImportResource(locations = "context/reader/merge/*.xml", reader = MergeXmlBeanDefinitionReader.class)
    public static class Config {
        
        // This essentially has zero effect because everything is overridden
        @Bean
        public ExampleBean exampleBean() {
            ExampleBean ex = new ExampleBean();
            ex.setField3("java");
            return ex;
        }
    }
    
    @Data
    public static class ExampleBean {

        public String field1;
        public String field2;
        public String field3;
    }
    
    public static class AnonymousBean { }
    
    @Autowired
    private ExampleBean ex;
    
    @Autowired
    private List<AnonymousBean> anonymousBeans;
    
    @Test
    public void validateDefinitionMerging() {
        Assert.assertEquals("file3", ex.field1);
        Assert.assertEquals("file2", ex.field2);
        // Not expecting that the @Bean method will do anything, it should skip any attempt at merging that bean definition
        // with the XML versions
        Assert.assertEquals(null, ex.field3);
    }
    
    @Test
    public void noDuplicatedAnonymousBeans() {
        Assert.assertEquals(1, anonymousBeans.size());
    }
    
}
