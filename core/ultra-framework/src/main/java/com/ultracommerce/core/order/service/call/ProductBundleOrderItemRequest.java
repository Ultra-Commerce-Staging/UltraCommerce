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
package com.ultracommerce.core.order.service.call;

import com.ultracommerce.common.money.Money;
import com.ultracommerce.core.catalog.domain.Category;
import com.ultracommerce.core.catalog.domain.ProductBundle;
import com.ultracommerce.core.catalog.domain.Sku;
import com.ultracommerce.core.order.domain.Order;

import java.util.HashMap;
import java.util.Map;

public class ProductBundleOrderItemRequest {

    protected String name;
    protected Category category;
    protected Sku sku;
    protected Order order;
    protected int quantity;
    protected ProductBundle productBundle;
    private Map<String,String> itemAttributes = new HashMap<String,String>();
    protected Money salePriceOverride;
    protected Money retailPriceOverride;

    public ProductBundleOrderItemRequest() {}
    
    public String getName() {
        return name;
    }

    public ProductBundleOrderItemRequest setName(String name) {
        this.name = name;
        return this;
    }

    public Category getCategory() {
        return category;
    }

    public ProductBundleOrderItemRequest setCategory(Category category) {
        this.category = category;
        return this;
    }
    
    public Sku getSku() {
        return sku;
    }

    public ProductBundleOrderItemRequest setSku(Sku sku) {
        this.sku = sku;
        return this;
    }
    
    public ProductBundleOrderItemRequest setOrder(Order order) {
        this.order = order;
        return this;
    }
    
    public Order getOrder() {
        return order;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductBundleOrderItemRequest setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public ProductBundle getProductBundle() {
        return productBundle;
    }

    public ProductBundleOrderItemRequest setProductBundle(ProductBundle productBundle) {
        this.productBundle = productBundle;
        return this;
    }

    public Map<String, String> getItemAttributes() {
        return itemAttributes;
    }

    public ProductBundleOrderItemRequest setItemAttributes(Map<String, String> itemAttributes) {
        this.itemAttributes = itemAttributes;
        return this;
    }

    public Money getSalePriceOverride() {
        return salePriceOverride;
    }

    public void setSalePriceOverride(Money salePriceOverride) {
        this.salePriceOverride = salePriceOverride;
    }

    public Money getRetailPriceOverride() {
        return retailPriceOverride;
    }

    public void setRetailPriceOverride(Money retailPriceOverride) {
        this.retailPriceOverride = retailPriceOverride;
    }

}
