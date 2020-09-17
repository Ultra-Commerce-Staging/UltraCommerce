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
package com.ultracommerce.core.catalog.service.type;

import com.ultracommerce.common.UltraEnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An extendible enumeration of product option validation strategy types.
 * 
 * @author ppatel
 *
 */
public class ProductOptionValidationStrategyType implements Serializable, UltraEnumerationType {

    private static final long serialVersionUID = 1L;

    private static final Map<String, ProductOptionValidationStrategyType> TYPES = new LinkedHashMap<String, ProductOptionValidationStrategyType>();

    public static final ProductOptionValidationStrategyType NONE = new ProductOptionValidationStrategyType("NONE", 1000, "None");
    public static final ProductOptionValidationStrategyType ADD_ITEM = new ProductOptionValidationStrategyType("ADD_ITEM", 2000, "Validate On Add Item");
    public static final ProductOptionValidationStrategyType SUBMIT_ORDER = new ProductOptionValidationStrategyType("SUBMIT_ORDER", 3000, "Validate On Submit");

    public static ProductOptionValidationStrategyType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;
    private Integer rank;

    public Integer getRank() {
        return rank;
    }

    public ProductOptionValidationStrategyType() {
        //do nothing
    }

    public ProductOptionValidationStrategyType(final String type, final Integer rank, final String friendlyType) {
        this.friendlyType = friendlyType;
        setType(type);
        this.rank = rank;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getFriendlyType() {
        return friendlyType;
    }

    private void setType(final String type) {
        this.type = type;
        if (!TYPES.containsKey(type)) {
            TYPES.put(type, this);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!getClass().isAssignableFrom(obj.getClass()))
            return false;
        ProductOptionValidationStrategyType other = (ProductOptionValidationStrategyType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
}
