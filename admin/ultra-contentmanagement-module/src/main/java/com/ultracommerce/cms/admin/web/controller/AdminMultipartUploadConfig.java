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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.config.PostAutoConfigurationImport;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.servlet.MultipartConfigElement;

/**
 * Borrows much of its config from {@link MultipartAutoConfiguration} but overrides the default property
 * used for max upload size
 *
 * @author Phillip Verheyden (phillipuniverse)
 */
@Configuration
@PostAutoConfigurationImport(NonAutoconfigMultiPartConfiguration.class)
@EnableConfigurationProperties(MultipartProperties.class)
public class AdminMultipartUploadConfig {

    private static final Log LOG = LogFactory.getLog(AdminMultipartUploadConfig.class);

    protected MultipartProperties multipartProperties;

    public AdminMultipartUploadConfig(MultipartProperties multipartProperties) {
        this.multipartProperties = multipartProperties;
    }

    @Bean
    public MultipartConfigElement multipartConfigElement(Environment env) {
        MultipartConfigElement multipartConfig = this.multipartProperties.createMultipartConfig();
        // If a user has configured the max file size from Ultra to be anything,
        // override it here
        String ucAssetUploadSizeProperty = "asset.server.max.uploadable.file.size";
        Long ucMaxFileSize = env.getProperty(ucAssetUploadSizeProperty, Long.class);
        if (ucMaxFileSize != null) {
            LOG.info(String.format("The %s has been set to %s, using this as the file upload limit and ignoring the Spring Boot settings", ucAssetUploadSizeProperty, ucMaxFileSize));
            multipartConfig = new MultipartConfigElement(multipartConfig.getLocation(),
                multipartConfig.getMaxRequestSize(),
                ucMaxFileSize,
                multipartConfig.getFileSizeThreshold());
        }
        return multipartConfig;
    }

}
