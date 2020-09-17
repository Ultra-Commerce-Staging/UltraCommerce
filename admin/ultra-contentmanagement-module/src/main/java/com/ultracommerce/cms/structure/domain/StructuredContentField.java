/*
 * #%L
 * UltraCommerce CMS Module
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
package com.ultracommerce.cms.structure.domain;

import com.ultracommerce.common.copy.MultiTenantCloneable;

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Holds the values for custom fields that are part of a <code>StructuredContent</code> item.
 * <br>
 * Each item maintains a list of its custom fields.    The fields associated with an item are
 * determined by the {@link com.ultracommerce.cms.field.domain.FieldDefinition}s  associated
 * with the {@link StructuredContentType}.
 *
 * @see StructuredContentType
 * @see com.ultracommerce.cms.field.domain.FieldDefinition
 * @author bpolster
 *
 */
public interface StructuredContentField extends Serializable, Cloneable,MultiTenantCloneable<StructuredContentField> {

    /**
     * Gets the primary key.
     *
     * @return the primary key
     */
    @Nullable
    public Long getId();


    /**
     * Sets the primary key.
     *
     * @param id the new primary key
     */
    public void setId(@Nullable Long id);

    /**
     * Returns the fieldKey associated with this field.   The key used for a
     * <code>StructuredContentField</code> is determined by the associated
     * {@link com.ultracommerce.cms.field.domain.FieldDefinition} that was used by the
     * Content Management System to create this instance.
     *
     * As an example, a <code>StructuredContentType</code> might be configured to contain a
     * field definition with a key of "targetUrl".
     *
     * @return the key associated with this item
     * @see com.ultracommerce.cms.field.domain.FieldDefinition
     */
    @Nonnull
    public String getFieldKey();

    /**
     * Sets the fieldKey.
     * @param fieldKey
     * @see com.ultracommerce.cms.field.domain.FieldDefinition
     */
    public void setFieldKey(@Nonnull String fieldKey);

    /**
     * Returns the value for this custom field.
     *
     * @param value
     */
    public void setValue(@Nonnull String value);

    /**
     * Sets the value of this custom field.
     * @return
     */
    @Nonnull
    public String getValue();

    /**
     * @return a deep copy of this object. By default, clones the fieldKey and value fields and ignores the auditable
     * and id fields.
     */
    public StructuredContentField clone();

}
