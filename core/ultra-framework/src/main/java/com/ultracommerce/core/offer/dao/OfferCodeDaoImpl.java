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
package com.ultracommerce.core.offer.dao;

import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.common.persistence.EntityConfiguration;
import com.ultracommerce.core.offer.domain.OfferCode;
import com.ultracommerce.core.offer.domain.OfferCodeImpl;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.domain.OrderImpl;
import org.hibernate.jpa.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

@Repository("ucOfferCodeDao")
public class OfferCodeDaoImpl implements OfferCodeDao {

    @PersistenceContext(unitName="ucPU")
    protected EntityManager em;

    @Resource(name="ucEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    @Resource(name = "ucOfferCodeDaoExtensionManager")
    protected OfferCodeDaoExtensionManager extensionManager;

    @Override
    public OfferCode create() {
        return ((OfferCode) entityConfiguration.createEntityInstance(OfferCode.class.getName()));
    }

    @Override
    public void delete(OfferCode offerCode) {
        if (!em.contains(offerCode)) {
            offerCode = readOfferCodeById(offerCode.getId());
        }
        em.remove(offerCode);
    }

    @Override
    public OfferCode save(OfferCode offerCode) {
        return em.merge(offerCode);
    }

    @Override
    public OfferCode readOfferCodeById(Long offerCodeId) {
        return em.find(OfferCodeImpl.class, offerCodeId);
    }

    @Override
    public List<OfferCode> readOfferCodesByIds(Collection<Long> offerCodeIds) {
        TypedQuery<OfferCode> query = em.createQuery("SELECT code FROM " + OfferCode.class.getName() + " code WHERE code.id in :offerCodeIds", OfferCode.class);
        query.setParameter("offerCodeIds", offerCodeIds);
        return query.getResultList();
    }

    @Override
    public Boolean offerCodeIsUsed(OfferCode code) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Order> criteria = builder.createQuery(Order.class);
        Root<OrderImpl> baseOrder = criteria.from(OrderImpl.class);
        criteria.select(baseOrder);
        Join<OrderImpl, OfferCodeImpl> join = baseOrder.join("addedOfferCodes");
        criteria.where(builder.equal(join.get("id"), code.getId()));
        TypedQuery<Order> query = em.createQuery(criteria);
        try {
            query.getSingleResult();
        } catch (NoResultException e) {
            return false;
        }
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public OfferCode readOfferCodeByCode(String code) {
        OfferCode offerCode = null;

        Query query = readOfferCodesQuery(code);
        List<OfferCode> result = query.getResultList();
        if (result.size() > 0) {
            offerCode = result.get(0);
        }

        return offerCode;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<OfferCode> readAllOfferCodesByCode(String code) {
        Query query = readOfferCodesQuery(code);

        return query.getResultList();
    }

    protected Query readOfferCodesQuery(String code) {
        Query query;

        ExtensionResultHolder<Query> resultHolder = new ExtensionResultHolder<Query>();
        ExtensionResultStatusType extensionResult =
                extensionManager.getProxy().createReadOfferCodeByCodeQuery(em, resultHolder, code, true, "query.Offer");

        if (extensionResult != null && ExtensionResultStatusType.HANDLED.equals(extensionResult)) {
            query = resultHolder.getResult();
        } else {
            query = em.createNamedQuery("UC_READ_OFFER_CODE_BY_CODE");
            query.setParameter("code", code);
            query.setHint(QueryHints.HINT_CACHEABLE, true);
            query.setHint(QueryHints.HINT_CACHE_REGION, "query.Offer");
        }
        return query;
    }

}
