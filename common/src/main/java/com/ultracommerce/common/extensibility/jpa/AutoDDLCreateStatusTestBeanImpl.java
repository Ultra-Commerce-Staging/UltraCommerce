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
package com.ultracommerce.common.extensibility.jpa;

import java.util.HashMap;
import java.util.Map;

/**
 * @see com.ultracommerce.common.extensibility.jpa.AutoDDLCreateStatusTestBean
 * @author Jeff Fischer
 */
public class AutoDDLCreateStatusTestBeanImpl implements AutoDDLCreateStatusTestBean {

    protected Map<String, Boolean> startedWithCreate = new HashMap<String, Boolean>();

    public Boolean getStartedWithCreate(String pu) {
        return startedWithCreate.get(pu);
    }

    public void setStartedWithCreate(String pu, Boolean val) {
        this.startedWithCreate.put(pu, val);
    }

}
