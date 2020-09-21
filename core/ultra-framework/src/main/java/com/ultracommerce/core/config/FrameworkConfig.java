/*
 * #%L
 * UltraCommerce Common Libraries
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
package com.ultracommerce.core.config;

import com.ultracommerce.common.config.FrameworkCommonClasspathPropertySource;
import com.ultracommerce.core.catalog.dao.CategoryDao;
import com.ultracommerce.core.catalog.dao.CategoryDaoImpl;
import com.ultracommerce.core.catalog.dao.ProductDao;
import com.ultracommerce.core.catalog.dao.ProductDaoImpl;
import com.ultracommerce.core.catalog.dao.SkuDao;
import com.ultracommerce.core.catalog.dao.SkuDaoImpl;
import com.ultracommerce.core.offer.dao.OfferDao;
import com.ultracommerce.core.offer.dao.OfferDaoImpl;
import com.ultracommerce.core.search.redirect.dao.SearchRedirectDao;
import com.ultracommerce.core.search.redirect.dao.SearchRedirectDaoImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @author Jeff Fischer
 */
@Configuration
public class FrameworkConfig {

    @Order(FrameworkCommonClasspathPropertySource.FRAMEWORK_ORDER)
    public static class FrameworkPropertySource implements FrameworkCommonClasspathPropertySource {

        @Override
        public String getClasspathFolder() {
            return "config/bc/fw/";
        }
        
    }

    @Bean
    public CategoryDao ucCategoryDao() {
        CategoryDaoImpl categoryDao = new CategoryDaoImpl();
        categoryDao.setCurrentDateResolution(10000L);
        return categoryDao;
    }

    @Bean
    public ProductDao ucProductDao() {
        ProductDaoImpl productDao = new ProductDaoImpl();
        productDao.setCurrentDateResolution(10000L);
        return productDao;
    }

    @Bean
    public SkuDao ucSkuDao() {
        SkuDaoImpl skuDao = new SkuDaoImpl();
        skuDao.setCurrentDateResolution(10000L);
        return skuDao;
    }

    @Bean
    public OfferDao ucOfferDao() {
        OfferDaoImpl offerDao = new OfferDaoImpl();
        offerDao.setCurrentDateResolution(10000L);
        return offerDao;
    }


    @Bean
    public SearchRedirectDao ucSearchRedirectDao() {
        SearchRedirectDaoImpl searchRedirectDao = new SearchRedirectDaoImpl();
        searchRedirectDao.setCurrentDateResolution(10000L);
        return searchRedirectDao;
    }
}
