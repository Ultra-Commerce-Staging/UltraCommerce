/*
 * #%L
 * UltraCommerce Framework
 * %%
 * Copyright (C) 2009 - 2018 Ultra Commerce
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
package com.ultracommerce.core.rule;

import com.ultracommerce.common.presentation.RuleOperatorType;
import com.ultracommerce.common.presentation.RuleOptionType;
import com.ultracommerce.common.presentation.client.SupportedFieldType;

/**
 * Provide confiration information about arbitrary field data used to drive rule builders in the admin. Can be employed
 * to create dynamic RuleBuilderFieldService implementations with field data being derived from external sources.
 *
 * @author Jeff Fischer
 */
public class RuleDTOConfig {

    /**
     * Name of the field as it appears in MVEL
     */
    protected String fieldName;
    /**
     * The name as it appears to the user in MVEL. Specify a i18n key here instead and declare the key/value pair in a message property file for translatable labels.
     */
    protected String label;
    /**
     * The type of operators for the rule
     */
    protected String operators = RuleOperatorType.TEXT_LIST;
    /**
     * The type of options to provide to the user for selection or entry
     */
    protected String options = RuleOptionType.EMPTY_COLLECTION;
    /**
     * The type of the value being entered by the user
     */
    protected SupportedFieldType type = SupportedFieldType.STRING;
    /**
     * An alternate name for the field. This is useful when the external source has a different name for the field than what you want to use in the rule builder.
     */
    protected String alternateName;

    public RuleDTOConfig(String fieldName, String label) {
        this.fieldName = fieldName;
        this.label = label;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getOperators() {
        return operators;
    }

    public void setOperators(String operators) {
        this.operators = operators;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public SupportedFieldType getType() {
        return type;
    }

    public void setType(SupportedFieldType type) {
        this.type = type;
    }

    public String getAlternateName() {
        return alternateName;
    }

    public void setAlternateName(String alternateName) {
        this.alternateName = alternateName;
    }
}
