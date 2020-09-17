/*
 * #%L
 * UltraCommerce Framework Web
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

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.money.Money;
import com.ultracommerce.common.payment.dto.PaymentRequestDTO;
import com.ultracommerce.common.payment.dto.PaymentResponseDTO;
import com.ultracommerce.common.payment.service.AbstractPaymentGatewayTransactionConfirmationService;
import com.ultracommerce.common.payment.service.PaymentGatewayTransactionService;
import com.ultracommerce.common.vendor.service.exception.PaymentException;
import org.springframework.stereotype.Service;

/**
 * @author Elbert Bautista (elbertbautista)
 */
@Service("ucNullIntegrationGatewayHostedTransactionConfirmationService")
public class NullIntegrationGatewayTransactionConfirmationServiceImpl extends AbstractPaymentGatewayTransactionConfirmationService {

    protected static final Log LOG = LogFactory.getLog(NullIntegrationGatewayTransactionConfirmationServiceImpl.class);

    private static Money oneHundred = new Money(100);
    private static Money twoHundred = new Money(200);

    @Resource(name = "ucNullIntegrationGatewayHostedConfiguration")
    protected NullIntegrationGatewayHostedConfiguration configuration;

    @Resource(name = "ucNullIntegrationGatewayTransactionService")
    protected PaymentGatewayTransactionService transactionService;

    @Override
    public PaymentResponseDTO confirmTransaction(PaymentRequestDTO paymentRequestDTO) throws PaymentException {
        if (LOG.isTraceEnabled()) {
            LOG.trace("Null Payment Hosted Gateway - Confirming Transaction with amount: " + paymentRequestDTO.getTransactionTotal());
        }

        //hard-coding capture, we do not provide authorization data in tests
        PaymentResponseDTO responseDTO = transactionService.capture(paymentRequestDTO);

        return responseDTO;

    }
}
