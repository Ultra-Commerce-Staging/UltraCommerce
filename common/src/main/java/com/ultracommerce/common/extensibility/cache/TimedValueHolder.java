/*
 * #%L
 * UltraCommerce Common Libraries
 * %%
 * Copyright (C) 2009 - 2019 Ultra Commerce
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
package com.ultracommerce.common.extensibility.cache;

import java.io.Serializable;

/**
 * JCache does not provide direct APIs for Per-Mapping expiry.  This means that, 
 * by default, JCache does not allow you to specify different expiry times per entry, 
 * effectively overriding the default expiry.  Instances of this class provide a time 
 * to live (TTL) along with a value.  This is just a convenience wrapper.  For caches that 
 * have or allow per-entry TTL settings, this provides a convenient way to pass those details in.
 * 
 * @author Kelly Tisdell
 *
 */
public class TimedValueHolder implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Integer timeToLive;
    private Object value;
    
    public TimedValueHolder(Object value, Integer ttlSeconds) {
        this.value = value;
        this.timeToLive = ttlSeconds;
    }
    
    public Object getValue() {
        return value;
    }
    
    public Integer getTimeToLiveSeconds() {
        return timeToLive;
    }

}
