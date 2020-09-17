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
package com.ultracommerce.core.payment.service;

import org.apache.commons.collections4.ListUtils;
import com.ultracommerce.common.payment.dto.PaymentRequestDTO;
import com.ultracommerce.profile.core.domain.Customer;
import com.ultracommerce.profile.core.domain.CustomerPhone;
import org.springframework.stereotype.Service;

@Service("ucPaymentRequestDTOService")
public class PaymentRequestDTOServiceImpl implements PaymentRequestDTOService {

    @Override
    public PaymentRequestDTO populateCustomerInfo(PaymentRequestDTO requestDTO, Customer customer) {
        return populateCustomerInfo(requestDTO, customer, null);
    }

    @Override
    public PaymentRequestDTO populateCustomerInfo(PaymentRequestDTO requestDTO, Customer customer, String defaultEmailAddress) {
        String phoneNumber = null;

        for (CustomerPhone phone : ListUtils.emptyIfNull(customer.getCustomerPhones())) {
            if (phone.getPhone().isDefault()) {
                phoneNumber =  phone.getPhone().getPhoneNumber();
            }
        }

        String emailAddress = (customer.getEmailAddress() == null)? defaultEmailAddress : customer.getEmailAddress();

        return requestDTO.customer()
                .customerId(customer.getId().toString())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(emailAddress)
                .phone(phoneNumber)
                .done();
    }
}
