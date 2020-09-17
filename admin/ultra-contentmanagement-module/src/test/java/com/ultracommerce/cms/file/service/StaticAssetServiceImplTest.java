/*
 * #%L
 * UltraCommerce CMS Module
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
package com.ultracommerce.cms.file.service;

import com.ultracommerce.common.file.service.StaticAssetPathServiceImpl;

import junit.framework.TestCase;

/**
 * Created by bpolster.
 */
public class StaticAssetServiceImplTest extends TestCase {
    
    public void testConvertURLProperties() throws Exception {
        StaticAssetPathServiceImpl staticAssetPathService = new StaticAssetPathServiceImpl();
        staticAssetPathService.setStaticAssetUrlPrefix("cmsstatic");
        staticAssetPathService.setStaticAssetEnvironmentUrlPrefix("http://images.mysite.com/myapp/cmsstatic");
        
        String url = staticAssetPathService.convertAssetPath("/cmsstatic/product.jpg","myapp", false);
        assertTrue(url.equals("http://images.mysite.com/myapp/cmsstatic/product.jpg"));

        staticAssetPathService.setStaticAssetEnvironmentUrlPrefix("http://images.mysite.com");
        url = staticAssetPathService.convertAssetPath("/cmsstatic/product.jpg","myapp", false);
        assertTrue(url.equals("http://images.mysite.com/product.jpg"));

        url = staticAssetPathService.convertAssetPath("/cmsstatic/product.jpg","myapp", true);
        assertTrue(url.equals("https://images.mysite.com/product.jpg"));


        staticAssetPathService.setStaticAssetEnvironmentUrlPrefix(null);
        url = staticAssetPathService.convertAssetPath("/cmsstatic/product.jpg","myapp", true);
        assertTrue(url.equals("/myapp/cmsstatic/product.jpg"));

        url = staticAssetPathService.convertAssetPath("cmsstatic/product.jpg","myapp", true);
        assertTrue(url.equals("/myapp/cmsstatic/product.jpg"));

    }
}
