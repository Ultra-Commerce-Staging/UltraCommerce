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
package com.ultracommerce.admin.web.rulebuilder.service;

import com.ultracommerce.common.presentation.RuleIdentifier;
import com.ultracommerce.common.presentation.RuleOperatorType;
import com.ultracommerce.common.presentation.RuleOptionType;
import com.ultracommerce.common.presentation.client.SupportedFieldType;
import com.ultracommerce.openadmin.web.rulebuilder.dto.FieldData;
import com.ultracommerce.openadmin.web.rulebuilder.service.AbstractRuleBuilderFieldService;
import org.springframework.stereotype.Service;

/**
 * An implementation of a RuleBuilderFieldService
 * that constructs metadata necessary
 * to build the supported fields for a Fulfillment Group entity
 *
 * @author Elbert Bautista (elbertbautista)
 */
@Service("ucFulfillmentGroupFieldService")
public class FulfillmentGroupFieldServiceImpl  extends AbstractRuleBuilderFieldService {

    @Override
    public void init() {
        fields.add(new FieldData.Builder()
                .label("rule_fulfillmentGroupFirstName")
                .name("address.firstName")
                .operators(RuleOperatorType.TEXT)
                .options(RuleOptionType.EMPTY_COLLECTION)
                .type(SupportedFieldType.STRING)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_fulfillmentGroupLastName")
                .name("address.lastName")
                .operators(RuleOperatorType.TEXT)
                .options(RuleOptionType.EMPTY_COLLECTION)
                .type(SupportedFieldType.STRING)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_fulfillmentGroupAddresLine1")
                .name("address.addressLine1")
                .operators(RuleOperatorType.TEXT)
                .options(RuleOptionType.EMPTY_COLLECTION)
                .type(SupportedFieldType.STRING)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_fulfillmentGroupAddressLine2")
                .name("address.addressLine2")
                .operators(RuleOperatorType.TEXT)
                .options(RuleOptionType.EMPTY_COLLECTION)
                .type(SupportedFieldType.STRING)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_fulfillmentGroupCity")
                .name("address.city")
                .operators(RuleOperatorType.TEXT)
                .options(RuleOptionType.EMPTY_COLLECTION)
                .type(SupportedFieldType.STRING)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_fulfillmentGroupCounty")
                .name("address.county")
                .operators(RuleOperatorType.TEXT)
                .options(RuleOptionType.EMPTY_COLLECTION)
                .type(SupportedFieldType.STRING)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_fulfillmentGroupState")
                .name("address.state.name")
                .operators(RuleOperatorType.TEXT)
                .options(RuleOptionType.EMPTY_COLLECTION)
                .type(SupportedFieldType.STRING)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_fulfillmentGroupPostalCode")
                .name("address.postalCode")
                .operators(RuleOperatorType.TEXT)
                .options(RuleOptionType.EMPTY_COLLECTION)
                .type(SupportedFieldType.STRING)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_fulfillmentGroupCountry")
                .name("address.country.name")
                .operators(RuleOperatorType.TEXT)
                .options(RuleOptionType.EMPTY_COLLECTION)
                .type(SupportedFieldType.STRING)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_fulfillmentGroupPrimaryPhone")
                .name("address.phonePrimary.phoneNumber")
                .operators(RuleOperatorType.TEXT)
                .options(RuleOptionType.EMPTY_COLLECTION)
                .type(SupportedFieldType.STRING)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_fulfillmentGroupSecondaryPhone")
                .name("address.phoneSecondary.phoneNumber")
                .operators(RuleOperatorType.TEXT)
                .options(RuleOptionType.EMPTY_COLLECTION)
                .type(SupportedFieldType.STRING)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_fulfillmentGroupFax")
                .name("address.phoneFax.phoneNumber")
                .operators(RuleOperatorType.TEXT)
                .options(RuleOptionType.EMPTY_COLLECTION)
                .type(SupportedFieldType.STRING)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_fulfillmentGroupTotal")
                .name("total")
                .operators(RuleOperatorType.NUMERIC)
                .options(RuleOptionType.EMPTY_COLLECTION)
                .type(SupportedFieldType.MONEY)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_fulfillmentGroupPrice")
                .name("fulfillmentPrice")
                .operators(RuleOperatorType.NUMERIC)
                .options(RuleOptionType.EMPTY_COLLECTION)
                .type(SupportedFieldType.MONEY)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_fulfillmentGroupRetailPrice")
                .name("retailFulfillmentPrice")
                .operators(RuleOperatorType.NUMERIC)
                .options(RuleOptionType.EMPTY_COLLECTION)
                .type(SupportedFieldType.MONEY)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_fulfillmentGroupSalePrice")
                .name("saleFulfillmentPrice")
                .operators(RuleOperatorType.NUMERIC)
                .options(RuleOptionType.EMPTY_COLLECTION)
                .type(SupportedFieldType.MONEY)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_fulfillmentGroupType")
                .name("type")
                .operators(RuleOperatorType.ENUMERATION)
                .options(RuleOptionType.FULFILLMENT_TYPE)
                .type(SupportedFieldType.ULTRA_ENUMERATION)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_fulfillmentGroupMerchandiseTotal")
                .name("merchandiseTotal")
                .operators(RuleOperatorType.NUMERIC)
                .options(RuleOptionType.EMPTY_COLLECTION)
                .type(SupportedFieldType.MONEY)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_fulfillmentGroupFulfillmentOption")
                .name("fulfillmentOption.name")
                .operators(RuleOperatorType.TEXT)
                .options(RuleOptionType.EMPTY_COLLECTION)
                .type(SupportedFieldType.STRING)
                .build());
    }

    @Override
    public String getName() {
        return RuleIdentifier.FULFILLMENTGROUP;
    }

    @Override
    public String getDtoClassName() {
        return "com.ultracommerce.core.order.domain.FulfillmentGroupImpl";
    }
}
