/*
 * #%L
 * UltraCommerce CMS Module
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
package com.ultracommerce.cms.file.service.operation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * @author Jeff Fischer
 */
public class StaticMapNamedOperationComponent implements NamedOperationComponent {

    @Override
    public List<String> setOperationValues(Map<String, String> originalParameters, Map<String, String> derivedParameters) {
        List<String> utilizedNames = new ArrayList<String>();
        expandFulfilledMap(originalParameters, derivedParameters, utilizedNames);

        return utilizedNames;
    }

    protected void expandFulfilledMap(Map<String, String> originalParameters, Map<String, String> derivedParameters, List<String> utilizedNames) {
        for (Map.Entry<String, String> entry : originalParameters.entrySet()) {
            if (namedOperations.containsKey(entry.getKey())) {
                expandFulfilledMap(namedOperations.get(entry.getKey()), derivedParameters, utilizedNames);
                if (!utilizedNames.contains(entry.getKey())) {
                    utilizedNames.add(entry.getKey());
                }
            } else {
                derivedParameters.put(entry.getKey(), entry.getValue());
            }
        }
    }

    @Resource(name="ucStaticMapNamedOperations")
    protected LinkedHashMap<String, LinkedHashMap<String, String>> namedOperations = new LinkedHashMap<String, LinkedHashMap<String, String>>();

    public LinkedHashMap<String, LinkedHashMap<String, String>> getNamedOperations() {
        return namedOperations;
    }

    public void setNamedOperations(LinkedHashMap<String, LinkedHashMap<String, String>> namedOperations) {
        this.namedOperations = namedOperations;
    }
}
