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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.payment.PaymentAdditionalFieldType;
import com.ultracommerce.common.payment.dto.PaymentResponseDTO;
import com.ultracommerce.common.payment.service.CustomerPaymentGatewayService;
import com.ultracommerce.common.payment.service.PaymentGatewayConfiguration;
import com.ultracommerce.common.web.payment.controller.CustomerPaymentGatewayAbstractController;
import com.ultracommerce.profile.core.domain.Address;
import com.ultracommerce.profile.core.domain.Customer;
import com.ultracommerce.profile.core.domain.CustomerPayment;
import com.ultracommerce.profile.core.service.AddressService;
import com.ultracommerce.profile.core.service.CustomerPaymentService;
import com.ultracommerce.profile.core.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.Map;

import javax.annotation.Resource;

/**
 * Core framework implementation of the {@link CustomerPaymentGatewayService}.
 *
 * @see {@link CustomerPaymentGatewayAbstractController}
 * @author Elbert Bautista (elbertbautista)
 */
@Service("ucCustomerPaymentGatewayService")
public class DefaultCustomerPaymentGatewayService implements CustomerPaymentGatewayService {

    private static final Log LOG = LogFactory.getLog(DefaultCustomerPaymentGatewayService.class);

    @Resource(name = "ucAddressService")
    protected AddressService addressService;

    @Resource(name = "ucCustomerPaymentService")
    protected CustomerPaymentService customerPaymentService;

    @Resource(name = "ucCustomerService")
    protected CustomerService customerService;

    @Resource(name = "ucPaymentResponseDTOToEntityService")
    protected PaymentResponseDTOToEntityService dtoToEntityService;

    @Override
    public Long createCustomerPaymentFromResponseDTO(PaymentResponseDTO responseDTO, PaymentGatewayConfiguration config)
            throws IllegalArgumentException {
        validateResponseAndConfig(responseDTO, config);

        Long customerId = Long.parseLong(responseDTO.getCustomer().getCustomerId());
        Customer customer = customerService.readCustomerById(customerId);

        if (customer != null) {
            if (isNewDefaultPaymentMethod(responseDTO)) {
                customerPaymentService.clearDefaultPaymentStatus(customer);
            }

            CustomerPayment customerPayment = customerPaymentService.create();
            populateCustomerPayment(customerPayment, responseDTO, config);
            customerPayment.setCustomer(customer);

            customerPayment = customerPaymentService.saveCustomerPayment(customerPayment);
            customer.getCustomerPayments().add(customerPayment);
            return customerPayment.getId();
        }

        return null;
    }

    private boolean isNewDefaultPaymentMethod(PaymentResponseDTO responseDTO) {
        Map<String, String> responseMap = responseDTO.getResponseMap();
        String defaultMethod = responseMap.get("isDefault");

        return Boolean.parseBoolean(defaultMethod);
    }

    @Override
    public Long updateCustomerPaymentFromResponseDTO(PaymentResponseDTO responseDTO, PaymentGatewayConfiguration config)
            throws IllegalArgumentException {
        validateResponseAndConfig(responseDTO, config);

        String paymentToken = responseDTO.getPaymentToken();
        CustomerPayment customerPayment = customerPaymentService.readCustomerPaymentByToken(paymentToken);

        if (customerPayment != null) {
            populateCustomerPayment(customerPayment, responseDTO, config);

            customerPayment = customerPaymentService.saveCustomerPayment(customerPayment);
            return customerPayment.getId();
        }

        return null;
    }

    @Override
    public void deleteCustomerPaymentFromResponseDTO(PaymentResponseDTO responseDTO, PaymentGatewayConfiguration config)
            throws IllegalArgumentException {
        validateResponseAndConfig(responseDTO, config);

        String paymentToken = responseDTO.getPaymentToken();
        customerPaymentService.deleteCustomerPaymentByToken(paymentToken);
    }


    protected void validateResponseAndConfig(PaymentResponseDTO responseDTO, PaymentGatewayConfiguration config) throws IllegalArgumentException {
        //Customer payment tokens can ONLY be parsed into Customer Payments if they are 'valid'
        if (!responseDTO.isValid()) {
            throw new IllegalArgumentException("Invalid customer token responses cannot be parsed into the customer payment domain");
        }

        if (config == null || responseDTO.getCustomer() == null || responseDTO.getCustomer().getCustomerId() == null) {
            throw new IllegalArgumentException("PaymentGatewayConfiguration and the customer/customer ID on the ResponseDTO cannot be null. Check your Web Response Service.");
        }
    }

    protected void populateCustomerPayment(CustomerPayment customerPayment, PaymentResponseDTO responseDTO, PaymentGatewayConfiguration config) {
        Map<String, String> responseMap = responseDTO.getResponseMap();

        customerPayment.setPaymentGatewayType(config.getGatewayType());
        customerPayment.setPaymentType(responseDTO.getPaymentType());
        customerPayment.setAdditionalFields(responseMap);
        dtoToEntityService.populateCustomerPaymentToken(responseDTO, customerPayment);

        if (responseDTO.getBillTo() != null && responseDTO.getBillTo().addressPopulated()) {
            Address billingAddress = addressService.create();
            dtoToEntityService.populateAddressInfo(responseDTO.getBillTo(), billingAddress);
            customerPayment.setBillingAddress(billingAddress);
        }

        if (responseDTO.getCreditCard() !=null && responseDTO.getCreditCard().creditCardPopulated()) {
            if (responseDTO.getCreditCard().getCreditCardHolderName() != null) {
                customerPayment.getAdditionalFields().put(PaymentAdditionalFieldType.NAME_ON_CARD.getType(), responseDTO.getCreditCard().getCreditCardHolderName());
            }
            if (responseDTO.getCreditCard().getCreditCardLastFour() != null) {
                customerPayment.getAdditionalFields().put(PaymentAdditionalFieldType.LAST_FOUR.getType(), responseDTO.getCreditCard().getCreditCardLastFour());
            }
            if (responseDTO.getCreditCard().getCreditCardType() != null) {
                customerPayment.getAdditionalFields().put(PaymentAdditionalFieldType.CARD_TYPE.getType(), responseDTO.getCreditCard().getCreditCardType());
            }
            if (responseDTO.getCreditCard().getCreditCardExpDate() != null) {
                customerPayment.getAdditionalFields().put(PaymentAdditionalFieldType.EXP_DATE.getType(), responseDTO.getCreditCard().getCreditCardExpDate());
            }
            if (responseDTO.getCreditCard().getCreditCardExpMonth() != null) {
                customerPayment.getAdditionalFields().put(PaymentAdditionalFieldType.EXP_MONTH.getType(), responseDTO.getCreditCard().getCreditCardExpMonth());
            }
            if (responseDTO.getCreditCard().getCreditCardExpYear() != null) {
                customerPayment.getAdditionalFields().put(PaymentAdditionalFieldType.EXP_YEAR.getType(), responseDTO.getCreditCard().getCreditCardExpYear());
            }
        }

        String isDefault = responseMap.get("isDefault");
        customerPayment.setIsDefault(Boolean.parseBoolean(isDefault));
    }
}
