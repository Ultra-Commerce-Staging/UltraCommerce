/*
 * #%L
 * UltraCommerce Open Admin Platform
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
package com.ultracommerce.openadmin.web.filter;

import com.ultracommerce.common.extension.ExtensionHandler;
import com.ultracommerce.common.extension.ExtensionManager;
import com.ultracommerce.common.extension.ExtensionManagerOperation;
import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.common.site.domain.Catalog;
import com.ultracommerce.common.site.domain.Site;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Set;

/**
 * @author Jeff Fischer
 */
@Component("ucAdminRequestProcessorExtensionManager")
public class AdminRequestProcessorExtensionManager extends ExtensionManager<AdminRequestProcessorExtensionHandler> implements AdminRequestProcessorExtensionHandler {

    public static final ExtensionManagerOperation retrieveProfiles = new ExtensionManagerOperation() {
        @Override
        public ExtensionResultStatusType execute(ExtensionHandler handler, Object... params) {
            return ((AdminRequestProcessorExtensionHandler) handler).retrieveProfiles((Site) params[0], (ExtensionResultHolder<Set<Site>>) params[1]);
        }
    };

    public static final ExtensionManagerOperation retrieveCatalogs = new ExtensionManagerOperation() {
        @Override
        public ExtensionResultStatusType execute(ExtensionHandler handler, Object... params) {
            return ((AdminRequestProcessorExtensionHandler) handler).retrieveCatalogs((Site) params[0], (ExtensionResultHolder<Set<Catalog>>) params[1]);
        }
    };

    public static final ExtensionManagerOperation overrideCurrentCatalog = new ExtensionManagerOperation() {
        @Override
        public ExtensionResultStatusType execute(ExtensionHandler handler, Object... params) {
            return ((AdminRequestProcessorExtensionHandler) handler).overrideCurrentCatalog((WebRequest) params[0], (Site) params[1], (ExtensionResultHolder<Catalog>) params[2]);
        }
    };

    public static final ExtensionManagerOperation overrideCurrentProfile = new ExtensionManagerOperation() {
        @Override
        public ExtensionResultStatusType execute(ExtensionHandler handler, Object... params) {
            return ((AdminRequestProcessorExtensionHandler) handler).overrideCurrentProfile((WebRequest) params[0], (Site) params[1], (ExtensionResultHolder<Site>) params[2]);
        }
    };

    public AdminRequestProcessorExtensionManager() {
        super(AdminRequestProcessorExtensionHandler.class);
    }

    @Override
    public ExtensionResultStatusType retrieveProfiles(Site currentSite, ExtensionResultHolder<Set<Site>> result) {
        return execute(retrieveProfiles, currentSite, result);
    }

    @Override
    public ExtensionResultStatusType retrieveCatalogs(Site currentSite, ExtensionResultHolder<Set<Catalog>> result) {
        return execute(retrieveCatalogs, currentSite, result);
    }

    @Override
    public ExtensionResultStatusType overrideCurrentCatalog(WebRequest request, Site currentSite,
                                                            ExtensionResultHolder<Catalog> result) {
        return execute(overrideCurrentCatalog, request, currentSite, result);
    }

    @Override
    public ExtensionResultStatusType overrideCurrentProfile(WebRequest request, Site currentSite,
                                                            ExtensionResultHolder<Site> result) {
        return execute(overrideCurrentProfile, request, currentSite, result);
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
