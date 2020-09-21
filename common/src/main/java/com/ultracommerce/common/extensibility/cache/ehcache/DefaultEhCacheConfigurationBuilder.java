/*
 * #%L
 * UltraCommerce Common Libraries
 * %%
 * Copyright (C) 2009 - 2020 Ultra Commerce
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

import com.ultracommerce.common.extensibility.cache.DefaultJCacheConfigurationBuilder;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.ExpiryPolicy;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.stereotype.Service;

import javax.cache.configuration.Configuration;

@Service("ucJCacheConfigurationBuilder")
@ConditionalOnEhCache
public class DefaultEhCacheConfigurationBuilder extends DefaultJCacheConfigurationBuilder {

    @Override
    public <K, V> Configuration<K, V> buildConfiguration(int ttlSeconds, int maxElementsInMemory, Class<K> keyClass, Class<V> valueClass) {
        ExpiryPolicy<Object, Object> expiryPolicy = new DefaultExpiryPolicy(ttlSeconds);
        
        CacheConfiguration config = CacheConfigurationBuilder.
                newCacheConfigurationBuilder(valueClass, keyClass, ResourcePoolsBuilder.heap(maxElementsInMemory))
                .withExpiry(expiryPolicy)
                .build();

        return Eh107Configuration.fromEhcacheCacheConfiguration(config);
    }

}
