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
package com.ultracommerce.profile.web.core.service;

import com.ultracommerce.profile.core.domain.Country;
import com.ultracommerce.profile.core.domain.State;
import com.ultracommerce.test.CommonSetupBaseTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class AddressTest extends CommonSetupBaseTest {

    List<Long> addressIds = new ArrayList<Long>();
    String userName = new String();
    Long userId;

    @Test(groups = "createCountry")
    public void createCountry() {
        super.createCountry();
    }

    @Test(groups = "findCountries", dependsOnGroups = "createCountry")
    public void findCountries() {
        List<Country> countries = countryService.findCountries();
        assert countries.size() > 0;
    }

    @Test(groups = "findCountryByShortName", dependsOnGroups = "createCountry")
    public void findCountryByShortName() {
        Country country = countryService.findCountryByAbbreviation("US");
        assert country != null;
    }

    @Test(groups = "createState")
    public void createState() {
        super.createState();
    }

    @Test(groups = "findStates", dependsOnGroups = "createState")
    public void findStates() {
        List<State> states = stateService.findStates();
        assert states.size() > 0;
    }

    @Test(groups = "findStateByAbbreviation", dependsOnGroups = "findStates")
    public void findStateByAbbreviation() {
        State state = stateService.findStateByAbbreviation("KY");
        assert state != null;
    }

}
