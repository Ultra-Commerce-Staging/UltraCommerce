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
package com.ultracommerce.openadmin.server.service.extension;

import com.ultracommerce.common.extension.AbstractExtensionHandler;
import com.ultracommerce.common.extension.ExtensionResultStatusType;

import java.util.ArrayList;


/**
 * Abstract implementation of {@link AdminSectionCustomCriteriaExtensionHandler}.
 * 
 * Individual implementations of this extension handler should subclass this class as it will allow them to 
 * only override the methods that they need for their particular scenarios.
 * 
 * @author ckittrell
 */
public class AbstractAdminSectionCustomCriteriaExtensionHandler extends AbstractExtensionHandler implements AdminSectionCustomCriteriaExtensionHandler {

    @Override
    public ExtensionResultStatusType addAdditionalSectionCustomCriteria(ArrayList<String> customCriteria, String sectionClassName) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }
}
