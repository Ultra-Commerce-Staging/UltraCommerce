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
package com.ultracommerce.common.presentation.override;

import com.ultracommerce.common.presentation.AdminPresentation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author pverheyden
 * @deprecated use {@link AdminPresentationMergeOverrides} instead
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Deprecated
public @interface AdminPresentationOverride {
    
    /**
     * The name of the property whose {@link AdminPresentation} annotation should be overwritten
     * 
     * @return the name of the property that should be overwritten
     */
    String name();
    
    /**
     * The {@link AdminPresentation} to overwrite the property with. This is a comprehensive override,
     * meaning whatever was declared on the target property previously will be completely replaced
     * with what is defined in this {@link AdminPresentation}.
     * 
     * @return the {@link AdminPresentation} being mapped to the attribute
     */
    AdminPresentation value() default @AdminPresentation();

}
