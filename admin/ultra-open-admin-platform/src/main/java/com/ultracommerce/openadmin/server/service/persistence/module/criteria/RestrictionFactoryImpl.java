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
package com.ultracommerce.openadmin.server.service.persistence.module.criteria;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Jeff Fischer
 */
@Service("ucRestrictionFactory")
public class RestrictionFactoryImpl implements RestrictionFactory {

    @Resource(name="ucRestrictionFactoryMap")
    protected Map<String, Restriction> restrictions = new LinkedHashMap<String, Restriction>();

    public Map<String, Restriction> getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(Map<String, Restriction> restrictions) {
        this.restrictions = restrictions;
    }

    @Override
    public Restriction getRestriction(String type, String propertyId) {
        Restriction restriction = restrictions.get(type).clone();
        restriction.setFieldPathBuilder(new FieldPathBuilder());

        return restriction;
    }
}
