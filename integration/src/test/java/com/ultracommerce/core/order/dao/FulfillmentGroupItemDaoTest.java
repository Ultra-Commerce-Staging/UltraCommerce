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
import com.ultracommerce.core.order.FulfillmentGroupDataProvider;
import com.ultracommerce.core.order.OrderItemDataProvider;
import com.ultracommerce.core.order.domain.DiscreteOrderItem;
import com.ultracommerce.core.order.domain.FulfillmentGroup;
import com.ultracommerce.core.order.domain.FulfillmentGroupItem;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.service.FulfillmentGroupService;
import com.ultracommerce.core.order.service.call.FulfillmentGroupItemRequest;
import com.ultracommerce.core.pricing.service.exception.PricingException;
import com.ultracommerce.profile.core.dao.CustomerAddressDao;
import com.ultracommerce.profile.core.domain.Address;
import com.ultracommerce.profile.core.domain.Customer;
import com.ultracommerce.profile.core.service.CustomerService;
import com.ultracommerce.test.TestNGSiteIntegrationSetup;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import java.util.List;

import javax.annotation.Resource;

public class FulfillmentGroupItemDaoTest extends TestNGSiteIntegrationSetup {

    private FulfillmentGroup fulfillmentGroup;
    private Order salesOrder;
    private Long fulfillmentGroupItemId;

    @Resource
    private FulfillmentGroupItemDao fulfillmentGroupItemDao;

    @Resource
    private CustomerService customerService;

    @Resource
    private OrderDao orderDao;
    
    @Resource
    private SkuDao skuDao;
    
    @Resource
    private OrderItemDao orderItemDao;
    
    @Resource
    private CustomerAddressDao customerAddressDao;
    
    @Resource
    private FulfillmentGroupDao fulfillmentGroupDao;
    
    @Resource
    private FulfillmentGroupService fulfillmentGroupService;

    @Test(groups = "createItemFulfillmentGroup", dataProvider = "basicFulfillmentGroup", dataProviderClass = FulfillmentGroupDataProvider.class, dependsOnGroups = { "createOrder", "createCustomerAddress" })
    @Rollback(false)
    @Transactional
    public void createDefaultFulfillmentGroup(FulfillmentGroup fulfillmentGroup) {
        String userName = "customer1";
        Customer customer = customerService.readCustomerByUsername(userName);
        Address address = (customerAddressDao.readActiveCustomerAddressesByCustomerId(customer.getId())).get(0).getAddress();
        salesOrder = orderDao.createNewCartForCustomer(customer);

        FulfillmentGroup newFG = fulfillmentGroupDao.createDefault();
        newFG.setAddress(address);
        newFG.setRetailShippingPrice(fulfillmentGroup.getRetailShippingPrice());
        newFG.setMethod(fulfillmentGroup.getMethod());
        newFG.setService(fulfillmentGroup.getService());
        newFG.setOrder(salesOrder);
        newFG.setReferenceNumber(fulfillmentGroup.getReferenceNumber());

        assert newFG.getId() == null;
        this.fulfillmentGroup = fulfillmentGroupService.save(newFG);
        assert this.fulfillmentGroup.getId() != null;
    }
    
    @Test(groups = { "createFulfillmentGroupItem" }, dataProvider = "basicDiscreteOrderItem", dataProviderClass = OrderItemDataProvider.class, dependsOnGroups = { "createOrder", "createSku", "createItemFulfillmentGroup" })
    @Rollback(false)
    @Transactional
    public void createFulfillmentGroupItem(DiscreteOrderItem orderItem) throws PricingException {        
        Sku si = skuDao.readFirstSku();
        orderItem.setSku(si);
        orderItem = (DiscreteOrderItem) orderItemDao.save(orderItem);
        orderItem.setOrder(salesOrder);
        salesOrder.addOrderItem(orderItem);
        orderDao.save(salesOrder);

        FulfillmentGroupItemRequest fulfillmentGroupItemRequest = new FulfillmentGroupItemRequest();
        fulfillmentGroupItemRequest.setOrderItem(orderItem);
        fulfillmentGroupItemRequest.setFulfillmentGroup(fulfillmentGroup);
        fulfillmentGroupService.addItemToFulfillmentGroup(fulfillmentGroupItemRequest, true);
        
        FulfillmentGroupItem fgi = fulfillmentGroup.getFulfillmentGroupItems().get(fulfillmentGroup.getFulfillmentGroupItems().size()-1);
        assert fgi.getId() != null;
        fulfillmentGroupItemId = fgi.getId();
    }

    @Test(groups = { "readFulfillmentGroupItemsForFulfillmentGroup" }, dependsOnGroups = { "createFulfillmentGroupItem" })
    @Transactional
    public void readFulfillmentGroupItemsForFulfillmentGroup() {
        List<FulfillmentGroupItem> fgis = fulfillmentGroupItemDao.readFulfillmentGroupItemsForFulfillmentGroup(fulfillmentGroup);
        assert fgis != null;
        assert fgis.size() > 0;
    }

    @Test(groups = { "readFulfillmentGroupItemsById" }, dependsOnGroups = { "createFulfillmentGroupItem" })
    @Transactional
    public void readFulfillmentGroupItemsById() {
        FulfillmentGroupItem fgi = fulfillmentGroupItemDao.readFulfillmentGroupItemById(fulfillmentGroupItemId);
        assert fgi != null;
        assert fgi.getId() != null;
    }
}
