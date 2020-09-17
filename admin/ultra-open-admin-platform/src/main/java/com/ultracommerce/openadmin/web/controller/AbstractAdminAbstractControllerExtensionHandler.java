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
import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.openadmin.dto.ClassMetadata;
import com.ultracommerce.openadmin.dto.Entity;
import com.ultracommerce.openadmin.dto.MapMetadata;
import com.ultracommerce.openadmin.web.form.entity.EntityForm;
import com.ultracommerce.openadmin.web.form.entity.EntityFormAction;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Abstract implementatino of {@link AdminAbstractControllerExtensionHandler}.
 * 
 * Individual implementations of this extension handler should subclass this class as it will allow them to 
 * only override the methods that they need for their particular scenarios.
 * 
 * @author Andre Azzolini (apazzolini)
 */
public class AbstractAdminAbstractControllerExtensionHandler extends AbstractExtensionHandler implements AdminAbstractControllerExtensionHandler {

    @Override
    public ExtensionResultStatusType addAdditionalMainActions(String sectionClassName, List<EntityFormAction> actions) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType modifyMainActions(ClassMetadata cmd, List<EntityFormAction> mainActions) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType setAdditionalModelAttributes(Model model, String sectionKey) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType modifyDynamicForm(EntityForm form, String parentEntityId) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType modifyModelForAddCollectionType(HttpServletRequest request,
                                                                     HttpServletResponse response,
                                                                     Model model,
                                                                     String sectionKey,
                                                                     String id,
                                                                     MultiValueMap<String, String> requestParams, MapMetadata md) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType isAddRequest(Entity entity, ExtensionResultHolder<Boolean> resultHolder) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType overrideSaveEntityJsonResponse(HttpServletResponse response, boolean hasValidationErrors, String sectionKey, String id, ExtensionResultHolder<String> resultHolder) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType modifyPreAddEntityForm(EntityForm entityForm, ClassMetadata cmd, Map<String, String> pathVars) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }
}
