/*
 * #%L
 * UltraCommerce Common Libraries
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
package com.ultracommerce.common.presentation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 
 * @author jfischer
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ValidationConfiguration {
    
    /**
     * <p>The fully qualified classname of the com.ultracommerce.openadmin.server.service.persistence.validation.PropertyValidator
     * instance to use for validation</p>
     * 
     * <p>If you need to do dependency injection, this can also correspond to a bean ID that implements the 
     * com.ultracommerce.openadmin.server.service.persistence.validation.PropertyValidator interface.</p>
     * 
     * @return the validator implementation. This implementation should be an instance of
     * com.ultracommerce.openadmin.server.service.persistence.validation.PropertyValidator and could be either a bean
     * name or fully-qualified class name
     */
    String validationImplementation();
    
    /**
     * <p>Optional configuration items that can be used to setup the validator</p>. Most validators should have at least
     * a single configuration item with {@link ConfigurationItem#ERROR_MESSAGE}.
     * 
     * @return validator configuration attributes
     */
    ConfigurationItem[] configurationItems() default {};
}
