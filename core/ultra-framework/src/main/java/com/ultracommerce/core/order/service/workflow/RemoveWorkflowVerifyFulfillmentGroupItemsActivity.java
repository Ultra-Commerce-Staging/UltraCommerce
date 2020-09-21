/*
 * #%L
 * UltraCommerce Framework
 * %%
 * Copyright (C) 2009 - 2017 Ultra Commerce
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
/**
 * 
 */
package com.ultracommerce.core.order.service.workflow;

import com.ultracommerce.core.order.strategy.FulfillmentGroupItemStrategy;
import com.ultracommerce.core.workflow.BaseActivity;
import com.ultracommerce.core.workflow.ProcessContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
@Component("ucRemoveWorkflowVerifyFulfillmentGroupItemsActivity")
public class RemoveWorkflowVerifyFulfillmentGroupItemsActivity extends BaseActivity<ProcessContext<CartOperationRequest>> {

    public static final int ORDER = 6000;
    
    @Resource(name = "ucFulfillmentGroupItemStrategy")
    protected FulfillmentGroupItemStrategy fgItemStrategy;
    
    public RemoveWorkflowVerifyFulfillmentGroupItemsActivity() {
        setOrder(ORDER);
    }
    
    @Override
    public ProcessContext<CartOperationRequest> execute(ProcessContext<CartOperationRequest> context) throws Exception {
        CartOperationRequest request = context.getSeedData();
        
        request = fgItemStrategy.verify(request);
        
        context.setSeedData(request);
        return context;
    }

}
