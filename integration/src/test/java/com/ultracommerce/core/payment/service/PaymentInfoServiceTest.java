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
package com.ultracommerce.core.payment.service;

import com.ultracommerce.common.payment.PaymentType;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.service.OrderService;
import com.ultracommerce.core.payment.PaymentInfoDataProvider;
import com.ultracommerce.core.payment.domain.OrderPayment;
import com.ultracommerce.profile.core.dao.CustomerAddressDao;
import com.ultracommerce.profile.core.domain.Address;
import com.ultracommerce.profile.core.domain.Customer;
import com.ultracommerce.profile.core.domain.CustomerAddress;
import com.ultracommerce.profile.core.service.CustomerService;
import com.ultracommerce.test.TestNGSiteIntegrationSetup;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import java.util.List;

import javax.annotation.Resource;

public class PaymentInfoServiceTest extends TestNGSiteIntegrationSetup {

    String userName = new String();
    private OrderPayment paymentInfo;

    @Resource
    private OrderPaymentService paymentInfoService;

    @Resource(name = "ucOrderService")
    private OrderService orderService;

    @Resource
    private CustomerAddressDao customerAddressDao;

    @Resource
    private CustomerService customerService;

    @Test(groups={"createPaymentInfo"}, dataProvider="basicPaymentInfo", dataProviderClass=PaymentInfoDataProvider.class, dependsOnGroups={"readCustomer", "createOrder"})
    @Rollback(false)
    @Transactional
    public void createPayment(OrderPayment payment){
        userName = "customer1";
        Customer customer = customerService.readCustomerByUsername(userName);
        List<CustomerAddress> addresses = customerAddressDao.readActiveCustomerAddressesByCustomerId(customer.getId());
        Address address = null;
        if (!addresses.isEmpty())
            address = addresses.get(0).getAddress();
        Order salesOrder = orderService.createNewCartForCustomer(customer);

        payment.setBillingAddress(address);
        payment.setOrder(salesOrder);
        payment.setType(PaymentType.CREDIT_CARD);

        assert payment.getId() == null;
        payment = paymentInfoService.save(payment);
        assert payment.getId() != null;
        this.paymentInfo = payment;
    }

    @Test(groups={"readPaymentInfoById"}, dependsOnGroups={"createPaymentInfo"})
    public void readPaymentInfoById(){
        OrderPayment sop = paymentInfoService.readPaymentById(paymentInfo.getId());
        assert sop !=null;
        assert sop.getId().equals(paymentInfo.getId());
    }

    @Test(groups={"readPaymentInfosByOrder"}, dependsOnGroups={"createPaymentInfo"})
    @Transactional
    public void readPaymentInfoByOrder(){
        List<OrderPayment> payments = paymentInfoService.readPaymentsForOrder(paymentInfo.getOrder());
        assert payments != null;
        assert payments.size() > 0;
    }

    @Test(groups={"testCreatePaymentInfo"}, dependsOnGroups={"createPaymentInfo"})
    @Transactional
    public void createTestPayment(){
        userName = "customer1";
        OrderPayment paymentInfo = paymentInfoService.create();
        Customer customer = customerService.readCustomerByUsername(userName);
        List<CustomerAddress> addresses = customerAddressDao.readActiveCustomerAddressesByCustomerId(customer.getId());
        Address address = null;
        if (!addresses.isEmpty())
            address = addresses.get(0).getAddress();
        Order salesOrder = orderService.findCartForCustomer(customer);

        paymentInfo.setBillingAddress(address);
        paymentInfo.setOrder(salesOrder);
        paymentInfo.setType(PaymentType.CREDIT_CARD);

        assert paymentInfo != null;
        paymentInfo = paymentInfoService.save(paymentInfo);
        assert paymentInfo.getId() != null;
        Long paymentInfoId = paymentInfo.getId();
        paymentInfoService.delete(paymentInfo);
        paymentInfo = paymentInfoService.readPaymentById(paymentInfoId);
        assert paymentInfo == null;
    }

}
