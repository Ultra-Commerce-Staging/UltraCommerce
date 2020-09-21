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
package com.ultracommerce.common.config;

import com.ultracommerce.common.config.EnableUltraSiteAutoConfiguration.UltraSiteAutoConfigurationOverrides;
import com.ultracommerce.common.extensibility.FrameworkXmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.servlet.ServletContainerInitializer;

/**
 * <p>
 * Bootstraps Ultra customer-facing configuration XML for both servlet and non-servlet beans in use with a traditional MVC application. If you are deploying Ultra in a
 * REST-API-only capacity or any other way then this annotation is probably not for you, and instead just {@link EnableUltraSiteRootAutoConfiguration}
 * is sufficient. If you have a customized {@link ServletContainerInitializer}
 * with a servlet-specific {@link ApplicationContext}, this annotation should only be placed on an {@literal @}Configuration class within
 * <b>that</b> servlet-specific {@lnk ApplicationContext}. If this is not the case and no servlet-specific {@link ApplicationContext} exists in your
 * project and you are using Spring Boot, this <b>must</b> be placed on an <b>inner static class</b> within the {@literal @}SpringBootApplication class. Example:
 * 
 * <pre>
 * {@literal @}SpringBootApplication
 * public class MyApplication extends SpringBootServletInitializer {
 * 
 *     {@literal @}Configuration
 *     {@literal @}EnableUltraSiteAutoConfiguration
 *     public static class UltraConfiguration { }
 *     
 *     public static void main(String[] args) {
 *         SpringApplication.run(ApiApplication.class, args);
 *     }
 *  
 *     {@literal @}Override
 *     protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
 *         return application.sources(ApiApplication.class);
 *     }
 * }
 *
 * </pre>
 *
 * <p>
 * Since this annotation is a meta-annotation for {@literal @}Import, this <b>can</b> be placed on a {@literal @}Configuration class
 * that contains an {@literal @}Import annotation, <b>but</b> this {@literal @}Import's beans will take precedence over
 * any additional {@literal @}Import applied.
 * 
 * <p>
 * This import utilizes the {@link FrameworkXmlBeanDefinitionReader} so that framework XML bean definitions will not
 * overwrite beans defined in a project.
 *
 * @author Philip Baggett (pbaggett)
 * @author Phillip Verheyden (phillipuniverse)
 * @author Nick Crum (ncrum)
 * @see EnableUltraSiteRootAutoConfiguration
 * @see EnableUltraSiteServletAutoConfiguration
 * @see EnableUltraAutoConfiguration
 * @since 5.2
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
    EnableUltraSiteRootAutoConfiguration.UltraSiteRootAutoConfiguration.class,
    EnableUltraSiteServletAutoConfiguration.UltraSiteServletAutoConfiguration.class,
    UltraSiteAutoConfigurationOverrides.class
})
public @interface EnableUltraSiteAutoConfiguration {
    
    @ImportResource("classpath:/override-contexts/site-root-autoconfiguration-overrides.xml")
    class UltraSiteAutoConfigurationOverrides { }
}
