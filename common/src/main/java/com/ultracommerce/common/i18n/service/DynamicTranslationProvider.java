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
package com.ultracommerce.common.i18n.service;

import org.apache.commons.lang3.StringUtils;
import com.ultracommerce.common.web.UltraRequestContext;

import java.util.Locale;

/**
 * Convenience class to provide dynamic field translations.
 * 
 * @author Andre Azzolini (apazzolini)
 */
public class DynamicTranslationProvider {
    
    /**
     * If translations are enabled, this method will look for a translation for the specified field. If translations are
     * disabled or if this particular field did not have a translation, it will return back the defaultValue.
     * 
     * @param obj
     * @param field
     * @param defaultValue
     * @return the translated value
     */
    public static String getValue(Object obj, String field, final String defaultValue) {
        UltraRequestContext requestContext = UltraRequestContext.getUltraRequestContext();
        String valueToReturn = defaultValue;

        if (TranslationConsiderationContext.hasTranslation()) {
            TranslationService translationService = TranslationConsiderationContext.getTranslationService();
            Locale locale = requestContext.getJavaLocale();
            String translatedValue = translationService.getTranslatedValue(obj, field, locale);

            if (StringUtils.isNotBlank(translatedValue)) {
                valueToReturn = translatedValue;
            } else {
                valueToReturn = translationService.getDefaultTranslationValue(obj, field, locale, defaultValue);
            }
        }
            
        return valueToReturn;
    }

}
