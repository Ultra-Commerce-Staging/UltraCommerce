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
package com.ultracommerce.core.spec.order.service.workflow

import com.ultracommerce.core.catalog.domain.SkuImpl
import com.ultracommerce.core.catalog.service.CatalogService
import com.ultracommerce.core.inventory.service.InventoryServiceImpl
import com.ultracommerce.core.inventory.service.InventoryUnavailableException
import com.ultracommerce.core.inventory.service.type.InventoryType
import com.ultracommerce.core.order.domain.BundleOrderItemImpl
import com.ultracommerce.core.order.domain.DiscreteOrderItemImpl
import com.ultracommerce.core.order.domain.OrderItemImpl
import com.ultracommerce.core.order.service.OrderItemService
import com.ultracommerce.core.order.service.workflow.CheckUpdateAvailabilityActivity



/*
 * 1a) orderItem instanceOf DiscreteOrderItem
 *     sku is set using DiscreteOrderItem.getSku() CONTINUE
 * 1b) orderItem instanceOf BundleOrderItem 
 *     sku is set using BundleOrderItem.getSku() CONTINUE
 * 1c) LOG.warn() issued and return context EXIT
 * 
 * 2) !sku.isAvailable()
 *      * throw InventoryUnavailableException EXIT
 * 
 * 3) sku.inventoryType equals InventoryType.CHECK_QUANTITY
 *      a) inventoryService is used to find if it is available
 *          i) !available
 *              * throw InventoryUnavailableException EXIT
 *          ii) available
 *              * nothing happens CONTINUE
 *              
 * 4) return context
 */
class CheckUpdateAvailabilityActivitySpec extends BaseOrderWorkflowSpec {

    CatalogService mockCatalogService = Mock()
    OrderItemService mockOrderItemService = Mock()
    InventoryServiceImpl mockInventoryService = Spy(InventoryServiceImpl) {
        retrieveQuantityAvailable(*_) >> 0
    }
    
    
    /*
     * 1) mock for Services, and Sku
     * 2) setup activity
     */
    def setup(){
        activity = Spy(CheckUpdateAvailabilityActivity).with {
            catalogService = mockCatalogService
            orderItemService = mockOrderItemService
            inventoryService = mockInventoryService
            it
        }
        
        
    }
    
    def "If the order item id is non-null, and there is a DiscreteOrderItem, then a sku from that DiscreteOrderItem will be tested for availability"(){
        
        setup: "setup a discrete order item and non-null orderitemId"
        DiscreteOrderItemImpl mockOrderItem = Spy(DiscreteOrderItemImpl)
        SkuImpl mockSku = Spy(SkuImpl)
        mockSku.getId() >> 1
        
        context.seedData.itemRequest.getQuantity() >> 0
        context.seedData.itemRequest.getOrderItemId() >> 1
        mockOrderItemService.readOrderItemById(_) >> mockOrderItem
        
        when: "the activity is executed"
        context = activity.execute(context)
        
        then: "that sku is checked for availability"
        1 * mockOrderItem.getSku() >> mockSku
        1 * mockInventoryService.checkSkuAvailability(*_)
        1 * mockSku.isAvailable() >> true
    }
    
    def "If the order item id is non-null, and there is a BundleOrderItem, then a sku from that BundleOrderItem will be tested for availability"(){
        setup: "setup a bundle order item and non-null orderitemId"
        BundleOrderItemImpl mockOrderItem = Spy(BundleOrderItemImpl)
        SkuImpl mockSku = Spy(SkuImpl)
        mockSku.getId() >> 1
        
        context.seedData.itemRequest.getQuantity() >> 0
        context.seedData.itemRequest.getOrderItemId() >> 1
        mockOrderItemService.readOrderItemById(_) >> mockOrderItem
        
        when: "the activity is executed"
        context = activity.execute(context)
        
        then: "that sku is checked for availability"
        1 * mockOrderItem.getSku() >> mockSku
        1 * mockInventoryService.checkSkuAvailability(*_)
        1 * mockSku.isAvailable() >> true
    }
    
    def "If the order item id is non-null, and the order item is not a familiar item, then availability is not checked"(){
        setup: "setup a discrete order item and non-null orderitemId"
        OrderItemImpl mockOrderItem = Spy(OrderItemImpl)
        SkuImpl mockSku = Spy(SkuImpl)
        mockSku.getId() >> 1
    
        
        context.seedData.itemRequest.getQuantity() >> 0
        context.seedData.itemRequest.getOrderItemId() >> 1
        mockOrderItemService.readOrderItemById(_) >> mockOrderItem
        
        when: "the activity is executed"
        context = activity.execute(context)
        
        then: "availability is not checked"
        0 * mockOrderItem.getSku() >> mockSku
        0 * mockInventoryService.checkSkuAvailability(*_)
        0 * mockSku.isAvailable()
    }
    
}
