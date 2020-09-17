/*
 * #%L
 * UltraCommerce Framework
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
package com.ultracommerce.core.store.dao;

import com.ultracommerce.core.store.domain.Store;
import com.ultracommerce.core.store.domain.StoreImpl;
import org.hibernate.jpa.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository("ucStoreDao")
public class StoreDaoImpl implements StoreDao {

    @PersistenceContext(unitName = "ucPU")
    protected EntityManager em;

    @Override
    public Store readStoreById(Long id) {
        return em.find(StoreImpl.class, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Store readStoreByStoreName(final String storeName) {
        Query query = em.createNamedQuery("UC_FIND_STORE_BY_STORE_NAME");
        query.setParameter("storeName", storeName);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        List result = query.getResultList();
        return (result.size() > 0) ? (Store) result.get(0) : null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Store readStoreByStoreCode(final String storeCode) {
        Query query = em.createNamedQuery("UC_FIND_STORE_BY_STORE_NAME");
        query.setParameter("storeName", storeCode.toUpperCase());
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        List result = query.getResultList();
        return (result.size() > 0) ? (Store) result.get(0) : null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Store> readAllStores() {
        Query query = em.createNamedQuery("UC_FIND_ALL_STORES");
        query.setParameter("archived", 'N');
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        return query.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Store> readAllStoresByState(final String state) {
        Query query = em.createNamedQuery("UC_FIND_ALL_STORES_BY_STATE");
        query.setParameter("state", state);
        query.setParameter("archived", 'N');
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        return query.getResultList();
    }

    @Override
    public Store save(Store store) {
        return em.merge(store);
    }
}
