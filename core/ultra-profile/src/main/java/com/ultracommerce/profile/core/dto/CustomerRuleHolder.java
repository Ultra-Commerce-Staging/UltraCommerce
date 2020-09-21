/*
 * #%L
 * UltraCommerce Menu
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

package com.ultracommerce.profile.core.dto;

import java.io.Serializable;

/**
 * Simple container for Customer-related rules used to decouple Customers from Offers.
 *
 * @author Chris Kittrell (ckittrell)
 */
public class CustomerRuleHolder implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String customerRule;

    public CustomerRuleHolder() {}

    public CustomerRuleHolder(String customerRule) {
        this.customerRule = customerRule;
    }


    public String getCustomerRule() {
        return customerRule;
    }

    public void setCustomerRule(String customerRule) {
        this.customerRule = customerRule;
    }
}
