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

package com.ultracommerce.core.catalog.dao;

import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.common.logging.SupportLogManager;
import com.ultracommerce.common.logging.SupportLogger;
import com.ultracommerce.common.persistence.EntityConfiguration;
import com.ultracommerce.common.sandbox.SandBoxHelper;
import com.ultracommerce.common.time.SystemTime;
import com.ultracommerce.common.util.DateUtil;
import com.ultracommerce.common.util.dao.TypedQueryBuilder;
import com.ultracommerce.core.catalog.domain.Sku;
import com.ultracommerce.core.catalog.domain.SkuFee;
import com.ultracommerce.core.catalog.domain.SkuImpl;
import org.hibernate.jpa.QueryHints;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * {@inheritDoc}
 *
 * @author Jeff Fischer
 */
public class SkuDaoImpl implements SkuDao {

    private static final SupportLogger logger = SupportLogManager.getLogger("Enterprise", SkuDaoImpl.class);

    @PersistenceContext(unitName = "ucPU")
    protected EntityManager em;

    @Resource(name = "ucEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    @Resource(name = "ucSandBoxHelper")
    protected SandBoxHelper sandBoxHelper;

    @Resource(name = "ucSkuDaoExtensionManager")
    protected SkuDaoExtensionManager extensionManager;

    protected Long currentDateResolution;
    protected Date cachedDate = SystemTime.asDate();

    @Override
    public Sku save(Sku sku) {
        return em.merge(sku);
    }

    @Override
    public SkuFee saveSkuFee(SkuFee fee) {
        return em.merge(fee);
    }

    @Override
    public Sku readSkuById(Long skuId) {
        return (Sku) em.find(SkuImpl.class, skuId);
    }

    @Override
    public Sku readSkuByExternalId(String externalId) {
        TypedQuery<Sku> query = new TypedQueryBuilder<Sku>(Sku.class, "sku")
                .addRestriction("sku.externalId", "=", externalId)
                .toQuery(em);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Sku readSkuByUpc(String upc) {
        TypedQuery<Sku> query = new TypedQueryBuilder<Sku>(Sku.class, "sku")
                .addRestriction("sku.upc", "=", upc)
                .toQuery(em);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Sku readFirstSku() {
        TypedQuery<Sku> query = em.createNamedQuery("UC_READ_FIRST_SKU", Sku.class);
        return query.getSingleResult();
    }

    @Override
    public List<Sku> readAllSkus() {
        TypedQuery<Sku> query = em.createNamedQuery("UC_READ_ALL_SKUS", Sku.class);
        //don't cache - could take up too much memory
        return query.getResultList();
    }

    @Override
    public List<Sku> readAllSkus(int offset, int limit) {
        TypedQuery<Sku> query = em.createNamedQuery("UC_READ_ALL_SKUS", Sku.class);
        //don't cache - could take up too much memory
        query.setFirstResult(offset);
        query.setMaxResults(limit);

        return query.getResultList();
    }

    @Override
    public List<Sku> readSkusByIds(List<Long> skuIds) {
        if (skuIds == null || skuIds.size() == 0) {
            return null;
        }
        if (skuIds.size() > 100) {
            logger.warn("Not recommended to use the readSkusByIds method for long lists of skuIds, since " +
                    "Hibernate is required to transform the distinct results. The list of requested" +
                    "sku ids was (" + skuIds.size() + ") in length.");
        }
        // Set up the criteria query that specifies we want to return Products
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Sku> criteria = builder.createQuery(Sku.class);
        Root<SkuImpl> sku = criteria.from(SkuImpl.class);

        criteria.select(sku);

        // We only want results that match the sku IDs
        criteria.where(sku.get("id").as(Long.class).in(
                sandBoxHelper.mergeCloneIds(SkuImpl.class,
                        skuIds.toArray(new Long[skuIds.size()]))));

        TypedQuery<Sku> query = em.createQuery(criteria);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Catalog");

        return query.getResultList();
    }

    @Override
    public void delete(Sku sku) {
        if (!em.contains(sku)) {
            sku = readSkuById(sku.getId());
        }
        em.remove(sku);
    }

    @Override
    public Sku create() {
        return (Sku) entityConfiguration.createEntityInstance(Sku.class.getName());
    }

    @Override
    public Long readCountAllActiveSkus() {
        Date currentDate = DateUtil.getCurrentDateAfterFactoringInDateResolution(cachedDate, getCurrentDateResolution());
        return readCountAllActiveSkusInternal(currentDate);
    }

    protected Long readCountAllActiveSkusInternal(Date currentDate) {
        // Set up the criteria query that specifies we want to return a Long
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);

        // The root of our search is sku
        Root<SkuImpl> sku = criteria.from(SkuImpl.class);

        // We want the count of products
        criteria.select(builder.count(sku));

        // Ensure the sku is currently active
        List<Predicate> restrictions = new ArrayList<Predicate>();

        // Add the active start/end date restrictions
        restrictions.add(builder.lessThan(sku.get("activeStartDate").as(Date.class), currentDate));
        restrictions.add(builder.or(
                builder.isNull(sku.get("activeEndDate")),
                builder.greaterThan(sku.get("activeEndDate").as(Date.class), currentDate)));

        // Add the restrictions to the criteria query
        criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));

        TypedQuery<Long> query = em.createQuery(criteria);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Catalog");

        return query.getSingleResult();
    }

    @Override
    public List<Sku> readAllActiveSkus(int page, int pageSize) {
        Date currentDate = DateUtil.getCurrentDateAfterFactoringInDateResolution(cachedDate, getCurrentDateResolution());
        return readAllActiveSkusInternal(page, pageSize, currentDate);
    }

    @Override
    public List<Sku> readAllActiveSkus(Integer pageSize, Long lastId) {
        Date currentDate = DateUtil.getCurrentDateAfterFactoringInDateResolution(cachedDate, getCurrentDateResolution());
        return readAllActiveSkusInternal(pageSize, currentDate, lastId);
    }

    @Override
    public Long getCurrentDateResolution() {
        return currentDateResolution;
    }

    @Override
    public void setCurrentDateResolution(Long currentDateResolution) {
        this.currentDateResolution = currentDateResolution;
    }

    @Override
    public List<Sku> findSkuByURI(String uri) {
        if (extensionManager != null) {
            ExtensionResultHolder holder = new ExtensionResultHolder();
            ExtensionResultStatusType result = extensionManager.getProxy().findSkuByURI(uri, holder);
            if (ExtensionResultStatusType.HANDLED.equals(result)) {
                return (List<Sku>) holder.getResult();
            }
        }
        String skuUrlKey = uri.substring(uri.lastIndexOf('/'));
        String productUrl = uri.substring(0, uri.lastIndexOf('/'));
        Query query;

        query = em.createNamedQuery("UC_READ_SKU_BY_OUTGOING_URL");
        query.setParameter("url", uri);
        query.setParameter("productUrl", productUrl);
        query.setParameter("skuUrlKey", skuUrlKey);
        query.setParameter("currentDate", DateUtil.getCurrentDateAfterFactoringInDateResolution(cachedDate, getCurrentDateResolution()));
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Catalog");

        @SuppressWarnings("unchecked")
        List<Sku> results = query.getResultList();
        return results;
    }

    protected List<Sku> readAllActiveSkusInternal(int page, int pageSize, Date currentDate) {
        CriteriaQuery<Sku> criteria = getCriteriaForActiveSkus(currentDate);
        int firstResult = page * pageSize;
        TypedQuery<Sku> query = em.createQuery(criteria);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Catalog");

        return query.setFirstResult(firstResult).setMaxResults(pageSize).getResultList();
    }

    protected List<Sku> readAllActiveSkusInternal(Integer pageSize, Date currentDate, Long lastId) {
        CriteriaQuery<Sku> criteria = getCriteriaForActiveSkus(currentDate, lastId);
        TypedQuery<Sku> query = em.createQuery(criteria);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Catalog");

        return query.setMaxResults(pageSize).getResultList();
    }

    protected CriteriaQuery<Sku> getCriteriaForActiveSkus(Date currentDate) {
        return getCriteriaForActiveSkus(currentDate, null);
    }

    protected CriteriaQuery<Sku> getCriteriaForActiveSkus(Date currentDate, Long lastId) {
        // Set up the criteria query that specifies we want to return Products
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Sku> criteria = builder.createQuery(Sku.class);

        // The root of our search is Product
        Root<SkuImpl> sku = criteria.from(SkuImpl.class);

        // Product objects are what we want back
        criteria.select(sku);

        // Ensure the product is currently active
        List<Predicate> restrictions = new ArrayList<Predicate>();

        // Add the active start/end date restrictions
        restrictions.add(builder.lessThan(sku.get("activeStartDate").as(Date.class), currentDate));
        restrictions.add(builder.or(
                builder.isNull(sku.get("activeEndDate")),
                builder.greaterThan(sku.get("activeEndDate").as(Date.class), currentDate)));
        if (lastId != null) {
            restrictions.add(builder.gt(sku.get("id").as(Long.class), lastId));
        }

        // Add the restrictions to the criteria query
        criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));

        //Add ordering so that paginated queries are consistent
        criteria.orderBy(builder.asc(sku.get("id")));
        return criteria;
    }
}
