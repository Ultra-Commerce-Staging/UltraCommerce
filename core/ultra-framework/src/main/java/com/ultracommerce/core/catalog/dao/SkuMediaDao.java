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
package com.ultracommerce.core.catalog.dao;

import com.ultracommerce.core.catalog.domain.SkuMediaXref;

import java.util.List;

/**
 * {@link SkuMediaDao} provides persistence access to {@link SkuMediaXref} instances
 *
 * @author Chris Kittrell (ckittrell)
 */
public interface SkuMediaDao {

    /**
     * Persist a {@link SkuMediaXref} instance to the datastore
     *
     * @param skuMediaXref the skuMediaXref to persist
     * @return the saved state of the passed in skuMediaXref
     */
    SkuMediaXref save(SkuMediaXref skuMediaXref);

    /**
     * Retrieve a list of {@link SkuMediaXref} instances by its sku id
     *
     * @param skuId the sku id of the skuMediaXref
     * @return the skuMediaXrefs with this sku id
     */
    List<SkuMediaXref> readSkuMediaBySkuId(Long skuId);

}
