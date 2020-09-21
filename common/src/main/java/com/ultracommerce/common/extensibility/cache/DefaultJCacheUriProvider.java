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
package com.ultracommerce.common.extensibility.cache;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.extensibility.cache.jcache.JCacheUriProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@Component("ucJCacheUriProvider")
@ConditionalOnEhCacheMissing
public class DefaultJCacheUriProvider implements JCacheUriProvider {

    private static final Log LOG = LogFactory.getLog(DefaultJCacheUriProvider.class);

    @Value("${hibernate.javax.cache.uri:#{null}}")
    protected String configLocation;

    @Value("${hibernate.javax.cache.uri.relative:true}")
    protected boolean isLocationRelative;

    @Override
    public URI getJCacheUri() {
        if (StringUtils.isEmpty(configLocation)) {
            return null;
        }
        try {
            if (isLocationRelative) {
                URL url = getClass().getClassLoader().getResource(configLocation);
                if (url != null) {
                    return url.toURI();
                }
                LOG.warn("The property hibernate.javax.cache.uri.relative was set to true however there was no resource found for " + configLocation + ". Falling back on creating a URI from the provided config location set by the property hibernate.javax.cache.uri");
            }
            return new URI(configLocation);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Incorrect URI syntax set for property hibernate.javax.cache.uri", e);
        }
    }

}
