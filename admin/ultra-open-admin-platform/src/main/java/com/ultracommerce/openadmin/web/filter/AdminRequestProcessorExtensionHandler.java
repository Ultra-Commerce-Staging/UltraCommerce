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
import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.common.site.domain.Catalog;
import com.ultracommerce.common.site.domain.Site;
import org.springframework.web.context.request.WebRequest;

import java.util.Set;

/**
 * @author Jeff Fischer
 */
public interface AdminRequestProcessorExtensionHandler extends ExtensionHandler {

    ExtensionResultStatusType retrieveProfiles(Site currentSite, ExtensionResultHolder<Set<Site>> result);

    ExtensionResultStatusType retrieveCatalogs(Site currentSite, ExtensionResultHolder<Set<Catalog>> result);

    ExtensionResultStatusType overrideCurrentCatalog(WebRequest request, Site currentSite, ExtensionResultHolder<Catalog> result);

    ExtensionResultStatusType overrideCurrentProfile(WebRequest request, Site currentSite, ExtensionResultHolder<Site> result);

}
