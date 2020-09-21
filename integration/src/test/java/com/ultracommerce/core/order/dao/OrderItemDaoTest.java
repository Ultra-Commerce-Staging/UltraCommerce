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
package com.ultracommerce.core.order.dao;

import com.ultracommerce.core.catalog.dao.SkuDao;
import com.ultracommerce.core.catalog.domain.Sku;
import com.ultracommerce.core.order.OrderItemDataProvider;
import com.ultracommerce.core.order.domain.DiscreteOrderItem;
import com.ultracommerce.core.order.domain.GiftWrapOrderItem;
import com.ultracommerce.core.order.domain.OrderItem;
import com.ultracommerce.test.TestNGSiteIntegrationSetup;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import javax.annotation.Resource;

public class OrderItemDaoTest extends TestNGSiteIntegrationSetup {

    private Long orderItemId;
    private Long giftWrapItemId;

    @Resource
    private OrderItemDao orderItemDao;

    @Resource
    private SkuDao skuDao;

    @Test(groups = { "createDiscreteOrderItem" }, dataProvider = "basicDiscreteOrderItem", dataProviderClass = OrderItemDataProvider.class, dependsOnGroups = { "createOrder", "createSku" })
    @Rollback(false)
    @Transactional
    public void createDiscreteOrderItem(DiscreteOrderItem orderItem) {
        Sku si = skuDao.readFirstSku();
        assert si.getId() != null;
        orderItem.setSku(si);
        assert orderItem.getId() == null;
        
        orderItem = (DiscreteOrderItem) orderItemDao.save(orderItem);
        assert orderItem.getId() != null;
        orderItemId = orderItem.getId();
    }

    @Test(groups = { "createGiftWrapOrderItem" }, dataProvider = "basicGiftWrapOrderItem", dataProviderClass = OrderItemDataProvider.class, dependsOnGroups = { "readOrderItemsById" })
    @Rollback(false)
    @Transactional
    public void createGiftWrapOrderItem(GiftWrapOrderItem orderItem) {
        Sku si = skuDao.readFirstSku();
        assert si.getId() != null;
        orderItem.setSku(si);
        assert orderItem.getId() == null;

        OrderItem discreteItem = orderItemDao.readOrderItemById(orderItemId);
        orderItem.getWrappedItems().add(discreteItem);
        discreteItem.setGiftWrapOrderItem(orderItem);

        orderItem = (GiftWrapOrderItem) orderItemDao.save(orderItem);
        assert orderItem.getId() != null;
        giftWrapItemId = orderItem.getId();
    }

    @Test(groups = { "readGiftWrapOrderItemsById" }, dependsOnGroups = { "createGiftWrapOrderItem" })
    @Transactional
    public void readGiftWrapOrderItemsById() {
        assert giftWrapItemId != null;
        OrderItem result = orderItemDao.readOrderItemById(giftWrapItemId);
        assert result != null;
        assert result.getId().equals(giftWrapItemId);
        assert ((GiftWrapOrderItem) result).getWrappedItems().get(0).getId().equals(orderItemId);
    }

    @Test(groups = { "deleteGiftWrapOrderItemsById" }, dependsOnGroups = { "readGiftWrapOrderItemsById" })
    @Rollback(false)
    public void deleteGiftWrapOrderItemsById() {
        OrderItem result = orderItemDao.readOrderItemById(giftWrapItemId);
        orderItemDao.delete(result);
        assert orderItemDao.readOrderItemById(giftWrapItemId) == null;
    }

    @Test(groups = { "readOrderItemsById" }, dependsOnGroups = { "createDiscreteOrderItem" })
    public void readOrderItemsById() {
        assert orderItemId != null;
        OrderItem result = orderItemDao.readOrderItemById(orderItemId);
        assert result != null;
        assert result.getId().equals(orderItemId);
    }

    @Test(groups = { "readOrderItemsByIdAfterGiftWrapDeletion" }, dependsOnGroups = { "deleteGiftWrapOrderItemsById" })
    public void readOrderItemsByIdAfterGiftWrapDeletion() {
        assert orderItemId != null;
        OrderItem result = orderItemDao.readOrderItemById(orderItemId);
        assert result != null;
        assert result.getId().equals(orderItemId);
        assert result.getGiftWrapOrderItem() == null;
    }
}
