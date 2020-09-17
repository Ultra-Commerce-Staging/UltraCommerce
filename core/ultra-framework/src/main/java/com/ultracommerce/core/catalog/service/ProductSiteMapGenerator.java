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

import org.apache.commons.lang3.StringUtils;
import com.ultracommerce.common.file.service.UltraFileUtils;
import com.ultracommerce.common.media.domain.Media;
import com.ultracommerce.common.sitemap.domain.SiteMapGeneratorConfiguration;
import com.ultracommerce.common.sitemap.service.SiteMapBuilder;
import com.ultracommerce.common.sitemap.service.SiteMapGenerator;
import com.ultracommerce.common.sitemap.service.type.SiteMapGeneratorType;
import com.ultracommerce.common.sitemap.wrapper.SiteMapImageWrapper;
import com.ultracommerce.common.sitemap.wrapper.SiteMapURLWrapper;
import com.ultracommerce.core.catalog.dao.ProductDao;
import com.ultracommerce.core.catalog.domain.Product;
import com.ultracommerce.core.util.service.UltraSitemapUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

/**
 * Responsible for generating site map entries for Product.
 * 
 * @author Joshua Skorton (jskorton)
 */
@Component("ucProductSiteMapGenerator")
public class ProductSiteMapGenerator implements SiteMapGenerator {

    @Resource(name = "ucProductDao")
    protected ProductDao productDao;

    @Value("${product.site.map.generator.row.limit}")
    protected int pageSize;

    @Override
    public boolean canHandleSiteMapConfiguration(SiteMapGeneratorConfiguration siteMapGeneratorConfiguration) {
        return SiteMapGeneratorType.PRODUCT.equals(siteMapGeneratorConfiguration.getSiteMapGeneratorType());
    }

    @Override
    public void addSiteMapEntries(SiteMapGeneratorConfiguration smgc, SiteMapBuilder siteMapBuilder) {

        int pageNum = 0;
        List<Product> products;

        do {
            products = productDao.readAllActiveProducts(pageNum++, pageSize);
            for (Product product : products) {
                if (StringUtils.isEmpty(product.getUrl())) {
                    continue;
                }

                SiteMapURLWrapper siteMapUrl = new SiteMapURLWrapper();

                // location
                siteMapUrl.setLoc(generateUri(siteMapBuilder, product));

                // change frequency
                siteMapUrl.setChangeFreqType(smgc.getSiteMapChangeFreq());

                // priority
                siteMapUrl.setPriorityType(smgc.getSiteMapPriority());

                // lastModDate
                siteMapUrl.setLastModDate(generateDate(product));

                constructImageURLs(siteMapBuilder, siteMapUrl, product);

                siteMapBuilder.addUrl(siteMapUrl);
            }
        } while (products.size() == pageSize);
    }

    protected void constructImageURLs(SiteMapBuilder siteMapBuilder, SiteMapURLWrapper siteMapUrl, Product product) {
        for (Media media : product.getMedia().values()) {
            SiteMapImageWrapper siteMapImage = new SiteMapImageWrapper();

            siteMapImage.setLoc(UltraSitemapUtils.generateImageUrl(siteMapBuilder, media));

            siteMapUrl.addImage(siteMapImage);
        }
    }

    protected String generateUri(SiteMapBuilder smb, Product product) {
        return UltraFileUtils.appendUnixPaths(smb.getBaseUrl(), product.getUrl());
    }

    protected Date generateDate(Product product) {
        return new Date();
    }

    public ProductDao getProductDao() {
        return productDao;
    }

    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

}
