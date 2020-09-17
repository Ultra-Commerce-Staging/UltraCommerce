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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.ultracommerce.core.store.dao.StoreDao;
import com.ultracommerce.core.store.domain.Store;
import com.ultracommerce.core.store.domain.ZipCode;
import com.ultracommerce.profile.core.domain.Address;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("ucStoreService")
public class StoreServiceImpl implements StoreService {

    // private final static int MAXIMUM_DISTANCE = Integer.valueOf(25);
    @Resource(name = "ucStoreDao")
    private StoreDao storeDao;

    @Resource(name = "ucZipCodeService")
    private ZipCodeService zipCodeService;

    public Store readStoreById(Long id) {
        return storeDao.readStoreById(id);
    }

    public Store readStoreByStoreName(String storeName) {
        return storeDao.readStoreByStoreName(storeName);
    }

    public Store readStoreByStoreCode(String storeCode) {
        return storeDao.readStoreByStoreCode(storeCode);
    }

    public List<Store> readAllStores() {
        return storeDao.readAllStores();
    }

    public List<Store> readAllStoresByState(String state) {
        return storeDao.readAllStoresByState(state);
    }

    @Override
    @Transactional("ucTransactionManager")
    public Store saveStore(Store store) {
        return storeDao.save(store);
    }

    public Map<Store, Double> findStoresByAddress(Address searchAddress, double distance) {
        Map<Store, Double> matchingStores = new HashMap<Store, Double>();
        for (Store store : readAllStores()) {
            Double storeDistance = findStoreDistance(store, Integer.parseInt(searchAddress.getPostalCode()));
            if (storeDistance != null && storeDistance <= distance) {
                matchingStores.put(store, storeDistance);
            }
        }

        return matchingStores;
    }

    private Double findStoreDistance(Store store, Integer zip) {
        ZipCode zipCode = zipCodeService.findZipCodeByZipCode(zip);
        if (zipCode == null) {
            return null;
        }
        // A constant used to convert from degrees to radians.
        double degreesToRadians = 57.3;
        double storeDistance = 3959 * Math.acos((Math.sin(zipCode.getZipLatitude() / degreesToRadians) * Math.sin(store.getLatitude() / degreesToRadians))
                + (Math.cos(zipCode.getZipLatitude() / degreesToRadians) * Math.cos(store.getLatitude() / degreesToRadians) * Math.cos((store.getLongitude() / degreesToRadians) - (zipCode.getZipLongitude() / degreesToRadians))));
        return storeDistance;
    }
}
