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

import com.ultracommerce.common.classloader.release.ThreadLocalManager;
import com.ultracommerce.common.exception.ExceptionHelper;
import com.ultracommerce.common.money.Money;
import com.ultracommerce.core.catalog.domain.Sku;
import com.ultracommerce.core.catalog.domain.SkuImpl;
import com.ultracommerce.core.catalog.domain.pricing.SkuPriceWrapper;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Convenient place to store the pricing considerations context and the pricing service on thread local. This class is
 * usually filled out by a DynamicSkuPricingFilter. The default implementation of this is DefaultDynamicSkuPricingFilter.
 * 
 * @author jfischer
 * @see {@link SkuImpl#getRetailPrice}
 * @see {@link SkuImpl#getSalePrice}
 */
public class SkuPricingConsiderationContext {

    protected static final ConcurrentHashMap<String, Field> FIELD_CACHE = new ConcurrentHashMap<>();
    private static final ThreadLocal<SkuPricingConsiderationContext> skuPricingConsiderationContext = ThreadLocalManager.createThreadLocal(SkuPricingConsiderationContext.class);

    public static HashMap getSkuPricingConsiderationContext() {
        return SkuPricingConsiderationContext.skuPricingConsiderationContext.get().considerations;
    }
    
    public static void setSkuPricingConsiderationContext(HashMap skuPricingConsiderations) {
        SkuPricingConsiderationContext.skuPricingConsiderationContext.get().considerations = skuPricingConsiderations;
    }

    public static DynamicSkuPricingService getSkuPricingService() {
        return SkuPricingConsiderationContext.skuPricingConsiderationContext.get().pricingService;
    }
    
    public static void setSkuPricingService(DynamicSkuPricingService skuPricingService) {
        SkuPricingConsiderationContext.skuPricingConsiderationContext.get().pricingService = skuPricingService;
    }

    public static void startPricingConsideration() {
        SkuPricingConsiderationContext.skuPricingConsiderationContext.get().isActive = true;
    }

    public static void endPricingConsideration() {
        SkuPricingConsiderationContext.skuPricingConsiderationContext.get().isActive = false;
    }

    public static boolean isPricingConsiderationActive() {
        return SkuPricingConsiderationContext.skuPricingConsiderationContext.get().isActive;
    }
    
    public static boolean hasDynamicPricing() {
        return (
            getSkuPricingConsiderationContext() != null &&
            getSkuPricingConsiderationContext().size() >= 0 &&
            getSkuPricingService() != null
        );
    }

    public static Map<Long, DynamicSkuPrices> getThreadCache() {
        return SkuPricingConsiderationContext.skuPricingConsiderationContext.get().pricesBySku;
    }

    public static void clearThreadCache() {
        SkuPricingConsiderationContext.skuPricingConsiderationContext.get().pricesBySku.clear();
    }

    public static void removeFromThreadCache(Long skuId) {
        SkuPricingConsiderationContext.skuPricingConsiderationContext.get().pricesBySku.remove(skuId);
    }

    public static DynamicSkuPrices getDynamicSkuPrices(Sku sku) {
        DynamicSkuPrices prices = null;
        if (SkuPricingConsiderationContext.hasDynamicPricing()) {
            if (!getThreadCache().containsKey(sku.getId())) {
                // We have dynamic pricing, so we will pull the retail price from there
                if (!SkuPricingConsiderationContext.isPricingConsiderationActive()) {
                    SkuPriceWrapper wrapper = new SkuPriceWrapper(sku);
                    SkuPricingConsiderationContext.startPricingConsideration();
                    try {
                        prices = SkuPricingConsiderationContext.getSkuPricingService().getSkuPrices(wrapper, SkuPricingConsiderationContext.getSkuPricingConsiderationContext());
                    } finally {
                        SkuPricingConsiderationContext.endPricingConsideration();
                    }
                } else {
                    try {
                        prices = new DynamicSkuPrices();
                        Field retail = getSingleField(sku.getClass(), "retailPrice");
                        Object retailVal = retail.get(sku);
                        Money retailPrice = retailVal == null ? null : new Money((BigDecimal) retailVal);
                        Field sale = getSingleField(sku.getClass(), "salePrice");
                        Object saleVal = sale.get(sku);
                        Money salePrice = saleVal == null ? null : new Money((BigDecimal) saleVal);
                        prices.setRetailPrice(retailPrice);
                        prices.setSalePrice(salePrice);
                    } catch (IllegalAccessException e) {
                        throw ExceptionHelper.refineException(e);
                    }
                }
                getThreadCache().put(sku.getId(), prices);
            } else {
                prices = getThreadCache().get(sku.getId());
            }
        }
        return prices;
    }

    protected static synchronized Field getSingleField(Class<?> clazz, String fieldName) throws IllegalStateException {
        String cacheKey = clazz.getName() + fieldName;
        if (FIELD_CACHE.containsKey(cacheKey)) {
            return FIELD_CACHE.get(cacheKey);
        }

        Field field = ReflectionUtils.findField(clazz, fieldName);
        if (field != null) {
            field.setAccessible(true);
        }

        FIELD_CACHE.put(cacheKey, field);

        return field;
    }

    protected DynamicSkuPricingService pricingService;
    protected HashMap considerations;
    protected boolean isActive = false;
    protected HashMap<Long, DynamicSkuPrices> pricesBySku = new HashMap<>();
}
