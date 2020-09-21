/*
 * #%L
 * UltraCommerce CMS Module
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
package com.ultracommerce.common.file.service;

import com.ultracommerce.common.extension.ExtensionHandler;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.common.site.domain.Site;
import org.springframework.ui.Model;

/**
 * 
 * @author Chris Kittrell (ckittrell)
 */
public interface UltraStaticAssetExtensionHandler extends ExtensionHandler {

    public ExtensionResultStatusType removeShareOptionsForMTStandardSite(Model model, Site currentSite);

    /**
     * Provide an extension point to modify the url for a StaticAsset in the case
     * where multiple assets have the same url.
     * @param urlBuilder
     * @return
     */
    public ExtensionResultStatusType modifyDuplicateAssetURL(StringBuilder urlBuilder);

}
