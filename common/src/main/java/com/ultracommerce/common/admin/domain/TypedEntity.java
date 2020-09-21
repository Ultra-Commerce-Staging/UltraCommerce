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
package com.ultracommerce.common.admin.domain;

import com.ultracommerce.common.UltraEnumerationType;

/**
 * @author Jon Fleschler (jfleschler)
 */
public interface TypedEntity {

    /**
     * Returns the type of the Entity
     * @return type
     */
    public UltraEnumerationType getType();

    /**
     * Sets the type of the Entity
     * @param type
     */
    public void setType(UltraEnumerationType type);

    /**
     * Returns the persisted type field name
     * @return fieldName
     */
    public String getTypeFieldName();

    /**
     * Returns the default type to be used for this entity
     * @return defaultType
     */
    public String getDefaultType();
}
