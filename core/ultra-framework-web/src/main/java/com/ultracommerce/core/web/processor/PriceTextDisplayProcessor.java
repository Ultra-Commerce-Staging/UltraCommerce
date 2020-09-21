/*
 * #%L
 * UltraCommerce Framework Web
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
package com.ultracommerce.core.web.processor;

import com.ultracommerce.common.money.Money;
import com.ultracommerce.common.util.UCMoneyFormatUtils;
import com.ultracommerce.presentation.condition.ConditionalOnTemplating;
import com.ultracommerce.presentation.dialect.AbstractUltraTagTextModifierProcessor;
import com.ultracommerce.presentation.model.UltraTemplateContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * A Thymeleaf processor that renders a Money object according to the currently set locale options.
 * For example, when rendering "6.99" in a US locale, the output text would be "$6.99".
 * When viewing in France for example, you might see "6,99 (US)$". Alternatively, if currency conversion
 * was enabled, you may see "5,59 (euro-symbol)"
 * 
 * @author apazzolini
 */
@Component("ucPriceTextDisplayProcessor")
@ConditionalOnTemplating
public class PriceTextDisplayProcessor extends AbstractUltraTagTextModifierProcessor {

    @Override
    public String getName() {
        return "price";
    }
    
    @Override
    public int getPrecedence() {
        return 1500;
    }

    @Override
    public String getTagText(String tagName, Map<String, String> tagAttributes, String attributeName, String attributeValue, UltraTemplateContext context) {
        Money price = null;

        Object result = context.parseExpression(attributeValue);
        if (result instanceof Money) {
            price = (Money) result;
        } else if (result instanceof Number) {
            price = new Money(((Number)result).doubleValue());
        }

        return UCMoneyFormatUtils.formatPrice(price);
    }

}
