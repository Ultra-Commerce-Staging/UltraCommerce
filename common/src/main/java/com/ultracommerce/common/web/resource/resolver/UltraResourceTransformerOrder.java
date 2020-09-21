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
 * Constants representing out of box Ultra Resource Transformer ordering.
 * 
 * Used by {@link UltraResourceHttpRequestHandler} which sorts resolvers that 
 * implement {@link Ordered} in its {@link PostConstruct} method.
 * 
 * @author bpolster
 *
 */
public class UltraResourceTransformerOrder {

    public static int UC_CACHE_RESOURCE_TRANSFORMER = 1000;
    public static int UC_MINIFY_RESOURCE_TRANSFORMER = 10000;
}
