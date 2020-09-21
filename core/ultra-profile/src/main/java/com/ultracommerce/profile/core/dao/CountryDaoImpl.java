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
package com.ultracommerce.profile.core.dao;

import com.ultracommerce.common.persistence.EntityConfiguration;
import com.ultracommerce.profile.core.domain.Country;
import com.ultracommerce.profile.core.domain.CountryImpl;
import org.hibernate.jpa.QueryHints;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository("ucCountryDao")
public class CountryDaoImpl implements CountryDao {

    @PersistenceContext(unitName = "ucPU")
    protected EntityManager em;

    @Resource(name="ucEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    public Country findCountryByAbbreviation(String abbreviation) {
        return (Country) em.find(CountryImpl.class, abbreviation);
    }

    @SuppressWarnings("unchecked")
    public List<Country> findCountries() {
        Query query = em.createNamedQuery("UC_FIND_COUNTRIES");
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        return query.getResultList();
    }
    
    public Country save(Country country) {
        return em.merge(country);
    }
}
