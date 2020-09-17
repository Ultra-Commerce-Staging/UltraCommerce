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
package com.ultracommerce.common.breadcrumbs.service;

import com.ultracommerce.common.extension.ExtensionManager;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component("ucBreadcrumbServiceExtensionManager")
/**
 * Provides an extension point for building breadcrumbs.   Handlers participate in reverse priority order.
 * @author bpolster
 *
 */
public class BreadcrumbServiceExtensionManager extends ExtensionManager<BreadcrumbServiceExtensionHandler> {

    /**
     * As each handler runs, it can work with the ContextMap from the ExtensionResultHolder 
     * to get the URL as other handlers have modified it.
     * 
     * Given that handlers run in reverse priority order
     * 
     * Handlers may use the fullUrl passed into the method or work with these URLs.
     */
    public static String CONTEXT_PARAM_STRIPPED_URL = "STRIPPED_URL";
    public static String CONTEXT_PARAM_STRIPPED_PARAMS = "STRIPPED_PARAMS";

    public BreadcrumbServiceExtensionManager() {
        super(BreadcrumbServiceExtensionHandler.class);
    }

    /**
     * This extension manager works the handlers in reverse priority order.   It starts with the
     * last crumb and works its way back.
     */
    @Override
    protected void sortHandlers() {
        super.sortHandlers();
        Collections.reverse(getHandlers());
    }
}
