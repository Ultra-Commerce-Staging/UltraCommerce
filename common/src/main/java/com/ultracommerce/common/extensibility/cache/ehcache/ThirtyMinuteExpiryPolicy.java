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
package com.ultracommerce.common.extensibility.cache.ehcache;

import com.ultracommerce.common.extensibility.cache.TimedValueHolder;

/**
 * Convenience class providing a 30 minute expiry policy, along with the ability to override it on a per entry basis when 
 * using a {@link TimedValueHolder} as the cached value. This can also be used in the EhCache XML to specify an expiry policy:
 * 
 * <pre>
 * {@code
 * <cache alias="myCache">
 *        <expiry>
 *            <class>com.ultracommerce.common.extensibility.cache.ehcache.ThirtyMinuteExpiryPolicy</class>
 *        </expiry>
 *        <heap>5000</heap>
 * </cache>
 * }
 * </pre>
 * 
 * @author Kelly Tisdell
 *
 */
public final class ThirtyMinuteExpiryPolicy extends DefaultExpiryPolicy {

    public ThirtyMinuteExpiryPolicy() {
        super(60 * 30);
    }
}
