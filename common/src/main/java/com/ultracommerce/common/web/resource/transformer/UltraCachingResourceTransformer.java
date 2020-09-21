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
package com.ultracommerce.common.web.resource.transformer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.web.resource.resolver.UltraResourceTransformerOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.Ordered;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.resource.CachingResourceResolver;
import org.springframework.web.servlet.resource.CachingResourceTransformer;
import org.springframework.web.servlet.resource.ResourceTransformerChain;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

/**
 * Wraps Spring's {@link CachingResourceResolver} but adds in support to disable with 
 * environment properties.
 * 
 *  {@code }
 * 
 * @author Brian Polster
 * @since Ultra 4.0
 */
@Component("ucCachingResourceTransformer")
public class UltraCachingResourceTransformer extends CachingResourceTransformer implements Ordered {

    protected static final Log LOG = LogFactory.getLog(UltraCachingResourceTransformer.class);
    private int order = UltraResourceTransformerOrder.UC_CACHE_RESOURCE_TRANSFORMER;
    
    @javax.annotation.Resource(name = "ucSpringCacheManager")
    private CacheManager cacheManager;
    
    private static final String DEFAULT_CACHE_NAME = "ucResourceTransformerCacheElements";

    @Value("${resource.transformer.caching.enabled:true}")
    protected boolean resourceTransformerCachingEnabled;

    @Autowired
    public UltraCachingResourceTransformer(@Qualifier("ucSpringCacheManager") CacheManager cacheManager) {
        super(cacheManager, DEFAULT_CACHE_NAME);
    }

    // Allows for an implementor to override the default cache settings.
    public UltraCachingResourceTransformer(Cache cache) {
        super(cache);
    }

    @Override
    public Resource transform(HttpServletRequest request, Resource resource, ResourceTransformerChain transformerChain)
            throws IOException {
        if (resourceTransformerCachingEnabled) {
            return super.transform(request, resource, transformerChain);
        } else {
            return transformerChain.transform(request, resource);
        }
    }

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

}
