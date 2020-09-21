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

import com.ultracommerce.profile.core.dao.CustomerPaymentDao;
import com.ultracommerce.profile.core.domain.Customer;
import com.ultracommerce.profile.core.domain.CustomerPayment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.annotation.Resource;

@Service("ucCustomerPaymentService")
public class CustomerPaymentServiceImpl implements CustomerPaymentService {

    /** Services */
    @Resource(name="ucCustomerPaymentDao")
    protected CustomerPaymentDao customerPaymentDao;

    @Resource(name="ucCustomerService")
    protected CustomerService customerService;

    @Override
    @Transactional("ucTransactionManager")
    public CustomerPayment saveCustomerPayment(CustomerPayment customerPayment) {
        return customerPaymentDao.save(customerPayment);
    }

    @Override
    public List<CustomerPayment> readCustomerPaymentsByCustomerId(Long customerId) {
        return customerPaymentDao.readCustomerPaymentsByCustomerId(customerId);
    }

    @Override
    public CustomerPayment readCustomerPaymentById(Long customerPaymentId) {
        return customerPaymentDao.readCustomerPaymentById(customerPaymentId);
    }

    @Override
    public CustomerPayment readCustomerPaymentByToken(String token) {
        return customerPaymentDao.readCustomerPaymentByToken(token);
    }

    @Override
    @Transactional("ucTransactionManager")
    public void deleteCustomerPaymentById(Long customerPaymentId) {
        customerPaymentDao.deleteCustomerPaymentById(customerPaymentId);
    }

    @Override
    @Transactional("ucTransactionManager")
    public void deleteCustomerPaymentByToken(String token) {
        customerPaymentDao.deleteCustomerPaymentByToken(token);
    }

    @Override
    @Transactional("ucTransactionManager")
    public CustomerPayment create() {
        return customerPaymentDao.create();
    }

    public CustomerPayment findDefaultPaymentForCustomer(Customer customer) {
        if (customer == null) { return null; }
        List<CustomerPayment> payments = readCustomerPaymentsByCustomerId(customer.getId());
        for (CustomerPayment payment : payments) {
            if (payment.isDefault()) {
                return payment;
            }
        }
        return null;
    }

    @Override
    @Transactional("ucTransactionManager")
    public CustomerPayment setAsDefaultPayment(CustomerPayment payment) {
        CustomerPayment oldDefault = findDefaultPaymentForCustomer(payment.getCustomer());
        if (oldDefault != null) {
            oldDefault.setIsDefault(false);
            saveCustomerPayment(oldDefault);
        }
        payment.setIsDefault(true);
        return saveCustomerPayment(payment);
    }

    @Override
    @Transactional("ucTransactionManager")
    public void clearDefaultPaymentStatus(Customer customer) {
        CustomerPayment oldDefault = findDefaultPaymentForCustomer(customer);

        if (oldDefault != null) {
            oldDefault.setIsDefault(false);
            saveCustomerPayment(oldDefault);
        }
    }

    @Override
    @Transactional("ucTransactionManager")
    public Customer deleteCustomerPaymentFromCustomer(Customer customer, CustomerPayment payment) {
        List<CustomerPayment> payments = customer.getCustomerPayments();
        for (CustomerPayment customerPayment : payments) {
            if (customerPayment.getId().equals(payment.getId())) {
                customer.getCustomerPayments().remove(customerPayment);
                break;
            }
        }
       return customerService.saveCustomer(customer);
    }

}
