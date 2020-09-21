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

package com.ultracommerce.common.payment;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import com.ultracommerce.common.UltraEnumerationType;

/**
 * when a payment returns in error, this value indicates whether it is a "soft" decline (apt to be retried) or a "hard" one.
 * @author gdiaz
 *
 */
public class PaymentDeclineType implements Serializable, UltraEnumerationType {

    private static final long serialVersionUID = 1L;

    private static final Map<String, PaymentDeclineType> TYPES = new LinkedHashMap<String, PaymentDeclineType>();

    public static final PaymentDeclineType SOFT = new PaymentDeclineType("SOFT", "Soft decline");
    public static final PaymentDeclineType HARD = new PaymentDeclineType("HARD", "Hard decline");

    public static PaymentDeclineType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public PaymentDeclineType() {
        //do nothing
    }

    public PaymentDeclineType(final String type, final String friendlyType) {
        this.friendlyType = friendlyType;
        setType(type);
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
        PaymentDeclineType other = (PaymentDeclineType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
}
