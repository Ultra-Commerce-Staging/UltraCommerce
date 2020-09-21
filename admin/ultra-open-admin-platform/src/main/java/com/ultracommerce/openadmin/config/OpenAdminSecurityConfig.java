/*
 * #%L
 * UltraCommerce Open Admin Platform
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
/**
 * 
 */

package com.ultracommerce.openadmin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Security configuration common to the open admin platform. Be cautious about what you do an {@link AuthenticationManager} or other Spring Security
 * configuration here; it easily collides with any {@code <sec:http>} entries in a client-side XML. The only way to do more
 * here is to include a migration away from anything custom in a client application.
 * 
 * @author Phillip Verheyden (phillipuniverse)
 * @since 5.2
 */
@Configuration
public class OpenAdminSecurityConfig {

    @Autowired
    @Qualifier("ucAdminUserDetailsService")
    protected UserDetailsService userDetailsService;
    
    @Autowired
    @Qualifier("ucAdminPasswordEncoder")
    protected PasswordEncoder adminPasswordEncoder;

    @Bean
    public AuthenticationProvider ucAdminAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(adminPasswordEncoder);
        return provider;
    }
    
}
