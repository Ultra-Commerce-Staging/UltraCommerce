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
package com.ultracommerce.profile.web.core.controller.dataprovider;

import com.ultracommerce.profile.core.domain.Phone;
import com.ultracommerce.profile.core.domain.PhoneImpl;
import com.ultracommerce.profile.web.core.model.PhoneNameForm;
import org.testng.annotations.DataProvider;

public class CustomerPhoneControllerTestDataProvider {

    @DataProvider(name = "setupCustomerPhoneControllerData")
    public static Object[][] createCustomerPhone() {
        PhoneNameForm pnf1 = new PhoneNameForm();
        Phone phone1 = new PhoneImpl();
        phone1.setPhoneNumber("111-222-3333");
        pnf1.setPhone(phone1);
        pnf1.setPhoneName("phone_1");

        PhoneNameForm pnf2 = new PhoneNameForm();
        Phone phone2 = new PhoneImpl();
        phone2.setPhoneNumber("222-333-4444");
        pnf2.setPhone(phone2);
        pnf2.setPhoneName("phone_2");

        return new Object[][] { new Object[] { pnf1 }, new Object[] { pnf2 } };
    }
}
