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
package com.ultracommerce.common.filter;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration bean that represents a Hibernate FilterDefinition
 *
 * @author Jeff Fischer
 */
public class FilterDefinition {

    protected String name;
    protected List<FilterParameter> params = new ArrayList<FilterParameter>();
    protected String entityImplementationClassName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FilterParameter> getParams() {
        return params;
    }

    public void setParams(List<FilterParameter> params) {
        this.params = params;
    }

    public String getEntityImplementationClassName() {
        return entityImplementationClassName;
    }

    public void setEntityImplementationClassName(String entityImplementationClassName) {
        this.entityImplementationClassName = entityImplementationClassName;
    }

    public FilterDefinition copy() {
        FilterDefinition copy = new FilterDefinition();
        copy.setName(name);
        if (!CollectionUtils.isEmpty(params)) {
            copy.getParams().addAll(params);
        }
        copy.setEntityImplementationClassName(entityImplementationClassName);
        return copy;
    }
}
