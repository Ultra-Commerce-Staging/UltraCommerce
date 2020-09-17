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

import com.ultracommerce.core.catalog.domain.Sku;

import java.util.Date;

import javax.annotation.Nonnull;

/**
 * <p>Interface for dynamically determining the activity dates.</p>
 * 
 * Provides an ability to set active dates programatically.   Intended for use by add-on modules like 
 * the PriceList module which supports activeDates dates by PriceList.   
 * 
 * Even if the dates are being overridden dynamically, the master activeStart and activeEnd dates still
 * control the global activeDates of a SKU.    
 * 
 * <p>Rather than implementing this interface directly, consider sub-classing the {@link DefaultDynamicSkuActiveDatesServiceImpl}
 * and providing overrides to methods there.</p>
 * 
 * @author bpolster
 *
 */
public interface DynamicSkuActiveDatesService {

    /**
     * Returns the activeStartDate for the SKU if it has been overridden.
     * 
     * @param sku
     * @return
     */
    @Nonnull
    @SuppressWarnings("rawtypes")
    public Date getDynamicSkuActiveStartDate(Sku sku);

    /**
     * Returns the activeEndDate for the SKU if it has been overridden.
     * 
     * @param sku
     * @return
     */
    @SuppressWarnings("rawtypes")
    public Date getDynamicSkuActiveEndDate(Sku sku);
}
