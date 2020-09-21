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
package com.ultracommerce.core.order.domain;

import com.ultracommerce.common.money.Money;
import com.ultracommerce.core.catalog.domain.Product;
import com.ultracommerce.core.catalog.domain.ProductBundle;
import com.ultracommerce.core.catalog.domain.Sku;

import java.util.List;

/**
 * @deprecated instead, see the ProductType Module's Product Add-On's usage of {@link DiscreteOrderItem}s
 */
@Deprecated
public interface BundleOrderItem extends OrderItem, OrderItemContainer, SkuAccessor {

    List<DiscreteOrderItem> getDiscreteOrderItems();

    void setDiscreteOrderItems(List<DiscreteOrderItem> discreteOrderItems);

    Money getTaxablePrice();
    
    public List<BundleOrderItemFeePrice> getBundleOrderItemFeePrices();

    public void setBundleOrderItemFeePrices(List<BundleOrderItemFeePrice> bundleOrderItemFeePrices);

    public boolean hasAdjustedItems();

    public Money getBaseRetailPrice();

    public void setBaseRetailPrice(Money baseRetailPrice);

    public Money getBaseSalePrice();

    public void setBaseSalePrice(Money baseSalePrice);

    /**
     * For BundleOrderItem created from a ProductBundle, this will represent the default sku of
     * the product bundle.
     *
     * This can be null for implementations that programatically create product bundles.
     *
     * @return
     */
    Sku getSku();

    void setSku(Sku sku);

    /**
     * Returns the associated ProductBundle or null if not applicable.
     *
     * If null, then this ProductBundle was manually created.
     *
     * @return
     */
    ProductBundle getProductBundle();

    /**
     * Sets the ProductBundle associated with this BundleOrderItem.
     *
     * @param bundle
     */
    void setProductBundle(ProductBundle bundle);

    /**
     * Same as getProductBundle.
     */
    Product getProduct();

    public boolean shouldSumItems();
}
