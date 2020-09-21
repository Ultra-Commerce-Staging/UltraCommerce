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
package com.ultracommerce.common.web.resource;

import com.ultracommerce.common.extension.ExtensionHandler;
import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import org.springframework.core.io.Resource;

/**
 * Provides extension points for dealing with requests for resources
 * 
 * @author bpolster, apazzolini
 */
public interface ResourceRequestExtensionHandler extends ExtensionHandler {
    
    public static final String RESOURCE_ATTR = "RESOURCE_ATTR";
    
    /**
     * Populates the RESOURCE_ATTR field in the ExtensionResultHolder map with an instance of
     * {@link Resource} if the value of the modified resource.
     * 
     * @param path
     * @param erh
     * @return the {@link ExtensionResultStatusType}
     */
    public ExtensionResultStatusType getModifiedResource(String path, ExtensionResultHolder erh);

    /**
     * Populates the RESOURCE_ATTR field in the ExtensionResultHolder map with an instance of
     * {@link Resource} if there is an override resource available for the current path.
     * 
     * @param path
     * @param erh
     * @return the {@link ExtensionResultStatusType}
     */
    public ExtensionResultStatusType getOverrideResource(String path, ExtensionResultHolder erh);

}
