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
 * to build the supported fields for an Order Item entity
 *
 * @author Elbert Bautista (elbertbautista)
 */
@Service("ucOrderItemFieldService")
public class OrderItemFieldServiceImpl extends AbstractRuleBuilderFieldService {

    //TODO: extensibility mechanism, support i18N
    @Override
    public void init() {
        fields.add(new FieldData.Builder()
                .label("rule_orderItemName")
                .name("name")
                .operators(RuleOperatorType.TEXT)
                .options(RuleOptionType.EMPTY_COLLECTION)
                .type(SupportedFieldType.STRING)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_orderItemPrice")
                .name("price")
                .operators(RuleOperatorType.NUMERIC)
                .options(RuleOptionType.EMPTY_COLLECTION)
                .type(SupportedFieldType.MONEY)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_orderItemQuantity")
                .name("quantity")
                .operators(RuleOperatorType.NUMERIC)
                .options(RuleOptionType.EMPTY_COLLECTION)
                .type(SupportedFieldType.INTEGER)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_orderItemCategoryName")
                .name("category.name")
                .operators(RuleOperatorType.TEXT)
                .options(RuleOptionType.EMPTY_COLLECTION)
                .type(SupportedFieldType.STRING)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_orderItemCategoryUrl")
                .name("category.url")
                .operators(RuleOperatorType.TEXT)
                .options(RuleOptionType.EMPTY_COLLECTION)
                .type(SupportedFieldType.STRING)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_orderItemProductManufacturer")
                .name("product.manufacturer")
                .operators(RuleOperatorType.TEXT)
                .options(RuleOptionType.EMPTY_COLLECTION)
                .type(SupportedFieldType.STRING)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_orderItemSkuLongDescription")
                .name("sku.longDescription")
                .operators(RuleOperatorType.TEXT)
                .options(RuleOptionType.EMPTY_COLLECTION)
                .type(SupportedFieldType.STRING)
                .build());
    }

    @Override
    public String getName() {
        return RuleIdentifier.ORDERITEM;
    }

    @Override
    public String getDtoClassName() {
        return "com.ultracommerce.core.order.domain.OrderItemImpl";
    }
}
