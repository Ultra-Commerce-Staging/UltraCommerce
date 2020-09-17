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
package com.ultracommerce.core.inventory.service;

import com.ultracommerce.common.extension.ExtensionHandler;
import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.core.catalog.domain.Product;
import com.ultracommerce.core.catalog.domain.Sku;
import com.ultracommerce.core.checkout.service.workflow.DecrementInventoryActivity;
import com.ultracommerce.core.order.service.workflow.CheckAddAvailabilityActivity;

import java.util.Collection;
import java.util.Map;
import java.util.Set;



/**
 * Marker interface to dictate the overridden methods within {@link ContextualInventoryService}. Usually, implementers
 * will want to only override the {@link ContextualInventoryService} methods rather than all of the methods included
 * in {@link InventoryService} and so you will extend from {@link AbstractInventoryServiceExtensionHandler}.
 * 
 * @author Phillip Verheyden (phillipuniverse)
 * @see {@link ContextualInventoryService}
 * @see {@link AbstractInventoryServiceExtensionHandler}
 */
public interface InventoryServiceExtensionHandler extends ExtensionHandler {

    /**
     * Usually invoked within the {@link CheckAddAvailabilityActivity} to retrieve the quantity that is available for the given
     * <b>skus</b>.
     * 
     * @param context can be null. If not null, this should at least contain the {@link ContextualInventoryService#ORDER_KEY}
     * @see {@link ContextualInventoryService#retrieveQuantitiesAvailable(Set, Map)}
     */
    public ExtensionResultStatusType retrieveQuantitiesAvailable(Collection<Sku> skus, Map<String, Object> context, ExtensionResultHolder<Map<Sku, Integer>> result);
    
    /**
     * Usually invoked within the {@link DecrementInventoryActivity} to decrement inventory for the {@link Sku}s that are in
     * <b>skuQuantities</b>
     * 
     * @param context can be null. If not null, this should at least contain the {@link ContextualInventoryService#ORDER_KEY} and/or the
     * {@link ContextualInventoryService#ROLLBACK_STATE_KEY}
     * @see {@link ContextualInventoryService#decrementInventory(Map, Map)}
     */
    public ExtensionResultStatusType decrementInventory(Map<Sku, Integer> skuQuantities, Map<String, Object> context) throws InventoryUnavailableException;

    /**
     * @param context can be null. If not null, this should at least contain the {@link ContextualInventoryService#ROLLBACK_STATE_KEY}
     * @see {@link ContextualInventoryService#incrementInventory(Map, Map)}
     */
    public ExtensionResultStatusType incrementInventory(Map<Sku, Integer> skuQuantities, Map<String, Object> context);

    /**
     * Usually invoked via the OMS {@link ReconcileInventoryChangeOrderActivity} to determine how to handle a change order.
     * @param decrementSkuQuantities
     * @param incrementSkuQuantities
     * @param context
     * @return
     * @throws InventoryUnavailableException
     */
    public ExtensionResultStatusType reconcileChangeOrderInventory(Map<Sku, Integer> decrementSkuQuantities, Map<Sku, Integer> incrementSkuQuantities, Map<String, Object> context) throws InventoryUnavailableException;

    /**
     * Usually invoked via the AdvancedProduct to determine the availability of product bundle.
     * @param product contains Product
     * @param quantity
     * @param holder
     */
    ExtensionResultStatusType isProductBundleAvailable(Product product, int quantity, ExtensionResultHolder<Boolean> holder);
}
