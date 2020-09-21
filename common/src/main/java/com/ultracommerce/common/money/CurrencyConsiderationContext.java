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

/**
 * 
 * @author jfischer
 *
 */
public class CurrencyConsiderationContext {
    
    private static final ThreadLocal<CurrencyDeterminationService> currencyDeterminationService = ThreadLocalManager.createThreadLocal(CurrencyDeterminationService.class);

    private static final ThreadLocal<HashMap> currencyConsiderationContext = ThreadLocalManager.createThreadLocal(HashMap.class);

    public static HashMap getCurrencyConsiderationContext() {
        return CurrencyConsiderationContext.currencyConsiderationContext.get();
    }
    
    public static void setCurrencyConsiderationContext(HashMap currencyConsiderationContext) {
        CurrencyConsiderationContext.currencyConsiderationContext.set(currencyConsiderationContext);
    }
    
    public static CurrencyDeterminationService getCurrencyDeterminationService() {
        return CurrencyConsiderationContext.currencyDeterminationService.get();
    }
    
    public static void setCurrencyDeterminationService(CurrencyDeterminationService currencyDeterminationService) {
        CurrencyConsiderationContext.currencyDeterminationService.set(currencyDeterminationService);
    }
}
