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
package com.ultracommerce.common.locale.domain;

import com.ultracommerce.common.currency.domain.UltraCurrency;

import java.io.Serializable;

/**
 * Created by jfischer
 */
public interface Locale extends Serializable {

    String getLocaleCode();

    void setLocaleCode(String localeCode);
    
    public java.util.Locale getJavaLocale();

    public String getFriendlyName();

    public void setFriendlyName(String friendlyName);

    public void setDefaultFlag(Boolean defaultFlag);

    public Boolean getDefaultFlag();

    public UltraCurrency getDefaultCurrency();

    public void setDefaultCurrency(UltraCurrency currency);

    /**
     * If true then the country portion of the locale will be used when building the search index.
     * If null or false then only the language will be used.
     * 
     * For example, if false, a locale of en_US will only index the results based
     * on the root of "en".
     * 
     * @return
     */
    public Boolean getUseCountryInSearchIndex();
    
    /**
     * Sets whether or not to use the country portion of the locale in the search index.
     * @param useInSearchIndex
     */
    public void setUseCountryInSearchIndex(Boolean useInSearchIndex);

}
