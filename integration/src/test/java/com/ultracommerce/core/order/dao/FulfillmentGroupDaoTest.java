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

import com.ultracommerce.core.order.FulfillmentGroupDataProvider;
import com.ultracommerce.core.order.domain.FulfillmentGroup;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.service.FulfillmentGroupService;
import com.ultracommerce.profile.core.dao.CustomerAddressDao;
import com.ultracommerce.profile.core.domain.Address;
import com.ultracommerce.profile.core.domain.Customer;
import com.ultracommerce.test.CommonSetupBaseTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import javax.annotation.Resource;

public class FulfillmentGroupDaoTest extends CommonSetupBaseTest {

    private Long defaultFulfillmentGroupOrderId;
    private Long defaultFulfillmentGroupId;
    private Long fulfillmentGroupId;

    @Resource
    private FulfillmentGroupDao fulfillmentGroupDao;
    
    @Resource
    private FulfillmentGroupService fulfillmentGroupService;

    @Resource
    private CustomerAddressDao customerAddressDao;

    @Resource
    private OrderDao orderDao;

    @Test(groups = "createDefaultFulfillmentGroup", dataProvider = "basicFulfillmentGroup", dataProviderClass = FulfillmentGroupDataProvider.class)
    @Transactional
    @Rollback(false)
    public void createDefaultFulfillmentGroup(FulfillmentGroup fulfillmentGroup) {
        Customer customer = createCustomerWithBasicOrderAndAddresses();
        Address address = (customerAddressDao.readActiveCustomerAddressesByCustomerId(customer.getId())).get(0).getAddress();
        Order salesOrder = orderDao.readOrdersForCustomer(customer.getId()).get(0);

        FulfillmentGroup newFG = fulfillmentGroupDao.createDefault();
        newFG.setAddress(address);
        newFG.setRetailShippingPrice(fulfillmentGroup.getRetailShippingPrice());
        newFG.setMethod(fulfillmentGroup.getMethod());
        newFG.setService(fulfillmentGroup.getService());
        newFG.setOrder(salesOrder);
        newFG.setReferenceNumber(fulfillmentGroup.getReferenceNumber());

        assert newFG.getId() == null;
        fulfillmentGroup = fulfillmentGroupService.save(newFG);
        assert fulfillmentGroup.getId() != null;
        defaultFulfillmentGroupOrderId = salesOrder.getId();
        defaultFulfillmentGroupId = fulfillmentGroup.getId();
    }

    @Test(groups = { "readDefaultFulfillmentGroupForOrder" }, dependsOnGroups = { "createDefaultFulfillmentGroup" })
    @Transactional
    public void readDefaultFulfillmentGroupForOrder() {
        Order order = orderDao.readOrderById(defaultFulfillmentGroupOrderId);
        assert order != null;
        assert order.getId() == defaultFulfillmentGroupOrderId;
        FulfillmentGroup fg = fulfillmentGroupDao.readDefaultFulfillmentGroupForOrder(order);
        assert fg.getId() != null;
        assert fg.getId().equals(defaultFulfillmentGroupId);
    }

    @Test(groups = { "readDefaultFulfillmentGroupForId" }, dependsOnGroups = { "createDefaultFulfillmentGroup" })
    @Transactional
    public void readDefaultFulfillmentGroupForId() {
        FulfillmentGroup fg = fulfillmentGroupDao.readFulfillmentGroupById(defaultFulfillmentGroupId);
        assert fg != null;
        assert fg.getId() != null;
        assert fg.getId().equals(defaultFulfillmentGroupId);
    }

    @Test(groups = "createFulfillmentGroup", dataProvider = "basicFulfillmentGroup", dataProviderClass = FulfillmentGroupDataProvider.class)
    @Transactional
    @Rollback(false)
    public void createFulfillmentGroup(FulfillmentGroup fulfillmentGroup) {
        Customer customer = createCustomerWithBasicOrderAndAddresses();
        Address address = (customerAddressDao.readActiveCustomerAddressesByCustomerId(customer.getId())).get(0).getAddress();
        Order salesOrder = orderDao.readOrdersForCustomer(customer.getId()).get(0);

        FulfillmentGroup newFG = fulfillmentGroupDao.create();
        newFG.setAddress(address);
        newFG.setRetailShippingPrice(fulfillmentGroup.getRetailShippingPrice());
        newFG.setMethod(fulfillmentGroup.getMethod());
        newFG.setService(fulfillmentGroup.getService());
        newFG.setReferenceNumber(fulfillmentGroup.getReferenceNumber());
        newFG.setOrder(salesOrder);

        assert newFG.getId() == null;
        fulfillmentGroup = fulfillmentGroupService.save(newFG);
        assert fulfillmentGroup.getId() != null;
        fulfillmentGroupId = fulfillmentGroup.getId();
    }

    @Test(groups = { "readFulfillmentGroupsForId" }, dependsOnGroups = { "createFulfillmentGroup" })
    @Transactional
    public void readFulfillmentGroupsForId() {
        FulfillmentGroup fg = fulfillmentGroupDao.readFulfillmentGroupById(fulfillmentGroupId);
        assert fg != null;
        assert fg.getId() != null;
    }
}
