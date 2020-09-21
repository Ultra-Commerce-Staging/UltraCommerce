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
package com.ultracommerce.common.web;

import com.ultracommerce.common.locale.domain.Locale;
import com.ultracommerce.common.locale.service.LocaleService;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Specific Spring component to override the default behavior of {@link CookieLocaleResolver} so that the default Ultra
 * Locale looked up in the database is used. This should be hooked up in applicationContext-servlet.xml in place of Spring's
 * {@link CookieResolver}.
 * 
 * @author Phillip Verheyden (phillipuniverse)
 * @see {@link UltraLocaleResolverImpl}
 */
public class UltraCookieLocaleResolver extends CookieLocaleResolver {

    @Resource(name = "ucLocaleService")
    private LocaleService localeService;
    
    @Override
    protected java.util.Locale determineDefaultLocale(HttpServletRequest request) {
        java.util.Locale defaultLocale = getDefaultLocale();
        if (defaultLocale == null) {
            Locale defaultUltraLocale = localeService.findDefaultLocale();
            if (defaultUltraLocale == null) {
                return super.determineDefaultLocale(request);
            } else {
                return UltraRequestContext.convertLocaleToJavaLocale(defaultUltraLocale);
            }
        }
        return defaultLocale;
    }
    
}
