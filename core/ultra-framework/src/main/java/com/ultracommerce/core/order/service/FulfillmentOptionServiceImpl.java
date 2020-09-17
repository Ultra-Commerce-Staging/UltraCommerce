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
package com.ultracommerce.core.order.service;

import com.ultracommerce.core.order.dao.FulfillmentOptionDao;
import com.ultracommerce.core.order.domain.FulfillmentOption;
import com.ultracommerce.core.order.service.type.FulfillmentType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.annotation.Resource;

/**
 * 
 * @author Phillip Verheyden
 */
@Service("ucFulfillmentOptionService")
public class FulfillmentOptionServiceImpl implements FulfillmentOptionService {

    @Resource(name = "ucFulfillmentOptionDao")
    protected FulfillmentOptionDao fulfillmentOptionDao;

    @Override
    public FulfillmentOption readFulfillmentOptionById(Long fulfillmentOptionId) {
        return fulfillmentOptionDao.readFulfillmentOptionById(fulfillmentOptionId);
    }

    @Override
    @Transactional("ucTransactionManager")
    public FulfillmentOption save(FulfillmentOption option) {
        return fulfillmentOptionDao.save(option);
    }

    @Override
    public List<FulfillmentOption> readAllFulfillmentOptions() {
        return fulfillmentOptionDao.readAllFulfillmentOptions();
    }

    @Override
    public List<FulfillmentOption> readAllFulfillmentOptionsByFulfillmentType(FulfillmentType type) {
        return fulfillmentOptionDao.readAllFulfillmentOptionsByFulfillmentType(type);
    }
}
