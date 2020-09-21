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
package com.ultracommerce.common.i18n.dao;

import com.ultracommerce.common.i18n.domain.ISOCountry;
import com.ultracommerce.common.i18n.domain.ISOCountryImpl;
import com.ultracommerce.common.persistence.EntityConfiguration;
import org.hibernate.jpa.QueryHints;
import org.springframework.stereotype.Repository;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * @author Elbert Bautista (elbertbautista)
 */
@Repository("ucISODao")
public class ISODaoImpl implements ISODao {

    @PersistenceContext(unitName = "ucPU")
    protected EntityManager em;

    @Resource(name = "ucEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    public ISOCountry findISOCountryByAlpha2Code(String alpha2) {
        return (ISOCountry) em.find(ISOCountryImpl.class, alpha2);
    }

    @SuppressWarnings("unchecked")
    public List<ISOCountry> findISOCountries() {
        Query query = em.createNamedQuery("UC_FIND_ISO_COUNTRIES");
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        return query.getResultList();
    }

    public ISOCountry save(ISOCountry isoCountry) {
        return em.merge(isoCountry);
    }
}
