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
package com.ultracommerce.common.extensibility.jpa.convert.inheritance;

import javax.persistence.DiscriminatorType;

/**
 * 
 * @author jfischer
 *
 */
public class SingleTableInheritanceInfo {

    protected String className;
    protected String discriminatorName;
    protected DiscriminatorType discriminatorType;
    protected int discriminatorLength;
    
    public String getClassName() {
        return className;
    }
    
    public void setClassName(String className) {
        this.className = className;
    }
    
    public String getDiscriminatorName() {
        return discriminatorName;
    }
    
    public void setDiscriminatorName(String discriminatorName) {
        this.discriminatorName = discriminatorName;
    }
    
    public DiscriminatorType getDiscriminatorType() {
        return discriminatorType;
    }
    
    public void setDiscriminatorType(DiscriminatorType discriminatorType) {
        this.discriminatorType = discriminatorType;
    }
    
    public int getDiscriminatorLength() {
        return discriminatorLength;
    }
    
    public void setDiscriminatorLength(int discriminatorLength) {
        this.discriminatorLength = discriminatorLength;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((className == null) ? 0 : className.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!getClass().isAssignableFrom(obj.getClass()))
            return false;
        SingleTableInheritanceInfo other = (SingleTableInheritanceInfo) obj;
        if (className == null) {
            if (other.className != null)
                return false;
        } else if (!className.equals(other.className))
            return false;
        return true;
    }
    
}
