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

import com.ultracommerce.profile.core.domain.Phone;
import com.ultracommerce.profile.core.service.PhoneService;
import com.ultracommerce.profile.dataprovider.PhoneDataProvider;
import com.ultracommerce.test.TestNGSiteIntegrationSetup;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

public class PhoneTest extends TestNGSiteIntegrationSetup {

    @Resource
    private PhoneService phoneService;

    List<Long> phoneIds = new ArrayList<>();
    String userName = new String();

    private Long phoneId;

    @Test(groups = { "createPhone" }, dataProvider = "setupPhone", dataProviderClass = PhoneDataProvider.class, dependsOnGroups = { "readCustomer" })
    @Transactional
    @Rollback(false)
    public void createPhone(Phone phone) {
        userName = "customer1";
        assert phone.getId() == null;
        phone = phoneService.savePhone(phone);
        assert phone.getId() != null;
        phoneId = phone.getId();
    }

    @Test(groups = { "readPhoneById" }, dependsOnGroups = { "createPhone" })
    public void readPhoneById() {
        Phone phone = phoneService.readPhoneById(phoneId);
        assert phone != null;
        assert phone.getId() == phoneId;
    }
}
