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
package com.ultracommerce.core.store.service;

import java.util.List;
import java.util.Map;

import com.ultracommerce.core.store.domain.Store;
import com.ultracommerce.profile.core.domain.Address;

public interface StoreService {

    public Store readStoreById(Long id);

    public Store readStoreByStoreName(String storeName);

    /**
     * @deprecated use {@link #readStoreByStoreName(String)} instead.
     *
     * @param storeCode
     * @return
     */
    @Deprecated
    public Store readStoreByStoreCode(String storeCode);

    public Store saveStore(Store store);

    public Map<Store,Double> findStoresByAddress(Address searchAddress, double distance);

    public List<Store> readAllStores();

    public List<Store> readAllStoresByState(String state);

}
