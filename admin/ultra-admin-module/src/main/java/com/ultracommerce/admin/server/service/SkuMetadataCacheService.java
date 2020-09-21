/*
 * #%L
 * UltraCommerce Admin Module
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
package com.ultracommerce.admin.server.service;

import com.ultracommerce.openadmin.dto.FieldMetadata;

import java.util.Map;

/**
 * @author Jerry Ocanas (jocanas)
 */
public interface SkuMetadataCacheService {

    /**
     * Whether or not to use the cache. If they cache is configured to be used but is
     * past the metadata TTL, the cache is cleared out from this method
     */
    boolean useCache();
    
    /**
     * Not generally used but could be useful in some scenarios if you need to invalidate the entire cache
     * @return the cache, keyed by {@link #buildCacheKey(String)}
     */
    Map<String, Map<String, FieldMetadata>> getEntireCache();

    /**
     * Builds out the cache key to use for the other methods
     * @param productId optional productId, can be null
     * @return
     */
    String buildCacheKey(String productId);
    
    Map<String, FieldMetadata> getFromCache(String cacheKey);

    boolean addToCache(String cacheKey, Map<String, FieldMetadata> metadata);
    
    /**
     * Allows for outside sources to invalidate entries from the Sku Metadata cache that
     * is being used during sku inspects.
     *
     * @param cacheKey from {@link #buildCacheKey(String)}
     */
    void invalidateFromCache(String cacheKey);
}