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
package com.ultracommerce.openadmin.server.security.service;

import java.util.List;

/**
 * <p>
 * Provides row-level security to the various CRUD operations in the admin
 * 
 * <p>
 * This security service can be extended by the use of {@link RowLevelSecurityProviders}, of which this service has a list.
 * To add additional providers, add this to an applicationContext merged into the admin application:
 * 
 * {@code
 *  <bean id="ucCustomRowSecurityProviders" class="org.springframework.beans.factory.config.ListFactoryBean" >
 *       <property name="sourceList">
 *          <list>
 *              <ref bean="customProvider" />
 *          </list>
 *      </property>
 *  </bean>
 *  <bean class="com.ultracommerce.common.extensibility.context.merge.LateStageMergeBeanPostProcessor">
 *      <property name="collectionRef" value="ucCustomRowSecurityProviders" />
 *      <property name="targetRef" value="ucRowLevelSecurityProviders" />
 *  </bean>
 * }
 * 
 * @author Phillip Verheyden (phillipuniverse)
 * @author Brian Polster (bpolster)
 */
public interface RowLevelSecurityService extends RowLevelSecurityProvider {

    /**
     * Gets all of the registered providers
     * @return the providers configured for this service
     */
    public List<RowLevelSecurityProvider> getProviders();
}
