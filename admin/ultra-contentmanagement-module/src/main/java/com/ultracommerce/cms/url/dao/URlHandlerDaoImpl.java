/*
 * #%L
 * UltraCommerce CMS Module
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
package com.ultracommerce.cms.url.dao;

import com.ultracommerce.cms.url.domain.URLHandler;
import com.ultracommerce.cms.url.domain.URLHandlerImpl;
import com.ultracommerce.common.persistence.EntityConfiguration;
import org.hibernate.jpa.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Created by ppatel.
 */
@Repository("ucURLHandlerDao")
public class URlHandlerDaoImpl implements URLHandlerDao {

    @PersistenceContext(unitName = "ucPU")
    protected EntityManager em;

    @Resource(name = "ucEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    @Override
    public URLHandler findURLHandlerByURI(String uri) {
        TypedQuery<URLHandler> query = em.createNamedQuery("UC_READ_BY_INCOMING_URL", URLHandler.class);
        query.setParameter("incomingURL", uri);
        query.setHint(QueryHints.HINT_CACHEABLE, true);

        List<URLHandler> results = query.getResultList();
        if (results != null && !results.isEmpty()) {
            return results.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<URLHandler> findAllRegexURLHandlers() {
        TypedQuery<URLHandler> query = em.createNamedQuery("UC_READ_ALL_REGEX_HANDLERS", URLHandler.class);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        return query.getResultList();
    }

    @Override
    public URLHandler findURLHandlerById(Long id) {
        return em.find(URLHandlerImpl.class, id);
    }

    @Override
    public List<URLHandler> findAllURLHandlers() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<URLHandler> criteria = builder.createQuery(URLHandler.class);
        Root<URLHandlerImpl> handler = criteria.from(URLHandlerImpl.class);
        criteria.select(handler);
        TypedQuery<URLHandler> query = em.createQuery(criteria);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return new ArrayList<URLHandler>();
        }
    }

    public URLHandler saveURLHandler(URLHandler handler) {
        return em.merge(handler);
    }

}
