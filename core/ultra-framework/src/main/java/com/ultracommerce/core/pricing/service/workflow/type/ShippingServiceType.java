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
package com.ultracommerce.core.pricing.service.workflow.type;

import com.ultracommerce.common.UltraEnumerationType;
import com.ultracommerce.core.order.domain.FulfillmentOption;
import com.ultracommerce.core.pricing.service.fulfillment.provider.FulfillmentPricingProvider;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An extendible enumeration of shipping service types.
 * 
 * @author jfischer
 * @deprecated Should use the {@link FulfillmentOption} and {@link FulfillmentPricingProvider} paradigm
 * @see {@link FulfillmentOption}, {@link FulfillmentPricingProvider}
 */
@Deprecated
public class ShippingServiceType implements Serializable, UltraEnumerationType {

    private static final long serialVersionUID = 1L;

    private static final Map<String, ShippingServiceType> TYPES = new LinkedHashMap<String, ShippingServiceType>();

    public static final ShippingServiceType BANDED_SHIPPING = new ShippingServiceType("BANDED_SHIPPING", "Banded Shipping");
    public static final ShippingServiceType USPS = new ShippingServiceType("USPS", "United States Postal Service");
    public static final ShippingServiceType FED_EX = new ShippingServiceType("FED_EX", "Federal Express");
    public static final ShippingServiceType UPS = new ShippingServiceType("UPS", "United Parcel Service");
    public static final ShippingServiceType DHL = new ShippingServiceType("DHL", "DHL");

    public static ShippingServiceType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public ShippingServiceType() {
        //do nothing
    }

    public ShippingServiceType(final String type, final String friendlyType) {
        this.friendlyType = friendlyType;
        setType(type);
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
        ShippingServiceType other = (ShippingServiceType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
}
