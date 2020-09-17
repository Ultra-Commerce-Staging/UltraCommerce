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
package com.ultracommerce.core.search.dao;

import com.ultracommerce.common.persistence.EntityConfiguration;
import com.ultracommerce.common.persistence.Status;
import com.ultracommerce.core.catalog.domain.ProductImpl;
import com.ultracommerce.core.catalog.domain.Sku;
import com.ultracommerce.core.search.domain.Field;
import com.ultracommerce.core.search.domain.FieldEntity;
import com.ultracommerce.core.search.domain.SearchFacet;
import com.ultracommerce.core.search.domain.SearchFacetImpl;
import com.ultracommerce.core.search.domain.SearchFacetRange;
import com.ultracommerce.core.search.domain.SearchFacetRangeImpl;
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
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Repository("ucSearchFacetDao")
public class SearchFacetDaoImpl implements SearchFacetDao {

    @PersistenceContext(unitName = "ucPU")
    protected EntityManager em;
    
    @Resource(name="ucEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    @Override
    public List<SearchFacet> readAllSearchFacets(FieldEntity entityType) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<SearchFacet> criteria = builder.createQuery(SearchFacet.class);

        Root<SearchFacetImpl> facet = criteria.from(SearchFacetImpl.class);

        criteria.select(facet);

        Path<Character> archived = facet.get("archiveStatus").get("archived");

        criteria.where(
                builder.equal(facet.get("showOnSearch").as(Boolean.class), true),
                builder.or(builder.isNull(archived.as(String.class)),
                           builder.notEqual(archived.as(Character.class), 'Y')),
                facet.join("fieldType")
                        .join("indexField")
                        .join("field")
                        .get("entityType")
                        .as(String.class)
                        .in(entityType.getAllLookupTypes())
        );

        TypedQuery<SearchFacet> query = em.createQuery(criteria);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Search");

        return query.getResultList();
    }
    
    @Override
    public <T> List<T> readDistinctValuesForField(String fieldName, Class<T> fieldValueClass) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(fieldValueClass);
        
        Root<ProductImpl> product = criteria.from(ProductImpl.class);
        Path<Sku> sku = product.get("defaultSku");
        
        Path<?> pathToUse;
        if (fieldName.contains("defaultSku.")) {
            pathToUse = sku;
            fieldName = fieldName.substring("defaultSku.".length());
        } else if (fieldName.contains("productAttributes.")) {
            pathToUse = product.join("productAttributes");
            
            fieldName = fieldName.substring("productAttributes.".length());
            criteria.where(builder.equal(
                builder.lower(pathToUse.get("name").as(String.class)), fieldName.toLowerCase()));
            
            fieldName = "value";
        } else if (fieldName.contains("product.")) {
            pathToUse = product;
            fieldName = fieldName.substring("product.".length());
        } else {
            throw new IllegalArgumentException("Invalid facet fieldName specified: " + fieldName);
        }
        
        criteria.where(pathToUse.get(fieldName).as(fieldValueClass).isNotNull());
        criteria.distinct(true).select(pathToUse.get(fieldName).as(fieldValueClass));

        TypedQuery<T> query = em.createQuery(criteria);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Search");
        
        return query.getResultList();
    }

    @Override
    public SearchFacet save(SearchFacet searchFacet) {
        return em.merge(searchFacet);
    }

    @Override
    public SearchFacet readSearchFacetForField(Field field) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<SearchFacet> criteria = builder.createQuery(SearchFacet.class);

        Root<SearchFacetImpl> facet = criteria.from(SearchFacetImpl.class);

        criteria.select(facet);
        criteria.where(
                builder.equal(facet.join("fieldType").join("indexField").join("field").get("id").as(Long.class), field.getId())
        );

        TypedQuery<SearchFacet> query = em.createQuery(criteria);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Search");

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<SearchFacetRange> readSearchFacetRangesForSearchFacet(SearchFacet searchFacet) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<SearchFacetRange> criteria = builder.createQuery(SearchFacetRange.class);

        Root<SearchFacetRangeImpl> ranges = criteria.from(SearchFacetRangeImpl.class);
        criteria.select(ranges);
        Predicate facetRestriction = builder.equal(ranges.get("searchFacet"), searchFacet);

        if (isSearchFacetRangeArchivable()) {
            criteria.where(
                    builder.and(
                        facetRestriction,
                        builder.or(builder.isNull(ranges.get("archiveStatus").get("archived").as(String.class)), 
                                   builder.notEqual(ranges.get("archiveStatus").get("archived").as(Character.class), 'Y'))
                    )
            );
        } else {
            criteria.where(facetRestriction);
        }

        TypedQuery<SearchFacetRange> query = em.createQuery(criteria);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Search");

        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }
    
    protected boolean isSearchFacetRangeArchivable() {
        return Status.class.isAssignableFrom(SearchFacetRangeImpl.class);
    }
}
