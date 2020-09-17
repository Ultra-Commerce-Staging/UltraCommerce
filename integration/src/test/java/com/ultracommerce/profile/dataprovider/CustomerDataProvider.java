/*
 * #%L
 * UltraCommerce Integration
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
package com.ultracommerce.profile.dataprovider;

import com.ultracommerce.profile.core.domain.Customer;
import com.ultracommerce.profile.core.domain.CustomerImpl;
import org.testng.annotations.DataProvider;

public class CustomerDataProvider {

    @DataProvider(name = "setupCustomers")
    public static Object[][] createCustomers() {
        Customer customer1 = new CustomerImpl();
        customer1.setPassword("customer1Password");
        customer1.setUsername("customer1");

        Customer customer2 = new CustomerImpl();
        customer2.setPassword("customer2Password");
        customer2.setUsername("customer2");

        return new Object[][] { new Object[] { customer1 }, new Object[] { customer2 } };
    }
}
