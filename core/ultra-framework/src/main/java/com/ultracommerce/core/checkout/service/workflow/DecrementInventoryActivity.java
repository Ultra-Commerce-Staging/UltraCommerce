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

import com.ultracommerce.core.catalog.domain.Sku;
import com.ultracommerce.core.inventory.service.ContextualInventoryService;
import com.ultracommerce.core.order.domain.OrderItem;
import com.ultracommerce.core.workflow.BaseActivity;
import com.ultracommerce.core.workflow.ProcessContext;
import com.ultracommerce.core.workflow.state.ActivityStateManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * Decrements inventory
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
@Component("ucDecrementInventoryActivity")
public class DecrementInventoryActivity extends BaseActivity<ProcessContext<CheckoutSeed>> {

    public static final int ORDER = 6000;
    
    @Resource(name = "ucInventoryService")
    protected ContextualInventoryService inventoryService;
    
    @Autowired
    public DecrementInventoryActivity(@Qualifier("ucDecrementInventoryRollbackHandler") DecrementInventoryRollbackHandler rollbackHandler) {
        super();
        super.setAutomaticallyRegisterRollbackHandler(false);
        setOrder(ORDER);
    }

    @Override
    public ProcessContext<CheckoutSeed> execute(ProcessContext<CheckoutSeed> context) throws Exception {
        CheckoutSeed seed = context.getSeedData();
        List<OrderItem> orderItems = seed.getOrder().getOrderItems();

        //map to hold skus and quantity purchased
        Map<Sku, Integer> skuInventoryMap = inventoryService.buildSkuInventoryMap(seed.getOrder());

        Map<String, Object> rollbackState = new HashMap<>();
        if (getRollbackHandler() != null && !getAutomaticallyRegisterRollbackHandler()) {
            if (getStateConfiguration() != null && !getStateConfiguration().isEmpty()) {
                rollbackState.putAll(getStateConfiguration());
            }
            // Register the map with the rollback state object early on; this allows the extension handlers to incrementally
            // add state while decrementing but still throw an exception
            ActivityStateManagerImpl.getStateManager().registerState(this, context, getRollbackRegion(), getRollbackHandler(), rollbackState);
        }
            
        if (!skuInventoryMap.isEmpty()) {
            Map<String, Object> contextualInfo = new HashMap<>();
            contextualInfo.put(ContextualInventoryService.ORDER_KEY, context.getSeedData().getOrder());
            contextualInfo.put(ContextualInventoryService.ROLLBACK_STATE_KEY, new HashMap<String, Object>());
            inventoryService.decrementInventory(skuInventoryMap, contextualInfo);
            
            if (getRollbackHandler() != null && !getAutomaticallyRegisterRollbackHandler()) {
                rollbackState.put(DecrementInventoryRollbackHandler.ROLLBACK_UC_INVENTORY_DECREMENTED, skuInventoryMap);
                rollbackState.put(DecrementInventoryRollbackHandler.ROLLBACK_UC_ORDER_ID, seed.getOrder().getId());
            }
            
            // add the rollback state that was used in the rollback handler
            rollbackState.put(DecrementInventoryRollbackHandler.EXTENDED_ROLLBACK_STATE, contextualInfo.get(ContextualInventoryService.ROLLBACK_STATE_KEY));
        }

        return context;
    }

}
