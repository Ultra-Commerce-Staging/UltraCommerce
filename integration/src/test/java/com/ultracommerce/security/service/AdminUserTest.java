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
package com.ultracommerce.security.service;

import com.ultracommerce.openadmin.server.security.domain.AdminUser;
import com.ultracommerce.openadmin.server.security.service.AdminSecurityService;
import com.ultracommerce.security.service.dataprovider.AdminUserDataProvider;
import com.ultracommerce.test.TestNGAdminIntegrationSetup;
import org.springframework.test.annotation.Rollback;
import org.testng.annotations.Test;

import javax.annotation.Resource;

public class AdminUserTest extends TestNGAdminIntegrationSetup {
    @Resource
    AdminSecurityService adminSecurityService;

    @Test(groups =  {"testAdminUserSave"}, dataProvider = "setupAdminUser", dataProviderClass = AdminUserDataProvider.class)
    @Rollback(true)
    public void testAdminUserSave(AdminUser user) throws Exception {
        AdminUser newUser = adminSecurityService.saveAdminUser(user);

        AdminUser userFromDB = adminSecurityService.readAdminUserById(newUser.getId());

        assert(userFromDB != null);
    }

}
