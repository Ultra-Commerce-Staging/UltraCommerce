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

import org.apache.commons.lang3.StringUtils;
import com.ultracommerce.common.web.UltraRequestContext;
import com.ultracommerce.openadmin.dto.BasicFieldMetadata;
import com.ultracommerce.openadmin.dto.Entity;
import com.ultracommerce.openadmin.dto.FieldMetadata;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;


/**
 * Ensures that field values submitted in the admin are less than or equal to the length specified in the metadata
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
@Component("ucFieldLengthValidator")
public class FieldLengthValidator implements GlobalPropertyValidator {

    @Override
    public PropertyValidationResult validate(Entity entity,
            Serializable instance,
            Map<String, FieldMetadata> entityFieldMetadata,
            BasicFieldMetadata propertyMetadata,
            String propertyName,
            String value) {
        boolean valid = true;
        String errorMessage = "";
        if (propertyMetadata.getLength() != null) {
            valid = StringUtils.length(value) <= propertyMetadata.getLength();
        }
        
        if (!valid) {
            UltraRequestContext context = UltraRequestContext.getUltraRequestContext();
            MessageSource messages = context.getMessageSource();
            errorMessage = messages.getMessage("fieldLengthValidationFailure",
                    new Object[] {propertyMetadata.getLength(), StringUtils.length(value) },
                    context.getJavaLocale());
        }
        
        return new PropertyValidationResult(valid, errorMessage);
    }

}
