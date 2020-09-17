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
package com.ultracommerce.common.site.service;

import com.ultracommerce.common.extension.ExtensionHandler;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.common.site.domain.Site;
import com.ultracommerce.common.site.domain.SiteImpl;

/**
 * <p>
 * ExtensionHandler for methods within {@link SiteServiceImpl}
 * 
 * <p>
 * Rather than implementing this interface directly you should extend your implementation from
 * {@link AbstractSiteServiceExtensionHandler}.
 * 
 * @author Phillip Verheyden (phillipuniverse)
 * @see {@link AbstractSiteServiceExtensionHandler}
 */
public interface SiteServiceExtensionHandler extends ExtensionHandler {

    /**
     * Invoked via {@link SiteServiceImpl#getNonPersistentSite(Site)} after the initial framework clone. If more properties
     * are dynamically weaved into {@link SiteImpl} then they should be cloned here.
     * 
     * @param from the {@link Site} being copied from, usually just looked up from the database
     * @param to the 
     * @see {@link SiteServiceImpl#getNonPersistentSite(Site)}
     */
    public ExtensionResultStatusType contributeNonPersitentSiteProperties(Site from, Site to);

}
