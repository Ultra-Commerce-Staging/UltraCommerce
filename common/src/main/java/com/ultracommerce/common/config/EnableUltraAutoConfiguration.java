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

import com.ultracommerce.common.config.EnableUltraAutoConfiguration.UltraAutoConfigurationOverrides;
import com.ultracommerce.common.config.EnableUltraServletAutoConfiguration.UltraServletAutoConfiguration;
import com.ultracommerce.common.extensibility.FrameworkXmlBeanDefinitionReader;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <b>STOP. This is probably not the annotation you want currently.</b>
 * <p>
 * Ultra is progressing towards the Spring Framework's methodology of bean definition and priority. The following
 * are the different phases Ultra will progress through with this goal in mind.
 * <p>
 * Phase 1 <i>(current)</i> - Ultra framework XML configuration is separated out into {@code site} and {@code admin}
 * specific directories within {@code /uc-config} in at least one module. For this reason, you must use {@link
 * EnableUltraAdminAutoConfiguration} and {@link EnableUltraSiteAutoConfiguration} for admin and site
 * applications respectively.
 * <p>
 * Phase 2 - <i>Medium level of effort</i> - All Ultra modules have had {@link
 * com.ultracommerce.common.admin.condition.ConditionalOnAdmin} applied to admin specific beans and the XML
 * configuration files have been moved to just {@code /uc-config}. You can now use {@link
 * EnableUltraAutoConfiguration} for both site and admin applications, however it isn't a requirement to migrate
 * {@link EnableUltraAdminAutoConfiguration} and {@link EnableUltraSiteAutoConfiguration} as they also include
 * root {@code /uc-config} in addition to the {@code admin} and {@code site} specific directories.
 * <p>
 * Phase 3 - <i>Very high level of effort</i> - All framework bean definitions in Ultra have been migrated to
 * JavaConfig or annotation based configuration and have applied a Spring Boot Autoconfiguration {@code
 * ConditionalOn...} annotation that defines the condition on which it should be registered. The most common of these
 * will be {@code ConditionalOnMissingBean} which will check if a client application has registered a bean of this type,
 * and if so the framework bean won't get registered. Additionally all Ultra module configuration is bootstrapped
 * using {@code spring.factories} which references a {@link org.springframework.context.annotation.Configuration}
 * classes that contains {@link org.springframework.context.annotation.Bean} definitions and a {@link
 * org.springframework.context.annotation.ComponentScan} of its entire package to pick up any other beans defined using
 * annotations. The {@link org.springframework.context.annotation.Configuration} classes referenced by {@code
 * spring.factories} are discovered by using the Spring Boot Autoconfiguration {@code EnableAutoConfiguration}
 * annotation. Client applications will now be required to migrate to using {@code EnableAutoConfiguration} instead of
 * {@link EnableUltraAutoConfiguration}, although this annotation could be modified to compose {@code
 * EnableAutoConfiguration} to mitigate this requirement. When modules are bootstrapped by {@code
 * EnableAutoConfiguration}/{@code spring.factories} those bean definitions are placed in a deferred registration list
 * so they will get registered last, so by the time framework beans are evaluated for registration all client beans that
 * override framework beans have already been registered and the framework bean's {@code ConditionalOnMissingBean}
 * annotation will prevent it from getting registered and consequently overriding the client bean. Also, in order to
 * facilitate commercial framework modules overriding beans defined in community framework modules, the {@link
 * org.springframework.core.annotation.Order} annotation is leveraged in commercial modules with a priority set higher
 * than the default {@link org.springframework.core.Ordered#LOWEST_PRECEDENCE}; this could be simplified by using
 * annotation composition on {@link org.springframework.core.annotation.Order} to create something like {@code
 * FrameworkPriority}. At this phase there will be little to no XML configuration remaining in the Ultra Framework
 * as XML configuration does not support these conditionals. Also, the use of bean IDs will be severely reduced as they
 * generally aren't required for bean overriding decisions when using {@code ConditionalOnMissingBean}. Bean IDs will
 * still be used for the situations that Spring has defined such as having multiple {@link javax.sql.DataSource} beans
 * all with unique IDs and one marked as {@link org.springframework.context.annotation.Primary} for injections where a
 * {@link org.springframework.beans.factory.annotation.Qualifier} isn't specified.
 * <p>
 * Bootstraps Ultra configuration XML using a glob import.
 * <p>
 * This import utilizes the {@link FrameworkXmlBeanDefinitionReader} so that framework XML bean definitions will not
 * overwrite beans defined in a project.
 *
 * @author Philip Baggett (pbaggett)
 * @author Brandon Hines (bhines)
 * @author Nick Crum (ncrum)
 * @see EnableUltraAdminAutoConfiguration
 * @see EnableUltraSiteAutoConfiguration
 * @since 5.2
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
    EnableUltraRootAutoConfiguration.UltraRootAutoConfiguration.class,
    UltraServletAutoConfiguration.class,
    UltraAutoConfigurationOverrides.class
})
public @interface EnableUltraAutoConfiguration {
    
    @ImportResource("classpath:/override-contexts/autoconfiguration-overrides.xml")
    class UltraAutoConfigurationOverrides { }
}
