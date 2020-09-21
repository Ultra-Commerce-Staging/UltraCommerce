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

import com.ultracommerce.common.i18n.domain.ISOCountry;
import java.util.List;

/**
 * Service that provide methods to look up the
 * standards published by the International Organization for Standardization (ISO)
 *
 * For example, ISO 3166-1 define codes for countries/dependent territories that are widely used
 * by many systems. You can use this service to find the defined countries based on the alpha-2 code for that country.
 *
 * @author Elbert Bautista (elbertbautista)
 */
public interface ISOService {

    public List<ISOCountry> findISOCountries();

    public ISOCountry findISOCountryByAlpha2Code(String alpha2);

    public ISOCountry save(ISOCountry isoCountry);

}
