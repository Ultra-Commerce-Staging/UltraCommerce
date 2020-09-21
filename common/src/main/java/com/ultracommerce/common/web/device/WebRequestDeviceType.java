/*
 * #%L
 * UltraCommerce Common Libraries
 * %%
 * Copyright (C) 2009 - 2017 Ultra Commerce
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
package com.ultracommerce.common.web.device;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import com.ultracommerce.common.UltraEnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Nathan Moore (nathanmoore).
 */
public class WebRequestDeviceType implements UltraEnumerationType, Serializable {
    private static final long serialVersionUID = 1L;
    
    private static final Map<String, WebRequestDeviceType> TYPES = new LinkedHashMap<>();
    
    public static final WebRequestDeviceType UNKNOWN = new WebRequestDeviceType("UNKNOWN", "Unknown");
    public static final WebRequestDeviceType NORMAL =  new WebRequestDeviceType("NORMAL", "Normal");
    public static final WebRequestDeviceType MOBILE =  new WebRequestDeviceType("MOBILE", "Mobile");
    public static final WebRequestDeviceType TABLET =  new WebRequestDeviceType("TABLET", "Tablet");
    
    private String type;
    private String friendlyType;
    
    WebRequestDeviceType(){}
    
    WebRequestDeviceType(final String type, final String friendlyType) {
        this.friendlyType = friendlyType;
        setType(type);
    }
    
    private void setType(final String type) {
        this.type = type;
        
        if (!TYPES.containsKey(type)) {
            TYPES.put(type, this);
        }
    }

    public static WebRequestDeviceType getInstance(final String type) {
        return TYPES.get(type);
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getFriendlyType() {
        return friendlyType;
    }

    @Override
    public String toString() {
        return getFriendlyType();
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && getClass().isAssignableFrom(o.getClass())) {
            final WebRequestDeviceType other = (WebRequestDeviceType) o;
            
            return new EqualsBuilder()
                    .append(getType(), other.getType())
                    .append(getFriendlyType(), other.getFriendlyType())
                    .build();
        }
        
        return false;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(getFriendlyType())
                .append(getType())
                .build();
    }
}
