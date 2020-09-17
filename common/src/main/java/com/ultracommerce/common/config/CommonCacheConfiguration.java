/*
 * #%L
 * UltraCommerce Common Libraries
 * %%
 * Copyright (C) 2009 - 2020 Ultra Commerce
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
package com.ultracommerce.common.config;

import com.ultracommerce.common.extensibility.cache.JCacheRegionConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for defining cache regions via Java config when either not using ehcache or when jcache.create.cache.forceJavaConfig is true
 * 
 * @author Jay Aisenbrey (cja769)
 *
 */
@Configuration
public class CommonCacheConfiguration {

    @Bean
    public JCacheRegionConfiguration defaultUpdateTimestampsRegion() {
        return new JCacheRegionConfiguration("default-update-timestamps-region", -1, 5000);
    }

    @Bean
    public JCacheRegionConfiguration defaultQueryResultsRegion() {
        return new JCacheRegionConfiguration("default-query-results-region", 600, 1000);
    }

    @Bean
    public JCacheRegionConfiguration ucStandardElements() {
        return new JCacheRegionConfiguration("ucStandardElements", 86400, 100000);
    }

    @Bean
    public JCacheRegionConfiguration ucProducts() {
        return new JCacheRegionConfiguration("ucProducts", 86400, 100000);
    }

    @Bean
    public JCacheRegionConfiguration ucProductUrlCache() {
        return new JCacheRegionConfiguration("ucProductUrlCache", 3600, 1000);
    }

    @Bean
    public JCacheRegionConfiguration ucCategories() {
        return new JCacheRegionConfiguration("ucCategories", 86400, 100000);
    }

    @Bean
    public JCacheRegionConfiguration ucCategoryUrlCache() {
        return new JCacheRegionConfiguration("ucCategoryUrlCache", 3600, 1000);
    }

    @Bean
    public JCacheRegionConfiguration ucOffers() {
        return new JCacheRegionConfiguration("ucOffers", 86400, 100000);
    }

    @Bean
    public JCacheRegionConfiguration ucInventoryElements() {
        return new JCacheRegionConfiguration("ucInventoryElements", 60, 100000);
    }

    @Bean
    public JCacheRegionConfiguration queryCatalog() {
        return new JCacheRegionConfiguration("query.Catalog", 600, 1000);
    }

    @Bean
    public JCacheRegionConfiguration queryPriceList() {
        return new JCacheRegionConfiguration("query.PriceList", 600, 1000);
    }

    @Bean
    public JCacheRegionConfiguration queryCms() {
        return new JCacheRegionConfiguration("query.Cms", 600, 1000);
    }

    @Bean
    public JCacheRegionConfiguration queryOffer() {
        return new JCacheRegionConfiguration("query.Offer", 600, 1000);
    }

    @Bean
    public JCacheRegionConfiguration ucOrderElements() {
        return new JCacheRegionConfiguration("ucOrderElements", 600, 100000);
    }

    @Bean
    public JCacheRegionConfiguration ucCustomerElements() {
        return new JCacheRegionConfiguration("ucCustomerElements", 600, 100000);
    }

    @Bean
    public JCacheRegionConfiguration queryOrder() {
        return new JCacheRegionConfiguration("query.Order", 60, 1000);
    }

    @Bean
    public JCacheRegionConfiguration querySearch() {
        return new JCacheRegionConfiguration("query.Search", 600, 1000);
    }

    @Bean
    public JCacheRegionConfiguration generatedResourceCache() {
        return new JCacheRegionConfiguration("generatedResourceCache", 600, 100);
    }

    @Bean
    public JCacheRegionConfiguration ucTemplateElements() {
        return new JCacheRegionConfiguration("ucTemplateElements", 3600, 1000);
    }

    @Bean
    public JCacheRegionConfiguration ucTranslationElements() {
        return new JCacheRegionConfiguration("ucTranslationElements", 3600, 10000000);
    }

    @Bean
    public JCacheRegionConfiguration ucBatchTranslationCache() {
        return new JCacheRegionConfiguration("ucBatchTranslationCache", -1, 10000);
    }

    @Bean
    public JCacheRegionConfiguration ucConfigurationModuleElements() {
        return new JCacheRegionConfiguration("ucConfigurationModuleElements", 600, 1000);
    }

    @Bean
    public JCacheRegionConfiguration queryConfigurationModuleElements() {
        return new JCacheRegionConfiguration("query.ConfigurationModuleElements", 600, 1000);
    }

    @Bean
    public JCacheRegionConfiguration ucSystemPropertyElements() {
        return new JCacheRegionConfiguration("ucSystemPropertyElements", 600, 1000);
    }

    @Bean
    public JCacheRegionConfiguration ucSystemPropertyNullCheckCache() {
        return new JCacheRegionConfiguration("ucSystemPropertyNullCheckCache", 600, 1000);
    }

    @Bean
    public JCacheRegionConfiguration ucBundleElements() {
        return new JCacheRegionConfiguration("ucBundleElements", 86400, 1000);
    }

    @Bean
    public JCacheRegionConfiguration ucResourceCacheElements() {
        return new JCacheRegionConfiguration("ucResourceCacheElements", 86400, 1000);
    }

    @Bean
    public JCacheRegionConfiguration ucResourceTransformerCacheElements() {
        return new JCacheRegionConfiguration("ucResourceTransformerCacheElements", 86400, 1000);
    }

    @Bean
    public JCacheRegionConfiguration ucSandBoxElements() {
        return new JCacheRegionConfiguration("ucSandBoxElements", 3, 2000);
    }

    @Bean
    public JCacheRegionConfiguration queryucSandBoxElements() {
        return new JCacheRegionConfiguration("query.ucSandBoxElements", 3, 500);
    }

    @Bean
    public JCacheRegionConfiguration ucSecurityElements() {
        return new JCacheRegionConfiguration("ucSecurityElements", 86400, 1000);
    }

    @Bean
    public JCacheRegionConfiguration ucSiteElements() {
        return new JCacheRegionConfiguration("ucSiteElements", 3600, 5000);
    }

    @Bean
    public JCacheRegionConfiguration ucSiteElementsQuery() {
        return new JCacheRegionConfiguration("ucSiteElementsQuery", 3600, 1000);
    }

    @Bean
    public JCacheRegionConfiguration ucProductOverrideCache() {
        return new JCacheRegionConfiguration("ucProductOverrideCache", -1, 100);
    }
}
