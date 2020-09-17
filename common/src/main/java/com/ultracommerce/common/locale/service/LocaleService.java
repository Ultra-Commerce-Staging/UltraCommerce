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
package com.ultracommerce.common.locale.service;

import com.ultracommerce.common.locale.domain.Locale;

import java.util.List;

/**
 * Created by bpolster.
 */
public interface LocaleService {

    /**
     * @return the locale for the passed in code
     */
    public Locale findLocaleByCode(String localeCode);

    /**
     * @return the default locale
     */
    public Locale findDefaultLocale();

    /**
     * @return a list of all known locales
     */
    public List<Locale> findAllLocales();
    
    /**
     * Persists the given locale
     * 
     * @param locale
     * @return the persisted locale
     */
    public Locale save(Locale locale);
    
}
