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
package com.ultracommerce.core.checkout.service.workflow.extension;

import com.ultracommerce.common.extension.ExtensionHandler;
import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.core.checkout.service.workflow.CheckoutSeed;

/**
 * @author Nick Crum ncrum
 */
public interface ValidateCheckoutActivityExtensionHandler extends ExtensionHandler {

    /**
     * This extension allows one to validate a checkout request before it succeeds and specify an exception to throw
     * if the request is found to be invalid.
     *
     * @param request
     * @param resultHolder
     * @return
     */
    public ExtensionResultStatusType validateCheckout(CheckoutSeed request, ExtensionResultHolder<Exception> resultHolder);
}
