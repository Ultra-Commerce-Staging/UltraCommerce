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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.persistence.EntityConfiguration;
import com.ultracommerce.common.time.SystemTime;
import com.ultracommerce.common.util.dao.TypedQueryBuilder;
import com.ultracommerce.core.offer.domain.OfferAudit;
import com.ultracommerce.core.offer.domain.OfferAuditImpl;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.domain.OrderImpl;
import com.ultracommerce.core.order.service.type.OrderStatus;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Repository("ucOfferAuditDao")
public class OfferAuditDaoImpl implements OfferAuditDao {
    
    protected static final Log LOG = LogFactory.getLog(OfferAuditDaoImpl.class);

    private static final Long NULL_ACCOUNT_ID = null;
    private static final Long NULL_CUSTOMER_ID = null;

    @PersistenceContext(unitName="ucPU")
    protected EntityManager em;

    @Resource(name="ucEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    protected Long currentDateResolution = 3600000L;
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

    @Override
    public OfferAudit create() {
        return ((OfferAudit) entityConfiguration.createEntityInstance(OfferAudit.class.getName()));
    }

    @Override
    public void delete(final OfferAudit offerAudit) {
        OfferAudit loa = offerAudit;
        
        if (!em.contains(loa)) {
            loa = readAuditById(offerAudit.getId());
        }
        
        em.remove(loa);
    }

    @Override
    public OfferAudit save(final OfferAudit offerAudit) {
        return em.merge(offerAudit);
    }

    @Override
    public OfferAudit readAuditById(final Long offerAuditId) {
        return em.find(OfferAuditImpl.class, offerAuditId);
    }

    @Override
    public Long countUsesByCustomer(Order order, Long customerId, Long offerId) {
        return countUsesByCustomer(order, customerId, offerId, null);
    }

    @Override
    public Long countUsesByAccount(Order order, Long accountId, Long offerId) {
        return countUsesByAccount(order, accountId, offerId, null);
    }

    @Override
    public Long countUsesByAccount(Order order, Long accountId, Long offerId, Long minimumDaysPerUsage) {
        return countUsesByAccountOrCustomer(order, NULL_CUSTOMER_ID, accountId, offerId, minimumDaysPerUsage);
    }

    @Override
    public Long countUsesByCustomer(Order order, Long customerId, Long offerId, Long minimumDaysPerUsage) {
        return countUsesByAccountOrCustomer(order, customerId, NULL_ACCOUNT_ID, offerId, minimumDaysPerUsage);
    }

    protected Long countUsesByAccountOrCustomer(Order order, Long customerId, Long accountId, Long offerId, Long minimumDaysPerUsage) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<OfferAuditImpl> root = criteria.from(OfferAuditImpl.class);
        Root<OrderImpl> orderRoot = criteria.from(OrderImpl.class);
        criteria.select(builder.count(root));

        Predicate customerOrAccountPredicate = null;

        if (customerId != null) {
            customerOrAccountPredicate = builder.equal(root.get("customerId"), customerId);
        } else if (accountId != null) {
            customerOrAccountPredicate = builder.equal(root.get("accountId"), accountId);
        } else {
            LOG.debug("Count uses by account or customer called without an account or a customer.");
            return 0L;
        }

        List<Predicate> restrictions = new ArrayList<>();
        restrictions.add(
            builder.and(
                customerOrAccountPredicate, 
                builder.equal(root.get("offerId"), offerId),
                builder.or(
                    builder.isNull(root.get("orderId")),
                    builder.and(
                        builder.notEqual(root.get("orderId"), getOrderId(order)),
                        builder.notEqual(orderRoot.get("status"),OrderStatus.CANCELLED.getType()),
                        builder.equal(orderRoot.get("id"),root.get("orderId"))
                    )
                )
            )
        );

        if (minimumDaysPerUsage != null && minimumDaysPerUsage != 0L) {
            Date currentDate = getCurrentDateAfterFactoringInDateResolution();

            Calendar previousCalendar = new GregorianCalendar();

            previousCalendar.setTime(currentDate);
            previousCalendar.add(Calendar.DAY_OF_YEAR, -minimumDaysPerUsage.intValue());

            restrictions.add(builder.between(root.<Date>get("redeemedDate"), previousCalendar.getTime(), currentDate));
        }

        criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));

        try {
            return em.createQuery(criteria).getSingleResult();
        } catch (Exception e) {
            LOG.error("Error counting offer uses by customer.", e);
            return null;
        }
    }
    
    protected Long getOrderId(Order order) {
        return order.getId();
    }
    
    @Deprecated
    @Override
    public Long countUsesByCustomer(Long customerId, Long offerId) {
        TypedQuery<Long> query = new TypedQueryBuilder<>(OfferAudit.class, "offerAudit")
                .addRestriction("offerAudit.customerId", "=", customerId)
                .addRestriction("offerAudit.offerId", "=", offerId)
                .toCountQuery(em);

        return query.getSingleResult();
    }

    @Override
    public Long countOfferCodeUses(Order order, Long offerCodeId) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<OfferAuditImpl> root = criteria.from(OfferAuditImpl.class);
        Root<OrderImpl> orderRoot = criteria.from(OrderImpl.class);
        criteria.select(builder.count(root));

        List<Predicate> restrictions = new ArrayList<>();
        restrictions.add(
            builder.and(
                builder.or(
                    builder.notEqual(root.get("orderId"),  getOrderId(order)),
                    builder.isNull(root.get("orderId"))
                ),
                builder.equal(root.get("offerCodeId"), offerCodeId),
                builder.or(
                        builder.isNull(root.get("orderId")),
                        builder.and(
                                builder.notEqual(orderRoot.get("status"),OrderStatus.CANCELLED.getType()),
                                builder.equal(orderRoot.get("id"),root.get("orderId"))
                        )
                )
            )
        );

        criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
        
        try {
            return em.createQuery(criteria).getSingleResult();
        } catch (Exception e) {
            LOG.error("Error counting offer code uses.", e);
            return null;
        }
    }
    
    @Deprecated
    @Override
    public Long countOfferCodeUses(Long offerCodeId) {
        TypedQuery<Long> query = new TypedQueryBuilder<>(OfferAudit.class, "offerAudit")
                .addRestriction("offerAudit.offerCodeId", "=", offerCodeId)
                .toCountQuery(em);

        return query.getSingleResult();
    }

    @Override
    public List<OfferAudit> readOfferAuditsByOrderId(Long orderId) {
        TypedQuery<OfferAudit> query = new TypedQueryBuilder<>(OfferAudit.class, "offerAudit")
                .addRestriction("offerAudit.orderId", "=", orderId)
                .toQuery(em);
        
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

}
