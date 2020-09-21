/*
 * #%L
 * UltraCommerce Common Libraries
 * %%
 * Copyright (C) 2009 - 2018 Ultra Commerce
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
package com.ultracommerce.common.persistence;

import com.ultracommerce.common.copy.MultiTenantCloneable;

import java.util.Map;

/**
 * Provides additional metadata and performs final modifications for an entity before persistence.
 * 
 * In order to perform duplication using {@link EntityDuplicator}, an 
 * {@code EntityDuplicationHelper} must be made for a specific entity.
 * 
 * @author Nathan Moore (nathanmoore).
 */
public interface EntityDuplicationHelper<T> {

    boolean canHandle(MultiTenantCloneable candidate);

    /**
     * @return Hints used to fine tune copying - generally support for hints is included in 
     * {@link com.ultracommerce.common.copy.MultiTenantCloneable#createOrRetrieveCopyInstance(com.ultracommerce.common.copy.MultiTenantCopyContext)} implementations.
     */
    Map<String, String> getCopyHints();
    
    void addCopyHint(final String name, final String hint);

    void modifyInitialDuplicateState(T copy);
}
