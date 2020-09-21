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

import com.ultracommerce.common.config.EnableUltraRootAutoConfiguration.UltraRootAutoConfiguration;
import com.ultracommerce.common.config.EnableUltraRootAutoConfiguration.UltraRootAutoConfigurationOverrides;
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
 * The same rules apply here as with {@link EnableUltraAutoConfiguration} but this is for only the root-level Ultra beans
 *
 * @author Philip Baggett (pbaggett)
 * @author Brandon Hines (bhines)
 * @author Nick Crum (ncrum)
 * @see EnableUltraAdminRootAutoConfiguration
 * @see EnableUltraSiteRootAutoConfiguration
 * @see EnableUltraAutoConfiguration
 * @since 5.2
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
    UltraRootAutoConfiguration.class,
    UltraRootAutoConfigurationOverrides.class
})
public @interface EnableUltraRootAutoConfiguration {

    /**
     * We are deliberately leaving off the {@link org.springframework.context.annotation.Configuration} annotation since
     * this inner class is being included in the {@code Import} above, which interprets this as a
     * {@link org.springframework.context.annotation.Configuration}. We do this to avoid component scanning this inner class.
     */
    @ImportResource(locations = {
            "classpath*:/uc-config/framework/uc-*-applicationContext.xml",
            "classpath*:/uc-config/early/uc-*-applicationContext.xml",
            "classpath*:/uc-config/uc-*-applicationContext.xml",
            "classpath*:/uc-config/late/uc-*-applicationContext.xml"
    }, reader = FrameworkXmlBeanDefinitionReader.class)
    class UltraRootAutoConfiguration {}
    
    @ImportResource("classpath:/override-contexts/autoconfiguration-overrides.xml")
    class UltraRootAutoConfigurationOverrides { }
}
