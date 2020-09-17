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
package com.ultracommerce.common.currency.dao;

import com.ultracommerce.common.currency.domain.UltraCurrency;
import com.ultracommerce.common.persistence.EntityConfiguration;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Author: jerryocanas
 * Date: 9/6/12
 */

@Repository("ucCurrencyDao")
public class UltraCurrencyDaoImpl implements UltraCurrencyDao {

    @PersistenceContext(unitName = "ucPU")
    protected EntityManager em;

    @Resource(name="ucEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    @Override
    public UltraCurrency findDefaultUltraCurrency() {
        Query query = em.createNamedQuery("UC_READ_DEFAULT_CURRENCY");
        query.setHint(org.hibernate.jpa.QueryHints.HINT_CACHEABLE, true);
        List<UltraCurrency> currencyList = query.getResultList();
        if (currencyList.size() >= 1) {
            return currencyList.get(0);
        }
        return null;
    }

    /**
     * @return The locale for the passed in code
     */
    @Override
    public UltraCurrency findCurrencyByCode(String currencyCode) {
        Query query = em.createNamedQuery("UC_READ_CURRENCY_BY_CODE");
        query.setParameter("currencyCode", currencyCode);
        query.setHint(org.hibernate.jpa.QueryHints.HINT_CACHEABLE, true);
        List<UltraCurrency> currencyList = query.getResultList();
        if (currencyList.size() >= 1) {
            return currencyList.get(0);
        }
        return null;
    }

    @Override
    public List<UltraCurrency> getAllCurrencies() {
        Query query = em.createNamedQuery("UC_READ_ALL_CURRENCIES");
        query.setHint(org.hibernate.jpa.QueryHints.HINT_CACHEABLE, true);
        return query.getResultList();
    }

    @Override
    public UltraCurrency save(UltraCurrency currency) {
        return em.merge(currency);
    }
    
    @Override
    public UltraCurrency create() {
        return entityConfiguration.createEntityInstance(UltraCurrency.class.getName(), UltraCurrency.class);
    }    
}
