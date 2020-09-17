/*
 * #%L
 * UltraCommerce Open Admin Platform
 * %%
 * Copyright (C) 2009 - 2020 Ultra Commerce
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
package com.ultracommerce.admin.server.service.extension;

import com.ultracommerce.common.extension.ExtensionHandler;
import com.ultracommerce.common.extension.ExtensionManager;
import com.ultracommerce.common.extension.ExtensionManagerOperation;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.openadmin.dto.Entity;
import org.springframework.stereotype.Service;


@Service("ucOfferCustomServiceExtensionManager")
public class OfferCustomServiceExtensionManager extends ExtensionManager<OfferCustomServiceExtensionHandler> implements OfferCustomServiceExtensionHandler {


    public OfferCustomServiceExtensionManager() {
        super(OfferCustomServiceExtensionHandler.class);
    }

    public static final ExtensionManagerOperation clearHiddenQualifiers = new ExtensionManagerOperation() {
        @Override
        public ExtensionResultStatusType execute(ExtensionHandler handler, Object... params) {
            return ((OfferCustomServiceExtensionHandler) handler).clearHiddenQualifiers((Entity) params[0]);
        }
    };

    @Override
    public ExtensionResultStatusType clearHiddenQualifiers(Entity entity) {
        return execute(clearHiddenQualifiers, entity);
    }

    @Override
    public boolean isEnabled() {
        //not used - fulfills interface contract
        return true;
    }
}
