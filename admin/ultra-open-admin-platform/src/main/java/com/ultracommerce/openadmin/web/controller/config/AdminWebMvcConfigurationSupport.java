/*
 * #%L
 * UltraCommerce Open Admin Platform
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
package com.ultracommerce.openadmin.web.controller.config;

import com.ultracommerce.common.config.PostAutoConfiguration;
import com.ultracommerce.openadmin.web.controller.AdminRequestMappingHandlerMapping;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * In absence of Spring Boot's auto-configuration {@link WebMvcAutoConfiguration} class, this ensures
 * that we still have the required override of the default RequestMappingHandlerAdapter
 * @author Phillip Verheyden (phillipuniverse)
 */
@PostAutoConfiguration
@ConditionalOnMissingBean(WebMvcConfigurationSupport.class)
public class AdminWebMvcConfigurationSupport extends DelegatingWebMvcConfiguration {
    
    @Override
    protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
        return new AdminRequestMappingHandlerMapping();
    }
    
}
