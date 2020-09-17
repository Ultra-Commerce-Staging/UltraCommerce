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

import com.ultracommerce.openadmin.server.security.domain.AdminPermission;
import com.ultracommerce.openadmin.server.security.service.AdminSecurityService;
import com.ultracommerce.security.service.dataprovider.AdminPermissionDataProvider;
import com.ultracommerce.test.TestNGAdminIntegrationSetup;
import org.springframework.test.annotation.Rollback;
import org.testng.annotations.Test;

import javax.annotation.Resource;

public class AdminPermissionTest extends TestNGAdminIntegrationSetup {
    @Resource
    AdminSecurityService adminSecurityService;

    @Test(groups =  {"testAdminPermissionSave"}, dataProvider = "setupAdminPermission", dataProviderClass = AdminPermissionDataProvider.class)
    @Rollback(true)
    public void testAdminPermissionSave(AdminPermission permission) throws Exception {
        AdminPermission newPermission = adminSecurityService.saveAdminPermission(permission);

        AdminPermission permissionFromDB = adminSecurityService.readAdminPermissionById(newPermission.getId());

        assert(permissionFromDB != null);
    }

}
