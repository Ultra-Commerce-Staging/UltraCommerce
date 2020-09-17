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
package com.ultracommerce.core.rating.dao;

import com.ultracommerce.common.persistence.EntityConfiguration;
import com.ultracommerce.common.util.dao.BatchRetrieveDao;
import com.ultracommerce.core.rating.domain.RatingDetail;
import com.ultracommerce.core.rating.domain.RatingSummary;
import com.ultracommerce.core.rating.domain.RatingSummaryImpl;
import com.ultracommerce.core.rating.domain.ReviewDetail;
import com.ultracommerce.core.rating.service.type.RatingType;
import com.ultracommerce.profile.core.domain.Customer;
import org.hibernate.jpa.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository("ucRatingSummaryDao")
public class RatingSummaryDaoImpl extends BatchRetrieveDao implements RatingSummaryDao {

    @PersistenceContext(unitName = "ucPU")
    protected EntityManager em;

    @Resource(name="ucEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    @Override
    public RatingSummary createSummary() {
        return entityConfiguration.createEntityInstance(RatingSummary.class.getName(), RatingSummary.class);
    }
    
    @Override
    public RatingSummary createSummary(String itemId, RatingType type) {
        RatingSummary summary = createSummary();
        summary.setItemId(itemId);
        summary.setRatingType(type);
        return summary;
    }
    
    @Override
    public RatingDetail createDetail() {
        return entityConfiguration.createEntityInstance(RatingDetail.class.getName(), RatingDetail.class);
    }
    
    @Override
    public RatingDetail createDetail(RatingSummary ratingSummary, Double rating, Date submittedDate, Customer customer) {
        RatingDetail detail = createDetail();
        detail.setRatingSummary(ratingSummary);
        detail.setRating(rating);
        detail.setRatingSubmittedDate(submittedDate);
        detail.setCustomer(customer);
        return detail;
    }
    
    @Override
    public void deleteRatingSummary(final RatingSummary summary) {
        RatingSummary lSummary = summary;
        if (!em.contains(lSummary)) {
            lSummary = em.find(RatingSummaryImpl.class, lSummary.getId());
        }
        em.remove(lSummary);
    }

    @Override
    public RatingSummary saveRatingSummary(final RatingSummary summary) {
        summary.resetAverageRating();
        return em.merge(summary);
    }

    @Override
    public List<RatingSummary> readRatingSummaries(final List<String> itemIds, final RatingType type) {
        final Query query = em.createNamedQuery("UC_READ_RATING_SUMMARIES_BY_ITEM_ID_AND_TYPE");
        query.setParameter("ratingType", type.getType());
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Catalog");
        List<RatingSummary> ratings = batchExecuteReadQuery(query, itemIds, "itemIds");

        return ratings;
    }

    @Override
    public RatingSummary readRatingSummary(final String itemId, final RatingType type) {
        final Query query = em.createNamedQuery("UC_READ_RATING_SUMMARY_BY_ITEM_ID_AND_TYPE");
        query.setParameter("itemId", itemId);
        query.setParameter("ratingType", type.getType());
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Catalog");
        RatingSummary ratingSummary = null;

        try {
            ratingSummary = (RatingSummary) query.getSingleResult();
        } catch (NoResultException e) {
            // ignore
        }

        return ratingSummary;
    }

    @Override
    public RatingDetail readRating(final Long customerId, final Long ratingSummaryId) {
        final Query query = em.createNamedQuery("UC_READ_RATING_DETAIL_BY_CUSTOMER_ID_AND_RATING_SUMMARY_ID");
        query.setParameter("customerId", customerId);
        query.setParameter("ratingSummaryId", ratingSummaryId);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Catalog");
        RatingDetail ratingDetail = null;

        try {
            ratingDetail = (RatingDetail) query.getSingleResult();
        } catch (NoResultException e) {
            // ignore
        }

        return ratingDetail;
    }

    @Override
    public ReviewDetail readReview(final Long customerId, final Long ratingSummaryId) {
        final Query query = em.createNamedQuery("UC_READ_REVIEW_DETAIL_BY_CUSTOMER_ID_AND_RATING_SUMMARY_ID");
        query.setParameter("customerId", customerId);
        query.setParameter("ratingSummaryId", ratingSummaryId);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Catalog");
        ReviewDetail reviewDetail = null;

        try {
            reviewDetail = (ReviewDetail) query.getSingleResult();
        } catch (NoResultException e) {
            // ignore
        }

        return reviewDetail;
    }

}
