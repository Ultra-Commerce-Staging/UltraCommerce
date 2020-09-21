/*
 * #%L
 * UltraCommerce Common Libraries
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
package com.ultracommerce.common.config.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Registers the {@link SystemPropertiesService} with the Spring {@link Environment}. This happens after
 * the Spring ApplicationContext has been refreshed in order to give a chance for all of the Ultra MT/Enterprise
 * InvocationProviders etc to be initialized.
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
@Component("ucSystemPropertiesPropertySourceRegistrar")
public class SystemPropertiesPropertySourceRegistrar {

    @Autowired
    protected SystemPropertiesService propsSvc;
    
    @Autowired
    protected Environment env;
    
    @EventListener
    public void registerSystemPropertySource(ContextRefreshedEvent refreshed) {
        ConfigurableEnvironment mutableEnv = (ConfigurableEnvironment) env;
        mutableEnv.getPropertySources().addFirst(new SystemPropertyPropertySource(SystemPropertiesServiceImpl.PROPERTY_SOURCE_NAME, propsSvc));
    }
    
    /**
     * Hook point for our database-backed properties to the Spring Environment
     * 
     * @author Phillip Verheyden (phillipuniverse)
     */
    protected static class SystemPropertyPropertySource extends PropertySource<SystemPropertiesService> {

        public SystemPropertyPropertySource(String name, SystemPropertiesService source) {
            super(name, source);
        }

        @Override
        public Object getProperty(String name) {
            SystemPropertiesServiceImpl.originatedFromEnvironment.set(true);
            Object property = source.resolveSystemProperty(name);
            SystemPropertiesServiceImpl.originatedFromEnvironment.set(false);
            return property;
        }
    }
}
