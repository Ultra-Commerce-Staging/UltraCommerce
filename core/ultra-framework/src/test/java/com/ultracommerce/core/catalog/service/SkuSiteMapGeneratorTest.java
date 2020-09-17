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
import com.ultracommerce.core.catalog.dao.SkuDao;
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
 * Sku site map generator tests
 * 
 * @author Joshua Skorton (jskorton)
 */
public class SkuSiteMapGeneratorTest extends SiteMapGeneratorTest {

    @Test
    public void testSkuSiteMapGenerator() throws SiteMapException, IOException {

        Product p1 = new ProductImpl();
        p1.setUrl("/hot-sauces/sudden_death_sauce");
        Sku s1 = new SkuImpl();
        p1.setDefaultSku(s1);
        
        Product p2 = new ProductImpl();
        p2.setUrl("/merchandise/hawt_like_a_habanero_mens");
        Sku s2 = new SkuImpl();
        s2.setUrlKey("/black_s");
        s2.setProduct(p2);
        Sku s3 = new SkuImpl();
        s3.setUrlKey("/black_m");
        s3.setProduct(p2);
        Sku s4 = new SkuImpl();
        s4.setUrlKey("/black_l");
        s4.setProduct(p2);
        
        List<Sku> skus = new ArrayList<Sku>();
        skus.add(s1);
        skus.add(s2);
        skus.add(s3);
        skus.add(s4);
        
        SkuDao skuDao = EasyMock.createMock(SkuDao.class);
        EasyMock.expect(skuDao.readAllActiveSkus(EasyMock.eq(0), EasyMock.eq(5))).andReturn(skus);
        EasyMock.replay(skuDao);

        SkuSiteMapGenerator ssmg = new SkuSiteMapGenerator();
        ssmg.setSkuDao(skuDao);
        ssmg.setPageSize(5);

        SiteMapGeneratorConfiguration smgc = new SiteMapGeneratorConfigurationImpl();
        smgc.setDisabled(false);
        smgc.setSiteMapGeneratorType(SiteMapGeneratorType.SKU);
        smgc.setSiteMapChangeFreq(SiteMapChangeFreqType.HOURLY);
        smgc.setSiteMapPriority(SiteMapPriorityType.POINT5);

        testGenerator(smgc, ssmg);

        File file1 = fileService.getResource("/sitemap_index.xml");
        File file2 = fileService.getResource("/sitemap1.xml");
        File file3 = fileService.getResource("/sitemap2.xml");

        compareFiles(file1, "src/test/resources/com/ultracommerce/sitemap/sku/sitemap_index.xml");
        compareFiles(file2, "src/test/resources/com/ultracommerce/sitemap/sku/sitemap1.xml");
        compareFiles(file3, "src/test/resources/com/ultracommerce/sitemap/sku/sitemap2.xml");

    }

}
