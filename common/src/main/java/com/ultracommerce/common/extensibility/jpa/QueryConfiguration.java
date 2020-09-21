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
package com.ultracommerce.common.extensibility.jpa;

import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedQueries;

/**
 * Interface for classes annotated with {@link NamedQueries} and {@link NamedNativeQueries}.
 * Provides a mechanism for creating generic classes for grouping named queries without having to explicitly declare them
 * at the top of individual {@link javax.persistence.Entity} implementation classes.
 * </p>
 * Declaring a query configuration class for the system to harvest named queries from is as simple as making the class implement
 * this interface, implementing the required method(s), and having spring recognize the class as a bean - either through
 * Configuration class, component scanning or xml declaration.
 *
 * @author Jeff Fischer
 */
public interface QueryConfiguration {

    String getPersistenceUnit();

}
