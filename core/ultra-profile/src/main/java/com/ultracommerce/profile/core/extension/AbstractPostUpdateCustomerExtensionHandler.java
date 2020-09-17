/*
 * #%L
 * UltraCommerce Profile
 * %%
 * Copyright (C) 2009 - 2017 Ultra Commerce
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
package com.ultracommerce.profile.core.extension;

import com.ultracommerce.common.extension.AbstractExtensionHandler;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.profile.core.domain.Customer;

import java.util.List;

public abstract class AbstractPostUpdateCustomerExtensionHandler
        extends AbstractExtensionHandler implements PostUpdateCustomerExtensionHandler {

    protected static final int DEFAULT_PRIORITY = 500;

    @Override
    public ExtensionResultStatusType postUpdate(long customerId) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType postUpdate(Customer customer) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType postUpdateAll(List<Customer> customers) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType postUpdateAllByIds(List<Long> customerIds) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }
}
