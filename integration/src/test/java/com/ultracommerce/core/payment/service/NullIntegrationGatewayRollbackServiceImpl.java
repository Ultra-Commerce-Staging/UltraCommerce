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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.money.Money;
import com.ultracommerce.common.payment.PaymentTransactionType;
import com.ultracommerce.common.payment.PaymentType;
import com.ultracommerce.common.payment.dto.PaymentRequestDTO;
import com.ultracommerce.common.payment.dto.PaymentResponseDTO;
import com.ultracommerce.common.payment.service.AbstractPaymentGatewayRollbackService;
import com.ultracommerce.common.payment.service.PaymentGatewayRollbackService;
import com.ultracommerce.common.vendor.service.exception.PaymentException;
import org.springframework.stereotype.Service;

@Service("ucNullIntegrationGatewayRollbackService")
public class NullIntegrationGatewayRollbackServiceImpl extends AbstractPaymentGatewayRollbackService {

    protected static final Log LOG = LogFactory.getLog(NullIntegrationGatewayRollbackServiceImpl.class);

    @Override
    public PaymentResponseDTO rollbackAuthorize(PaymentRequestDTO transactionToBeRolledBack) throws PaymentException {
        if (LOG.isTraceEnabled()) {
            LOG.trace("Null Payment Gateway - Rolling back authorize transaction with amount: " + transactionToBeRolledBack.getTransactionTotal());
        }

        return new PaymentResponseDTO(PaymentType.CREDIT_CARD,
                NullIntegrationGatewayType.NULL_INTEGRATION_GATEWAY)
                .rawResponse("rollback authorize - successful")
                .successful(true)
                .paymentTransactionType(PaymentTransactionType.REVERSE_AUTH)
                .amount(new Money(transactionToBeRolledBack.getTransactionTotal()));

    }

    @Override
    public PaymentResponseDTO rollbackCapture(PaymentRequestDTO transactionToBeRolledBack) throws PaymentException {
        throw new PaymentException("The Rollback Capture method is not supported for this module");
    }

    @Override
    public PaymentResponseDTO rollbackAuthorizeAndCapture(PaymentRequestDTO transactionToBeRolledBack) throws PaymentException {
        if (LOG.isTraceEnabled()) {
            LOG.trace("Null Payment Gateway - Rolling back authorize and capture transaction with amount: " + transactionToBeRolledBack.getTransactionTotal());
        }

        return new PaymentResponseDTO(PaymentType.CREDIT_CARD,
                NullIntegrationGatewayType.NULL_INTEGRATION_GATEWAY)
                .rawResponse("rollback authorize and capture - successful")
                .successful(true)
                .paymentTransactionType(PaymentTransactionType.VOID)
                .amount(new Money(transactionToBeRolledBack.getTransactionTotal()));
    }

    @Override
    public PaymentResponseDTO rollbackRefund(PaymentRequestDTO transactionToBeRolledBack) throws PaymentException {
        throw new PaymentException("The Rollback Refund method is not supported for this module");
    }

}
