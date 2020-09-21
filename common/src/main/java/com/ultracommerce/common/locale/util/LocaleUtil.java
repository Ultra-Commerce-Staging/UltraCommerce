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
package com.ultracommerce.common.locale.util;

import com.ultracommerce.common.locale.domain.Locale;

/**
 * Author: jerryocanas
 * Date: 9/17/12
 */
public final class LocaleUtil {

    public static String findLanguageCode(Locale locale) {
        if (locale != null && locale.getLocaleCode() != null && locale.getLocaleCode().indexOf("_") > 0) {
            int endIndex = locale.getLocaleCode().indexOf("_");
            char[] localeCodeChars = locale.getLocaleCode().toCharArray();
            StringBuffer sb = new StringBuffer();
            for(int i=0; i < endIndex; i++){
                sb.append(localeCodeChars[i]);
            }
            return sb.toString();
        }
        return null;
    }

}
