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
package com.ultracommerce.core.offer.service.type;

import com.ultracommerce.common.UltraEnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An extendible enumeration of offer rule types.
 *
 */
public class OfferRuleType implements Serializable, UltraEnumerationType {
    
    private static final long serialVersionUID = 1L;

    private static final Map<String, OfferRuleType> TYPES = new LinkedHashMap<String, OfferRuleType>();

    public static final OfferRuleType ORDER = new OfferRuleType("ORDER", "Order");
    public static final OfferRuleType FULFILLMENT_GROUP = new OfferRuleType("FULFILLMENT_GROUP", "Fulfillment Group");
    public static final OfferRuleType CUSTOMER = new OfferRuleType("CUSTOMER", "Customer");
    public static final OfferRuleType TIME = new OfferRuleType("TIME", "Time");
    public static final OfferRuleType REQUEST = new OfferRuleType("REQUEST", "Request");
    public static OfferRuleType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public OfferRuleType() {
        //do nothing
    }

    public OfferRuleType(final String type, final String friendlyType) {
        this.friendlyType = friendlyType;
        setType(type);
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
        OfferRuleType other = (OfferRuleType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

}
