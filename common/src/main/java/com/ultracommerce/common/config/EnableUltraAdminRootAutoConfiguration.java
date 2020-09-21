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

import com.ultracommerce.common.config.EnableUltraAdminRootAutoConfiguration.UltraAdminRootAutoConfiguration;
import com.ultracommerce.common.config.EnableUltraAdminRootAutoConfiguration.UltraAdminRootAutoConfigurationOverrides;
import com.ultracommerce.common.extensibility.FrameworkXmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Bootstraps the Ultra <b>root</b> admin configuration XML for only non-servlet beans. This can be placed on any {@literal @}Configuration
 * class (except ones with additional {@literal @}ImportResource) to make the root Ultra beans apart of the {@link ApplicationContext}.
 * If you are using Spring boot, this <b>must</b> be placed on an <b>inner static class</b> within the {@literal @}SpringBootApplication class. Example:
 * 
 * <pre>
 * {@literal @}SpringBootApplication
 * public class MyApplication extends SpringBootServletInitializer {
 * 
 *     {@literal @}Configuration
 *     {@literal @}EnableUltraAdminRootAutoConfiguration
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
 * Since this does not include any of the servlet-specific Ultra beans, this is generally only used when you are not running in a 
 * servlet environment at all or there is a parent-child relationship between a root {@link ApplicationContext} and you want to
 * configure multiple servlets that share much of the same beans. In general, rather than create multiple servlets with shared
 * configuration you should instead create separate deployments and utilize {@link EnableUltraAdminAutoConfiguration} in a single place.
 * 
 * <p>
 * This import utilizes the {@link FrameworkXmlBeanDefinitionReader} so that framework XML bean definitions will not
 * overwrite beans defined in a project.
 *
 * @author Philip Baggett (pbaggett)
 * @author Phillip Verheyden (phillipuniverse)
 * @author Nick Crum (ncrum)
 * @see EnableUltraAdminAutoConfiguration
 * @see EnableUltraAdminServletAutoConfiguration
 * @see EnableUltraAutoConfiguration
 * @since 5.2
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
    EnableUltraRootAutoConfiguration.UltraRootAutoConfiguration.class,
    UltraAdminRootAutoConfiguration.class,
    UltraAdminRootAutoConfigurationOverrides.class
})
public @interface EnableUltraAdminRootAutoConfiguration {

    /**
     * We are deliberately leaving off the {@link org.springframework.context.annotation.Configuration} annotation since
     * this inner class is being included in the {@code Import} above, which interprets this as a
     * {@link org.springframework.context.annotation.Configuration}. We do this to avoid component scanning this inner class.
     */
    @Import(EnableUltraRootAutoConfiguration.UltraRootAutoConfiguration.class)
    @ImportResource(locations = {
            "classpath*:/uc-config/admin/framework/uc-*-applicationContext.xml",
            "classpath*:/uc-config/admin/early/uc-*-applicationContext.xml",
            "classpath*:/uc-config/admin/uc-*-applicationContext.xml",
            "classpath*:/uc-config/admin/late/uc-*-applicationContext.xml"
    }, reader = FrameworkXmlBeanDefinitionReader.class)
    class UltraAdminRootAutoConfiguration {

        @Bean
        public AdminOnlyTarget ucAdminOnlyTarget() {
            return new AdminOnlyTarget();
        }
    }
    
    @ImportResource("classpath:/override-contexts/admin-root-autoconfiguration-overrides.xml")
    class UltraAdminRootAutoConfigurationOverrides {}
}
