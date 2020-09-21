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
import com.ultracommerce.core.search.domain.Field;
import com.ultracommerce.core.search.domain.FieldEntity;
import com.ultracommerce.core.search.domain.FieldImpl;
import org.hibernate.jpa.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository("ucFieldDao")
public class FieldDaoImpl implements FieldDao {

    @PersistenceContext(unitName = "ucPU")
    protected EntityManager em;
    
    @Resource(name="ucEntityConfiguration")
    protected EntityConfiguration entityConfiguration;
    
    @Override
    public Field readFieldByAbbreviation(String abbreviation) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Field> criteria = builder.createQuery(Field.class);
        
        Root<FieldImpl> root = criteria.from(FieldImpl.class);
        
        criteria.select(root);
        criteria.where(
            builder.equal(root.get("abbreviation").as(String.class), abbreviation)
        );

        TypedQuery<Field> query = em.createQuery(criteria);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Catalog");

        try {
            return query.getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            //must not be an abbreviation
            return null;
        }
    }
    
    @Override
    public List<Field> readAllProductFields() {
        return readFieldsByEntityType(FieldEntity.PRODUCT);

    }

    @Override
    public List<Field> readAllSkuFields() {
        return readFieldsByEntityType(FieldEntity.SKU);
    }

    @Override
    public List<Field> readFieldsByEntityType(FieldEntity entityType) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Field> criteria = builder.createQuery(Field.class);

        Root<FieldImpl> root = criteria.from(FieldImpl.class);

        criteria.select(root);
        criteria.where(
                root.get("entityType").as(String.class).in(entityType.getAllLookupTypes())
                );

        TypedQuery<Field> query = em.createQuery(criteria);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Catalog");

        return query.getResultList();
    }

    @Override
    public Field save(Field field) {
        return em.merge(field);
    }
}
