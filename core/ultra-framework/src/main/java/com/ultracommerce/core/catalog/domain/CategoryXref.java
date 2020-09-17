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

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * Implementations of this interface are used to hold data about the many-to-many relationship between
 * the Category table and a parent Category.
 * </p>
 * You should implement this class if you want to make significant changes to the
 * relationship between Category and parent Category.  If you just want to add additional fields
 * then you should extend {@link CategoryXrefImpl}.
 *
 *  @see {@link CategoryXrefImpl},{@link Category}
 *
 */
public interface CategoryXref extends Serializable, MultiTenantCloneable<CategoryXref> {

    /**
     * Return the order for sorting
     *
     * @return
     */
    public BigDecimal getDisplayOrder();

    public void setDisplayOrder(final BigDecimal displayOrder);

    /**
     * Return the parent category
     *
     * @return
     */
    public Category getCategory();

    public void setCategory(final Category category);

    /**
     * Return the child category
     *
     * @return
     */
    public Category getSubCategory();

    public void setSubCategory(final Category subCategory);

    /**
     * Return the primary key
     *
     * @param id
     */
    public void setId(Long id);

    public Long getId();

    /**
     * Specifies the default reference between a category and a parent category. This replaces the concept of
     * {@link com.ultracommerce.core.catalog.domain.CategoryImpl#getDefaultParentCategory()} ()}
     *
     * @see com.ultracommerce.core.catalog.domain.CategoryImpl#getParentCategory() ()
     * @return the default reference between a category and a parent category
     */
    Boolean getDefaultReference();

    void setDefaultReference(Boolean defaultReference);
}
