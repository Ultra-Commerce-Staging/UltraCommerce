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

import com.ultracommerce.core.order.service.type.OrderStatus;
import com.ultracommerce.core.workflow.Activity;
import com.ultracommerce.core.workflow.ProcessContext;
import com.ultracommerce.core.workflow.state.RollbackFailureException;
import com.ultracommerce.core.workflow.state.RollbackHandler;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * Rollback handler to execute after an order has been marked as 'completed' and there is an exception.
 * 
 *  1. Change the status back to IN_PROCESS
 *  2. Change the order number back to null
 *  3. Change the submit date back to null
 *
 * @author Phillip Verheyden (phillipuniverse)
 */
@Component("ucCompleteOrderRollbackHandler")
public class CompleteOrderRollbackHandler implements RollbackHandler<ProcessContext<CheckoutSeed>> {

    @Override
    public void rollbackState(Activity<ProcessContext<CheckoutSeed>> activity, ProcessContext<CheckoutSeed> processContext, Map<String, Object> stateConfiguration) throws RollbackFailureException {
        CheckoutSeed seed = processContext.getSeedData();
        seed.getOrder().setStatus(OrderStatus.IN_PROCESS);
        seed.getOrder().setOrderNumber(null);
        seed.getOrder().setSubmitDate(null);
    }

}
