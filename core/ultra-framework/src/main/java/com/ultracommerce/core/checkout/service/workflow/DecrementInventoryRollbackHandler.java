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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.util.StringUtil;
import com.ultracommerce.core.catalog.domain.Sku;
import com.ultracommerce.core.inventory.service.ContextualInventoryService;
import com.ultracommerce.core.inventory.service.InventoryUnavailableException;
import com.ultracommerce.core.workflow.Activity;
import com.ultracommerce.core.workflow.ProcessContext;
import com.ultracommerce.core.workflow.state.RollbackFailureException;
import com.ultracommerce.core.workflow.state.RollbackHandler;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

/**
 * Decrements inventory that was put on by the {@link DecrementInventoryActivity}
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
@Component("ucDecrementInventoryRollbackHandler")
public class DecrementInventoryRollbackHandler implements RollbackHandler<ProcessContext<CheckoutSeed>>{

    private static final Log LOG = LogFactory.getLog(DecrementInventoryRollbackHandler.class);
    
    public static final String ROLLBACK_UC_INVENTORY_DECREMENTED = "ROLLBACK_UC_INVENTORY_DECREMENTED";
    public static final String ROLLBACK_UC_INVENTORY_INCREMENTED = "ROLLBACK_UC_INVENTORY_INCREMENTED";
    public static final String ROLLBACK_UC_ORDER_ID = "ROLLBACK_UC_ORDER_ID";
    public static final String EXTENDED_ROLLBACK_STATE = "UC_EXTENDED_ROLLBACK_STATE";

    @Resource(name = "ucInventoryService")
    protected ContextualInventoryService inventoryService;
    
    @Override
    public void rollbackState(Activity<ProcessContext<CheckoutSeed>> activity, ProcessContext<CheckoutSeed> processContext, Map<String, Object> stateConfiguration)
            throws RollbackFailureException {

        if (shouldExecute(activity, processContext, stateConfiguration)) {

            String orderId = "(Not Known)";
            if (stateConfiguration.get(ROLLBACK_UC_ORDER_ID) != null) {
                orderId = String.valueOf(stateConfiguration.get(ROLLBACK_UC_ORDER_ID));
            }
            
            @SuppressWarnings("unchecked")
            Map<Sku, Integer> inventoryToIncrement = (Map<Sku, Integer>) stateConfiguration.get(ROLLBACK_UC_INVENTORY_DECREMENTED);
            @SuppressWarnings("unchecked")
            Map<Sku, Integer> inventoryToDecrement = (Map<Sku, Integer>) stateConfiguration.get(ROLLBACK_UC_INVENTORY_INCREMENTED);
            
            Map<String, Object> contextualInformation = new HashMap<>();
            contextualInformation.put(ContextualInventoryService.ROLLBACK_STATE_KEY, stateConfiguration.get(EXTENDED_ROLLBACK_STATE));
            contextualInformation.put(ContextualInventoryService.ORDER_KEY, processContext.getSeedData().getOrder());
            if (inventoryToIncrement != null && !inventoryToIncrement.isEmpty()) {
                try {
                    inventoryService.incrementInventory(inventoryToIncrement, contextualInformation);
                } catch (Exception ex) {
                    RollbackFailureException rfe = new RollbackFailureException("An unexpected error occured in the error handler of the checkout workflow trying to compensate for inventory. This happend for order ID: " +
                            orderId + ". This should be corrected manually!", ex);
                    rfe.setActivity(activity);
                    rfe.setProcessContext(processContext);
                    rfe.setStateItems(stateConfiguration);
                    throw rfe;
                }
            }
    
            if (inventoryToDecrement != null && !inventoryToDecrement.isEmpty()) {
                try {
                    inventoryService.decrementInventory(inventoryToDecrement, contextualInformation);
                } catch (InventoryUnavailableException e) {
                    //This is an awkward, unlikely state.  I just added some inventory, but something happened, and I want to remove it, but it's already gone!
                    RollbackFailureException rfe = new RollbackFailureException("While trying roll back (decrement) inventory, we found that there was none left decrement.", e);
                    rfe.setActivity(activity);
                    rfe.setProcessContext(processContext);
                    rfe.setStateItems(stateConfiguration);
                    throw rfe;
                } catch (RuntimeException ex) {
                    LOG.error("An unexpected error occured in the error handler of the checkout workflow trying to compensate for inventory. This happend for order ID: " +
                            StringUtil.sanitize(orderId) + ". This should be corrected manually!", ex);
                    RollbackFailureException rfe = new RollbackFailureException("An unexpected error occured in the error handler of the checkout workflow " +
                            "trying to compensate for inventory. This happend for order ID: " +
                            orderId + ". This should be corrected manually!", ex);
                    rfe.setActivity(activity);
                    rfe.setProcessContext(processContext);
                    rfe.setStateItems(stateConfiguration);
                    throw rfe;
                }
            }
        }
    }
    
    /**
     * Returns true if this rollback handler should execute
     */
    protected boolean shouldExecute(Activity<? extends ProcessContext<CheckoutSeed>> activity, ProcessContext<CheckoutSeed> processContext, Map<String, Object> stateConfiguration) {
        return stateConfiguration != null && (
                stateConfiguration.get(ROLLBACK_UC_INVENTORY_DECREMENTED) != null ||
                stateConfiguration.get(ROLLBACK_UC_INVENTORY_INCREMENTED) != null ||
                stateConfiguration.get(EXTENDED_ROLLBACK_STATE) != null
             );
    }

}
