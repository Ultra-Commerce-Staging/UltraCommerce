/*
 * #%L
 * UltraCommerce Integration
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
package com.ultracommerce.test.junit;
/**
 * 
 */

import com.ultracommerce.core.catalog.service.CatalogService;
import com.ultracommerce.test.config.UltraSiteIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * 
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
@UltraSiteIntegrationTest
@RunWith(SpringRunner.class)
public class JUnitExampleTest {
    
    @Resource
    private CatalogService catalogService;
    
    @Test
    public void testInjectionWorks() {
        Assert.assertNotEquals(catalogService, null);
    }
}
