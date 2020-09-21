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

import com.ultracommerce.profile.core.domain.CustomerPhone;
import com.ultracommerce.profile.core.domain.CustomerPhoneImpl;
import com.ultracommerce.profile.core.domain.Phone;
import com.ultracommerce.profile.core.domain.PhoneImpl;
import org.testng.annotations.DataProvider;

public class CustomerPhoneDataProvider {

    @DataProvider(name = "setupCustomerPhone")
    public static Object[][] createCustomerPhone() {
        CustomerPhone cp1 = new CustomerPhoneImpl();
        Phone phone1 = new PhoneImpl();
        phone1.setPhoneNumber("111-111-1111");
        cp1.setPhone(phone1);
        cp1.setPhoneName("phone1");

        CustomerPhone cp2 = new CustomerPhoneImpl();
        Phone phone2 = new PhoneImpl();
        phone1.setPhoneNumber("222-222-2222");
        cp2.setPhone(phone2);
        cp2.setPhoneName("phone2");

        return new Object[][] { new Object[] { cp1 }, new Object[] { cp2 } };
    }
}
