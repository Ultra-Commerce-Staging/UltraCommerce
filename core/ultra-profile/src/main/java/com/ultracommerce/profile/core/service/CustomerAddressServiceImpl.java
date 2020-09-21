/*
 * #%L
 * UltraCommerce Profile
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
package com.ultracommerce.profile.core.service;

import org.apache.commons.collections4.CollectionUtils;
import com.ultracommerce.common.util.TransactionUtils;
import com.ultracommerce.profile.core.dao.CustomerAddressDao;
import com.ultracommerce.profile.core.domain.Customer;
import com.ultracommerce.profile.core.domain.CustomerAddress;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.annotation.Resource;

@Service("ucCustomerAddressService")
public class CustomerAddressServiceImpl implements CustomerAddressService {

    @Resource(name="ucCustomerAddressDao")
    protected CustomerAddressDao customerAddressDao;

    @Override
    @Transactional(TransactionUtils.DEFAULT_TRANSACTION_MANAGER)
    public CustomerAddress saveCustomerAddress(CustomerAddress customerAddress) {
        Customer customer = customerAddress.getCustomer();

        List<CustomerAddress> activeCustomerAddresses = readActiveCustomerAddressesByCustomerId(customer.getId());
        if (CollectionUtils.isEmpty(activeCustomerAddresses)) {
            customerAddress.getAddress().setDefault(true);
        }

        customerAddress = customerAddressDao.save(customerAddress);

        // if parameter address is set as default, unset all other default addresses
        if (customerAddress.getAddress().isDefault()) {
            customerAddressDao.makeCustomerAddressDefault(customerAddress.getId(), customer.getId());
        }

        return customerAddress;
    }

    @Override
    public List<CustomerAddress> readActiveCustomerAddressesByCustomerId(Long customerId) {
        return customerAddressDao.readActiveCustomerAddressesByCustomerId(customerId);
    }

    @Override
    public CustomerAddress readCustomerAddressById(Long customerAddressId) {
        return customerAddressDao.readCustomerAddressById(customerAddressId);
    }

    @Override
    @Transactional(TransactionUtils.DEFAULT_TRANSACTION_MANAGER)
    public void makeCustomerAddressDefault(Long customerAddressId, Long customerId) {
        customerAddressDao.makeCustomerAddressDefault(customerAddressId, customerId);
    }

    @Override
    @Transactional(TransactionUtils.DEFAULT_TRANSACTION_MANAGER)
    public void deleteCustomerAddressById(Long customerAddressId){
        customerAddressDao.deleteCustomerAddressById(customerAddressId);
    }

    @Override
    public CustomerAddress findDefaultCustomerAddress(Long customerId) {
        return customerAddressDao.findDefaultCustomerAddress(customerId);
    }

    @Override
    public CustomerAddress create() {
        return customerAddressDao.create();
    }

    @Override
    public List<CustomerAddress> readBatchAddresses(int start, int pageSize) {
        return customerAddressDao.readBatchCustomerAddresses(start, pageSize);
    }

    @Override
    public Long readNumberOfAddresses() {
        return customerAddressDao.readNumberOfAddresses();
    }
}
