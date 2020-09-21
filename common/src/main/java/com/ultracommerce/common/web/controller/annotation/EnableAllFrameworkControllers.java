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
package com.ultracommerce.common.web.controller.annotation;

import com.ultracommerce.common.config.UltraBeanNameGenerator;
import com.ultracommerce.common.web.controller.FrameworkControllerHandlerMapping;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enables {@link FrameworkController} and {@link FrameworkRestController} annotations.
 * <p>
 * Scan all Ultra modules for {@link FrameworkController} and {@link FrameworkRestController} so that their {@link
 * FrameworkMapping}s will get included in {@link
 * FrameworkControllerHandlerMapping} to provide default implementations of web endpoints.
 * <p>
 * If only some controllers are desired, then you must individually use {@link EnableFrameworkControllers} and
 * {@link EnableFrameworkRestControllers} and utilize their excludeFilters property to disable the unwanted controllers.
 * See {@link EnableFrameworkControllers} documentation for how to properly use these two annotations together.
 * <p>
 * <b>DO NOT place this annotation on the same class as another {@link ComponentScan} or other annotations that compose
 * {@link ComponentScan} such as {@code @SpringBootApplication}, as they will conflict when Spring performs annotation
 * composition.</b> Instead, you can create a nested class in your {@code @SprintBootApplication} class like this:
 * <pre>
 * {@code
 * @literal @SpringBootApplication
 * public class MyApplication {
 *
 *     @literal @EnableAllFrameworkControllers
 *     public static class EnableAllUltraControllers {}
 *
 *     public static void main(String[] args) {
 *         SpringApplication.run(MyApplication.class, args);
 *     }
 * }
 * }
 * </pre>
 *
 * @author Philip Baggett (pbaggett)
 * @see FrameworkController
 * @see FrameworkRestController
 * @see FrameworkMapping
 * @see FrameworkControllerHandlerMapping
 * @since 5.2
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ComponentScan(
        useDefaultFilters = false,
        basePackages = {"com.ultracommerce", "com.ultracommerce"},
        includeFilters = @Filter({FrameworkController.class, FrameworkRestController.class}),
        nameGenerator = UltraBeanNameGenerator.class)
public @interface EnableAllFrameworkControllers {
}
