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
package com.ultracommerce.core.order.dao;

import com.ultracommerce.common.persistence.EntityConfiguration;
import com.ultracommerce.core.order.domain.FulfillmentGroup;
import com.ultracommerce.core.order.domain.FulfillmentGroupFee;
import com.ultracommerce.core.order.domain.FulfillmentGroupImpl;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.service.type.FulfillmentGroupStatusType;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository("ucFulfillmentGroupDao")
public class FulfillmentGroupDaoImpl implements FulfillmentGroupDao {

    @PersistenceContext(unitName = "ucPU")
    protected EntityManager em;

    @Resource(name="ucEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    @Override
    public FulfillmentGroup save(final FulfillmentGroup fulfillmentGroup) {
        return em.merge(fulfillmentGroup);
    }

    @Override
    public FulfillmentGroup readFulfillmentGroupById(final Long fulfillmentGroupId) {
        return em.find(FulfillmentGroupImpl.class, fulfillmentGroupId);
    }

    @Override
    public FulfillmentGroupImpl readDefaultFulfillmentGroupForOrder(final Order order) {
        final Query query = em.createNamedQuery("UC_READ_DEFAULT_FULFILLMENT_GROUP_BY_ORDER_ID");
        query.setParameter("orderId", order.getId());
        @SuppressWarnings("unchecked")
        List<FulfillmentGroupImpl> fulfillmentGroups = query.getResultList();
        return fulfillmentGroups == null || fulfillmentGroups.isEmpty() ? null : fulfillmentGroups.get(0);
    }

    @Override
    public void delete(FulfillmentGroup fulfillmentGroup) {
        if (!em.contains(fulfillmentGroup)) {
            fulfillmentGroup = readFulfillmentGroupById(fulfillmentGroup.getId());
        }
        em.remove(fulfillmentGroup);
    }

    @Override
    public FulfillmentGroup createDefault() {
        final FulfillmentGroup fg = ((FulfillmentGroup) entityConfiguration.createEntityInstance("com.ultracommerce.core.order.domain.FulfillmentGroup"));
        fg.setPrimary(true);
        return fg;
    }

    @Override
    public FulfillmentGroup create() {
        final FulfillmentGroup fg =  ((FulfillmentGroup) entityConfiguration.createEntityInstance("com.ultracommerce.core.order.domain.FulfillmentGroup"));
        return fg;
    }

    @Override
    public FulfillmentGroupFee createFulfillmentGroupFee() {
        return ((FulfillmentGroupFee) entityConfiguration.createEntityInstance("com.ultracommerce.core.order.domain.FulfillmentGroupFee"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<FulfillmentGroup> readUnfulfilledFulfillmentGroups(int start,
            int maxResults) {
        Query query = em.createNamedQuery("UC_READ_UNFULFILLED_FULFILLMENT_GROUP_ASC");
        query.setFirstResult(start);
        query.setMaxResults(maxResults);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<FulfillmentGroup> readPartiallyFulfilledFulfillmentGroups(int start,
            int maxResults) {
        Query query = em.createNamedQuery("UC_READ_PARTIALLY_FULFILLED_FULFILLMENT_GROUP_ASC");
        query.setFirstResult(start);
        query.setMaxResults(maxResults);
        
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<FulfillmentGroup> readUnprocessedFulfillmentGroups(int start,
            int maxResults) {
        Query query = em.createNamedQuery("UC_READ_UNPROCESSED_FULFILLMENT_GROUP_ASC");
        query.setFirstResult(start);
        query.setMaxResults(maxResults);
        
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<FulfillmentGroup> readFulfillmentGroupsByStatus(
            FulfillmentGroupStatusType status, int start, int maxResults, boolean ascending) {
        Query query = null;
        if (ascending) {
            query = em.createNamedQuery("UC_READ_FULFILLMENT_GROUP_BY_STATUS_ASC");
        } else {
            query = em.createNamedQuery("UC_READ_FULFILLMENT_GROUP_BY_STATUS_DESC");
        }
        query.setParameter("status", status.getType());
        query.setFirstResult(start);
        query.setMaxResults(maxResults);
        
        return query.getResultList();
    }

    @Override
    public List<FulfillmentGroup> readFulfillmentGroupsByStatus(
            FulfillmentGroupStatusType status, int start, int maxResults) {
        return readFulfillmentGroupsByStatus(status, start, maxResults, true);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Integer readNextFulfillmentGroupSequnceForOrder(Order order) {
        Query query = em.createNamedQuery("UC_READ_MAX_FULFILLMENT_GROUP_SEQUENCE");
        query.setParameter("orderId", order.getId());
        List<Integer> max = query.getResultList();
        if (max != null && !max.isEmpty()) {
            Integer maxNumber = max.get(0);
            if (maxNumber == null) {
                return 1;
            }
            return maxNumber + 1;
        }
        return 1;
    }
}
