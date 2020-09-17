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
import com.ultracommerce.core.order.domain.FulfillmentGroupItem;
import com.ultracommerce.core.order.domain.FulfillmentGroupItemImpl;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository("ucFulfillmentGroupItemDao")
public class FulfillmentGroupItemDaoImpl implements FulfillmentGroupItemDao {

    @PersistenceContext(unitName="ucPU")
    protected EntityManager em;

    @Resource(name="ucEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    public void delete(FulfillmentGroupItem fulfillmentGroupItem) {
        if (!em.contains(fulfillmentGroupItem)) {
            fulfillmentGroupItem = readFulfillmentGroupItemById(fulfillmentGroupItem.getId());
        }
        em.remove(fulfillmentGroupItem);
    }

    public FulfillmentGroupItem save(final FulfillmentGroupItem fulfillmentGroupItem) {
        return em.merge(fulfillmentGroupItem);
    }

    public FulfillmentGroupItem readFulfillmentGroupItemById(final Long fulfillmentGroupItemId) {
        return (FulfillmentGroupItem) em.find(FulfillmentGroupItemImpl.class, fulfillmentGroupItemId);
    }

    @SuppressWarnings("unchecked")
    public List<FulfillmentGroupItem> readFulfillmentGroupItemsForFulfillmentGroup(final FulfillmentGroup fulfillmentGroup) {
        final Query query = em.createNamedQuery("UC_READ_FULFILLMENT_GROUP_ITEM_BY_FULFILLMENT_GROUP_ID");
        query.setParameter("fulfillmentGroupId", fulfillmentGroup.getId());
        return query.getResultList();
    }

    public FulfillmentGroupItem create() {
        return ((FulfillmentGroupItem) entityConfiguration.createEntityInstance("com.ultracommerce.core.order.domain.FulfillmentGroupItem"));
    }
}
