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

import java.util.Currency;

public class NullUltraCurrency implements UltraCurrency {
    private static final long serialVersionUID = 7926395625817119455L;

    @Override
    public String getCurrencyCode() {
        return null;
    }

    @Override
    public void setCurrencyCode(String code) {
        // Do nothing
    }

    @Override
    public String getFriendlyName() {
        return null;
    }

    @Override
    public void setFriendlyName(String friendlyName) {
        // Do nothing
    }

    @Override
    public boolean getDefaultFlag() {
        return false;
    }

    @Override
    public void setDefaultFlag(boolean defaultFlag) {
        // Do nothing
    }

    @Override
    public Currency getJavaCurrency() {
        return null;
    }

}
