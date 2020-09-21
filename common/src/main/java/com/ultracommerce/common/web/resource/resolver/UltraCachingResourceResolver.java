/*
 * #%L
 * ultra-theme
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
package com.ultracommerce.common.web.resource.resolver;

import com.ultracommerce.common.site.domain.Theme;
import com.ultracommerce.common.web.UltraRequestContext;
import com.ultracommerce.common.web.resource.UltraContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.Ordered;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.servlet.resource.AbstractResourceResolver;
import org.springframework.web.servlet.resource.CachingResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * A ResourceResolver that handles using the theme as part of the cache key and adds in
 * support to disable with environment properties.
 *
 * We bypass {@link CachingResourceResolver} and instead borrow its code in order to be
 * able to inject the theme key that is needed by UC since Spring's class could not be
 * leveraged otherwise.
 *
 *  {@code }
 * 
 * @author Brian Polster
 * @since Ultra 4.0
 */
@Component("ucCacheResourceResolver")
public class UltraCachingResourceResolver extends AbstractResourceResolver implements Ordered {

    public static final String RESOLVED_RESOURCE_CACHE_KEY_PREFIX = "resolvedResource:";
    public static final String RESOLVED_URL_PATH_CACHE_KEY_PREFIX = "resolvedUrlPath:";
    public static final String RESOLVED_RESOURCE_CACHE_KEY_PREFIX_NULL = "resolvedResourceNull:";
    public static final String RESOLVED_URL_PATH_CACHE_KEY_PREFIX_NULL = "resolvedUrlPathNull:";
    private static final Object NULL_REFERENCE = new Object();
    private int order = UltraResourceResolverOrder.UC_CACHE_RESOURCE_RESOLVER;

    private final Cache cache;

    @javax.annotation.Resource(name = "ucSpringCacheManager")
    private CacheManager cacheManager;

    @javax.annotation.Resource(name = "ucUltraContextUtil")
    protected UltraContextUtil ucContextUtil;
    
    private static final String DEFAULT_CACHE_NAME = "ucResourceCacheElements";

    @Value("${resource.caching.enabled:true}")
    protected boolean resourceCachingEnabled;

    @Autowired
    public UltraCachingResourceResolver(@Qualifier("ucSpringCacheManager") CacheManager cacheManager) {
        this(cacheManager.getCache(DEFAULT_CACHE_NAME));
    }

    // Allows for an implementor to override the default cache settings.
    public UltraCachingResourceResolver(Cache cache) {
        Assert.notNull(cache, "'cache' is required");
        this.cache = cache;
    }

    /**
     * Return the configured {@code Cache}.
     */
    public Cache getCache() {
        return this.cache;
    }

    @Override
    protected Resource resolveResourceInternal(HttpServletRequest request, String requestPath,
            List<? extends Resource> locations, ResourceResolverChain chain) {
        ucContextUtil.establishThinRequestContext();

        if (resourceCachingEnabled) {
            String key = computeKey(request, requestPath) + getThemePathFromBRC();
            Resource resource = this.cache.get(key, Resource.class);

            if (resource != null && resource.exists()) {
                if (logger.isTraceEnabled()) {
                    logger.trace("Found match");
                }
                return resource;
            }

            resource = chain.resolveResource(request, requestPath, locations);
            if (resource != null) {
                if (logger.isTraceEnabled()) {
                    logger.trace("Putting resolved resource in cache");
                }
                this.cache.put(key, resource);
            }

            if (logger.isDebugEnabled()) {
                if (resource == null) {
                    logger.debug("Cache resolver, returned a null resource " + requestPath);
                } else if (!resource.exists()) {
                    logger.debug("Cache resolver, returned a resource that doesn't exist "
                            + requestPath + " - " + resource);
                }
            }
            return resource;
        } else {
            return chain.resolveResource(request, requestPath, locations);
        }
    }

    /**
     * Pulled from {@link CachingResourceResolver}
     *
     * @param request
     * @param requestPath
     * @return
     */
    protected String computeKey(HttpServletRequest request, String requestPath) {
        StringBuilder key = new StringBuilder(RESOLVED_RESOURCE_CACHE_KEY_PREFIX);
        key.append(requestPath);
        if (request != null) {
            String encoding = request.getHeader("Accept-Encoding");
            if (encoding != null && encoding.contains("gzip")) {
                key.append("+encoding=gzip");
            }
        }
        return key.toString();
    }

    @Override
    protected String resolveUrlPathInternal(String resourceUrlPath,
            List<? extends Resource> locations, ResourceResolverChain chain) {
        if (resourceCachingEnabled) {
            String response = null;

            String notFoundKey = RESOLVED_URL_PATH_CACHE_KEY_PREFIX_NULL + resourceUrlPath + getThemePathFromBRC();
            Object nullResource = getCache().get(notFoundKey, Object.class);
            if (nullResource != null) {
                logNullReferenceUrlPatchMatch(resourceUrlPath);
                return null;
            }

            String foundKey = RESOLVED_URL_PATH_CACHE_KEY_PREFIX + resourceUrlPath + getThemePathFromBRC();
            String resolvedUrlPath = this.cache.get(foundKey, String.class);
            if (resolvedUrlPath != null) {
                if (logger.isTraceEnabled()) {
                    logger.trace("Found match");
                }
                response = resolvedUrlPath;
            } else {
                resolvedUrlPath = chain.resolveUrlPath(resourceUrlPath, locations);
                if (resolvedUrlPath != null) {
                    if (logger.isTraceEnabled()) {
                        logger.trace("Putting resolved resource URL path in cache");
                    }
                    this.cache.put(foundKey, resolvedUrlPath);
                    response = resolvedUrlPath;
                }
            }

            if (response == null) {
                if (logger.isTraceEnabled()) {
                    logger.trace(String.format("Putting resolved null reference url " +
                            "path in cache for '%s'", resourceUrlPath));
                }
                getCache().put(notFoundKey, NULL_REFERENCE);
            }
            return response;
        } else {
            return chain.resolveUrlPath(resourceUrlPath, locations);
        }
    }

    private void logNullReferenceUrlPatchMatch(String resourceUrlPath) {
        if (logger.isTraceEnabled()) {
            logger.trace(String.format("Found null reference url path match for '%s'", resourceUrlPath));
        }
    }

    /**
     * Returns the theme path from the {@link com.ultracommerce.common.web.UltraRequestContext} or an empty
     * string if no theme was resolved
     *
     * @return
     */
    protected String getThemePathFromBRC() {
        String themePath = null;
        Theme theme = UltraRequestContext.getUltraRequestContext().getTheme();
        if (theme != null) {
            themePath = theme.getPath();
        }
        return themePath == null ? "" : "-" + themePath;
    }

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

}
