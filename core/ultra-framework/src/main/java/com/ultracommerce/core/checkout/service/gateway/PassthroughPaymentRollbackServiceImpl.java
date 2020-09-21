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
package com.ultracommerce.core.checkout.service.gateway;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.money.Money;
import com.ultracommerce.common.payment.PaymentGatewayType;
import com.ultracommerce.common.payment.PaymentTransactionType;
import com.ultracommerce.common.payment.PaymentType;
import com.ultracommerce.common.payment.dto.PaymentRequestDTO;
import com.ultracommerce.common.payment.dto.PaymentResponseDTO;
import com.ultracommerce.common.payment.service.AbstractPaymentGatewayRollbackService;
import com.ultracommerce.common.payment.service.PaymentGatewayRollbackService;
import com.ultracommerce.common.vendor.service.exception.PaymentException;
import com.ultracommerce.core.order.service.OrderService;
import com.ultracommerce.core.payment.service.OrderPaymentService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

/**
 * This default implementation will create a compensating transaction response based on the transaction type passed in
 * for any Passthrough Order Payments on the order.
 * This is by default initiated from UltraCheckoutController.processPassthroughCheckout();
 * If an error occurs in the checkout workflow, the {@link ConfirmPaymentsRollbackHandler} gets invoked and will call this class.
 *
 * @author Elbert Bautista (elbertbautista)
 */
@Service("ucPassthroughPaymentRollbackService")
public class PassthroughPaymentRollbackServiceImpl extends AbstractPaymentGatewayRollbackService {

    protected static final Log LOG = LogFactory.getLog(PassthroughPaymentRollbackServiceImpl.class);

    @Resource(name = "ucOrderService")
    protected OrderService orderService;

    @Resource(name = "ucOrderPaymentService")
    protected OrderPaymentService orderPaymentService;

    @Override
    public PaymentResponseDTO rollbackAuthorize(PaymentRequestDTO transactionToBeRolledBack) throws PaymentException {

        if (LOG.isTraceEnabled()) {
            LOG.trace("Passthrough Payment Gateway - Rolling back authorize transaction with amount: " + transactionToBeRolledBack.getTransactionTotal());
        }

        if (transactionToBeRolledBack.getAdditionalFields().containsKey(PassthroughPaymentConstants.PASSTHROUGH_PAYMENT_TYPE)) {

            return new PaymentResponseDTO(
                    PaymentType.getInstance((String)transactionToBeRolledBack.getAdditionalFields().get(PassthroughPaymentConstants.PASSTHROUGH_PAYMENT_TYPE)),
                    PaymentGatewayType.PASSTHROUGH)
                    .rawResponse("rollback authorize - successful")
                    .successful(true)
                    .paymentTransactionType(PaymentTransactionType.REVERSE_AUTH)
                    .amount(new Money(transactionToBeRolledBack.getTransactionTotal()));

        }

        throw new PaymentException("Make sure transaction contains a Passthrough Payment Type");
    }

    @Override
    public PaymentResponseDTO rollbackCapture(PaymentRequestDTO transactionToBeRolledBack) throws PaymentException {
        throw new PaymentException("The Rollback Capture method is not supported for this module");
    }

    @Override
    public PaymentResponseDTO rollbackAuthorizeAndCapture(PaymentRequestDTO transactionToBeRolledBack) throws PaymentException {
        if (LOG.isTraceEnabled()) {
            LOG.trace("Passthrough Payment Gateway - Rolling back authorize and capture transaction with amount: " + transactionToBeRolledBack.getTransactionTotal());
        }

        if (transactionToBeRolledBack.getAdditionalFields().containsKey(PassthroughPaymentConstants.PASSTHROUGH_PAYMENT_TYPE)) {

            return new PaymentResponseDTO(
                    PaymentType.getInstance((String)transactionToBeRolledBack.getAdditionalFields().get(PassthroughPaymentConstants.PASSTHROUGH_PAYMENT_TYPE)),
                    PaymentGatewayType.PASSTHROUGH)
                    .rawResponse("rollback authorize and capture - successful")
                    .successful(true)
                    .paymentTransactionType(PaymentTransactionType.VOID)
                    .amount(new Money(transactionToBeRolledBack.getTransactionTotal()));

        }

        throw new PaymentException("Make sure transaction contains a Passthrough Payment Type");
    }

    @Override
    public PaymentResponseDTO rollbackRefund(PaymentRequestDTO transactionToBeRolledBack) throws PaymentException {
        throw new PaymentException("The Rollback Refund method is not supported for this module");
    }
}
