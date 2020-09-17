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
package com.ultracommerce.core.checkout.service.workflow;

import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.core.checkout.service.workflow.extension.ValidateCheckoutActivityExtensionManager;
import com.ultracommerce.core.workflow.BaseActivity;
import com.ultracommerce.core.workflow.ProcessContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * This activity is responsible for providing an extension point for validating a checkout request.
 *
 * @author Nick Crum ncrum
 */
@Component("ucValidateCheckoutActivity")
public class ValidateCheckoutActivity extends BaseActivity<ProcessContext<CheckoutSeed>> {

    public static final int ORDER = 500;
    
    @Resource(name = "ucValidateCheckoutActivityExtensionManager")
    protected ValidateCheckoutActivityExtensionManager extensionManager;
    
    public ValidateCheckoutActivity() {
        setOrder(ORDER);
    }

    @Override
    public ProcessContext<CheckoutSeed> execute(ProcessContext<CheckoutSeed> context) throws Exception {
        ExtensionResultHolder<Exception> resultHolder = new ExtensionResultHolder<>();
        resultHolder.setResult(null);
        ExtensionResultStatusType result = extensionManager.getProxy().validateCheckout(context.getSeedData(), resultHolder);
        if (!ExtensionResultStatusType.NOT_HANDLED.equals(result)) {
            if (resultHolder.getResult() != null) {
                throw resultHolder.getResult();
            }
        }

        return context;
    }
}
