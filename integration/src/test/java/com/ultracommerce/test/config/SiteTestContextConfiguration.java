/*
 * #%L
 * UltraCommerce Integration
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
package com.ultracommerce.test.config;

import com.ultracommerce.common.config.EnableUltraSiteRootAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Configuration class holder for all of the configuration for scanning all Ultra non-servlet beans for an integration test.
 * This particular class can be used for composing your own context configuration but generally this class is not used directly.
 * Instead see {@link UltraSiteIntegrationTest} which makes use of this configuration to initialize a Spring ApplicationContext
 * 
 * @see UltraSiteIntegrationTest
 * @see EnableUltraSiteRootAutoConfiguration
 * @author Phillip Verheyden (phillipuniverse)
 */
@Configuration
@EnableUltraSiteRootAutoConfiguration
@ImportResource(value = {
        "classpath:uc-applicationContext-test-security.xml",
        "classpath:uc-applicationContext-test.xml"
    })
@ComponentScan({"com.ultracommerce.profile.web.controller", "com.ultracommerce.profile.web.core.service.login"})
public class SiteTestContextConfiguration {

}
