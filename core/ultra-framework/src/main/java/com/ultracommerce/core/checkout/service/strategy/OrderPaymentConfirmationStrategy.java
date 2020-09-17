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
package com.ultracommerce.core.checkout.service.strategy;

import com.ultracommerce.common.payment.dto.PaymentResponseDTO;
import com.ultracommerce.common.vendor.service.exception.PaymentException;
import com.ultracommerce.core.checkout.service.exception.CheckoutException;
import com.ultracommerce.core.checkout.service.workflow.CheckoutSeed;
import com.ultracommerce.core.payment.domain.PaymentTransaction;
import com.ultracommerce.core.workflow.ProcessContext;
import com.ultracommerce.core.workflow.WorkflowException;

/**
 * Implementation to "confirm" an unconfirmed transaction.
 *
 * Default implementation is to:
 * - If it is an unconfirmed {@link com.ultracommerce.common.payment.PaymentType#CREDIT_CARD},
 * then it will attempt to either "Authorize" or "Authorize and Capture" it at this time.
 * - If the transaction is of any other type, it will attempt to call the implementing gateway's
 * {@link com.ultracommerce.common.payment.service.PaymentGatewayTransactionConfirmationService#confirmTransaction(com.ultracommerce.common.payment.dto.PaymentRequestDTO)}
 * - If the system is configured to handle PENDING payments during a checkout, it will create that in the interim.
 *
 * @author Elbert Bautista (elbertbautista)
 */
public interface OrderPaymentConfirmationStrategy {

    /**
     * Strategy to determine how to "confirm" an OrderPayment at checkout
     */
    public PaymentResponseDTO confirmTransaction(PaymentTransaction tx, ProcessContext<CheckoutSeed> context) throws PaymentException, WorkflowException, CheckoutException;

    /**
     * Strategy to determine how to "confirm" a PENDING OrderPayment post-checkout
     */
    public PaymentResponseDTO confirmPendingTransaction(PaymentTransaction tx, ProcessContext<CheckoutSeed> context) throws PaymentException, WorkflowException, CheckoutException;
}
