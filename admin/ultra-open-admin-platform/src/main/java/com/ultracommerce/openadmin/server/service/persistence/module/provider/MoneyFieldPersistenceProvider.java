/*
 * #%L
 * UltraCommerce Open Admin Platform
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
package com.ultracommerce.openadmin.server.service.persistence.module.provider;

import org.apache.commons.lang3.StringUtils;
import com.ultracommerce.common.currency.util.CurrencyCodeIdentifiable;
import com.ultracommerce.common.presentation.client.SupportedFieldType;
import com.ultracommerce.common.web.UltraRequestContext;
import com.ultracommerce.openadmin.dto.Property;
import com.ultracommerce.openadmin.server.service.persistence.module.provider.request.ExtractValueRequest;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Currency;
import java.util.Locale;

/**
 * Persistence provider capable of extracting friendly display values for Money fields
 * 
 * @author Andre Azzolini (apazzolini)
 */
@Scope("prototype")
@Component("ucMoneyFieldPersistenceProvider")
public class MoneyFieldPersistenceProvider extends AbstractMoneyFieldPersistenceProvider {
    
    @Override
    public int getOrder() {
        return FieldPersistenceProvider.MONEY;
    }
    
    @Override
    protected boolean canHandleExtraction(ExtractValueRequest extractValueRequest, Property property) {
        return extractValueRequest.getMetadata().getFieldType() == SupportedFieldType.MONEY;
    }
    
    @Override
    protected Locale getLocale(ExtractValueRequest extractValueRequest, Property property) {
        UltraRequestContext brc = UltraRequestContext.getUltraRequestContext();
        return brc.getJavaLocale();
    }

    @Override
    protected Currency getCurrency(ExtractValueRequest extractValueRequest, Property property) {
        String currencyCodeField = extractValueRequest.getMetadata().getCurrencyCodeField();
        if (!StringUtils.isEmpty(currencyCodeField)) {
            try {
                return Currency.getInstance((String) extractValueRequest.getFieldManager().getFieldValue(extractValueRequest.getEntity(), currencyCodeField));
            } catch (Exception e) {
                //do nothing
            }
        }
        if (extractValueRequest.getEntity() instanceof CurrencyCodeIdentifiable) {
            try {
                return Currency.getInstance(((CurrencyCodeIdentifiable) extractValueRequest.getEntity()).getCurrencyCode());
            } catch (Exception e) {
                //do nothing
            }
        }
        UltraRequestContext brc = UltraRequestContext.getUltraRequestContext();
        return brc.getJavaCurrency();
    }
    
}
