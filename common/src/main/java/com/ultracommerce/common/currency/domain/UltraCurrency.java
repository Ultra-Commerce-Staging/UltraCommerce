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
package com.ultracommerce.common.currency.domain;

import java.io.Serializable;

/**
 * Author: jerryocanas
 * Date: 9/6/12
 */
public interface UltraCurrency extends Serializable {

    public String getCurrencyCode();
    
    public java.util.Currency getJavaCurrency();

    public void setCurrencyCode(String code);

    public String getFriendlyName();

    public void setFriendlyName(String friendlyName);

    public boolean getDefaultFlag();

    public void setDefaultFlag(boolean defaultFlag);
}
