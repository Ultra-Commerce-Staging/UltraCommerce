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
package com.ultracommerce.common.money;

import com.ultracommerce.common.classloader.release.ThreadLocalManager;

import java.util.HashMap;

public class CurrencyConversionContext {
    
    private static final ThreadLocal<CurrencyConversionService> currencyConversionService = ThreadLocalManager.createThreadLocal(CurrencyConversionService.class, false);

    private static final ThreadLocal<HashMap> currencyConversionContext = ThreadLocalManager.createThreadLocal(HashMap.class);

    public static HashMap getCurrencyConversionContext() {
        return CurrencyConversionContext.currencyConversionContext.get();
    }
    
    public static void setCurrencyConversionContext(HashMap currencyConsiderationContext) {
        CurrencyConversionContext.currencyConversionContext.set(currencyConsiderationContext);
    }
    
    public static CurrencyConversionService getCurrencyConversionService() {
        return CurrencyConversionContext.currencyConversionService.get();
    }
    
    public static void setCurrencyConversionService(CurrencyConversionService currencyDeterminationService) {
        CurrencyConversionContext.currencyConversionService.set(currencyDeterminationService);
    }

}
