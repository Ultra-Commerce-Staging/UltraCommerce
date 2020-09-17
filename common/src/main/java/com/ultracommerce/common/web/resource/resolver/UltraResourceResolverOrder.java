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
package com.ultracommerce.common.web.resource.resolver;

import com.ultracommerce.common.web.resource.UltraResourceHttpRequestHandler;
import org.springframework.core.Ordered;

import javax.annotation.PostConstruct;

/**
 * Constants representing out of box Ultra Resource Resolvers.
 * 
 * Used by {@link UltraResourceHttpRequestHandler} which sorts resolvers that 
 * implement {@link Ordered} in its {@link PostConstruct} method.
 * 
 * @author bpolster
 *
 */
public class UltraResourceResolverOrder {

    // Negative values should occur before any custom resolvers
    public static int THEME_FILE_URL_RESOLVER = -1000;
    public static int UC_JS_PATH_RESOLVER = -2000;

    // Implementors typically want dynamic URL before the cache resolver (e.g. UC_CACHE_RESOURCE_RESOLVER -1) 
    // and anything else after the version resolver (e.g. UC_VERSION_RESOURCE_RESOLVER + 1)
    public static int UC_CACHE_RESOURCE_RESOLVER = 1000;
    public static int UC_VERSION_RESOURCE_RESOLVER = 2000;

    // Custom resolvers (various lookup and file modification scenarios)
    public static int UC_BUNDLE_RESOURCE_RESOLVER = 10000;
    public static int UC_JS_RESOURCE_RESOLVER = 11000;
    public static int UC_SYSTEM_PROPERTY_RESOURCE_RESOLVER = 12000;
    public static int UC_THEME_FILE_RESOLVER = 13000;

    // Path Resolvers should always be last   
    public static int UC_PATH_RESOURCE_RESOLVER = Integer.MAX_VALUE;

}
