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
package com.ultracommerce.openadmin.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * @author Jeff Fischer
 */
public class BatchDynamicResultSet implements Serializable {

    private static final long serialVersionUID = 1L;

    protected DynamicResultSet[] dynamicResultSets;

    public DynamicResultSet[] getDynamicResultSets() {
        return dynamicResultSets;
    }

    public void setDynamicResultSets(DynamicResultSet[] dynamicResultSets) {
        this.dynamicResultSets = dynamicResultSets;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == this) {
            return true;
        }
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        }
        BatchDynamicResultSet rhs = (BatchDynamicResultSet) obj;
        return new EqualsBuilder()
                .append(this.dynamicResultSets, rhs.dynamicResultSets)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(dynamicResultSets)
                .toHashCode();
    }
}
