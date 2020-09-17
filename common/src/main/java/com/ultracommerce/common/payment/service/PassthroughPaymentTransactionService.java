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
package com.ultracommerce.common.payment.service;

import com.ultracommerce.common.money.Money;
import com.ultracommerce.common.payment.PaymentGatewayType;
import com.ultracommerce.common.payment.PaymentTransactionType;
import com.ultracommerce.common.payment.dto.PaymentRequestDTO;
import com.ultracommerce.common.payment.dto.PaymentResponseDTO;
import com.ultracommerce.common.vendor.service.exception.PaymentException;
import org.springframework.stereotype.Service;

@Service("ucPassthroughPaymentTransactionService")
public class PassthroughPaymentTransactionService extends AbstractPaymentGatewayTransactionService {

    @Override
    public PaymentResponseDTO authorize(PaymentRequestDTO paymentRequestDTO) throws PaymentException {
        PaymentResponseDTO response = new PaymentResponseDTO(paymentRequestDTO.getPaymentType(), PaymentGatewayType.PASSTHROUGH)
                .paymentTransactionType(PaymentTransactionType.AUTHORIZE)
                .amount(new Money(paymentRequestDTO.getTransactionTotal()))
                .successful(true);
        return response;
    }

    @Override
    public PaymentResponseDTO capture(PaymentRequestDTO paymentRequestDTO) throws PaymentException {
        PaymentResponseDTO response = new PaymentResponseDTO(paymentRequestDTO.getPaymentType(), PaymentGatewayType.PASSTHROUGH)
                .paymentTransactionType(PaymentTransactionType.CAPTURE)
                .amount(new Money(paymentRequestDTO.getTransactionTotal()))
                .successful(true);
        return response;
    }

    @Override
    public PaymentResponseDTO authorizeAndCapture(PaymentRequestDTO paymentRequestDTO) throws PaymentException {
        PaymentResponseDTO response = new PaymentResponseDTO(paymentRequestDTO.getPaymentType(), PaymentGatewayType.PASSTHROUGH)
                .paymentTransactionType(PaymentTransactionType.AUTHORIZE_AND_CAPTURE)
                .amount(new Money(paymentRequestDTO.getTransactionTotal()))
                .successful(true);
        return response;
    }

    @Override
    public PaymentResponseDTO reverseAuthorize(PaymentRequestDTO paymentRequestDTO) throws PaymentException {
        PaymentResponseDTO response = new PaymentResponseDTO(paymentRequestDTO.getPaymentType(), PaymentGatewayType.PASSTHROUGH)
                .paymentTransactionType(PaymentTransactionType.REVERSE_AUTH)
                .amount(new Money(paymentRequestDTO.getTransactionTotal()))
                .successful(true);
        return response;
    }

    @Override
    public PaymentResponseDTO refund(PaymentRequestDTO paymentRequestDTO) throws PaymentException {
        PaymentResponseDTO response = new PaymentResponseDTO(paymentRequestDTO.getPaymentType(), PaymentGatewayType.PASSTHROUGH)
                .paymentTransactionType(PaymentTransactionType.REFUND)
                .amount(new Money(paymentRequestDTO.getTransactionTotal()))
                .successful(true);
        return response;
    }

    @Override
    public PaymentResponseDTO voidPayment(PaymentRequestDTO paymentRequestDTO) throws PaymentException {
        PaymentResponseDTO response = new PaymentResponseDTO(paymentRequestDTO.getPaymentType(), PaymentGatewayType.PASSTHROUGH)
                .paymentTransactionType(PaymentTransactionType.VOID)
                .amount(new Money(paymentRequestDTO.getTransactionTotal()))
                .successful(true);
        return response;
    }

}
