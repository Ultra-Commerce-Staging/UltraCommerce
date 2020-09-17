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

import org.springframework.core.annotation.Order;

/**
 * Adds a default property source to look for a {@code runtime-environment} folder and apply the -shared overriding
 * mechanics to it.
 * 
 * @author Phillip Verheyden (phillipuniverse)
 * @since 5.2
 */
@Order(UltraSharedOverrideProfileAwarePropertySource.DEFAULT_ORDER)
public class DefaultRuntimeEnvironmentProfileAwarePropertySource implements UltraSharedOverrideProfileAwarePropertySource {

    @Override
    public String getClasspathFolder() {
        return "runtime-properties";
    }

}
