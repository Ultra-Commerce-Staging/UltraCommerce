/*
 * #%L
 * UltraCommerce Open Admin Platform
 * %%
 * Copyright (C) 2009 - 2019 Ultra Commerce
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
package com.ultracommerce.openadmin.server.security.extension;

import com.ultracommerce.common.extension.ExtensionHandler;
import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.openadmin.server.security.domain.AdminUser;
import com.ultracommerce.openadmin.server.security.service.type.PermissionType;

/**
 * Extension handler for {@link com.ultracommerce.openadmin.server.security.service.AdminSecurityService}
 */
public interface AdminSecurityServiceExtensionHandler extends ExtensionHandler {

    /**
     * Checks if the user can perform APPROVE/REJECT operation without edit privileges
     *
     * @param adminUser
     * @param permissionType
     * @param erh
     * @return
     */
    ExtensionResultStatusType hasPrivilegesForOperation(AdminUser adminUser, PermissionType permissionType, ExtensionResultHolder<Boolean> erh);

}
