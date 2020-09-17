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

package com.ultracommerce.common.web.payment.expression;

import com.ultracommerce.common.web.expression.UltraVariableExpression;
import com.ultracommerce.presentation.condition.ConditionalOnTemplating;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

/**
 * <p>A Thymeleaf Variable Expression implementation for Payment Gateway Specific fields.
 * The Payment Module specific names are invoked via the ExtensionManager.
 * Therefore, each module will need to register itself properly.</p>
 * <p>The input name mappings are those properties defined in the corresponding
 * DTOs.</p>
 *
 * <pre><code>
 * <input type="text" th:name="${#paymentGatewayField.mapName("creditCard.creditCardNum")}"/>
 * </code></pre>
 * translates to:
 *
 * <pre><code>
 * PayPal PayFlow Pro: <input type="text" name="CARDNUM"/>
 * Braintree:          <input type="text" name="transaction[credit_card][number]"/>
 * etc...
 * </code></pre>
 *
 * @see {@link com.ultracommerce.common.payment.dto.PaymentRequestDTO}
 * @see {@link com.ultracommerce.common.payment.dto.CreditCardDTO}
 * @see {@link com.ultracommerce.common.payment.dto.AddressDTO}
 *
 * @author Elbert Bautista (elbertbautista)
 */
@Component("ucpaymentGatewayFieldVariableExpression")
@ConditionalOnTemplating
public class PaymentGatewayFieldVariableExpression implements UltraVariableExpression {

    @Resource(name = "ucPaymentGatewayFieldExtensionManager")
    protected PaymentGatewayFieldExtensionManager extensionManager;

    @Override
    public String getName() {
        return "paymentGatewayField";
    }

    public String mapName(String fieldName) {
        Map<String, String> fieldNameMap = new HashMap<>();
        fieldNameMap.put(fieldName, fieldName);
        extensionManager.getProxy().mapFieldName(fieldName, fieldNameMap);
        return fieldNameMap.get(fieldName);
    }

    public PaymentGatewayFieldExtensionManager getExtensionManager() {
        return extensionManager;
    }

    public void setExtensionManager(PaymentGatewayFieldExtensionManager extensionManager) {
        this.extensionManager = extensionManager;
    }
}
