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
package com.ultracommerce.common.util;

import com.ultracommerce.common.currency.util.UltraCurrencyUtils;
import com.ultracommerce.common.money.Money;
import com.ultracommerce.common.web.UltraRequestContext;

/**
 * Convenience class to format prices for front-end display.
 * 
 * @author Chris Kittrell (ckittrell)
 */
public class UCMoneyFormatUtils {
    
    /**
     * Reformats the given price field for front-end display.
     * 
     * @param price
     * @return the formatted price
     */
    public static String formatPrice(Money price) {
        if (price == null) {
            return "Not Available";
        }

        UltraRequestContext brc = UltraRequestContext.getUltraRequestContext();
        if (brc.getJavaLocale() != null) {
            return UltraCurrencyUtils.getNumberFormatFromCache(brc.getJavaLocale(), price.getCurrency()).format(price.getAmount());
        } else {
            // Setup your UC_CURRENCY and UC_LOCALE to display a diff default.
            return "$ " + price.getAmount().toString();
        }
    }
}
