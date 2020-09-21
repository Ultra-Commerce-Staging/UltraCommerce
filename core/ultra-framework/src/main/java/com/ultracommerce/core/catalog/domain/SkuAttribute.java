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
 * Implementations of this interface are used to hold data about a SKU's Attributes.
 * A SKU Attribute is a designator on a SKU that differentiates it from other similar SKUs
 * (for example: Blue attribute for hat).
 * <br>
 * <br>
 * You should implement this class if you want to make significant changes to how the
 * class is persisted.  If you just want to add additional fields then you should
 * extend {@link SkuAttributeImpl}.
 *
 * @see {@link SkuAttributeImpl}, {@link Sku}
 * @author btaylor
 *
 */
public interface SkuAttribute extends ValueAssignable<String>, MultiTenantCloneable<SkuAttribute> {

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
     * Gets the sku.
     *
     * @return the sku
     */
    public Sku getSku();

    /**
     * Sets the sku.
     *
     * @param sku the new sku
     */
    public void setSku(Sku sku);

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
