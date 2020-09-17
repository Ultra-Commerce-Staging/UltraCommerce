/*
 * class#%L
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.util.EfficientLRUMap;
import com.ultracommerce.common.util.StringUtil;
import com.ultracommerce.core.catalog.domain.SkuImpl;
import com.ultracommerce.openadmin.dto.FieldMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Jerry Ocanas (jocanas)
 */
@Service("ucSkuMetadataCacheService")
public class SkuMetadataCacheServiceImpl implements SkuMetadataCacheService {

    private static final Log LOG = LogFactory.getLog(SkuMetadataCacheServiceImpl.class);

    @Value("${cache.entity.dao.metadata.ttl}")
    protected int cacheEntityMetaDataTtl;

    protected long lastCacheFlushTime = System.currentTimeMillis();

    protected static final Map<String, Map<String, FieldMetadata>> METADATA_CACHE = new EfficientLRUMap<>(1000);

    @Override
    public Map<String, Map<String, FieldMetadata>> getEntireCache() {
        return METADATA_CACHE;
    }
    
    @Override
    public boolean useCache() {
        if (cacheEntityMetaDataTtl < 0) {
            return true;
        }
        if (cacheEntityMetaDataTtl == 0) {
            return false;
        } else {
            if ((System.currentTimeMillis() - lastCacheFlushTime) > cacheEntityMetaDataTtl) {
                lastCacheFlushTime = System.currentTimeMillis();
                METADATA_CACHE.clear();
                return true;
            } else {
                return true;
            }
        }
    }
    
    @Override
    public Map<String, FieldMetadata> getFromCache(String cacheKey) {
        if (useCache()) {
            return METADATA_CACHE.get(cacheKey);
        } else {
            return null;
        }
    }
    
    @Override
    public boolean addToCache(String cacheKey, Map<String, FieldMetadata> metadata) {
        if (useCache()) {
            METADATA_CACHE.put(cacheKey, metadata);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void invalidateFromCache(String cacheKey) {
        LOG.debug("Invalidating Sku metadata cache for: " + StringUtil.sanitize(cacheKey));
        METADATA_CACHE.remove(cacheKey);
    }
    
    @Override
    public String buildCacheKey(String productId) {
        String key = SkuImpl.class.getName();
        if (productId != null) {
            key += "_" + productId;
        }
        return key;
    }

}
