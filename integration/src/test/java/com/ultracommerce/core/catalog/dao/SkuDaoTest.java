/*
 * #%L
 * UltraCommerce Integration
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

import com.ultracommerce.common.money.Money;
import com.ultracommerce.core.catalog.SkuDaoDataProvider;
import com.ultracommerce.core.catalog.domain.Sku;
import com.ultracommerce.core.catalog.service.CatalogService;
import com.ultracommerce.test.TestNGSiteIntegrationSetup;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.annotation.Resource;

public class SkuDaoTest extends TestNGSiteIntegrationSetup {

    private Long skuId;

    @Resource
    private SkuDao skuDao;
    
    @Resource
    private CatalogService catalogService;

    @Test(groups = { "createSku" }, dataProvider = "basicSku", dataProviderClass = SkuDaoDataProvider.class, dependsOnGroups = { "readCustomer", "createOrder", "createProducts" })
    @Rollback(false)
    public void createSku(Sku sku) {
        Calendar activeStartCal = Calendar.getInstance();
        activeStartCal.add(Calendar.DAY_OF_YEAR, -2);
        sku.setSalePrice(new Money(BigDecimal.valueOf(10.0)));
        sku.setRetailPrice(new Money(BigDecimal.valueOf(15.0)));
        sku.setName("test sku");
        sku.setActiveStartDate(activeStartCal.getTime());
        assert sku.getId() == null;
        sku = catalogService.saveSku(sku);
        assert sku.getId() != null;
        skuId = sku.getId();
    }

    @Test(groups = { "readFirstSku" }, dependsOnGroups = { "createSku" })
    @Transactional
    public void readFirstSku() {
        Sku si = skuDao.readFirstSku();
        assert si != null;
        assert si.getId() != null;
    }

    @Test(groups = { "readSkuById" }, dependsOnGroups = { "createSku" })
    @Transactional
    public void readSkuById() {
        Sku item = skuDao.readSkuById(skuId);
        assert item != null;
        assert item.getId() == skuId;
    }

}
