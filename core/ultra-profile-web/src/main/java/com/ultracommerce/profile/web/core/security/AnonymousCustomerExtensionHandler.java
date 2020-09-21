/*
 * #%L
 * UltraCommerce Profile Web
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
package com.ultracommerce.profile.web.core.security;

import com.ultracommerce.common.extension.ExtensionHandler;
import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.profile.core.domain.Customer;
import org.springframework.web.context.request.WebRequest;

public interface AnonymousCustomerExtensionHandler extends ExtensionHandler {

    /**
     * This allows other modules to handle the resolution of an anonymous customer.
     * The return value, if handled generally, should be ExtensionResultStatusType.HANDLED_CONTINUE.
     * This allows multiple modules to set/modify the outcome.
     *
     * @param customerHolder
     * @param request
     * @return
     */
    public ExtensionResultStatusType getAnonymousCustomer(ExtensionResultHolder<Customer> customerHolder, WebRequest request);

}
