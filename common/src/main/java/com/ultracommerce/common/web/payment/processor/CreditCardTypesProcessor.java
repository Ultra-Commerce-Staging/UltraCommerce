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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.presentation.condition.ConditionalOnTemplating;
import com.ultracommerce.presentation.dialect.AbstractUltraVariableModifierProcessor;
import com.ultracommerce.presentation.model.UltraTemplateContext;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

/**
 * <p>The following processor will add any Payment Gateway specific Card Type 'codes' to the model if
 * the gateway requires that a 'Card Type' (e.g. Visa, MasterCard, etc...) be sent along with
 * the credit card number and expiry date.
 * </p>
 *
 * <p>This processor will put the key 'paymentGatewayCardTypes' on the model if there are any types available</p>
 *
 * <p>Here is an example:</p>
 *
 * <pre><code>
 *  <blc:credit_card_types >
 *      <div th:if="${paymentGatewayCardTypes != null}" class="form-group">
 *          <label for="cardNumber">Card Type</label>
 *          <select th:name="${#paymentGatewayField.mapName('creditCard.creditCardType')}">
 *              <option th:each="entry : ${paymentGatewayCardTypes}" th:value="${entry.key}" th:text="${entry.value}"></option>
 *          </select>
 *      </div>
 *  </blc:credit_card_types>
 * </code></pre>
 *
 * @author Elbert Bautista (elbertbautista)
 */
@Component("ucCreditCardTypesProcessor")
@ConditionalOnTemplating
public class CreditCardTypesProcessor extends AbstractUltraVariableModifierProcessor {

    protected static final Log LOG = LogFactory.getLog(CreditCardTypesProcessor.class);

    @Resource(name = "ucCreditCardTypesExtensionManager")
    protected CreditCardTypesExtensionManager extensionManager;

    @Override
    public String getName() {
        return "credit_card_types";
    }
    
    @Override
    public int getPrecedence() {
        return 100;
    }
    
    @Override
    public boolean useGlobalScope() {
        return false;
    }

    @Override
    public Map<String, Object> populateModelVariables(String tagName, Map<String, String> tagAttributes, UltraTemplateContext context) {
        Map<String, String> creditCardTypes = new HashMap<>();

        try {
            extensionManager.getProxy().populateCreditCardMap(creditCardTypes);
        } catch (Exception e) {
            LOG.warn("Unable to Populate Credit Card Types Map for this Payment Module, or card type is not needed.");
        }

        if (!creditCardTypes.isEmpty()) {
            return ImmutableMap.of("paymentGatewayCardTypes", (Object) creditCardTypes);
        } else {
            return null;
        }
    }

}
