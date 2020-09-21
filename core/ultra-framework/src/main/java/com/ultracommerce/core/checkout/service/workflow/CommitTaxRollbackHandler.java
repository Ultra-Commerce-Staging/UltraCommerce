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
package com.ultracommerce.core.checkout.service.workflow;

import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.pricing.service.TaxService;
import com.ultracommerce.core.pricing.service.exception.TaxException;
import com.ultracommerce.core.workflow.Activity;
import com.ultracommerce.core.workflow.ProcessContext;
import com.ultracommerce.core.workflow.state.RollbackFailureException;
import com.ultracommerce.core.workflow.state.RollbackHandler;
import org.springframework.stereotype.Component;

import java.util.Map;

import javax.annotation.Resource;

@Component("ucCommitTaxRollbackHandler")
public class CommitTaxRollbackHandler implements RollbackHandler<ProcessContext<CheckoutSeed>> {

    @Resource(name = "ucTaxService")
    protected TaxService taxService;

    @Override
    public void rollbackState(Activity<ProcessContext<CheckoutSeed>> activity, ProcessContext<CheckoutSeed> processContext, Map<String, Object> stateConfiguration) throws RollbackFailureException {
        ProcessContext<CheckoutSeed> ctx = processContext;
        Order order = ctx.getSeedData().getOrder();
        try {
            taxService.cancelTax(order);
        } catch (TaxException e) {
            throw new RollbackFailureException("An exception occured cancelling taxes for order id: " + order.getId(), e);
        }

    }

}
