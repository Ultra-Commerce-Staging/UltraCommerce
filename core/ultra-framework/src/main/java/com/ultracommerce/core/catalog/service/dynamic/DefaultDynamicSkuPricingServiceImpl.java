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
package com.ultracommerce.core.catalog.service.dynamic;

import com.ultracommerce.common.money.Money;
import com.ultracommerce.core.catalog.domain.ProductOptionValueImpl;
import com.ultracommerce.core.catalog.domain.Sku;
import com.ultracommerce.core.catalog.domain.SkuBundleItem;
import com.ultracommerce.core.catalog.domain.pricing.SkuPriceWrapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Default implementation of the {@link DynamicSkuPricingService} which simply ignores the considerations hashmap in all
 * method implementations
 * 
 * @author jfischer
 * 
 */
@Service("ucDynamicSkuPricingService")
public class DefaultDynamicSkuPricingServiceImpl implements DynamicSkuPricingService {

    @Override
    @Deprecated
    public DynamicSkuPrices getSkuPrices(Sku sku, HashMap skuPricingConsiderations) {
        SkuPriceWrapper wrapper = new SkuPriceWrapper(sku);
        return getSkuPrices(wrapper, skuPricingConsiderations);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public DynamicSkuPrices getSkuPrices(SkuPriceWrapper skuWrapper, HashMap skuPricingConsiderations) {
        Sku sku = skuWrapper.getTargetSku();
        
        DynamicSkuPrices prices = new DynamicSkuPrices();
        prices.setRetailPrice(sku.getRetailPrice());
        prices.setSalePrice(sku.getSalePrice());
        prices.setPriceAdjustment(sku.getProductOptionValueAdjustments());
        return prices;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public DynamicSkuPrices getSkuBundleItemPrice(SkuBundleItem skuBundleItem,
            HashMap skuPricingConsiderations) {
        DynamicSkuPrices prices = new DynamicSkuPrices();
        prices.setSalePrice(skuBundleItem.getSalePrice());
        return prices;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public DynamicSkuPrices getPriceAdjustment(ProductOptionValueImpl productOptionValueImpl, Money priceAdjustment,
            HashMap skuPricingConsiderationContext) {
        DynamicSkuPrices prices = new DynamicSkuPrices();

        prices.setPriceAdjustment(priceAdjustment);
        return prices;
    }

}
