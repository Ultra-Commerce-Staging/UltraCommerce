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
package com.ultracommerce.core.spec.checkout.service.workflow

import com.ultracommerce.common.money.Money
import com.ultracommerce.core.catalog.domain.Sku
import com.ultracommerce.core.catalog.domain.SkuImpl
import com.ultracommerce.core.checkout.service.workflow.DecrementInventoryActivity
import com.ultracommerce.core.inventory.service.ContextualInventoryService
import com.ultracommerce.core.inventory.service.type.InventoryType
import com.ultracommerce.core.order.domain.BundleOrderItem
import com.ultracommerce.core.order.domain.BundleOrderItemImpl
import com.ultracommerce.core.order.domain.DiscreteOrderItem
import com.ultracommerce.core.order.domain.DiscreteOrderItemImpl
import com.ultracommerce.core.order.domain.OrderItem
import com.ultracommerce.core.workflow.state.ActivityStateManagerImpl
import com.ultracommerce.core.workflow.state.NullCheckoutRollbackHandler
import com.ultracommerce.core.workflow.state.RollbackHandler
import com.ultracommerce.core.workflow.state.RollbackStateLocal

class DecrementInventoryActivitySpec extends BaseCheckoutActivitySpec{

    ContextualInventoryService mockInventoryService
    DiscreteOrderItem discreteOrderItem
    BundleOrderItem bundleOrderItem
    RollbackHandler mockRollbackHandler

    def setup(){
        def rollbackStateLocal = new RollbackStateLocal()
        rollbackStateLocal.setThreadId("SPOCK_THREAD")
        rollbackStateLocal.setWorkflowId("TEST")
        RollbackStateLocal.setRollbackStateLocal(rollbackStateLocal)

        new ActivityStateManagerImpl().init()

        discreteOrderItem = new DiscreteOrderItemImpl()
        bundleOrderItem = new BundleOrderItemImpl()
        DiscreteOrderItem discreteOrderItemForBundleOrderItem = new DiscreteOrderItemImpl()
        List<OrderItem> orderItems = new ArrayList()

        mockInventoryService = Mock()

        //each discreteOrderItem will need a unique sku, and a quantity
        //each sku will need to have its inventoryType set to InventoryType.CHECK_QUANTITY
        //each bundleOrderItem will need a List<DiscreteOrderItem> holding a DiscreteOrderItem
        //each '' will also need a sku and a quantity value with its sku's inventoryType set to InventoryType.CHECK_QUANTITY
        discreteOrderItem.sku = new SkuImpl()
        discreteOrderItem.sku.inventoryType = InventoryType.CHECK_QUANTITY
        discreteOrderItem.sku.id = 0
        discreteOrderItem.sku.setRetailPrice(new Money(1.00))
        discreteOrderItem.quantity = 1
        discreteOrderItemForBundleOrderItem.sku = new SkuImpl()
        discreteOrderItemForBundleOrderItem.sku.inventoryType = InventoryType.CHECK_QUANTITY
        discreteOrderItemForBundleOrderItem.sku.id = 1
        discreteOrderItemForBundleOrderItem.sku.setRetailPrice(new Money(1.00))
        discreteOrderItemForBundleOrderItem.quantity = 1
        Sku bundleOrderItemSku = new SkuImpl()
        bundleOrderItemSku.setId(2)
        bundleOrderItemSku.setRetailPrice(new Money(1.00))
        bundleOrderItemSku.setInventoryType(InventoryType.CHECK_QUANTITY)
        bundleOrderItem.sku = bundleOrderItemSku
        bundleOrderItem.quantity = 1
        bundleOrderItem.discreteOrderItems << discreteOrderItemForBundleOrderItem
        context.seedData.order.orderItems << discreteOrderItem
        context.seedData.order.orderItems << bundleOrderItem

        Map<Sku, Integer> skuIntegerMap = new HashMap<>()
        skuIntegerMap.put(discreteOrderItem.sku, discreteOrderItem.quantity)
        skuIntegerMap.put(discreteOrderItemForBundleOrderItem.sku, discreteOrderItemForBundleOrderItem.quantity)
        skuIntegerMap.put(bundleOrderItem.sku, bundleOrderItem.quantity)

        mockInventoryService.buildSkuInventoryMap(*_) >> skuIntegerMap

    }


    def "Test DecrementInventory with Valid Data"() {
        setup:"I have one DiscreteOrderItem by itself in the order and one BundleOrderItem holding one DiscreteOrderItem in the Order"

        activity = new DecrementInventoryActivity().with {
            inventoryService = mockInventoryService
            rollbackHandler = new NullCheckoutRollbackHandler()
            it
        }

        when: "I execute the DecerementInventoryActivity"
        context = activity.execute(context)

        then: "decrementInventory() should have run once and there should be 3 state containers for the Activity State Manager rollback thread"
        def containers = ActivityStateManagerImpl.stateManager.stateMap.get("SPOCK_THREAD_TEST")
        containers.size() == 1
        1 * mockInventoryService.decrementInventory(_, _)
    }
}
