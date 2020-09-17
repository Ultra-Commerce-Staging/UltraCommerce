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
import com.ultracommerce.profile.core.domain.State;
import com.ultracommerce.profile.core.domain.StateImpl;
import org.hibernate.jpa.QueryHints;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * @deprecated - use {@link com.ultracommerce.profile.core.dao.CountrySubdivisionDaoImpl} instead.
 */
@Deprecated
@Repository("ucStateDao")
public class StateDaoImpl implements StateDao {

    @PersistenceContext(unitName = "ucPU")
    protected EntityManager em;

    @Resource(name = "ucEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    public State findStateByAbbreviation(String abbreviation) {
        return (State) em.find(StateImpl.class, abbreviation);
    }

    @SuppressWarnings("unchecked")
    public List<State> findStates() {
        Query query = em.createNamedQuery("UC_FIND_STATES");
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<State> findStates(String countryAbbreviation) {
        Query query = em.createNamedQuery("UC_FIND_STATES_BY_COUNTRY_ABBREVIATION");
        query.setParameter("countryAbbreviation", countryAbbreviation);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        return query.getResultList();
    }

    public Country findCountryByShortName(String shortName) {
        return (Country) em.find(CountryImpl.class, shortName);
    }

    @SuppressWarnings("unchecked")
    public List<Country> findCountries() {
        Query query = em.createNamedQuery("UC_FIND_COUNTRIES");
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        return query.getResultList();
    }

    public State create() {
        return (State) entityConfiguration.createEntityInstance(State.class.getName());
    }
    
    public State save(State state) {
        return em.merge(state);
    }
}
