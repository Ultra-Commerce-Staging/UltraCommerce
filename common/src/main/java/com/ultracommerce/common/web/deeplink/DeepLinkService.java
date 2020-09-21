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
package com.ultracommerce.common.web.deeplink;


import com.ultracommerce.common.web.BaseUrlResolver;

import java.util.List;

import javax.annotation.Resource;

/**
 * This abstract class should be extended by services that provide deep links for specific entities.
 * 
 * @author Andre Azzolini (apazzolini)
 */
public abstract class DeepLinkService<T> {
    
    @Resource(name = "ucBaseUrlResolver")
    protected BaseUrlResolver baseUrlResolver;
    
    /**
     * Returns a list of {@link DeepLink} objects that represent the location of 1 or more admin elements
     * 
     * @param item
     * @return the list of DeepLinks
     */
    public final List<DeepLink> getLinks(T item) {
        return getLinksInternal(item);
    }
    
    protected String getAdminBaseUrl() {
        return baseUrlResolver.getAdminBaseUrl();
    }

    protected abstract List<DeepLink> getLinksInternal(T item);

}
