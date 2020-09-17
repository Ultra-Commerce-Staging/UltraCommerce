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

import com.ultracommerce.common.time.SystemTime;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.service.type.OrderStatus;
import com.ultracommerce.core.workflow.BaseActivity;
import com.ultracommerce.core.workflow.ProcessContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component("ucCompleteOrderActivity")
public class CompleteOrderActivity extends BaseActivity<ProcessContext<CheckoutSeed>> {

    public static final int ORDER = 7000;

    @Autowired
    public CompleteOrderActivity(@Qualifier("ucCompleteOrderRollbackHandler") CompleteOrderRollbackHandler rollbackHandler) {
        //no specific state to set here for the rollback handler; it's always safe for it to run
        setAutomaticallyRegisterRollbackHandler(true);
        setRollbackHandler(rollbackHandler);
        setOrder(ORDER);
    }

    @Override
    public ProcessContext<CheckoutSeed> execute(ProcessContext<CheckoutSeed> context) throws Exception {
        CheckoutSeed seed = context.getSeedData();

        seed.getOrder().setStatus(getCompletedStatus());
        seed.getOrder().setOrderNumber(determineOrderNumber(seed.getOrder()));
        seed.getOrder().setSubmitDate(determineSubmitDate(seed.getOrder()));

        return context;
    }

    protected Date determineSubmitDate(Order order) {
        return Calendar.getInstance().getTime();
    }

    protected String determineOrderNumber(Order order) {
        return new SimpleDateFormat("yyyyMMddHHmmssS").format(SystemTime.asDate()) + order.getId();
    }

    protected OrderStatus getCompletedStatus() {
        return OrderStatus.SUBMITTED;
    }

}
