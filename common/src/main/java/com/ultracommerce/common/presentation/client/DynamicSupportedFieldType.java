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
package com.ultracommerce.common.presentation.client;

import com.ultracommerce.common.UltraEnumerationType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This extensible enumeration controls the field types that are available for users to choose from when creating
 * FieldDefinitions in the admin tool. This list should be a strict subset of {@link SupportedFieldType} and will
 * throw an exception if a non-matching type is added.
 * 
 * @author Andre Azzolini (apazzolini)
 */
public class DynamicSupportedFieldType implements Serializable, UltraEnumerationType {

    private static final long serialVersionUID = 1L;

    private static final Map<String, DynamicSupportedFieldType> TYPES = new LinkedHashMap<String, DynamicSupportedFieldType>();

    public static final DynamicSupportedFieldType STRING = new DynamicSupportedFieldType("STRING", "String");
    public static final DynamicSupportedFieldType HTML = new DynamicSupportedFieldType("HTML", "Rich Text");
    public static final DynamicSupportedFieldType MONEY = new DynamicSupportedFieldType("MONEY", "Money");
    public static final DynamicSupportedFieldType COLOR = new DynamicSupportedFieldType("COLOR", "Color");
    public static final DynamicSupportedFieldType ASSET_LOOKUP = new DynamicSupportedFieldType("ASSET_LOOKUP", "Image");
    public static final DynamicSupportedFieldType PRODUCT_LOOKUP = new DynamicSupportedFieldType("ADDITIONAL_FOREIGN_KEY|com.ultracommerce.core.catalog.domain.Product", "Product Lookup");
    public static final DynamicSupportedFieldType CATEGORY_LOOKUP = new DynamicSupportedFieldType("ADDITIONAL_FOREIGN_KEY|com.ultracommerce.core.catalog.domain.Category", "Category Lookup");
    public static final DynamicSupportedFieldType DATE = new DynamicSupportedFieldType("DATE", "Date");

    public static DynamicSupportedFieldType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public DynamicSupportedFieldType() {
        //do nothing
    }

    public DynamicSupportedFieldType(final String type, final String friendlyType) {
        verifyLegalType(type);
        this.friendlyType = friendlyType;
        setType(type);
    }
    
    /**
     * @param type
     * @throws IllegalArgumentException when the given type does not exist in {@link SupportedFieldType}
     */
    public static void verifyLegalType(String type) {
        if (type.contains("|")) {
            type = type.substring(0, type.indexOf('|'));
        }
        SupportedFieldType.valueOf(type);
    }
    
    /**
     * @return a cloned list of the currently known {@link DynamicSupportedFieldType}s.
     */
    public static List<DynamicSupportedFieldType> getTypes() {
        List<DynamicSupportedFieldType> list = new ArrayList<DynamicSupportedFieldType>(TYPES.size());
        for (Entry<String, DynamicSupportedFieldType> entry : TYPES.entrySet()) {
            list.add(entry.getValue());
        }
        return list;
    }
    
    public String getType() {
        return type;
    }

    public String getFriendlyType() {
        return friendlyType;
    }

    private void setType(final String type) {
        this.type = type;
        if (!TYPES.containsKey(type)) {
            TYPES.put(type, this);
        } else {
            throw new RuntimeException("Cannot add the type: (" + type + "). It already exists as a type via " + getInstance(type).getClass().getName());
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
        DynamicSupportedFieldType other = (DynamicSupportedFieldType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

}
