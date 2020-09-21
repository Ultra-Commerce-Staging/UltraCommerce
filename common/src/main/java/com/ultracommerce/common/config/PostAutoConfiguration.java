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
package com.ultracommerce.common.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Marker annotation indicating that the current class is both an {@code @Configuration} class
 * <i>and</i> should be executed <i>after</i> any Spring Boot {@link EnableAutoConfiguration}.
 * Classes that are annotated with this are assumed to be imported by {@link PostAutoConfigurationImport}.
 * 
 * <p>
 * Classes with this annotation <b>cannot</b> be within a nested {@code @Configuration} class or else
 * it will get picked up too early with the parent {@code @Configuration} class. It must be defined in
 * its own file.
 * 
 * <p>
 * Any classes that contains this annotation <b>must</b> be excluded from any component-scans. Corresponding required
 * XML configuration example:
 * 
 * <pre>
 * {@code
 * <context:component-scan base-package="com.ultracommerce.some.package">
 *     <context:exclude-filter type="annotation" expression="com.ultracommerce.common.config.PostAutoConfiguration"/>
 * </context:component-scan>
 * }
 * </pre>
 * 
 * <p>
 * Or in an {@link @ComponentScan} annotation:
 * 
 * <pre>
 * {@literal @}ComponentScan(basePackages = "com.ultracommerce.some.package",
 *    excludeFilters = {@literal @}Filter(type = FilterType.ANNOTATION, classes = PostAutoConfiguration.class))
 * }
 * </pre>
 * 
 * @author Phillip Verheyden (phillipuniverse)
 * @see PostAutoConfigurationImport
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration
public @interface PostAutoConfiguration {

}
