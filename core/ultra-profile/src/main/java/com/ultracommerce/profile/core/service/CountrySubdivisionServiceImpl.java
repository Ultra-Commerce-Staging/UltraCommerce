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

import com.ultracommerce.profile.core.dao.CountrySubdivisionDao;
import com.ultracommerce.profile.core.domain.CountrySubdivision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import javax.annotation.Resource;

/**
 * @author Elbert Bautista (elbertbautista)
 */
@Service("ucCountrySubdivisionService")
public class CountrySubdivisionServiceImpl implements CountrySubdivisionService {

    @Resource(name="ucCountrySubdivisionDao")
    protected CountrySubdivisionDao countrySubdivisionDao;

    @Override
    public List<CountrySubdivision> findSubdivisions() {
        return countrySubdivisionDao.findSubdivisions();
    }

    @Override
    public List<CountrySubdivision> findSubdivisions(String countryAbbreviation) {
        return countrySubdivisionDao.findSubdivisions(countryAbbreviation);
    }

    @Override
    public List<CountrySubdivision> findSubdivisionsByCountryAndCategory(String countryAbbreviation, String category) {
        return countrySubdivisionDao.findSubdivisionsByCountryAndCategory(countryAbbreviation, category);
    }

    @Override
    public CountrySubdivision findSubdivisionByAbbreviation(String abbreviation) {
        return countrySubdivisionDao.findSubdivisionByAbbreviation(abbreviation);
    }

    @Override
    public CountrySubdivision findSubdivisionByCountryAndAltAbbreviation(String countryAbbreviation, String altAbbreviation) {
        return countrySubdivisionDao.findSubdivisionByCountryAndAltAbbreviation(countryAbbreviation, altAbbreviation);
    }

    @Override
    public CountrySubdivision findSubdivisionByCountryAndName(String countryAbbreviation, String name) {
        return countrySubdivisionDao.findSubdivisionByCountryAndName(countryAbbreviation, name);
    }

    @Override
    @Transactional("ucTransactionManager")
    public CountrySubdivision save(CountrySubdivision subdivision) {
        return countrySubdivisionDao.save(subdivision);
    }
}
