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

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Factory class that returns the appropriate RuleBuilderFieldService
 * given the service name. The services are injected into the factory defined in applicationContext-servlet-open-admin.xml
 * @see com.ultracommerce.openadmin.web.rulebuilder.service.RuleBuilderFieldService
 *
 * @author Elbert Bautista (elbertbautista)
 */
@Service("ucRuleBuilderFieldServiceFactory")
public class RuleBuilderFieldServiceFactoryImpl implements RuleBuilderFieldServiceFactory {

    @Resource(name="ucRuleBuilderFieldServices")
    protected List<RuleBuilderFieldService> fieldServices;

    @Override
    public RuleBuilderFieldService createInstance(String name) {

        for (RuleBuilderFieldService service : fieldServices) {
            if (service.getName().equals(name)){
                try {
                    return service.clone();
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return null;
    }

    @Override
    public List<RuleBuilderFieldService> getFieldServices() {
        return fieldServices;
    }

    @Override
    public void setFieldServices(List<RuleBuilderFieldService> fieldServices) {
        this.fieldServices = fieldServices;
    }
}
