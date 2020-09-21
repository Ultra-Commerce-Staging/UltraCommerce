/*
 * #%L
 * UltraCommerce Common Libraries
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
package com.ultracommerce.common.copy;

import com.ultracommerce.common.extension.AbstractExtensionHandler;
import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.extension.ExtensionResultStatusType;

public class AbstractMultiTenantCopierExtensionHandler extends AbstractExtensionHandler 
        implements MultiTenantCopierExtensionHandler {

    @Override
    public ExtensionResultStatusType transformCopy(MultiTenantCopyContext context, Object from, Object to) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType prepareForSave(MultiTenantCopyContext context, Object from, Object to) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType postSave(MultiTenantCopyContext context, Object from, Object to) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType shouldClone(MultiTenantCopyContext context, Object from, ExtensionResultHolder<Boolean> result) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }
}
