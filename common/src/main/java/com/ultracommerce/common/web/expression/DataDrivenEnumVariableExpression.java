/*
 * #%L
 * UltraCommerce Common Libraries
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
/**
 * 
 */
package com.ultracommerce.common.web.expression;

import org.apache.commons.lang3.StringUtils;
import com.ultracommerce.common.enumeration.domain.DataDrivenEnumeration;
import com.ultracommerce.common.enumeration.domain.DataDrivenEnumerationValue;
import com.ultracommerce.common.enumeration.service.DataDrivenEnumerationService;
import com.ultracommerce.presentation.condition.ConditionalOnTemplating;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

/**
 * Variable expression that looks up a list of {@link DataDrivenEnumerationValue}s based on its enum key
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
@Component("ucDataDrivenEnumVariableExpression")
@ConditionalOnTemplating
public class DataDrivenEnumVariableExpression implements UltraVariableExpression {

    @Resource(name = "ucDataDrivenEnumerationService")
    protected DataDrivenEnumerationService enumService;
    
    @Override
    public String getName() {
        return "enumeration";
    }
    
    /**
     * Looks up a list of {@link DataDrivenEnumerationValue} by the {@link DataDrivenEnumeration#getKey()} specified by <b>key</b>
     * @param key lookup for the {@link DataDrivenEnumeration}
     * @return the list of {@link DataDrivenEnumerationValue} for the given <b>key</b>
     */
    public List<DataDrivenEnumerationValue> getEnumValues(String key) {
        return getEnumValues(key, null);
    }
    
    /**
     * Looks up a list of {@link DataDrivenEnumerationValue} by the {@link DataDrivenEnumeration#getKey()} specified by <b>key</b>
     * @param key lookup for the {@link DataDrivenEnumeration}
     * @param sort optional, either 'ASCENDING' or 'DESCENDING' depending on how you want the result list sorted
     * @return the list of {@link DataDrivenEnumerationValue} for the given <b>key</b>
     */
    public List<DataDrivenEnumerationValue> getEnumValues(String key, final String sort) {
        if (StringUtils.isEmpty(key)) {
            throw new IllegalArgumentException("No 'key' parameter was passed to find enumeration values");
        }
        
        DataDrivenEnumeration ddEnum = enumService.findEnumByKey(key);
        if (ddEnum == null) {
            throw new IllegalArgumentException("Could not find a data driven enumeration keyed by " + key);
        }
        List<DataDrivenEnumerationValue> enumValues = new ArrayList<>(ddEnum.getEnumValues());
        
        if (StringUtils.isNotEmpty(sort)) {
            Collections.sort(enumValues, new Comparator<DataDrivenEnumerationValue>() {

                @Override
                public int compare(DataDrivenEnumerationValue arg0, DataDrivenEnumerationValue arg1) {
                    if (sort.equals("ASCENDING")) {
                        return arg0.getDisplay().compareTo(arg1.getDisplay());
                    } else {
                        return arg1.getDisplay().compareTo(arg0.getDisplay());
                    }
                }
            });
        }
        return enumValues;
    }

}
