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
import com.ultracommerce.common.persistence.EntityConfiguration;
import com.ultracommerce.common.persistence.Status;
import com.ultracommerce.common.sandbox.SandBoxHelper;
import com.ultracommerce.common.time.SystemTime;
import com.ultracommerce.common.util.dao.TypedQueryBuilder;
import com.ultracommerce.core.catalog.domain.Category;
import com.ultracommerce.core.catalog.domain.CategoryImpl;
import com.ultracommerce.core.catalog.domain.Product;
import org.hibernate.jpa.QueryHints;

import java.util.Date;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

/**
 * 
 * @author Jeff Fischer
 */

public class CategoryDaoImpl implements CategoryDao {

    protected Long currentDateResolution;
    protected Date cachedDate = SystemTime.asDate();

    protected Date getCurrentDateAfterFactoringInDateResolution() {
        Date returnDate = SystemTime.getCurrentDateWithinTimeResolution(cachedDate, getCurrentDateResolution());
        if (returnDate != cachedDate) {
            if (SystemTime.shouldCacheDate()) {
                cachedDate = returnDate;
            }
        }
        return returnDate;
    }

    @PersistenceContext(unitName="ucPU")
    protected EntityManager em;

    @Resource(name="ucEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    @Resource(name = "ucSandBoxHelper")
    protected SandBoxHelper sandBoxHelper;

    @Resource(name = "ucCategoryDaoExtensionManager")
    protected CategoryDaoExtensionManager extensionManager;

    @Override
    public Category save(Category category) {
        return em.merge(category);
    }

    @Override
    public Category readCategoryById(Long categoryId) {
        return em.find(CategoryImpl.class, categoryId);
    }

    @Override
    public List<Category> readCategoriesByIds(List<Long> categoryIds) {
        TypedQuery<Category> query = em.createQuery(
                "select category from com.ultracommerce.core.catalog.domain.Category category "
                        + "where category.id in :ids",
                Category.class);
        query.setParameter("ids", sandBoxHelper.mergeCloneIds(CategoryImpl.class,
                categoryIds.toArray(new Long[categoryIds.size()])));
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Catalog");

        return query.getResultList();
    }

    @Override
    public Category readCategoryByExternalId(@Nonnull String externalId) {
        TypedQuery<Category> query = new TypedQueryBuilder<Category>(Category.class, "cat")
                .addRestriction("cat.externalId", "=", externalId)
                .toQuery(em);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    @Deprecated
    public Category readCategoryByName(String categoryName) {
        Query query = em.createNamedQuery("UC_READ_CATEGORY_BY_NAME");
        query.setParameter("categoryName", categoryName);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Catalog");
        return (Category) query.getSingleResult();
    }

    @Override
    public List<Category> readCategoriesByName(String categoryName) {
        TypedQuery<Category> query = em.createNamedQuery("UC_READ_CATEGORY_BY_NAME", Category.class);
        query.setParameter("categoryName", categoryName);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Catalog");
        return query.getResultList();
    }

    @Override
    public List<Category> readCategoriesByName(String categoryName, int limit, int offset) {
        TypedQuery<Category> query = em.createNamedQuery("UC_READ_CATEGORY_BY_NAME", Category.class);
        query.setParameter("categoryName", categoryName);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Catalog");
        query.setFirstResult(offset);
        query.setMaxResults(limit);

        return query.getResultList();
    }

    @Nonnull
    @Override
    public List<Category> readCategoriesByNames(List<String> names) {
        TypedQuery<Category> query = em.createQuery(
                "select category from com.ultracommerce.core.catalog.domain.Category category "
                        + "where category.name in :names",
                Category.class);
        query.setParameter("names", names);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Catalog");

        return query.getResultList();
    }

    @Override
    public List<Category> readAllCategories() {
        TypedQuery<Category> query = em.createNamedQuery("UC_READ_ALL_CATEGORIES", Category.class);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Catalog");
        return query.getResultList();
    }

    @Override
    public List<Category> readAllCategories(int limit, int offset) {
        TypedQuery<Category> query = em.createNamedQuery("UC_READ_ALL_CATEGORIES", Category.class);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Catalog");

        return query.getResultList();
    }

    @Override
    public Long readTotalCategoryCount() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        criteria.select(builder.count(criteria.from(CategoryImpl.class)));

        TypedQuery<Long> query = em.createQuery(criteria);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Catalog");
        return query.getSingleResult();
    }

    @Override
    public List<Product> readAllProducts() {
        TypedQuery<Product> query = em.createNamedQuery("UC_READ_ALL_PRODUCTS", Product.class);
        //don't cache - could take up too much memory
        return query.getResultList();
    }

    @Override
    public List<Product> readAllProducts(int limit, int offset) {
        TypedQuery<Product> query = em.createNamedQuery("UC_READ_ALL_PRODUCTS", Product.class);
        //don't cache - could take up too much memory
        query.setFirstResult(offset);
        query.setMaxResults(limit);

        return query.getResultList();
    }

    @Override
    public List<Category> readAllSubCategories(Category category) {
        TypedQuery<Category> query = em.createNamedQuery("UC_READ_ALL_SUBCATEGORIES", Category.class);
        query.setParameter("parentCategoryId", sandBoxHelper.mergeCloneIds(CategoryImpl.class, category.getId()));
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Catalog");

        return query.getResultList();
    }

    @Override
    public List<Category> readAllSubCategories(Category category, int limit, int offset) {
        TypedQuery<Category> query = em.createNamedQuery("UC_READ_ALL_SUBCATEGORIES", Category.class);
        query.setParameter("parentCategoryId", sandBoxHelper.mergeCloneIds(CategoryImpl.class, category.getId()));
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Catalog");
        query.setFirstResult(offset);
        query.setMaxResults(limit);

        return query.getResultList();
    }

    @Override
    public List<Category> readActiveSubCategoriesByCategory(Category category) {
        TypedQuery<Category> query = em.createNamedQuery("UC_READ_ACTIVE_SUBCATEGORIES_BY_CATEGORY", Category.class);
        query.setParameter("parentCategoryId", sandBoxHelper.mergeCloneIds(CategoryImpl.class, category.getId()));
        query.setParameter("currentDate", getCurrentDateAfterFactoringInDateResolution());
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Catalog");

        return query.getResultList();
    }

    @Override
    public List<Category> readActiveSubCategoriesByCategory(Category category, int limit, int offset) {
        TypedQuery<Category> query = em.createNamedQuery("UC_READ_ACTIVE_SUBCATEGORIES_BY_CATEGORY", Category.class);
        query.setParameter("parentCategoryId", sandBoxHelper.mergeCloneIds(CategoryImpl.class, category.getId()));
        query.setParameter("currentDate", getCurrentDateAfterFactoringInDateResolution());
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Catalog");
        query.setFirstResult(offset);
        query.setMaxResults(limit);

        return query.getResultList();
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
    public void delete(Category category) {
        ((Status) category).setArchived('Y');
        em.merge(category);
    }

    @Override
    public Category create() {
        return (Category) entityConfiguration.createEntityInstance(Category.class.getName());
    }

    @Override
    public Category findCategoryByURI(String uri) {
        if (extensionManager != null) {
            ExtensionResultHolder holder = new ExtensionResultHolder();
            ExtensionResultStatusType result = extensionManager.getProxy().findCategoryByURI(uri, holder);
            if (ExtensionResultStatusType.HANDLED.equals(result)) {
                return (Category) holder.getResult();
            }
        }
        Query query;
        query = em.createNamedQuery("UC_READ_CATEGORY_OUTGOING_URL");
        query.setParameter("currentDate", getCurrentDateAfterFactoringInDateResolution());
        query.setParameter("url", uri);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Catalog");

        @SuppressWarnings("unchecked")
        List<Category> results = query.getResultList();
        if (results != null && !results.isEmpty()) {
            return results.get(0);

        } else {
            return null;
        }
    }

    @Override
    public Long readCountAllActiveProductsByCategory(Category category) {
        TypedQuery<Long> query = em.createNamedQuery("UC_READ_COUNT_ALL_ACTIVE_PRODUCTS_BY_CATEGORY", Long.class);
        query.setParameter("categoryId", category.getId());
        query.setParameter("currentDate", getCurrentDateAfterFactoringInDateResolution());
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Catalog");

        return query.getSingleResult();
    }
}
