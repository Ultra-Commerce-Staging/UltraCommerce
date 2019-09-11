package org.broadleafcommerce.common.extensibility.cache.ehcache;

import org.broadleafcommerce.common.extensibility.cache.TimedValueHolder;

/**
 * Convenience class providing a 10 minute expiry policy, along with the ability to override it on a per entry basis when 
 * using a {@link TimedValueHolder} as the cached value.
 * 
 * @author Kelly Tisdell
 *
 */
public final class TenMinuteExpiryPolicy extends DefaultExpiryPolicy {

    public TenMinuteExpiryPolicy() {
        super(60 * 10);
    }
}
