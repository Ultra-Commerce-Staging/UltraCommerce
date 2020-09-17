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
package com.ultracommerce.openadmin.web.service;

import com.ultracommerce.common.extension.AbstractExtensionHandler;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.openadmin.dto.Entity;
import com.ultracommerce.openadmin.web.form.component.ListGrid;
import com.ultracommerce.openadmin.web.form.component.ListGridRecord;
import com.ultracommerce.openadmin.web.form.entity.EntityForm;

/**
 * Abstract class to provide convenience for determining how to handle form 
 * extensions in the admin
 * 
 * @author Kelly Tisdell
 * @author Phillip Verheyden (phillipuniverse)
 */
public abstract class AbstractFormBuilderExtensionHandler extends AbstractExtensionHandler implements FormBuilderExtensionHandler {

    @Override
    public ExtensionResultStatusType modifyUnpopulatedEntityForm(EntityForm ef) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType modifyPopulatedEntityForm(EntityForm ef, Entity entity) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType modifyDetailEntityForm(EntityForm ef) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }
    
    @Override
    public ExtensionResultStatusType modifyListGridRecord(String className, ListGridRecord record, Entity entity) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }
    
    @Override
    public ExtensionResultStatusType addAdditionalFormActions(EntityForm entityForm) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType modifyListGrid(String className, ListGrid listGrid) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType addAdditionalAdornedFormActions(EntityForm entityForm) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

}
