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
import com.ultracommerce.core.rating.domain.ReviewDetail;
import com.ultracommerce.core.rating.domain.ReviewDetailImpl;
import com.ultracommerce.core.rating.domain.ReviewFeedback;
import com.ultracommerce.profile.core.domain.Customer;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository("ucReviewDetailDao")
public class ReviewDetailDaoImpl implements ReviewDetailDao {

    @PersistenceContext(unitName = "ucPU")
    protected EntityManager em;

    @Resource(name="ucEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    public ReviewDetail readReviewDetailById(Long reviewId) {
        return em.find(ReviewDetailImpl.class, reviewId);
    }

    public ReviewDetail saveReviewDetail(ReviewDetail reviewDetail) {
        return em.merge(reviewDetail);
    }
    
    @Override
    public ReviewDetail readReviewByCustomerAndItem(Customer customer, String itemId) {
        final Query query = em.createNamedQuery("UC_READ_REVIEW_DETAIL_BY_CUSTOMER_ID_AND_ITEM_ID");
        query.setParameter("customerId", customer.getId());
        query.setParameter("itemId", itemId);
        ReviewDetail reviewDetail = null;
        try {
            reviewDetail = (ReviewDetail) query.getSingleResult();
        } catch (NoResultException nre) {
            //ignore
        }
        return reviewDetail;
    }

    public ReviewDetail create() {
        return (ReviewDetail) entityConfiguration.createEntityInstance(ReviewDetail.class.getName());
    }

    public ReviewFeedback createFeedback() {
        return (ReviewFeedback) entityConfiguration.createEntityInstance(ReviewFeedback.class.getName());
    }
}
