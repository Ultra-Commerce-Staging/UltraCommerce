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
package com.ultracommerce.core.promotionMessage.domain.type;

import com.ultracommerce.common.UltraEnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An extendible enumeration of promotion message types.
 *
 */
public class PromotionMessageType implements Serializable, UltraEnumerationType, Comparable<PromotionMessageType> {

    private static final long serialVersionUID = 1L;

    private static final Map<String, PromotionMessageType> TYPES = new LinkedHashMap<String, PromotionMessageType>();
    public static final PromotionMessageType TARGETS = new PromotionMessageType("TARGETS", "Targets only", 1000);
    public static final PromotionMessageType QUALIFIERS = new PromotionMessageType("QUALIFIERS", "Qualifers only", 2000);
    public static final PromotionMessageType TARGETS_OR_QUALIFIERS = new PromotionMessageType("TARGETS_OR_QUALIFIERS", "Targets or Qualifiers", 3000);


    public static PromotionMessageType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;
    private int order;

    public PromotionMessageType() {
        //do nothing
    }

    public PromotionMessageType(final String type, final String friendlyType, int order) {
        this.friendlyType = friendlyType;
        setType(type);
        setOrder(order);
    }

    public void setType(final String type) {
        this.type = type;
        if (!TYPES.containsKey(type)) {
            TYPES.put(type, this);
        }
    }

    public String getType() {
        return type;
    }

    public String getFriendlyType() {
        return friendlyType;
    }
    
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
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
        PromotionMessageType other = (PromotionMessageType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
    
    @Override
    public int compareTo(PromotionMessageType arg0) {
        return this.order - arg0.order;
    }

}
