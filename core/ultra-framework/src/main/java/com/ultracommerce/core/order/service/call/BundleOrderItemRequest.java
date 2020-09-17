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
import com.ultracommerce.core.order.domain.BundleOrderItemFeePrice;
import com.ultracommerce.core.order.domain.Order;

import java.util.ArrayList;
import java.util.List;

public class BundleOrderItemRequest {

    protected String name;
    protected Category category;
    protected int quantity;
    protected Order order;
    protected List<DiscreteOrderItemRequest> discreteOrderItems = new ArrayList<DiscreteOrderItemRequest>();
    protected List<BundleOrderItemFeePrice> bundleOrderItemFeePrices = new ArrayList<BundleOrderItemFeePrice>();
    protected Money salePriceOverride;
    protected Money retailPriceOverride;

    
    public Order getOrder() {
        return order;
    }

    
    public void setOrder(Order order) {
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<DiscreteOrderItemRequest> getDiscreteOrderItems() {
        return discreteOrderItems;
    }

    public void setDiscreteOrderItems(List<DiscreteOrderItemRequest> discreteOrderItems) {
        this.discreteOrderItems = discreteOrderItems;
    }

    public List<BundleOrderItemFeePrice> getBundleOrderItemFeePrices() {
        return bundleOrderItemFeePrices;
    }

    public void setBundleOrderItemFeePrices(
            List<BundleOrderItemFeePrice> bundleOrderItemFeePrices) {
        this.bundleOrderItemFeePrices = bundleOrderItemFeePrices;
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!getClass().isAssignableFrom(obj.getClass()))
            return false;
        BundleOrderItemRequest other = (BundleOrderItemRequest) obj;
        if (category == null) {
            if (other.category != null)
                return false;
        } else if (!category.equals(other.category))
            return false;
        if (discreteOrderItems == null) {
            if (other.discreteOrderItems != null)
                return false;
        } else if (!discreteOrderItems.equals(other.discreteOrderItems))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (quantity != other.quantity)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((category == null) ? 0 : category.hashCode());
        result = prime * result + ((discreteOrderItems == null) ? 0 : discreteOrderItems.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + quantity;
        return result;
    }
}
