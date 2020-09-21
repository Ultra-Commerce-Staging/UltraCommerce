/*
 * #%L
 * UltraCommerce Framework Web
 * %%
 * Copyright (C) 2009 - 2018 Ultra Commerce
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
package com.ultracommerce.core.web.payment.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.payment.dto.PaymentRequestDTO;
import com.ultracommerce.common.payment.service.CurrentCustomerPaymentRequestService;
import com.ultracommerce.common.vendor.service.exception.PaymentException;
import com.ultracommerce.core.payment.service.PaymentRequestDTOService;
import com.ultracommerce.profile.core.domain.Customer;
import com.ultracommerce.profile.core.domain.CustomerAttribute;
import com.ultracommerce.profile.core.domain.CustomerAttributeImpl;
import com.ultracommerce.profile.core.service.CustomerService;
import com.ultracommerce.profile.web.core.CustomerState;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

/**
 * @author Elbert Bautista (elbertbautista)
 */
@Service("ucDefaultCurrentCustomerPaymentRequestService")
public class DefaultCurrentCustomerPaymentRequestService implements CurrentCustomerPaymentRequestService {

    private static final Log LOG = LogFactory.getLog(DefaultCurrentCustomerPaymentRequestService.class);

    @Resource(name = "ucPaymentRequestDTOService")
    protected PaymentRequestDTOService paymentRequestDTOService;

    @Resource(name = "ucCustomerService")
    protected CustomerService customerService;

    @Override
    public PaymentRequestDTO getPaymentRequestFromCurrentCustomer() {
        Customer customer = CustomerState.getCustomer();
        PaymentRequestDTO customerRequest = new PaymentRequestDTO();
        return paymentRequestDTOService.populateCustomerInfo(customerRequest, customer);
    }

    @Override
    public void addCustomerAttributeToCurrentCustomer(String customerAttributeKey, String customerAttributeValue) throws PaymentException {
        addCustomerAttributeToCustomer(null, customerAttributeKey, customerAttributeValue);
    }

    @Override
    public void addCustomerAttributeToCustomer(Long customerId, String customerAttributeKey, String customerAttributeValue) throws PaymentException {
        Customer currentCustomer = CustomerState.getCustomer();
        Long currentCustomerId = currentCustomer.getId();

        if (customerId != null && !currentCustomerId.equals(customerId)) {
            logWarningIfCustomerMismatch(currentCustomerId, customerId);
            currentCustomer = customerService.readCustomerById(customerId);
        }

        CustomerAttribute customerAttribute = new CustomerAttributeImpl();
        customerAttribute.setName(customerAttributeKey);
        customerAttribute.setValue(customerAttributeValue);
        customerAttribute.setCustomer(currentCustomer);
        currentCustomer.getCustomerAttributes().put(customerAttributeKey, customerAttribute);

        customerService.saveCustomer(currentCustomer);

    }

    @Override
    public String retrieveCustomerAttributeFromCurrentCustomer(String customerAttributeKey) {
        return retrieveCustomerAttributeFromCustomer(null, customerAttributeKey);
    }

    @Override
    public String retrieveCustomerAttributeFromCustomer(Long customerId, String customerAttributeKey) {
        Customer currentCustomer = CustomerState.getCustomer();
        Long currentCustomerId = currentCustomer.getId();

        if (customerId != null && !currentCustomerId.equals(customerId)) {
            logWarningIfCustomerMismatch(currentCustomerId, customerId);
            currentCustomer = customerService.readCustomerById(customerId);
        }

        if (currentCustomer.getCustomerAttributes().containsKey(customerAttributeKey)) {
            return currentCustomer.getCustomerAttributes().get(customerAttributeKey).getValue();
        }

        return null;
    }

    protected void logWarningIfCustomerMismatch(Long currentCustomerId, Long customerId) {
        if (LOG.isWarnEnabled()) {
            LOG.warn(String.format("The current customer resolved from customer state [%s] is not the same as the requested customer ID [%s]. Session may have expired or local cart state was lost. This may need manual review.", currentCustomerId, customerId));
        }
    }
}
