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
package com.ultracommerce.core.order.strategy;

import com.ultracommerce.core.order.service.workflow.CartOperationRequest;

/**
 * This class provides the implementation of a strategy that does not touch 
 * FulfillmentGroupItems when cart add or update operations have been performed.
 * However, the remove operation must still remove the FulfillmentGroupItems, and this
 * strategy will delegate to the default Ultra FulfillmentGroupItemStrategy to perform
 * the removal.
 * 
 * @author Andre Azzolini (apazzolini)
 */
public class NullFulfillmentGroupItemStrategyImpl extends FulfillmentGroupItemStrategyImpl {
    
    protected boolean removeEmptyFulfillmentGroups = false;
    
    @Override
    public CartOperationRequest onItemAdded(CartOperationRequest request) {
        return request;
    }
    
    @Override
    public CartOperationRequest onItemUpdated(CartOperationRequest request) {
        return request;
    }
    
    /** 
     * When we remove an order item, we must also remove the associated fulfillment group
     * item to respsect the database constraints.
     */
    @Override
    public CartOperationRequest onItemRemoved(CartOperationRequest request) {
        return super.onItemRemoved(request);
    }
    
    @Override
    public CartOperationRequest verify(CartOperationRequest request) {
        return request;
    }
    
    @Override
    public boolean isRemoveEmptyFulfillmentGroups() {
        return removeEmptyFulfillmentGroups;
    }

}
