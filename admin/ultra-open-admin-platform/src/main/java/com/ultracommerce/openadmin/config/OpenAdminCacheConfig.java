/*
 * #%L
 * UltraCommerce Open Admin Platform
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
package com.ultracommerce.openadmin.config;

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
public class OpenAdminCacheConfig {

    @Bean
    public JCacheRegionConfiguration ucAdminSecurityQuery() {
        return new JCacheRegionConfiguration("ucAdminSecurityQuery", 86400, 1000);
    }

    @Bean
    public JCacheRegionConfiguration ucAdminSecurityQueryVolatile() {
        return new JCacheRegionConfiguration("ucAdminSecurityQueryVolatile", 60, 200);
    }

    @Bean
    public JCacheRegionConfiguration ucAdminSecurity() {
        return new JCacheRegionConfiguration("ucAdminSecurity", 86400, 1000);
    }

    @Bean
    public JCacheRegionConfiguration ucAdminSecurityVolatile() {
        return new JCacheRegionConfiguration("ucAdminSecurityVolatile", 60, 200);
    }
}
