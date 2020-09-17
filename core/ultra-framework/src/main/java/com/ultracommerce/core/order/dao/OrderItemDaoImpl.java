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

import org.apache.commons.collections.CollectionUtils;
import com.ultracommerce.common.persistence.EntityConfiguration;
import com.ultracommerce.core.order.domain.GiftWrapOrderItem;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.domain.OrderImpl;
import com.ultracommerce.core.order.domain.OrderItem;
import com.ultracommerce.core.order.domain.OrderItemImpl;
import com.ultracommerce.core.order.domain.OrderItemPriceDetail;
import com.ultracommerce.core.order.domain.OrderItemPriceDetailImpl;
import com.ultracommerce.core.order.domain.OrderItemQualifier;
import com.ultracommerce.core.order.domain.OrderItemQualifierImpl;
import com.ultracommerce.core.order.domain.PersonalMessage;
import com.ultracommerce.core.order.service.type.OrderItemType;
import com.ultracommerce.core.order.service.type.OrderStatus;
import org.hibernate.jpa.QueryHints;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Repository("ucOrderItemDao")
public class OrderItemDaoImpl implements OrderItemDao {

    @PersistenceContext(unitName="ucPU")
    protected EntityManager em;

    @Resource(name="ucEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    @Override
    @Transactional("ucTransactionManager")
    public OrderItem save(final OrderItem orderItem) {
        return em.merge(orderItem);
    }

    @Override
    public OrderItem readOrderItemById(final Long orderItemId) {
        return em.find(OrderItemImpl.class, orderItemId);
    }

    @Override
    @Transactional("ucTransactionManager")
    public void delete(OrderItem orderItem) {
        if (!em.contains(orderItem)) {
            orderItem = readOrderItemById(orderItem.getId());
        }
        if (GiftWrapOrderItem.class.isAssignableFrom(orderItem.getClass())) {
            final GiftWrapOrderItem giftItem = (GiftWrapOrderItem) orderItem;
            for (OrderItem wrappedItem : giftItem.getWrappedItems()) {
                wrappedItem.setGiftWrapOrderItem(null);
                wrappedItem = save(wrappedItem);
            }
        }
        em.remove(orderItem);
        em.flush();
    }

    @Override
    public OrderItem create(final OrderItemType orderItemType) {
        final OrderItem item = (OrderItem) entityConfiguration.createEntityInstance(orderItemType.getType());
        item.setOrderItemType(orderItemType);
        return item;
    }
    
    @Override
    public PersonalMessage createPersonalMessage() {
        PersonalMessage personalMessage = (PersonalMessage) entityConfiguration.createEntityInstance(PersonalMessage.class.getName());
        return personalMessage;
    }

    @Override
    @Transactional("ucTransactionManager")
    public OrderItem saveOrderItem(final OrderItem orderItem) {
        return em.merge(orderItem);
    }

    @Override
    public OrderItemPriceDetail createOrderItemPriceDetail() {
        return (OrderItemPriceDetail) entityConfiguration.createEntityInstance(OrderItemPriceDetail.class.getName());
    }

    @Override
    public OrderItemQualifier createOrderItemQualifier() {
        return (OrderItemQualifier) entityConfiguration.createEntityInstance(OrderItemQualifier.class.getName());
    }

    @Override
    public OrderItemPriceDetail initializeOrderItemPriceDetails(OrderItem item) {
        OrderItemPriceDetail detail = createOrderItemPriceDetail();
        detail.setOrderItem(item);
        detail.setQuantity(item.getQuantity());
        detail.setUseSalePrice(item.getIsOnSale());
        item.getOrderItemPriceDetails().add(detail);
        return detail;
    }

    @Override
    public List<OrderItem> readOrderItemsForCustomersInDateRange(List<Long> customerIds, Date startDate, Date endDate) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<OrderItem> criteria = builder.createQuery(OrderItem.class);
        Root<OrderImpl> order = criteria.from(OrderImpl.class);
        Join<Order, OrderItem> orderItems = order.join("orderItems");
        criteria.select(orderItems);

        List<Predicate> restrictions = new ArrayList<>();
        restrictions.add(builder.between(order.<Date>get("submitDate"), startDate, endDate));
        restrictions.add(order.get("customer").get("id").in(customerIds));
        criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));

        criteria.orderBy(builder.desc(order.get("customer")), builder.asc(order.get("submitDate")));

        TypedQuery<OrderItem> query = em.createQuery(criteria);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Order");

        return query.getResultList();
    }

    @Override
    public Long readNumberOfOrderItems() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        criteria.select(builder.count(criteria.from(OrderItemImpl.class)));
        TypedQuery<Long> query = em.createQuery(criteria);
        return query.getSingleResult();    }

    @Override
    public List<OrderItem> readBatchOrderItems(int start, int count, List<OrderStatus> statuses) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<OrderItem> criteria = builder.createQuery(OrderItem.class);
        Root<OrderImpl> order = criteria.from(OrderImpl.class);
        Join<Order, OrderItem> orderItems = order.join("orderItems");
        criteria.select(orderItems);

        List<Predicate> restrictions = new ArrayList<>();
        criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));

        if (CollectionUtils.isNotEmpty(statuses)) {
            // We only want results that match the orders with the correct status
            ArrayList<String> statusStrings = new ArrayList<String>();
            for (OrderStatus status : statuses) {
                statusStrings.add(status.getType());
            }
            criteria.where(order.get("status").as(String.class).in(statusStrings));
        }

        TypedQuery<OrderItem> query = em.createQuery(criteria);
        query.setFirstResult(start);
        query.setMaxResults(count);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Order");

        return query.getResultList();
    }
}
