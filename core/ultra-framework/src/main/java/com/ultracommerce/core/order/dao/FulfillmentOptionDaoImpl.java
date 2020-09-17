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
import com.ultracommerce.core.order.domain.FulfillmentOption;
import com.ultracommerce.core.order.domain.FulfillmentOptionImpl;
import com.ultracommerce.core.order.service.type.FulfillmentType;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * 
 * @author Phillip Verheyden
 */
@Repository("ucFulfillmentOptionDao")
public class FulfillmentOptionDaoImpl implements FulfillmentOptionDao {

    @PersistenceContext(unitName = "ucPU")
    protected EntityManager em;

    @Resource(name="ucEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    @Override
    public FulfillmentOption readFulfillmentOptionById(final Long fulfillmentOptionId) {
        return em.find(FulfillmentOptionImpl.class, fulfillmentOptionId);
    }

    @Override
    public FulfillmentOption save(FulfillmentOption option) {
        return em.merge(option);
    }

    @Override
    public List<FulfillmentOption> readAllFulfillmentOptions() {
        TypedQuery<FulfillmentOption> query = em.createNamedQuery("UC_READ_ALL_FULFILLMENT_OPTIONS", FulfillmentOption.class);
        return query.getResultList();
    }

    @Override
    public List<FulfillmentOption> readAllFulfillmentOptionsByFulfillmentType(FulfillmentType type) {
        TypedQuery<FulfillmentOption> query = em.createNamedQuery("UC_READ_ALL_FULFILLMENT_OPTIONS_BY_TYPE", FulfillmentOption.class);
        query.setParameter("fulfillmentType", type.getType());
        return query.getResultList();
    }
}
