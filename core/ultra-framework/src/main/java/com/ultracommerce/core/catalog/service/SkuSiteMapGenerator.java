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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import com.ultracommerce.common.file.service.UltraFileUtils;
import com.ultracommerce.common.sitemap.domain.SiteMapGeneratorConfiguration;
import com.ultracommerce.common.sitemap.service.SiteMapBuilder;
import com.ultracommerce.common.sitemap.service.SiteMapGenerator;
import com.ultracommerce.common.sitemap.service.type.SiteMapGeneratorType;
import com.ultracommerce.common.sitemap.wrapper.SiteMapImageWrapper;
import com.ultracommerce.common.sitemap.wrapper.SiteMapURLWrapper;
import com.ultracommerce.core.catalog.dao.SkuDao;
import com.ultracommerce.core.catalog.domain.Product;
import com.ultracommerce.core.catalog.domain.ProductBundle;
import com.ultracommerce.core.catalog.domain.Sku;
import com.ultracommerce.core.catalog.domain.SkuMediaXref;
import com.ultracommerce.core.util.service.UltraSitemapUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

/**
 * Responsible for generating site map entries for Sku.
 * 
 * @author Joshua Skorton (jskorton)
 */
@Component("ucSkuSiteMapGenerator")
public class SkuSiteMapGenerator implements SiteMapGenerator {

    @Resource(name = "ucSkuDao")
    protected SkuDao skuDao;

    @Value("${sku.site.map.generator.row.limit}")
    protected int pageSize;

    @Override
    public boolean canHandleSiteMapConfiguration(SiteMapGeneratorConfiguration siteMapGeneratorConfiguration) {
        return SiteMapGeneratorType.SKU.equals(siteMapGeneratorConfiguration.getSiteMapGeneratorType());
    }

    @Override
    public void addSiteMapEntries(SiteMapGeneratorConfiguration smgc, SiteMapBuilder siteMapBuilder) {

        int pageNum = 0;
        List<Sku> skus;

        do {
            skus = skuDao.readAllActiveSkus(pageNum++, pageSize);
            for (Sku sku : skus) {
                Product defaultProduct = sku.getDefaultProduct();
                if (defaultProduct != null && CollectionUtils.isNotEmpty(defaultProduct.getAdditionalSkus())) {
                    continue;
                }
                if (defaultProduct instanceof ProductBundle) {
                    continue;
                }
                if (StringUtils.isEmpty(sku.getProduct().getUrl() + sku.getUrlKey())) {
                    continue;
                }
                
                SiteMapURLWrapper siteMapUrl = new SiteMapURLWrapper();

                // location
                siteMapUrl.setLoc(generateUri(siteMapBuilder, sku));

                // change frequency
                siteMapUrl.setChangeFreqType(smgc.getSiteMapChangeFreq());

                // priority
                siteMapUrl.setPriorityType(smgc.getSiteMapPriority());

                // lastModDate
                siteMapUrl.setLastModDate(generateDate(sku));

                constructImageURLs(siteMapBuilder, siteMapUrl, sku);

                siteMapBuilder.addUrl(siteMapUrl);
            }
        } while (skus.size() == pageSize);
    }

    protected void constructImageURLs(SiteMapBuilder siteMapBuilder, SiteMapURLWrapper siteMapUrl, Sku sku) {
        for (SkuMediaXref skuMediaXref : sku.getSkuMediaXref().values()) {
            SiteMapImageWrapper siteMapImage = new SiteMapImageWrapper();

            siteMapImage.setLoc(UltraSitemapUtils.generateImageUrl(siteMapBuilder, skuMediaXref.getMedia()));

            siteMapUrl.addImage(siteMapImage);
        }
    }

    protected String generateUri(SiteMapBuilder smb, Sku sku) {
        String uri = null;
        if (sku.getUrlKey() != null) {
            uri = sku.getProduct().getUrl() + sku.getUrlKey();
        } else {
            uri = sku.getProduct().getUrl();
        }
        return UltraFileUtils.appendUnixPaths(smb.getBaseUrl(), uri);
    }

    protected Date generateDate(Sku sku) {
        return new Date();
    }
    
    public SkuDao getSkuDao() {
        return skuDao;
    }

    public void setSkuDao(SkuDao skuDao) {
        this.skuDao = skuDao;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

}
