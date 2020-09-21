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
package com.ultracommerce.common.web.payment.processor;

import com.ultracommerce.common.extension.AbstractExtensionHandler;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.common.payment.PaymentGatewayType;
import com.ultracommerce.common.payment.service.PaymentGatewayResolver;
import java.util.Map;
import javax.annotation.Resource;

/**
 * @author Elbert Bautista (elbertbautista)
 */
public abstract class AbstractCreditCardTypesExtensionHandler extends AbstractExtensionHandler
        implements CreditCardTypesExtensionHandler {

    @Resource(name = "ucPaymentGatewayResolver")
    protected PaymentGatewayResolver paymentGatewayResolver;

    @Override
    public ExtensionResultStatusType populateCreditCardMap(Map<String, String> creditCardTypes) {

        if (paymentGatewayResolver.isHandlerCompatible(getHandlerType())) {
            setCardTypes(creditCardTypes);
            return ExtensionResultStatusType.HANDLED;
        }

        return ExtensionResultStatusType.NOT_HANDLED;
    }

    public abstract PaymentGatewayType getHandlerType();

    public abstract void setCardTypes(Map<String, String> creditCardTypes);

}
