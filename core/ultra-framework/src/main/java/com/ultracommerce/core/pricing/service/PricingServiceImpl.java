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
package com.ultracommerce.core.pricing.service;

import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.pricing.service.exception.PricingException;
import com.ultracommerce.core.workflow.ProcessContext;
import com.ultracommerce.core.workflow.Processor;
import com.ultracommerce.core.workflow.WorkflowException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service("ucPricingService")
public class PricingServiceImpl implements PricingService {

    @Resource(name="ucPricingWorkflow")
    protected Processor pricingWorkflow;

    @Transactional(rollbackFor = PricingException.class)
    public Order executePricing(Order order) throws PricingException {
        try {
            ProcessContext<Order> context = (ProcessContext<Order>) pricingWorkflow.doActivities(order);
            Order response = context.getSeedData();

            return response;
        } catch (WorkflowException e) {
            throw new PricingException("Unable to execute pricing for order -- id: " + order.getId(), e);
        }
    }

}
