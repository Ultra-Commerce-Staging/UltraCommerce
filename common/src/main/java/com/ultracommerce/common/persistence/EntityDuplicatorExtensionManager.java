/*
 * #%L
 * UltraCommerce Framework
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
package com.ultracommerce.common.persistence;

import com.ultracommerce.common.copy.MultiTenantCopyContext;
import com.ultracommerce.common.extension.ExtensionHandler;
import com.ultracommerce.common.extension.ExtensionManager;
import com.ultracommerce.common.extension.ExtensionManagerOperation;
import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.extension.ExtensionResultStatusType;

import org.springframework.stereotype.Service;

/**
 * Manage the interaction between a duplication operation and other modules that wish to contribute to that lifecycle
 *
 * @author Jeff Fischer
 */
@Service("ucEntityDuplicatorExtensionManager")
public class EntityDuplicatorExtensionManager extends ExtensionManager<EntityDuplicatorExtensionHandler> implements EntityDuplicatorExtensionHandler {

    public static final ExtensionManagerOperation validateDuplicate = new ExtensionManagerOperation() {
        @Override
        public ExtensionResultStatusType execute(ExtensionHandler handler, Object... params) {
            return ((EntityDuplicatorExtensionHandler) handler).validateDuplicate(params[0], (ExtensionResultHolder<Boolean>) params[1]);
        }
    };

    public static final ExtensionManagerOperation setupDuplicate = new ExtensionManagerOperation() {
        @Override
        public ExtensionResultStatusType execute(ExtensionHandler handler, Object... params) {
            return ((EntityDuplicatorExtensionHandler) handler).setupDuplicate(params[0], (ExtensionResultHolder<MultiTenantCopyContext>) params[1]);
        }
    };

    public static final ExtensionManagerOperation tearDownDuplicate = new ExtensionManagerOperation() {
        @Override
        public ExtensionResultStatusType execute(ExtensionHandler handler, Object... params) {
            return ((EntityDuplicatorExtensionHandler) handler).tearDownDuplicate();
        }
    };

    public EntityDuplicatorExtensionManager() {
        super(EntityDuplicatorExtensionHandler.class);
    }

    @Override
    public ExtensionResultStatusType validateDuplicate(Object entity, ExtensionResultHolder<Boolean> resultHolder) {
        return execute(validateDuplicate, entity, resultHolder);
    }

    @Override
    public ExtensionResultStatusType setupDuplicate(Object entity, ExtensionResultHolder<MultiTenantCopyContext> resultHolder) {
        return execute(setupDuplicate, entity, resultHolder);
    }

    @Override
    public ExtensionResultStatusType tearDownDuplicate() {
        return execute(tearDownDuplicate);
    }

    @Override
    public boolean isEnabled() {
        //not used - fulfills interface contract
        return true;
    }
}
