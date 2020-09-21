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
package com.ultracommerce.common.config.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.cache.AbstractCacheMissAware;
import com.ultracommerce.common.cache.PersistentRetrieval;
import com.ultracommerce.common.config.domain.NullSystemProperty;
import com.ultracommerce.common.config.domain.SystemProperty;
import com.ultracommerce.common.config.domain.SystemPropertyImpl;
import com.ultracommerce.common.extensibility.jpa.SiteDiscriminator;
import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.common.persistence.EntityConfiguration;
import com.ultracommerce.common.web.UltraRequestContext;
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
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * This DAO enables access to manage system properties that can be stored in the database.
 * <p/>
 * User: Kelly Tisdell
 * Date: 6/20/12
 */
@Repository("ucSystemPropertiesDao")
public class SystemPropertiesDaoImpl extends AbstractCacheMissAware<SystemProperty> implements SystemPropertiesDao {

    protected static final Log LOG = LogFactory.getLog(SystemPropertiesDaoImpl.class);

    @PersistenceContext(unitName = "ucPU")
    protected EntityManager em;

    @Resource(name = "ucEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    @Resource(name = "ucSystemPropertyDaoQueryExtensionManager")
    protected SystemPropertyDaoQueryExtensionManager queryExtensionManager;

    private SystemProperty nullObject;

    @Override
    public SystemProperty readById(Long id) {
        return em.find(SystemPropertyImpl.class, id);
    }

    @Override
    public SystemProperty saveSystemProperty(SystemProperty systemProperty) {
        return em.merge(systemProperty);
    }

    @Override
    public void deleteSystemProperty(SystemProperty systemProperty) {
        em.remove(systemProperty);
    }

    @Override
    public List<SystemProperty> readAllSystemProperties() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<SystemProperty> criteria = builder.createQuery(SystemProperty.class);
        Root<SystemPropertyImpl> handler = criteria.from(SystemPropertyImpl.class);
        criteria.select(handler);
        List<Predicate> restrictions = new ArrayList<Predicate>();
        List<Order> sorts = new ArrayList<Order>();
        try {
            if (queryExtensionManager != null) {
                queryExtensionManager.getProxy().setup(SystemPropertyImpl.class, null);
                queryExtensionManager.getProxy().refineRetrieve(SystemPropertyImpl.class, null, builder, criteria, handler, restrictions);
                queryExtensionManager.getProxy().refineOrder(SystemPropertyImpl.class, null, builder, criteria, handler, sorts);
            }
            criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
            return em.createQuery(criteria).setHint(QueryHints.HINT_CACHEABLE, Boolean.TRUE).getResultList();
        } catch (NoResultException e) {
            LOG.error(e);
            return new ArrayList<SystemProperty>();
        } finally {
            if (queryExtensionManager != null) {
                queryExtensionManager.getProxy().breakdown(SystemPropertyImpl.class, null);
            }
        }
    }

    @Override
    public SystemProperty readSystemPropertyByName(final String name) {
        return getCachedObject(SystemProperty.class, "ucSystemPropertyNullCheckCache", "SYSTEM_PROPERTY_MISSING_CACHE_HIT_RATE", new PersistentRetrieval<SystemProperty>() {
            @Override
            public SystemProperty retrievePersistentObject() {
                CriteriaBuilder builder = em.getCriteriaBuilder();
                CriteriaQuery<SystemProperty> criteria = builder.createQuery(SystemProperty.class);
                Root<SystemPropertyImpl> handler = criteria.from(SystemPropertyImpl.class);
                criteria.select(handler);

                List<Predicate> restrictions = new ArrayList<Predicate>();
                restrictions.add(builder.equal(handler.get("name"), name));

                try {
                    if (queryExtensionManager != null) {
                        queryExtensionManager.getProxy().setup(SystemPropertyImpl.class, null);
                        queryExtensionManager.getProxy().refineRetrieve(SystemPropertyImpl.class, null, builder, criteria, handler, restrictions);
                    }
                    criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));

                    TypedQuery<SystemProperty> query = em.createQuery(criteria);
                    query.setHint(QueryHints.HINT_CACHEABLE, true);
                    List<SystemProperty> response = query.getResultList();
                    if (response.size() > 0) {
                        ExtensionResultHolder<List> resultHolder = new ExtensionResultHolder<>();
                        if (queryExtensionManager != null) {
                            ExtensionResultStatusType resultStatusType = queryExtensionManager.getProxy().refineResults(SystemPropertyImpl.class, null, response, resultHolder);
                            if (!resultStatusType.equals(ExtensionResultStatusType.NOT_HANDLED)) {
                                response = resultHolder.getResult();
                            }
                        }

                        UltraRequestContext ultraRequestContext = UltraRequestContext.getUltraRequestContext();
                        if ((ultraRequestContext == null || ultraRequestContext.getNonPersistentSite() == null) && SiteDiscriminator.class.isAssignableFrom(SystemPropertyImpl.class)) {
                            for (SystemProperty prop : response) {
                                if (((SiteDiscriminator) prop).getSiteDiscriminator() == null) {
                                    return prop;
                                }
                            }
                        } else {
                            return response.get(0);
                        }
                    }
                    return null;
                } finally {
                    if (queryExtensionManager != null) {
                        queryExtensionManager.getProxy().breakdown(SystemPropertyImpl.class, null);
                    }
                }
            }
        }, name, getSite());
    }

    @Override
    public void removeFromCache(SystemProperty systemProperty) {
        String site = "";
        if (systemProperty instanceof SiteDiscriminator && ((SiteDiscriminator) systemProperty).getSiteDiscriminator() != null) {
            site = String.valueOf(((SiteDiscriminator) systemProperty).getSiteDiscriminator());
        }
        super.removeItemFromCache("ucSystemPropertyNullCheckCache", systemProperty.getName(), site);
    }

    @Override
    public SystemProperty createNewSystemProperty() {
        return (SystemProperty) entityConfiguration.createEntityInstance(SystemProperty.class.getName());
    }

    @Override
    protected Log getLogger() {
        return LOG;
    }

    protected String getSite() {
        String site = "";
        UltraRequestContext brc = UltraRequestContext.getUltraRequestContext();
        if (brc != null) {
            if (brc.getSite() != null) {
                site = String.valueOf(brc.getSite().getId());
            }
        }
        return site;
    }

    @Override
    protected synchronized SystemProperty getNullObject(final Class<SystemProperty> responseClass) {
        if (nullObject == null) {
            nullObject = new NullSystemProperty();
        }
        return nullObject;
    }

}
