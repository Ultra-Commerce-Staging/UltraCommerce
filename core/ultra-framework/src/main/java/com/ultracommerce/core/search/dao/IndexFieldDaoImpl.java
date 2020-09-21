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

import org.apache.commons.collections.CollectionUtils;
import com.ultracommerce.common.persistence.EntityConfiguration;
import com.ultracommerce.core.search.domain.Field;
import com.ultracommerce.core.search.domain.FieldEntity;
import com.ultracommerce.core.search.domain.IndexField;
import com.ultracommerce.core.search.domain.IndexFieldImpl;
import com.ultracommerce.core.search.domain.IndexFieldType;
import com.ultracommerce.core.search.domain.IndexFieldTypeImpl;
import com.ultracommerce.core.search.domain.solr.FieldType;
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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @author Nick Crum (ncrum)
 */
@Repository("ucIndexFieldDao")
public class IndexFieldDaoImpl implements IndexFieldDao {

    @PersistenceContext(unitName = "ucPU")
    protected EntityManager em;

    @Resource(name="ucEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    @Override
    public IndexField readIndexFieldForField(Field field) {
        return readIndexFieldByFieldId(field.getId());
    }

    @Override
    public IndexField readIndexFieldByFieldId(Long fieldId) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<IndexField> criteria = builder.createQuery(IndexField.class);

        Root<IndexFieldImpl> search = criteria.from(IndexFieldImpl.class);

        criteria.select(search);
        criteria.where(
                builder.equal(search.join("field").get("id").as(Long.class), fieldId)
        );

        TypedQuery<IndexField> query = em.createQuery(criteria);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Search");

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<IndexField> readAllIndexFieldsByFieldId(Long fieldId) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<IndexField> criteria = builder.createQuery(IndexField.class);

        Root<IndexFieldImpl> search = criteria.from(IndexFieldImpl.class);

        criteria.select(search);
        criteria.where(
                builder.equal(search.join("field").get("id").as(Long.class), fieldId)
        );

        TypedQuery<IndexField> query = em.createQuery(criteria);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Search");

        return query.getResultList();
    }

    @Override
    public List<IndexField> readFieldsByEntityType(FieldEntity entityType) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<IndexField> criteria = builder.createQuery(IndexField.class);

        Root<IndexFieldImpl> root = criteria.from(IndexFieldImpl.class);
        
        criteria.select(root);
        criteria.where(root.get("field").get("entityType").as(String.class).in(entityType.getAllLookupTypes()));

        TypedQuery<IndexField> query = em.createQuery(criteria);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Search");

        return query.getResultList();
    }

    @Override
    public List<IndexField> readSearchableFieldsByEntityType(FieldEntity entityType) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<IndexField> criteria = builder.createQuery(IndexField.class);

        Root<IndexFieldImpl> root = criteria.from(IndexFieldImpl.class);

        criteria.select(root);
        criteria.where(
                builder.equal(root.get("searchable").as(Boolean.class), Boolean.TRUE),
                root.get("field").get("entityType").as(String.class).in(entityType.getAllLookupTypes())
        );

        TypedQuery<IndexField> query = em.createQuery(criteria);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Search");

        return query.getResultList();
    }

    @Override
    public List<IndexFieldType> getIndexFieldTypesByAbbreviation(String abbreviation) {
        return getIndexFieldTypesByAbbreviationAndEntityType(abbreviation, null);
    }
    
    @Override
    public List<IndexFieldType> getIndexFieldTypesByAbbreviationAndEntityType(String abbreviation, FieldEntity entityType) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<IndexFieldType> criteria = builder.createQuery(IndexFieldType.class);

        Root<IndexFieldTypeImpl> root = criteria.from(IndexFieldTypeImpl.class);

        criteria.select(root);
        if (entityType == null) {
            criteria.where(
                builder.equal(root.get("indexField").get("field").get("abbreviation").as(String.class), abbreviation)
            );
        } else {
            criteria.where(
                    builder.and(
                        builder.equal(root.get("indexField").get("field").get("abbreviation").as(String.class), abbreviation),
                        builder.equal(root.get("indexField").get("field").get("entityType").as(String.class), entityType.getType())
                    )
            );
        }

        TypedQuery<IndexFieldType> query = em.createQuery(criteria);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Search");

        return query.getResultList();
    }


    @Override
    public List<IndexFieldType> getIndexFieldTypesByAbbreviationOrPropertyName(String name) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<IndexFieldType> criteria = builder.createQuery(IndexFieldType.class);

        Root<IndexFieldTypeImpl> root = criteria.from(IndexFieldTypeImpl.class);

        criteria.select(root);
        List<Predicate> restrictions = new ArrayList<>();
        restrictions.add(builder.or(
                builder.equal(root.get("indexField").get("field").get("abbreviation").as(String.class), name),
                builder.equal(root.get("indexField").get("field").get("propertyName").as(String.class), name)));
        restrictions.add(builder.or(
                builder.isNull(root.get("archiveStatus").get("archived")),
                builder.equal(root.get("archiveStatus").get("archived"), 'N')));
        criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));

        TypedQuery<IndexFieldType> query = em.createQuery(criteria);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Catalog");

        return query.getResultList();
    }

    @Override
    public List<IndexFieldType> getIndexFieldTypes(FieldType facetFieldType) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<IndexFieldType> criteria = builder.createQuery(IndexFieldType.class);

        Root<IndexFieldTypeImpl> root = criteria.from(IndexFieldTypeImpl.class);

        criteria.select(root);
        criteria.where(
                builder.equal(root.get("fieldType").as(String.class), facetFieldType.getType())
        );

        TypedQuery<IndexFieldType> query = em.createQuery(criteria);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Search");

        return query.getResultList();
    }

    @Override
    public IndexField readIndexFieldByAbbreviation(String abbreviation) {
        return readIndexFieldByAbbreviationAndEntityType(abbreviation, null);
    }
    
    @Override
    public IndexField readIndexFieldByAbbreviationAndEntityType(String abbreviation, FieldEntity entityType) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<IndexField> criteria = builder.createQuery(IndexField.class);

        Root<IndexFieldImpl> root = criteria.from(IndexFieldImpl.class);

        criteria.select(root);
        if (entityType == null) {
            criteria.where(
                builder.equal(root.get("field").get("abbreviation").as(String.class), abbreviation)
            );
        } else {
            criteria.where(
                    builder.and(
                        builder.equal(root.get("field").get("abbreviation").as(String.class), abbreviation),
                        builder.equal(root.get("field").get("entityType").as(String.class), entityType.getType())
                    )
            );
        }

        TypedQuery<IndexField> query = em.createQuery(criteria);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Search");

        List<IndexField> resultList = query.getResultList();
        return CollectionUtils.isNotEmpty(resultList) ? resultList.get(0) : null;
    }

}
