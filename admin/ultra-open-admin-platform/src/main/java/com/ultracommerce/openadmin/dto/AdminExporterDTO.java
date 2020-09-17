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

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author Phillip Verheyden
 */
public class AdminExporterDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String name;
    protected String friendlyName;
    protected List<Property> additionalCriteriaProperties;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getFriendlyName() {
        return friendlyName;
    }
    
    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }
    
    public List<Property> getAdditionalCriteriaProperties() {
        return additionalCriteriaProperties;
    }
    
    public void setAdditionalCriteriaProperties(List<Property> additionalCriteriaProperties) {
        this.additionalCriteriaProperties = additionalCriteriaProperties;
    }
    
    
}
