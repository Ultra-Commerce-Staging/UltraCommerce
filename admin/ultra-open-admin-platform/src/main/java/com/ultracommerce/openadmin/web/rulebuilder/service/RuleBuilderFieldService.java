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
package com.ultracommerce.openadmin.web.rulebuilder.service;

import com.ultracommerce.common.presentation.client.SupportedFieldType;
import com.ultracommerce.openadmin.web.rulebuilder.dto.FieldDTO;
import com.ultracommerce.openadmin.web.rulebuilder.dto.FieldData;
import com.ultracommerce.openadmin.web.rulebuilder.dto.FieldWrapper;

import java.util.List;

/**
 * @author Elbert Bautista (elbertbautista)
 */
public interface RuleBuilderFieldService extends Cloneable {

    public String getName();

    public FieldWrapper buildFields();

    public FieldDTO getField(String fieldName);

    public SupportedFieldType getSupportedFieldType(String fieldName);

    public SupportedFieldType getSecondaryFieldType(String fieldName);

    public List<FieldData> getFields();

    public void setFields(List<FieldData> fields);

    public String getOverrideFieldEntityKey(String fieldName);

    public RuleBuilderFieldService clone() throws CloneNotSupportedException;

    public void setRuleBuilderFieldServiceExtensionManager(RuleBuilderFieldServiceExtensionManager extensionManager);
}
