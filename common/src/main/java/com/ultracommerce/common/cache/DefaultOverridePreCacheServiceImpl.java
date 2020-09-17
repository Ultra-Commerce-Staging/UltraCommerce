/*
 * #%L
 * UltraCommerce Common Enterprise
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
package com.ultracommerce.common.cache;

import com.ultracommerce.common.extension.StandardCacheItem;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * Default implementation of {@link OverridePreCacheService}. Simply contains no-ops methods.
 *
 * @author Jeff Fischer
 */
@Service("ucOverridePreCacheService")
public class DefaultOverridePreCacheServiceImpl implements OverridePreCacheService {

    @Override
    public List<StandardCacheItem> findElements(String... cacheKeys) {
        return null;
    }

    @Override
    public boolean isActiveForType(String type) {
        return false;
    }

    @Override
    public boolean isActiveIsolatedSiteForType(Long siteId, String entityType) {
        return false;
    }

    @Override
    public void groomCacheBySiteOverride(String entityType, Long cloneId, boolean isRemove) {
        //do nothing
    }

    @Override
    public void groomCacheByTargetEntity(String entityType, Serializable id) {
        //do nothing
    }

    @Override
    public void refreshCache() {
        //do nothing
    }
}
