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
package com.ultracommerce.common.cache;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

import com.ultracommerce.common.time.SystemTime;

/**
 * @author Jeff Fischer
 */
public class CacheStat {

    protected AtomicLong requestCount = new AtomicLong(0L);
    protected AtomicLong cacheHitCount = new AtomicLong(0L);
    protected Long lastLogTime = SystemTime.asMillis(true);

    public Long getCacheHitCount() {
        return cacheHitCount.longValue();
    }

    public Long getLastLogTime() {
        return lastLogTime;
    }

    public synchronized void setLastLogTime(Long lastLogTime) {
        this.lastLogTime = lastLogTime;
    }

    public Long getRequestCount() {
        return requestCount.longValue();
    }

    public void incrementRequest() {
        requestCount.incrementAndGet();
    }

    public void incrementHit() {
        cacheHitCount.incrementAndGet();
    }

    public BigDecimal getHitRate() {
        if (getRequestCount() == 0) {
            return new BigDecimal(-1);
        }
        BigDecimal percentage = new BigDecimal(getCacheHitCount()).divide(new BigDecimal(getRequestCount
                ()), 2, BigDecimal.ROUND_HALF_UP);
        percentage = percentage.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
        return percentage;
    }
}
