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

import com.ultracommerce.profile.core.domain.Address;
import com.ultracommerce.profile.core.domain.AddressImpl;
import com.ultracommerce.profile.core.domain.State;
import com.ultracommerce.profile.core.domain.StateImpl;
import org.testng.annotations.DataProvider;

public class AddressDataProvider {

    @DataProvider(name = "setupAddress")
    public static Object[][] createAddress() {
        Address address1 = new AddressImpl();
        address1.setAddressLine1("1234 Merit Drive");
        address1.setCity("Dallas");
        State state = new StateImpl();
        state.setAbbreviation("TX");
        address1.setState(state);
        address1.setPostalCode("75251");

        Address address2 = new AddressImpl();
        address2.setAddressLine1("12 Testing Drive");
        address2.setCity("San Jose");
        state = new StateImpl();
        state.setAbbreviation("CA");
        address2.setState(state);
        address2.setPostalCode("75251");

        return new Object[][] { new Object[] { address1 }, new Object[] { address2 } };
    }
}
