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
package com.ultracommerce.core.order.service.workflow;

import com.ultracommerce.core.workflow.DefaultProcessContextImpl;
import com.ultracommerce.core.workflow.ProcessContext;
import com.ultracommerce.core.workflow.ProcessContextFactory;
import com.ultracommerce.core.workflow.WorkflowException;

/**
 * Provides a method that creates the seed ProcessContext object for a cart
 * operation. The same seed object is used for add item, update item, and remove
 * item cart operations.
 * 
 * @author apazzolini
 */
public class CartOperationProcessContextFactory implements ProcessContextFactory<CartOperationRequest, CartOperationRequest> {

    /**
     * Creates the necessary context for cart operations
     */
    public ProcessContext<CartOperationRequest> createContext(CartOperationRequest seedData) throws WorkflowException {
        ProcessContext<CartOperationRequest> context = new DefaultProcessContextImpl<>();
        context.setSeedData(seedData);
        return context;
    }

}
