/*
 * #%L
 * UltraCommerce CMS Module
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
/**
 *
 */
package com.ultracommerce.cms.admin.web.controller;

import com.ultracommerce.cms.file.service.StaticAssetStorageServiceImpl;
import com.ultracommerce.common.config.PostAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Used in non-boot projects where AutoConfiguration does not occur. This sets up a MultiPartResolver
 * so that image uploads in the admin will still work.
 * @See {@link MultipartAutoConfiguration}
 */
@PostAutoConfiguration
public class NonAutoconfigMultiPartConfiguration {

    @Bean(name = DispatcherServlet.MULTIPART_RESOLVER_BEAN_NAME)
    @ConditionalOnMissingBean
    public MultipartResolver multipartResolver(Environment env) {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setMaxUploadSize(env.getProperty("asset.server.max.uploadable.file.size", long.class, StaticAssetStorageServiceImpl.DEFAULT_ASSET_UPLOAD_SIZE));
        return resolver;
    }
}
