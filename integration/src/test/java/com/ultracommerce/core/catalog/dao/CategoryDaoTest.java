/*
 * #%L
 * UltraCommerce Integration
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
package com.ultracommerce.core.catalog.dao;

import com.ultracommerce.core.catalog.CategoryDaoDataProvider;
import com.ultracommerce.core.catalog.domain.Category;
import com.ultracommerce.core.catalog.domain.FeaturedProduct;
import com.ultracommerce.core.catalog.domain.FeaturedProductImpl;
import com.ultracommerce.core.catalog.domain.Product;
import com.ultracommerce.core.catalog.domain.ProductImpl;
import com.ultracommerce.core.catalog.domain.Sku;
import com.ultracommerce.core.catalog.domain.SkuImpl;
import com.ultracommerce.core.catalog.service.CatalogService;
import com.ultracommerce.test.TestNGSiteIntegrationSetup;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

public class CategoryDaoTest extends TestNGSiteIntegrationSetup {

    @Resource
    private CategoryDao categoryDao;
    @Resource
    private CatalogService catalogService;

    @Test(groups =  {"testSetFeaturedProducts"}, dataProvider="basicCategory", dataProviderClass=CategoryDaoDataProvider.class)
    @Transactional
    public void testSetFeaturedProducts(Category category) {
        category = catalogService.saveCategory(category);

        Sku sku = new SkuImpl();
        sku.setDescription("This thing will change your life");
        sku.setName("Test Product");
        catalogService.saveSku(sku);
        
        Product product = new ProductImpl();
        product.setModel("KGX200");
        product.setDefaultSku(sku);
        product = catalogService.saveProduct(product);

        FeaturedProduct featuredProduct = new FeaturedProductImpl();
        featuredProduct.setCategory(category);
        featuredProduct.setProduct(product);
        featuredProduct.setPromotionMessage("BUY ME NOW!!!!");
        List<FeaturedProduct> featuredProducts = new ArrayList<>();
        featuredProducts.add(featuredProduct);
        category.setFeaturedProducts(featuredProducts);
        category = catalogService.saveCategory(category);

        Category categoryTest = categoryDao.readCategoryById(category.getId());
        FeaturedProduct featuredProductTest = categoryTest.getFeaturedProducts().get(0);

        assert (featuredProductTest.getPromotionMessage() == "BUY ME NOW!!!!");
        assert (featuredProductTest.getProduct().getModel().equals("KGX200"));
    }

}
