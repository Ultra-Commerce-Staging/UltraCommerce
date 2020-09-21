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
package com.ultracommerce.openadmin.server.security.service.navigation;

import com.ultracommerce.common.extension.ExtensionHandler;
import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.openadmin.server.security.domain.AdminSection;
import com.ultracommerce.openadmin.web.controller.AdminAbstractController;

/**
 * Extension handler for methods present in {@link AdminNavigationService}.
 * 
 * @author Jeff Fischer
 */
public interface AdminNavigationServiceExtensionHandler extends ExtensionHandler {

    public static final String NEW_CLASS_NAME = "newClassName";

    /**
     * Invoked whenever {@link AdminAbstractController#getClassNameForSection(String)} is invoked. If an extension
     * handler sets the {@link #NEW_CLASS_NAME} variable in the ExtensionResultHolder, the overriden value will be used.
     * 
     * @param erh
     * @param sectionKey
     * @param section
     * @return
     */
    public ExtensionResultStatusType overrideClassNameForSection(ExtensionResultHolder erh, String sectionKey,
                                                                 AdminSection section);

}
