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
import com.ultracommerce.core.catalog.domain.Sku;
import com.ultracommerce.core.catalog.domain.SkuBundleItem;

import java.util.List;
import java.util.Map;

public interface DiscreteOrderItem extends OrderItem, SkuAccessor, Cloneable {

    Sku getSku();

    void setSku(Sku sku);

    Product getProduct();

    void setProduct(Product product);

    /**
     * If this item is part of a bundle, this method will return the containing bundle item.
     * @return
     */
    BundleOrderItem getBundleOrderItem();

    /**
     * Sets the parent bundle item.
     *
     * Setting to null removes this item from the bundle.
     *
     * @param bundleOrderItem
     */
    void setBundleOrderItem(BundleOrderItem bundleOrderItem);

    /**
     * If this item is part of a bundle that was created via a ProductBundle, then this
     * method returns a reference to the corresponding SkuBundleItem.
     *
     * For manually created
     *
     * For all others, this method returns null.
     *
     * @return
     */
    SkuBundleItem getSkuBundleItem();

    /**
     * Sets the associated skuBundleItem.
     *
     * @param skuBundleItem
     */
    void setSkuBundleItem(SkuBundleItem skuBundleItem);

    Money getTaxablePrice();

    /**
     * Arbitrary attributes associated with the order item
     *
     * @deprecated use getOrderItemAttributes instead
     * @return the attributes
     */
    public Map<String, String> getAdditionalAttributes();

    /**
     * Arbitrary attributes associated with the order item
     *
     * @deprecated use setOrderItemAttributes instead
     * @param additionalAttributes the map of attributes
     */
    public void setAdditionalAttributes(Map<String, String> additionalAttributes);

    public Money getBaseRetailPrice();

    public void setBaseRetailPrice(Money baseRetailPrice);

    public Money getBaseSalePrice();

    public void setBaseSalePrice(Money baseSalePrice);
    
    public List<DiscreteOrderItemFeePrice> getDiscreteOrderItemFeePrices();

    public void setDiscreteOrderItemFeePrices(List<DiscreteOrderItemFeePrice> orderItemFeePrices);

    /**
     * For items that are part of a bundle, this method will return the parent bundle item.  Otherwise,
     * returns null.
     * 
     * @return
     */
    public BundleOrderItem findParentItem();

    /**
     * Returns a boolean indicating whether this sku is active.  This is used to determine whether a user
     * the sku can add the sku to their cart.
     */
    public boolean isSkuActive();
}
