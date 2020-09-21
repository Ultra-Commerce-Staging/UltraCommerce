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
import com.ultracommerce.core.workflow.BaseActivity;
import com.ultracommerce.core.workflow.ProcessContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * This is an optional activity to allow a committal of taxes to a tax sub system. Many tax 
 * providers store tax details for reference, debugging, reporting, and reconciliation.
 * 
 * @author Kelly Tisdell
 *
 */
@Component("ucCommitTaxActivity")
public class CommitTaxActivity extends BaseActivity<ProcessContext<CheckoutSeed>> {
    
    public static final int ORDER = 5000;
    
    @Resource(name = "ucTaxService")
    protected TaxService taxService;

    @Autowired
    public CommitTaxActivity(@Qualifier("ucCommitTaxRollbackHandler") CommitTaxRollbackHandler rollbackHandler) {
        //We can automatically register a rollback handler because the state will be in the process context.
        super.setAutomaticallyRegisterRollbackHandler(true);
        setRollbackHandler(rollbackHandler);
        setOrder(ORDER);
    }

    @Override
    public ProcessContext<CheckoutSeed> execute(ProcessContext<CheckoutSeed> context) throws Exception {
        Order order = context.getSeedData().getOrder();

        if (!order.getTaxOverride()) {
            order = taxService.commitTaxForOrder(order);
            context.getSeedData().setOrder(order);
        }

        return context;
    }

}
