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
package com.ultracommerce.security.service.dataprovider;

import com.ultracommerce.openadmin.server.security.domain.AdminPermission;
import com.ultracommerce.openadmin.server.security.domain.AdminPermissionImpl;
import com.ultracommerce.openadmin.server.security.service.type.PermissionType;
import org.testng.annotations.DataProvider;

public class AdminPermissionDataProvider {
    @DataProvider(name = "setupAdminPermission")
    public static Object[][] createAdminUser() {
        AdminPermission adminPermission = new AdminPermissionImpl();
        adminPermission.setName("TestAdminPermissionName");
        adminPermission.setDescription("Test Admin Permission Description");
        adminPermission.setType(PermissionType.ALL);

        return new Object[][] { new Object[] { adminPermission } };
    }
}
