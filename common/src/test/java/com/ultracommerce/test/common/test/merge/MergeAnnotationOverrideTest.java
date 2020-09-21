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
package com.ultracommerce.test.common.test.merge;

import com.ultracommerce.common.extensibility.FrameworkXmlBeanDefinitionReader;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Resource;

/**
 * @author Nick Crum ncrum
 */
@RunWith(SpringRunner.class)
public class MergeAnnotationOverrideTest {

    @Configuration
    @Import({MainConfiguration.UltraFrameworkConfiguration.class}) // import our Ultra configurations
    @ImportResource("classpath:context/merge/local.xml") // import our local xml configuration
    public static class MainConfiguration {

        @ImportResource(value = {                               // define our Ultra xml configurations
                "classpath:context/merge/uc-framework.xml",
                "classpath:context/merge/uc-module.xml"
        }, reader = FrameworkXmlBeanDefinitionReader.class)
        public static class UltraFrameworkConfiguration {}
    }

    @Resource(name="mergedList")
    protected Set<String> mergedList;

    @Test
    public void testMergedOrder() {
        Iterator<String> iterator = mergedList.iterator();
        Assert.assertEquals("common-xml", iterator.next());
        Assert.assertEquals("framework-xml", iterator.next());
        Assert.assertEquals("module-xml", iterator.next());
        Assert.assertEquals("framework-config1", iterator.next());
        Assert.assertEquals("framework-config2", iterator.next());
        Assert.assertEquals("local-xml", iterator.next());
        Assert.assertEquals("local-config1", iterator.next());
        Assert.assertEquals("local-config2", iterator.next());
    }


}
