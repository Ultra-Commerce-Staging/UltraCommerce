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
package com.ultracommerce.common.currency.domain;

/**
 * The UltraCurrencyResolver can be implemented to set the currency (e.g. CurrencyToUse).   
 * 
 * This may differ from the currency that was requested (e.g. from the locale, etc.)   
 * 
 * By storing the desired currency, we have the opportunity for a later module (like PriceLists) to 
 * check the DesiredCurrency and possibly alter the currency for the request. 
 * 
 * @author bpolster
 *
 */
public class UltraRequestedCurrencyDto {

    UltraCurrency currencyToUse;
    UltraCurrency requestedCurrency;

    public UltraRequestedCurrencyDto(UltraCurrency currencyToUse, UltraCurrency requestedCurrency) {
        super();
        this.currencyToUse = currencyToUse;
        this.requestedCurrency = requestedCurrency;
    }

    /**
     * @return the currencyToUse
     */
    public UltraCurrency getCurrencyToUse() {
        return currencyToUse;
    }

    /**
     * @return the requestedCurrency
     */
    public UltraCurrency getRequestedCurrency() {
        return requestedCurrency;
    }

}
