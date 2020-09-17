/*
 * #%L
 * UltraCommerce Common Libraries
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
package com.ultracommerce.profile.web.core.expression;

import com.ultracommerce.common.web.expression.UltraVariableExpression;
import com.ultracommerce.presentation.condition.ConditionalOnTemplating;
import com.ultracommerce.profile.core.domain.Customer;
import com.ultracommerce.profile.core.domain.CustomerAddress;
import com.ultracommerce.profile.core.domain.CustomerPayment;
import com.ultracommerce.profile.core.service.CustomerAddressService;
import com.ultracommerce.profile.core.service.CustomerPaymentService;
import com.ultracommerce.profile.web.core.CustomerState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;


/**
 * This Thymeleaf variable expression class serves to expose elements from the UltraRequestContext
 * 
 * @author Andre Azzolini (apazzolini)
 */
@Service("ucCustomerVariableExpression")
@ConditionalOnTemplating
public class CustomerVariableExpression implements UltraVariableExpression {

    @Resource(name = "ucCustomerAddressService")
    protected CustomerAddressService customerAddressService;

    @Resource(name = "ucCustomerPaymentService")
    protected CustomerPaymentService customerPaymentService;

    @Autowired
    protected Environment env;
    
    @Override
    public String getName() {
        return "customer";
    }
    
    public Customer getCurrent() {
        return CustomerState.getCustomer();
    }

    public List<CustomerAddress> getCustomerAddresses() {
        Customer customer = CustomerState.getCustomer();

        return customerAddressService.readActiveCustomerAddressesByCustomerId(customer.getId());
    }

    public boolean savedPaymentsAreEnabled() {
        return env.getProperty("saved.customer.payments.enabled", boolean.class, true);
    }

    public List<CustomerPayment> getCustomerPayments() {
        Customer customer = CustomerState.getCustomer();
        List<CustomerPayment> customerPayments = new ArrayList<>();

        if (savedPaymentsAreEnabled()) {
            customerPayments = customerPaymentService.readCustomerPaymentsByCustomerId(customer.getId());
            sortCustomerPaymentsByDefault(customerPayments);
        }

        return customerPayments;
    }

    protected void sortCustomerPaymentsByDefault(List<CustomerPayment> savedPayments) {
        Collections.sort(savedPayments, new Comparator<CustomerPayment>() {
            @Override
            public int compare(CustomerPayment sp1, CustomerPayment sp2) {
                if (sp1.isDefault()) {
                    return -1;
                } else if (sp2.isDefault()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
    }
    
}
