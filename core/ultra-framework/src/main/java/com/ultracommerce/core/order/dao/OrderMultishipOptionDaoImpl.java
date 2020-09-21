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
import com.ultracommerce.core.order.domain.OrderMultishipOption;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Repository("ucOrderMultishipOptionDao")
public class OrderMultishipOptionDaoImpl implements OrderMultishipOptionDao {

    @PersistenceContext(unitName = "ucPU")
    protected EntityManager em;

    @Resource(name = "ucEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    @Override
    @Transactional("ucTransactionManager")
    public OrderMultishipOption save(final OrderMultishipOption orderMultishipOption) {
        return em.merge(orderMultishipOption);
    }

    @Override
    public List<OrderMultishipOption> readOrderMultishipOptions(final Long orderId) {
        TypedQuery<OrderMultishipOption> query = em.createNamedQuery("UC_READ_MULTISHIP_OPTIONS_BY_ORDER_ID", OrderMultishipOption.class);
        query.setParameter("orderId", orderId);
        return query.getResultList();
    }
    
    @Override
    public List<OrderMultishipOption> readOrderItemOrderMultishipOptions(final Long orderItemId) {
        TypedQuery<OrderMultishipOption> query = em.createNamedQuery("UC_READ_MULTISHIP_OPTIONS_BY_ORDER_ITEM_ID", OrderMultishipOption.class);
        query.setParameter("orderItemId", orderItemId);
        return query.getResultList();
    }
    
    @Override
    public OrderMultishipOption create() {
        return (OrderMultishipOption) entityConfiguration.createEntityInstance(OrderMultishipOption.class.getName());
    }
    
    @Override
    @Transactional("ucTransactionManager")
    public void deleteAll(List<OrderMultishipOption> options) {
        for (OrderMultishipOption option : options) {
            em.remove(option);
        }
    }
}
