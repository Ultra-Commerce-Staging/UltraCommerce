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
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

/**
 * Implements {@link #getCopyHints()} and {@link #addCopyHint(String, String)}, 
 * using a {@link HashMap} as the underlying data structure for storing the hints. Also provides a
 * helper method {@link #getCopySuffix()} for the usual use-case of changing the name of the 
 * duplicated entity.
 * 
 * @author Nathan Moore (nathanmoore).
 */
public abstract class AbstractEntityDuplicationHelper<T> implements EntityDuplicationHelper<T> {
    
    protected Map<String, String> copyHints = new HashMap<>();
    
    protected final Environment env;
    
    public AbstractEntityDuplicationHelper(final Environment environment) {
        this.env = environment;
    }

    /**
     * Defaults to " - Copy" but can be overridden using 
     * {@code admin.entity.duplication.suffix.default}. 
     * 
     * @return suffix to append to the name/identifier of the entity copy
     */
    protected String getCopySuffix() {
        return env.getProperty("admin.entity.duplication.suffix.default", String.class, " - Copy");
    }
    
    @Override 
    public abstract boolean canHandle(final MultiTenantCloneable candidate);

    @Override 
    public Map<String, String> getCopyHints() {
        return copyHints;
    }

    @Override 
    public void addCopyHint(final String name, final String hint) {
        copyHints.put(name, hint);
    }

    @Override 
    public abstract void modifyInitialDuplicateState(final T copy);
}
