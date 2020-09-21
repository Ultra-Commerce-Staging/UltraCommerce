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

import com.ultracommerce.common.config.EnableUltraAdminRootAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Configuration class that instantiates all of the Ultra Admin beans. This is not generally used outside of the
 * {@link UltraAdminIntegrationTest} annotation but it can be used to compose other contexts outside of that
 * annotation.
 * 
 * @see EnableUltraAdminRootAutoConfiguration
 * @author Phillip Verheyden (phillipuniverse)
 */
@Configuration
@EnableUltraAdminRootAutoConfiguration
@ImportResource(value = {
    "classpath:uc-applicationContext-test.xml"
})
public class AdminTestContextConfiguration {
    
}
