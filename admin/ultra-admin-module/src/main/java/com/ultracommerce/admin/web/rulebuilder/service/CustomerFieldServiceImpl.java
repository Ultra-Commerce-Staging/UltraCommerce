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
 * to build the supported fields for a Customer entity
 *
 * @author Elbert Bautista (elbertbautista)
 */
@Service("ucCustomerFieldService")
public class CustomerFieldServiceImpl extends AbstractRuleBuilderFieldService {

    @Override
    public void init() {
        fields.add(new FieldData.Builder()
                .label("rule_customerLoggedIn")
                .name("loggedIn")
                .operators(RuleOperatorType.BOOLEAN)
                .options(RuleOptionType.EMPTY_COLLECTION)
                .type(SupportedFieldType.BOOLEAN)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_customerRegistered")
                .name("registered")
                .operators(RuleOperatorType.BOOLEAN)
                .options(RuleOptionType.EMPTY_COLLECTION)
                .type(SupportedFieldType.BOOLEAN)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_customerId")
                .name("id")
                .operators(RuleOperatorType.NUMERIC)
                .options(RuleOptionType.EMPTY_COLLECTION)
                .type(SupportedFieldType.ID)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_customerReceiveEmail")
                .name("receiveEmail")
                .operators(RuleOperatorType.BOOLEAN)
                .options(RuleOptionType.EMPTY_COLLECTION)
                .type(SupportedFieldType.BOOLEAN)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_customerUserName")
                .name("username")
                .operators(RuleOperatorType.TEXT)
                .options(RuleOptionType.EMPTY_COLLECTION)
                .type(SupportedFieldType.STRING)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_customerEmailAddress")
                .name("emailAddress")
                .operators(RuleOperatorType.TEXT)
                .options(RuleOptionType.EMPTY_COLLECTION)
                .type(SupportedFieldType.STRING)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_customerFirstName")
                .name("firstName")
                .operators(RuleOperatorType.TEXT)
                .options(RuleOptionType.EMPTY_COLLECTION)
                .type(SupportedFieldType.STRING)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_customerLastName")
                .name("lastName")
                .operators(RuleOperatorType.TEXT)
                .options(RuleOptionType.EMPTY_COLLECTION)
                .type(SupportedFieldType.STRING)
                .build());
    }

    @Override
    public String getName() {
        return RuleIdentifier.CUSTOMER;
    }

    @Override
    public String getDtoClassName() {
        return "com.ultracommerce.profile.core.domain.CustomerImpl";
    }

}
