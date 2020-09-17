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

import com.ultracommerce.common.extension.AbstractExtensionHandler;
import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.openadmin.server.security.domain.AdminUser;
import com.ultracommerce.openadmin.server.security.service.type.PermissionType;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Default implementation used by the core framework.
 *
 * @see com.ultracommerce.openadmin.server.security.extension.AdminSecurityServiceExtensionHandler
 */
@Component("ucDefaultAdminSecurityServiceExtensionHandler")
public class DefaultAdminSecurityServiceExtensionHandler extends AbstractExtensionHandler implements AdminSecurityServiceExtensionHandler {

    @Resource(name = "ucAdminSecurityServiceExtensionManager")
    protected AdminSecurityServiceExtensionManager extensionManager;

    @PostConstruct
    public void init() {
        if (isEnabled()) {
            extensionManager.registerHandler(this);
        }
    }

    @Override
    public ExtensionResultStatusType hasPrivilegesForOperation(AdminUser adminUser, PermissionType permissionType, ExtensionResultHolder<Boolean> erh) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }
}
