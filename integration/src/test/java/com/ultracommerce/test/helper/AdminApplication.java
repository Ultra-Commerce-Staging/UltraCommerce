/*
 * #%L
 * Reference Site Admin
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
package com.ultracommerce.test.helper;


import com.ultracommerce.common.config.EnableUltraAdminAutoConfiguration;
import com.ultracommerce.test.junit.JUnitSpringBootAdminIntegrationSetup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

/**
 * Useful starting point for a admin Spring Boot integration test. See docs in {@link JUnitSpringBootAdminIntegrationSetup}
 * for more information on creating a Spring Boot integration test for the admin.
 *
 * @author Jeff Fischer
 */
@SpringBootApplication
@EnableAutoConfiguration
public class AdminApplication {

    @Configuration
    @EnableUltraAdminAutoConfiguration
    public static class UltraFrameworkConfiguration {
    }

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }

}

