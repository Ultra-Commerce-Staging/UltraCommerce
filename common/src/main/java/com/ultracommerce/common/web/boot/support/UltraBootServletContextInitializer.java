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
package com.ultracommerce.common.web.boot.support;

import org.apache.commons.logging.LogFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * Used in conjunction with {@link SpringBootBootstrappingServletContextListener}. Since that class
 * establishes its own delegate {@link ContextLoaderListener} that closes the Spring ApplicationContext we want
 * to disable the fact that Spring Boot tries to add a {@link ContextLoaderListener}.
 * 
 * SPRING-UPGRADE-CHECK
 * @author Phillip Verheyden (phillipuniverse)
 */
public abstract class UltraBootServletContextInitializer extends SpringBootServletInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        this.logger = LogFactory.getLog(getClass());
        createRootApplicationContext(servletContext);
    }
}
