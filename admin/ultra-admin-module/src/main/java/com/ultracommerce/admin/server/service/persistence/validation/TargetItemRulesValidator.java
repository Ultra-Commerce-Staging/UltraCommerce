/*
 * #%L
 * UltraCommerce Admin Module
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
package com.ultracommerce.admin.server.service.persistence.validation;

import org.apache.commons.collections.CollectionUtils;
import com.ultracommerce.core.offer.domain.Offer;
import com.ultracommerce.core.offer.service.type.OfferType;
import com.ultracommerce.openadmin.dto.BasicFieldMetadata;
import com.ultracommerce.openadmin.dto.Entity;
import com.ultracommerce.openadmin.dto.FieldMetadata;
import com.ultracommerce.openadmin.server.service.persistence.validation.PropertyValidationResult;
import com.ultracommerce.openadmin.server.service.persistence.validation.PropertyValidator;
import com.ultracommerce.openadmin.server.service.persistence.validation.RequiredPropertyValidator;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;


/**
 * Validator that ensures that an offer of type {@link OfferType#ORDER_ITEM} has at least one rule for the target criteria
 *
 * @author Phillip Verheyden (phillipuniverse)
 */
@Component("ucTargetItemRulesValidator")
public class TargetItemRulesValidator implements PropertyValidator {

    @Override
    public PropertyValidationResult validate(Entity entity, Serializable instance, Map<String, FieldMetadata> entityFieldMetadata, Map<String, String> validationConfiguration, BasicFieldMetadata propertyMetadata, String propertyName, String value) {
        Offer offer = (Offer)instance;
        if (OfferType.ORDER_ITEM.equals(offer.getType())) {
            return new PropertyValidationResult(CollectionUtils.isNotEmpty(offer.getTargetItemCriteriaXref()),
                    RequiredPropertyValidator.ERROR_MESSAGE);
        } else {
            return new PropertyValidationResult(true);
        }
    }
}
