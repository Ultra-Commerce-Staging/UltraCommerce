/*
 * #%L
 * UltraCommerce Admin Module
 * %%
 * Copyright (C) 2009 - 2019 Ultra Commerce
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
package com.ultracommerce.admin.web.controller.extension;

import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.core.catalog.domain.Product;
import com.ultracommerce.openadmin.web.form.component.ListGrid;
import com.ultracommerce.openadmin.web.form.entity.Field;
import com.ultracommerce.openadmin.web.service.AbstractFormBuilderExtensionHandler;
import com.ultracommerce.openadmin.web.service.FormBuilderExtensionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Service("ucParentCategorySortExtensionHandler")
public class ParentCategorySortExtensionHandler extends AbstractFormBuilderExtensionHandler {


    @Value("${allow.product.parent.category.sorting:false}")
    protected boolean allowProductParentCategorySorting = false;

    @Resource(name = "ucFormBuilderExtensionManager")
    protected FormBuilderExtensionManager extensionManager;

    @PostConstruct
    public void init() {
        if (isEnabled()) {
            extensionManager.registerHandler(this);
        }
    }

    @Override
    public ExtensionResultStatusType modifyListGrid(String className, ListGrid listGrid) {
        if (Product.class.getName().equals(className) && !allowProductParentCategorySorting) {
            for (Field f : listGrid.getHeaderFields()) {
                if (f.getName().equals("defaultCategory")) {
                    f.setFilterSortDisabled(true);
                    return ExtensionResultStatusType.HANDLED;
                }
            }
        }
        return ExtensionResultStatusType.NOT_HANDLED;
    }
}
