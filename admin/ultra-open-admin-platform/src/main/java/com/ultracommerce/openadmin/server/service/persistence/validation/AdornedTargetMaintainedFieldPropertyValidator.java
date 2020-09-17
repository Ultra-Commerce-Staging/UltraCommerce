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
package com.ultracommerce.openadmin.server.service.persistence.validation;

import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.openadmin.dto.BasicFieldMetadata;
import com.ultracommerce.openadmin.dto.Entity;
import com.ultracommerce.openadmin.dto.FieldMetadata;
import com.ultracommerce.openadmin.server.service.persistence.extension.AdornedTargetAutoPopulateExtensionManager;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.Resource;


/**
 * A basic entity persistence validation hook into validation provided by instances of
 * {@link com.ultracommerce.openadmin.server.service.persistence.extension.AdornedTargetAutoPopulateExtensionHandler}
 *
 * @author Jeff Fischer
 */
@Component("ucAdornedTargetMaintainedFieldPropertyValidator")
public class AdornedTargetMaintainedFieldPropertyValidator implements GlobalPropertyValidator {

    public static String ERROR_MESSAGE = "adornedTargetMaintainedFieldValidationFailure";

    @Resource(name = "ucAdornedTargetAutoPopulateExtensionManager")
    protected AdornedTargetAutoPopulateExtensionManager adornedTargetAutoPopulateExtensionManager;
    
    @Override
    public PropertyValidationResult validate(Entity entity,
                            Serializable instance,
                            Map<String, FieldMetadata> entityFieldMetadata,
                            BasicFieldMetadata propertyMetadata,
                            String propertyName,
                            String value) {
        ExtensionResultHolder<Boolean> validationResult = new ExtensionResultHolder<Boolean>();
        ExtensionResultStatusType status = adornedTargetAutoPopulateExtensionManager.getProxy().validateSubmittedAdornedTargetManagedFields(entity, instance,
                entityFieldMetadata, propertyMetadata, propertyName, value, validationResult);
        Boolean valid = true;
        if (ExtensionResultStatusType.NOT_HANDLED != status && validationResult.getResult() != null) {
            valid = validationResult.getResult();
        }
        return new PropertyValidationResult(valid, ERROR_MESSAGE);
    }

}
