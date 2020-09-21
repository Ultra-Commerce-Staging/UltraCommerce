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
package com.ultracommerce.core.search.service.solr.index;

import org.apache.commons.collections4.MapUtils;
import com.ultracommerce.common.exception.ServiceException;
import com.ultracommerce.common.site.domain.Catalog;
import com.ultracommerce.common.web.UltraRequestContext;
import com.ultracommerce.core.search.dao.CatalogStructure;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides a single cache while exposing a block of code for execution to
 * {@link com.ultracommerce.core.search.service.solr.index.SolrIndexService#performCachedOperation(com.ultracommerce.core.search.service.solr.SolrIndexCachedOperation.CacheOperation)}.
 * This serves to boost performance while executing multiple calls to {@link com.ultracommerce.core.search.service.solr.index.SolrIndexService#buildIncrementalIndex(int, int, boolean)}.
 *
 * @see com.ultracommerce.core.search.service.solr.index.SolrIndexService
 * @author Jeff Fischer
 */
public class SolrIndexCachedOperation {

    public static final Long DEFAULT_CATALOG_CACHE_KEY = 0l;
    
    private static final ThreadLocal<Map<Long, CatalogStructure>> CACHE = new ThreadLocal<Map<Long, CatalogStructure>>();

    /**
     * Retrieve the cache bound to the current thread.
     *
     * @return The cache for the current thread, or null if not set
     */
    public static CatalogStructure getCache() {
        UltraRequestContext ctx = UltraRequestContext.getUltraRequestContext();
        Catalog currentCatalog = ctx == null ? null : ctx.getCurrentCatalog();
        if (currentCatalog != null) {
            return MapUtils.getObject(CACHE.get(), currentCatalog.getId());
        } else {
            return MapUtils.getObject(CACHE.get(), DEFAULT_CATALOG_CACHE_KEY);
        }
    }

    /**
     * Set the cache on the current thread
     *
     * @param cache the cache object (usually an empty map)
     */
    public static void setCache(CatalogStructure cache) {
        UltraRequestContext ctx = UltraRequestContext.getUltraRequestContext();
        Catalog currentCatalog = ctx == null ? null : ctx.getCurrentCatalog();
        Map<Long, CatalogStructure> catalogCaches = CACHE.get();
        if (catalogCaches == null) {
            catalogCaches = new HashMap<Long, CatalogStructure>();
            CACHE.set(catalogCaches);
        }
        if (currentCatalog != null) {
            catalogCaches.put(currentCatalog.getId(), cache);
        } else {
            catalogCaches.put(DEFAULT_CATALOG_CACHE_KEY, cache);
        }
    }

    /**
     * Clear the thread local cache from the thread
     */
    public static void clearCache() {
        CACHE.remove();
    }

    /**
     * Basic interface representing a block of work to perform with a single cache instance
     */
    public interface CacheOperation {

        /**
         * Execute the block of work
         *
         * @throws ServiceException
         */
        void execute() throws ServiceException;

    }
}
