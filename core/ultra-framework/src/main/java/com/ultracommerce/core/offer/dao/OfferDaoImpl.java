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

import com.ultracommerce.common.persistence.EntityConfiguration;
import com.ultracommerce.common.persistence.Status;
import com.ultracommerce.common.time.SystemTime;
import com.ultracommerce.core.offer.domain.CandidateFulfillmentGroupOffer;
import com.ultracommerce.core.offer.domain.CandidateItemOffer;
import com.ultracommerce.core.offer.domain.CandidateOrderOffer;
import com.ultracommerce.core.offer.domain.FulfillmentGroupAdjustment;
import com.ultracommerce.core.offer.domain.Offer;
import com.ultracommerce.core.offer.domain.OfferImpl;
import com.ultracommerce.core.offer.domain.OfferInfo;
import com.ultracommerce.core.offer.domain.OrderAdjustment;
import com.ultracommerce.core.offer.domain.OrderItemAdjustment;
import com.ultracommerce.core.offer.domain.OrderItemPriceDetailAdjustment;
import com.ultracommerce.core.offer.domain.ProratedOrderItemAdjustment;
import org.hibernate.jpa.QueryHints;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class OfferDaoImpl implements OfferDao {

    @PersistenceContext(unitName="ucPU")
    protected EntityManager em;

    @Resource(name="ucEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

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

    @Override
    public Offer create() {
        return ((Offer) entityConfiguration.createEntityInstance(Offer.class.getName()));
    }

    @Override
    public OfferInfo createOfferInfo() {
        return ((OfferInfo) entityConfiguration.createEntityInstance(OfferInfo.class.getName()));
    }

    @Override
    public CandidateOrderOffer createCandidateOrderOffer() {
        return ((CandidateOrderOffer) entityConfiguration.createEntityInstance(CandidateOrderOffer.class.getName()));
    }

    @Override
    public CandidateItemOffer createCandidateItemOffer() {
        return ((CandidateItemOffer) entityConfiguration.createEntityInstance(CandidateItemOffer.class.getName()));
    }

    @Override
    public CandidateFulfillmentGroupOffer createCandidateFulfillmentGroupOffer() {
        return ((CandidateFulfillmentGroupOffer) entityConfiguration.createEntityInstance(CandidateFulfillmentGroupOffer.class.getName()));
    }

    @Override
    public OrderItemAdjustment createOrderItemAdjustment() {
        return ((OrderItemAdjustment) entityConfiguration.createEntityInstance(OrderItemAdjustment.class.getName()));
    }

    @Override
    public OrderItemPriceDetailAdjustment createOrderItemPriceDetailAdjustment() {
        return ((OrderItemPriceDetailAdjustment) entityConfiguration.createEntityInstance(OrderItemPriceDetailAdjustment.class.getName()));
    }

    @Override
    public OrderAdjustment createOrderAdjustment() {
        return ((OrderAdjustment) entityConfiguration.createEntityInstance(OrderAdjustment.class.getName()));
    }

    @Override
    public FulfillmentGroupAdjustment createFulfillmentGroupAdjustment() {
        return ((FulfillmentGroupAdjustment) entityConfiguration.createEntityInstance(FulfillmentGroupAdjustment.class.getName()));
    }

    @Override
    public void delete(Offer offer) {
        ((Status) offer).setArchived('Y');
        em.merge(offer);
    }

    @Override
    public void delete(OfferInfo offerInfo) {
        if (!em.contains(offerInfo)) {
            offerInfo = (OfferInfo) em.find(entityConfiguration.lookupEntityClass(OfferInfo.class.getName()), offerInfo.getId());
        }
        em.remove(offerInfo);
    }

    @Override
    public Offer save(Offer offer) {
        return em.merge(offer);
    }

    @Override
    public OfferInfo save(OfferInfo offerInfo) {
        return em.merge(offerInfo);
    }

    @Override
    public ProratedOrderItemAdjustment save(ProratedOrderItemAdjustment adjustment) {
        return em.merge(adjustment);
    }

    @Override
    public List<Offer> readAllOffers() {
        Query query = em.createNamedQuery("UC_READ_ALL_OFFERS");
        return query.getResultList();
    }

    @Override
    public Offer readOfferById(Long offerId) {
        return em.find(OfferImpl.class, offerId);
    }

    @Override
    public List<Offer> readOffersByAutomaticDeliveryType() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Offer> criteria = builder.createQuery(Offer.class);
        Root<OfferImpl> root = criteria.from(OfferImpl.class);
        criteria.select(root);

        List<Predicate> restrictions = new ArrayList<>();
        Date myDate = getCurrentDateAfterFactoringInDateResolution();

        Calendar c = Calendar.getInstance();
        c.setTime(myDate);
        c.add(Calendar.DATE, +1);
        restrictions.add(builder.lessThan(root.get("startDate"), c.getTime()));

        c = Calendar.getInstance();
        c.setTime(myDate);
        c.add(Calendar.DATE, -1);
        restrictions.add(
                builder.or(
                        builder.isNull(root.get("endDate")),
                        builder.greaterThan(root.get("endDate"), c.getTime())));

        restrictions.add(
                builder.or(
                        builder.equal(root.get("archiveStatus").get("archived"), 'N'),
                        builder.isNull(root.get("archiveStatus").get("archived"))));

        restrictions.add(builder.equal(root.get("automaticallyAdded"), true));

        criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
        TypedQuery<Offer> query = em.createQuery(criteria);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Offer");

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
