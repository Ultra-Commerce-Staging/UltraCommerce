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

import com.ultracommerce.common.util.TransactionUtils;
import com.ultracommerce.profile.core.dao.CustomerPhoneDao;
import com.ultracommerce.profile.core.domain.CustomerPhone;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.annotation.Resource;

@Service("ucCustomerPhoneService")
public class CustomerPhoneServiceImpl implements CustomerPhoneService {

    @Resource(name="ucCustomerPhoneDao")
    protected CustomerPhoneDao customerPhoneDao;

    @Override
    @Transactional(TransactionUtils.DEFAULT_TRANSACTION_MANAGER)
    public CustomerPhone saveCustomerPhone(CustomerPhone customerPhone) {
        List<CustomerPhone> activeCustomerPhones = readActiveCustomerPhonesByCustomerId(customerPhone.getCustomer().getId());
        if (activeCustomerPhones != null && activeCustomerPhones.isEmpty()) {
            customerPhone.getPhone().setDefault(true);
        } else {
            // if parameter customerPhone is set as default, unset all other default phones
            if (customerPhone.getPhone().isDefault()) {
                for (CustomerPhone activeCustomerPhone : activeCustomerPhones) {
                    if (!activeCustomerPhone.getId().equals(customerPhone.getId()) && activeCustomerPhone.getPhone().isDefault()) {
                        activeCustomerPhone.getPhone().setDefault(false);
                        customerPhoneDao.save(activeCustomerPhone);
                    }
                }
            }
        }
        return customerPhoneDao.save(customerPhone);
    }

    @Override
    public List<CustomerPhone> readActiveCustomerPhonesByCustomerId(Long customerId) {
        return customerPhoneDao.readActiveCustomerPhonesByCustomerId(customerId);
    }

    @Override
    public CustomerPhone readCustomerPhoneById(Long customerPhoneId) {
        return customerPhoneDao.readCustomerPhoneById(customerPhoneId);
    }

    @Override
    @Transactional(TransactionUtils.DEFAULT_TRANSACTION_MANAGER)
    public void makeCustomerPhoneDefault(Long customerPhoneId, Long customerId) {
        customerPhoneDao.makeCustomerPhoneDefault(customerPhoneId, customerId);
    }

    @Override
    @Transactional(TransactionUtils.DEFAULT_TRANSACTION_MANAGER)
    public void deleteCustomerPhoneById(Long customerPhoneId){
        customerPhoneDao.deleteCustomerPhoneById(customerPhoneId);
    }

    @Override
    public CustomerPhone findDefaultCustomerPhone(Long customerId) {
        return customerPhoneDao.findDefaultCustomerPhone(customerId);
    }

    @Override
    public List<CustomerPhone> readAllCustomerPhonesByCustomerId(Long customerId) {
        return customerPhoneDao.readAllCustomerPhonesByCustomerId(customerId);
    }

    @Override
    public CustomerPhone create() {
        return customerPhoneDao.create();
    }

}
