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
package com.ultracommerce.admin.persistence.validation;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import com.ultracommerce.core.offer.service.type.OfferType;
import com.ultracommerce.openadmin.dto.BasicFieldMetadata;
import com.ultracommerce.openadmin.dto.Entity;
import com.ultracommerce.openadmin.dto.FieldMetadata;
import com.ultracommerce.openadmin.dto.Property;
import com.ultracommerce.openadmin.server.service.persistence.module.provider.RuleFieldExtractionUtility;
import com.ultracommerce.openadmin.server.service.persistence.validation.PropertyValidationResult;
import com.ultracommerce.openadmin.server.service.persistence.validation.ValidationConfigurationBasedPropertyValidator;
import com.ultracommerce.openadmin.web.rulebuilder.dto.DataWrapper;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.Resource;

/**
 * Checks to make sure that the TargetItemCriteria is not empty if required
 * 
 * @author Jaci Eckert
 */
@Component("ucOfferTargetCriteriaItemValidator")
public class OfferTargetItemCriteriaValidator extends ValidationConfigurationBasedPropertyValidator {

    @Resource(name = "ucRuleFieldExtractionUtility")
    protected RuleFieldExtractionUtility ruleFieldExtractionUtility;


    @Override
    public PropertyValidationResult validate(Entity entity,
            Serializable instance,
            Map<String, FieldMetadata> entityFieldMetadata,
            Map<String, String> validationConfiguration,
            BasicFieldMetadata propertyMetadata,
            String propertyName,
            String value) {

        Property offerTypeProperty = entity.findProperty("type");
        Property useListForDiscountsProperty = entity.findProperty("useListForDiscounts");
        if(OfferType.ORDER_ITEM.getType().equals(offerTypeProperty.getValue()) && BooleanUtils.isNotTrue(Boolean.parseBoolean(useListForDiscountsProperty.getValue()))) {
            String targetItemCriteriaJson = entity.findProperty("targetItemCriteria").getUnHtmlEncodedValue();
            if (targetItemCriteriaJson == null) {
                targetItemCriteriaJson = entity.findProperty("targetItemCriteriaJson").getUnHtmlEncodedValue();
            }
            DataWrapper dw = ruleFieldExtractionUtility.convertJsonToDataWrapper(targetItemCriteriaJson);

            if (CollectionUtils.isEmpty(dw.getData()) && dw.getRawMvel() == null) {
                return new PropertyValidationResult(false, "requiredValidationFailure");
            }
        }

        return new PropertyValidationResult(true);
    }
}
