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
package com.ultracommerce.openadmin.web.controller;

import com.ultracommerce.common.extension.AbstractExtensionHandler;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.openadmin.dto.ClassMetadata;
import com.ultracommerce.openadmin.dto.SectionCrumb;
import com.ultracommerce.openadmin.server.domain.PersistencePackageRequest;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;

import java.util.List;


/**
 * Abstract implementation of {@link AdminBasicOperationsControllerExtensionHandler}.
 * 
 * Individual implementations of this extension handler should subclass this class as it will allow them to 
 * only override the methods that they need for their particular scenarios.
 * 
 * @author ckittrell
 */
public class AbstractAdminBasicOperationsControllerExtensionHandler extends AbstractExtensionHandler implements AdminBasicOperationsControllerExtensionHandler {

    @Override
    public ExtensionResultStatusType buildLookupListGrid(PersistencePackageRequest ppr, ClassMetadata cmd, String owningClass,
            List<SectionCrumb> sectionCrumbs, Model model, MultiValueMap<String, String> requestParams) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

}
