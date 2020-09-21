/*
 * #%L
 * UltraCommerce Framework
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
package com.ultracommerce.core.catalog.domain;

import com.ultracommerce.common.copy.MultiTenantCloneable;
import com.ultracommerce.common.value.ValueAssignable;

/**
 * 
 * @author Phillip Verheyden
 */
public interface CategoryAttribute extends ValueAssignable<String>, MultiTenantCloneable<CategoryAttribute> {

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public Long getId();

    /**
     * Sets the id.
     * 
     * @param id the new id
     */
    public void setId(Long id);

    /**
     * Gets the value.
     * 
     * @return the value
     */
    public String getValue();

    /**
     * Sets the value.
     * 
     * @param value the new value
     */
    public void setValue(String value);

    /**
     * Gets the {@link Category}.
     * 
     * @return the {@link Category}
     */
    public Category getCategory();

    /**
     * Sets the {@link Category}.
     * 
     * @param category the new {@link Category}
     */
    public void setCategory(Category category);

    /**
     * Gets the name.
     * 
     * @return the name
     */
    public String getName();

    /**
     * Sets the name.
     * 
     * @param name the new name
     */
    public void setName(String name);

}
