/*
 * #%L
 * UltraCommerce Profile
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
package com.ultracommerce.profile.core.service;

import com.ultracommerce.profile.core.dao.CountryDao;
import com.ultracommerce.profile.core.domain.Country;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("ucCountryService")
public class CountryServiceImpl implements CountryService {

    @Resource(name="ucCountryDao")
    protected CountryDao countryDao;

    public List<Country> findCountries() {
        return countryDao.findCountries();
    }

    public Country findCountryByAbbreviation(String abbreviation) {
        return countryDao.findCountryByAbbreviation(abbreviation);
    }

    @Transactional("ucTransactionManager")
    public Country save(Country country) {
        return countryDao.save(country);
    }
}
