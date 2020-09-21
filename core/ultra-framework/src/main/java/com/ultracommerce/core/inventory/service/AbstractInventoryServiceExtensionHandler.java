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

import com.ultracommerce.common.extension.AbstractExtensionHandler;
import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.core.catalog.domain.Product;
import com.ultracommerce.core.catalog.domain.Sku;

import java.util.Collection;
import java.util.Map;


public abstract class AbstractInventoryServiceExtensionHandler extends AbstractExtensionHandler implements InventoryServiceExtensionHandler {

    @Override
    public ExtensionResultStatusType retrieveQuantitiesAvailable(Collection<Sku> skus, Map<String, Object> context, ExtensionResultHolder<Map<Sku, Integer>> result) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType decrementInventory(Map<Sku, Integer> skuQuantities, Map<String, Object> context) throws InventoryUnavailableException {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType incrementInventory(Map<Sku, Integer> skuQuantities, Map<String, Object> context) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType reconcileChangeOrderInventory(Map<Sku, Integer> decrementSkuQuantities, Map<Sku, Integer> incrementSkuQuantities, Map<String, Object> context) throws InventoryUnavailableException {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType isProductBundleAvailable(Product product, int quantity, ExtensionResultHolder<Boolean> holder) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }
}
