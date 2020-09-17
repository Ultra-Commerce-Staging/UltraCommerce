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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows a non-comprehensive override of admin presentation annotation
 * property values for a target entity field.
 *
 * @see com.ultracommerce.common.presentation.AdminPresentation
 * @see com.ultracommerce.common.presentation.AdminPresentationToOneLookup
 * @see com.ultracommerce.common.presentation.AdminPresentationDataDrivenEnumeration
 * @see com.ultracommerce.common.presentation.AdminPresentationCollection
 * @see com.ultracommerce.common.presentation.AdminPresentationAdornedTargetCollection
 * @see com.ultracommerce.common.presentation.AdminPresentationMap
 * @author Jeff Fischer
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AdminPresentationMergeOverride {

    /**
     * The name of the property whose admin presentation annotation should be overwritten
     *
     * @return the name of the property that should be overwritten
     */
    String name();

    /**
     * The array of override configuration values. Each entry correlates to a property on
     * {@link com.ultracommerce.common.presentation.AdminPresentation},
     * {@link com.ultracommerce.common.presentation.AdminPresentationToOneLookup},
     * {@link com.ultracommerce.common.presentation.AdminPresentationDataDrivenEnumeration},
     * {@link com.ultracommerce.common.presentation.AdminPresentationAdornedTargetCollection},
     * {@link com.ultracommerce.common.presentation.AdminPresentationCollection} or
     * {@link com.ultracommerce.common.presentation.AdminPresentationMap}
     *
     * @return The array of override configuration values.
     */
    AdminPresentationMergeEntry[] mergeEntries();
}
