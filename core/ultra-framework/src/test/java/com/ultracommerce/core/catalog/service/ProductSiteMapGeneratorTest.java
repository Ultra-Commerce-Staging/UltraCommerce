/*
 * #%L
 * UltraCommerce Framework
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
package com.ultracommerce.core.catalog.service;

import com.ultracommerce.common.sitemap.domain.SiteMapGeneratorConfiguration;
import com.ultracommerce.common.sitemap.domain.SiteMapGeneratorConfigurationImpl;
import com.ultracommerce.common.sitemap.exception.SiteMapException;
import com.ultracommerce.common.sitemap.service.SiteMapGeneratorTest;
import com.ultracommerce.common.sitemap.service.type.SiteMapChangeFreqType;
import com.ultracommerce.common.sitemap.service.type.SiteMapGeneratorType;
import com.ultracommerce.common.sitemap.service.type.SiteMapPriorityType;
import com.ultracommerce.core.catalog.dao.ProductDao;
import com.ultracommerce.core.catalog.domain.Product;
import com.ultracommerce.core.catalog.domain.ProductImpl;
import com.ultracommerce.core.catalog.domain.Sku;
import com.ultracommerce.core.catalog.domain.SkuImpl;
import org.easymock.EasyMock;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Product site map generator tests
 * 
 * @author Joshua Skorton (jskorton)
 */
public class ProductSiteMapGeneratorTest extends SiteMapGeneratorTest {

    @Test
    public void testProductSiteMapGenerator() throws SiteMapException, IOException {

        Product p1 = new ProductImpl();
        p1.setUrl("/hot-sauces/sudden_death_sauce");
        Sku s1 = new SkuImpl();
        p1.setDefaultSku(s1);
        Product p2 = new ProductImpl();
        p2.setUrl("hot-sauces/sweet_death_sauce");
        Sku s2 = new SkuImpl();
        p2.setDefaultSku(s2);
        Product p3 = new ProductImpl();
        p3.setUrl("/hot-sauces/hoppin_hot_sauce");
        Sku s3 = new SkuImpl();
        p3.setDefaultSku(s3);
        Product p4 = new ProductImpl();
        p4.setUrl("/hot-sauces/day_of_the_dead_chipotle_hot_sauce");
        Sku s4 = new SkuImpl();
        p4.setDefaultSku(s4);

        List<Product> products = new ArrayList<Product>();
        products.add(p1);
        products.add(p2);
        products.add(p3);
        products.add(p4);
        
        ProductDao productDao = EasyMock.createMock(ProductDao.class);
        EasyMock.expect(productDao.readAllActiveProducts(EasyMock.eq(0), EasyMock.eq(5))).andReturn(products);
        EasyMock.replay(productDao);

        ProductSiteMapGenerator psmg = new ProductSiteMapGenerator();
        psmg.setProductDao(productDao);
        psmg.setPageSize(5);

        SiteMapGeneratorConfiguration smgc = new SiteMapGeneratorConfigurationImpl();
        smgc.setDisabled(false);
        smgc.setSiteMapGeneratorType(SiteMapGeneratorType.PRODUCT);
        smgc.setSiteMapChangeFreq(SiteMapChangeFreqType.HOURLY);
        smgc.setSiteMapPriority(SiteMapPriorityType.POINT5);

        testGenerator(smgc, psmg);

        File file1 = fileService.getResource("/sitemap_index.xml");
        File file2 = fileService.getResource("/sitemap1.xml");
        File file3 = fileService.getResource("/sitemap2.xml");

        compareFiles(file1, "src/test/resources/com/ultracommerce/sitemap/product/sitemap_index.xml");
        compareFiles(file2, "src/test/resources/com/ultracommerce/sitemap/product/sitemap1.xml");
        compareFiles(file3, "src/test/resources/com/ultracommerce/sitemap/product/sitemap2.xml");

    }

}
