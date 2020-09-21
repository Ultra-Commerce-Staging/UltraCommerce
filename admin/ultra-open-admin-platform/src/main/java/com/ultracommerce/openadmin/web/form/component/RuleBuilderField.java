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
package com.ultracommerce.openadmin.web.form.component;

import com.ultracommerce.openadmin.web.form.entity.Field;
import com.ultracommerce.openadmin.web.rulebuilder.dto.DataWrapper;

/**
 * @author Elbert Bautista (elbertbautista)
 */
public class RuleBuilderField extends Field {

    protected String fieldBuilder;
    protected String ruleType;
    protected DataWrapper dataWrapper;
    protected String json;
    protected String jsonFieldName;

    public String getFieldBuilder() {
        return fieldBuilder;
    }

    public void setFieldBuilder(String fieldBuilder) {
        this.fieldBuilder = fieldBuilder;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public DataWrapper getDataWrapper() {
        return dataWrapper;
    }

    public void setDataWrapper(DataWrapper dataWrapper) {
        this.dataWrapper = dataWrapper;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getJsonFieldName() {
        return jsonFieldName;
    }

    public void setJsonFieldName(String jsonFieldName) {
        this.jsonFieldName = jsonFieldName;
    }
}
