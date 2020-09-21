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
/**
 * @author Austin Rooke(austinrooke)
 */
package com.ultracommerce.core.spec.checkout.service.workflow

import com.ultracommerce.common.money.Money
import com.ultracommerce.common.payment.PaymentGatewayType
import com.ultracommerce.common.payment.PaymentTransactionType
import com.ultracommerce.common.payment.dto.PaymentRequestDTO
import com.ultracommerce.common.payment.dto.PaymentResponseDTO
import com.ultracommerce.common.payment.service.PaymentGatewayCheckoutService
import com.ultracommerce.common.payment.service.PaymentGatewayClientTokenService
import com.ultracommerce.common.payment.service.PaymentGatewayConfiguration
import com.ultracommerce.common.payment.service.PaymentGatewayConfigurationService
import com.ultracommerce.common.payment.service.PaymentGatewayConfigurationServiceProvider
import com.ultracommerce.common.payment.service.PaymentGatewayCreditCardService
import com.ultracommerce.common.payment.service.PaymentGatewayCustomerService
import com.ultracommerce.common.payment.service.PaymentGatewayFraudService
import com.ultracommerce.common.payment.service.PaymentGatewayHostedService
import com.ultracommerce.common.payment.service.PaymentGatewayReportingService
import com.ultracommerce.common.payment.service.PaymentGatewayRollbackService
import com.ultracommerce.common.payment.service.PaymentGatewaySubscriptionService
import com.ultracommerce.common.payment.service.PaymentGatewayTransactionConfirmationService
import com.ultracommerce.common.payment.service.PaymentGatewayTransactionService
import com.ultracommerce.common.payment.service.PaymentGatewayTransparentRedirectService
import com.ultracommerce.common.payment.service.PaymentGatewayWebResponseService
import com.ultracommerce.common.vendor.service.exception.PaymentException
import com.ultracommerce.common.web.payment.expression.PaymentGatewayFieldExtensionHandler
import com.ultracommerce.common.web.payment.processor.CreditCardTypesExtensionHandler
import com.ultracommerce.common.web.payment.processor.TRCreditCardExtensionHandler
import com.ultracommerce.core.checkout.service.workflow.ConfirmPaymentsRollbackHandler
import com.ultracommerce.core.checkout.service.workflow.ValidateAndConfirmPaymentActivity
import com.ultracommerce.core.order.domain.Order
import com.ultracommerce.core.order.service.OrderService
import com.ultracommerce.core.payment.domain.OrderPayment
import com.ultracommerce.core.payment.domain.OrderPaymentImpl
import com.ultracommerce.core.payment.domain.PaymentTransaction
import com.ultracommerce.core.payment.domain.PaymentTransactionImpl
import com.ultracommerce.core.payment.service.OrderPaymentService
import com.ultracommerce.core.payment.service.OrderToPaymentRequestDTOService
import com.ultracommerce.core.pricing.service.exception.PricingException
import com.ultracommerce.core.workflow.state.RollbackFailureException
import com.ultracommerce.core.workflow.state.RollbackHandler


class ConfirmPaymentsRollbackHandlerSpec extends BaseCheckoutRollbackSpec{

    PaymentGatewayConfigurationServiceProvider mockPaymentConfigurationServiceProvider
    OrderToPaymentRequestDTOService mockOrderToPaymentRequestDTOService
    OrderPaymentService mockOrderPaymentService
    PaymentGatewayCheckoutService mockPaymentGatewayCheckoutService
    OrderService mockOrderService
    Order order
    Collection<PaymentTransaction> paymentTransactions

    def setup() {
        mockPaymentConfigurationServiceProvider = Mock()
        mockOrderToPaymentRequestDTOService = Mock()
        mockPaymentGatewayCheckoutService = Mock()

        stateConfiguration = new HashMap<String, Collection<PaymentTransaction>>()

        PaymentGatewayConfigurationService cfg = new PaymentGatewayConfigurationService() {

            PaymentGatewayRollbackService paymentGatewayRollbackService = new PaymentGatewayRollbackService() {

                public PaymentResponseDTO rollbackRefund(PaymentRequestDTO paymentRequestDTO) {
                    PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO(null, null)
                    paymentResponseDTO.amount= new Money(1.00)
                    paymentResponseDTO.rawResponse("rawResponse")
                    paymentResponseDTO.successful = true
                    paymentResponseDTO.paymentTransactionType = PaymentTransactionType.REVERSE_AUTH
                    paymentResponseDTO.responseMap("key","value")

                    return paymentResponseDTO
                }

                public PaymentResponseDTO rollbackCapture(PaymentRequestDTO paymentRequestDTO) {
                    PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO(null, null)
                    paymentResponseDTO.amount(new Money(1.00))
                    paymentResponseDTO.rawResponse("rawResponse")
                    paymentResponseDTO.successful(true)
                    paymentResponseDTO.paymentTransactionType(PaymentTransactionType.CAPTURE)
                    paymentResponseDTO.responseMap("key","value")

                    return paymentResponseDTO
                }

                public PaymentResponseDTO rollbackAuthorize(PaymentRequestDTO paymentRequestDTO) throws PaymentException {
                    if(paymentRequestDTO != null){
                        throw new PaymentException()
                    }

                    PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO(null, null)
                    paymentResponseDTO.amount(new Money(1.00))
                    paymentResponseDTO.rawResponse("rawResponse")
                    paymentResponseDTO.successful(true)
                    paymentResponseDTO.paymentTransactionType(PaymentTransactionType.REVERSE_AUTH)
                    paymentResponseDTO.responseMap("key","value")

                    return paymentResponseDTO
                }

                public PaymentResponseDTO rollbackAuthorizeAndCapture(PaymentRequestDTO paymentRequestDTO) {
                    PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO(null, null)
                    paymentResponseDTO.amount(new Money(1.00))
                    paymentResponseDTO.rawResponse("rawResponse")
                    paymentResponseDTO.successful = false
                    paymentResponseDTO.paymentTransactionType(PaymentTransactionType.VOID)
                    paymentResponseDTO.responseMap("key","value")

                    return paymentResponseDTO
                }
            }
            public PaymentGatewayRollbackService getRollbackService() {
                return paymentGatewayRollbackService
            }

            public PaymentGatewayConfiguration getConfiguration() {
                return null
            }

            public PaymentGatewayTransactionService getTransactionService() {
                return null
            }

            public PaymentGatewayTransactionConfirmationService getTransactionConfirmationService() {
                return null
            }

            public PaymentGatewayReportingService getReportingService() {
                return null
            }

            public PaymentGatewayCreditCardService getCreditCardService() {
                return null
            }

            public PaymentGatewayCustomerService getCustomerService() {
                return null
            }

            public PaymentGatewaySubscriptionService getSubscriptionService() {
                return null
            }

            public PaymentGatewayFraudService getFraudService() {
                return null
            }

            public PaymentGatewayHostedService getHostedService() {
                return null
            }

            public PaymentGatewayWebResponseService getWebResponseService() {
                return null
            }

            public PaymentGatewayTransparentRedirectService getTransparentRedirectService() {
                return null
            }

            public PaymentGatewayClientTokenService getClientTokenService() {
                return null
            }

            public TRCreditCardExtensionHandler getCreditCardExtensionHandler() {
                return null
            }

            public PaymentGatewayFieldExtensionHandler getFieldExtensionHandler() {
                return null
            }

            public CreditCardTypesExtensionHandler getCreditCardTypesExtensionHandler() {
                return null
            }
        }
        mockPaymentConfigurationServiceProvider.getGatewayConfigurationService(_) >> { cfg }
    }



    def "Test that Exception is thrown when a paymentConfigurationServiceProvider is not provided"() {
        RollbackHandler rollbackHandler = new ConfirmPaymentsRollbackHandler().with {
            paymentConfigurationServiceProvider = null
            it
        }


        when: "rollbackState is executed"
        rollbackHandler.rollbackState(activity, context, stateConfiguration)

        then: "RollbackFailureException is thrown"
        thrown(RollbackFailureException)
    }

    def "Test that Exception is thrown when an error occurs during transaction rollback"() {
        setup:"Place a PaymentTransaction into the seed as well as the StateConfiguration"
        PaymentTransaction tx1 = new PaymentTransactionImpl()
        tx1.id = 1
        tx1.amount = new Money(1.00)
        tx1.orderPayment = new OrderPaymentImpl()
        tx1.orderPayment.id = 2
        tx1.orderPayment.gatewayType = PaymentGatewayType.TEMPORARY
        tx1.type = PaymentTransactionType.AUTHORIZE
        paymentTransactions = new ArrayList()
        paymentTransactions.add(tx1)
        context.seedData.order.payments.add(tx1.orderPayment)
        stateConfiguration.put(ValidateAndConfirmPaymentActivity.ROLLBACK_TRANSACTIONS,paymentTransactions)
        mockOrderToPaymentRequestDTOService.translatePaymentTransaction(_, _) >> { new PaymentRequestDTO() }

        mockOrderService = Mock()
        mockOrderPaymentService = Mock()
        order = context.seedData.order
        RollbackHandler rollbackHandler = new ConfirmPaymentsRollbackHandler().with {
            paymentConfigurationServiceProvider = mockPaymentConfigurationServiceProvider
            transactionToPaymentRequestDTOService = mockOrderToPaymentRequestDTOService
            orderPaymentService = mockOrderPaymentService
            paymentGatewayCheckoutService = mockPaymentGatewayCheckoutService
            orderService = mockOrderService
            it
        }

        when: "rollbackState is executed"
        rollbackHandler.rollbackState(activity, context, stateConfiguration)

        then: "PaymentException is thrown during transaction logging"
        thrown(RollbackFailureException)
    }
    
    def "Test that Exception is thrown when a rollback Failure occurs when attemping to invalidate payments"() {
        setup: "Place a PaymentTransaction into the seed as well as the StateConfiguration"
        PaymentTransaction tx1 = new PaymentTransactionImpl()
        tx1.id = 1
        tx1.amount = new Money(1.00)
        tx1.orderPayment = new OrderPaymentImpl()
        tx1.orderPayment.id = 2
        tx1.orderPayment.gatewayType = PaymentGatewayType.TEMPORARY
        tx1.type = PaymentTransactionType.AUTHORIZE_AND_CAPTURE
        paymentTransactions = new ArrayList()
        paymentTransactions.add(tx1)
        context.seedData.order.payments.add(tx1.orderPayment)
        stateConfiguration.put(ValidateAndConfirmPaymentActivity.ROLLBACK_TRANSACTIONS,paymentTransactions)

        mockOrderPaymentService = Mock()
        mockOrderService = Mock()
        order = context.seedData.order
        RollbackHandler rollbackHandler = new ConfirmPaymentsRollbackHandler().with {
            paymentConfigurationServiceProvider = mockPaymentConfigurationServiceProvider
            transactionToPaymentRequestDTOService = mockOrderToPaymentRequestDTOService
            orderPaymentService = mockOrderPaymentService
            paymentGatewayCheckoutService = mockPaymentGatewayCheckoutService
            orderService = mockOrderService
            it
        }

        when: "rollbackState is executed"
        rollbackHandler.rollbackState(activity, context, stateConfiguration)

        then: "The Transaction is recorded and a RollbackFailureException is thrown during payment invalidation"
        1 * mockOrderPaymentService.createTransaction() >> { new PaymentTransactionImpl() }
        // once after adding the rollback transaction, once after marking the payment as invalid
        2 * mockOrderPaymentService.save(_) >> { OrderPayment payment -> payment }
        thrown(RollbackFailureException)
    }

    def "Test that Exception is thrown when the OrderService is unable to save invalidated payments"() {
        setup: "Place a PaymentTransaction into the seedData as well as into the StateConfiguration as well as set up the orderService to throw a PricingException"
        PaymentTransaction tx1 = new PaymentTransactionImpl()
        tx1.id = 1
        tx1.amount = new Money(1.00)
        tx1.orderPayment = new OrderPaymentImpl()
        tx1.orderPayment.id = 2
        tx1.orderPayment.gatewayType = PaymentGatewayType.TEMPORARY
        tx1.type = PaymentTransactionType.AUTHORIZE
        paymentTransactions = new ArrayList()
        paymentTransactions.add(tx1)
        context.seedData.order.payments.add(tx1.orderPayment)
        stateConfiguration.put(ValidateAndConfirmPaymentActivity.ROLLBACK_TRANSACTIONS,paymentTransactions)
        mockOrderPaymentService = Mock()
        mockOrderService = Mock()
        order = context.seedData.order
        mockOrderPaymentService.createTransaction() >> { new PaymentTransactionImpl() }
        mockOrderPaymentService.save(_) >> { args -> return args[0] }

        RollbackHandler rollbackHandler = new ConfirmPaymentsRollbackHandler().with {
            paymentConfigurationServiceProvider = mockPaymentConfigurationServiceProvider
            transactionToPaymentRequestDTOService = mockOrderToPaymentRequestDTOService
            orderPaymentService = mockOrderPaymentService
            paymentGatewayCheckoutService = mockPaymentGatewayCheckoutService
            orderService = mockOrderService
            it
        }

        when: "rollbackState is executed"
        rollbackHandler.rollbackState(activity, context, stateConfiguration)

        then: "RollbackFailureException is thrown during saving of payment invalidation"
        1 * mockOrderService.save(_, _) >> { throw new PricingException() }
        thrown(RollbackFailureException)
    }

    def "Test a successful run with valid data"() {
        setup: "Place a paymentTransaction into the seedData as well as the stateConfiguration"
        PaymentTransaction tx1 = new PaymentTransactionImpl()
        tx1.id = 1
        tx1.amount = new Money(1.00)
        tx1.orderPayment = new OrderPaymentImpl()
        tx1.orderPayment.id = 2
        tx1.orderPayment.gatewayType = PaymentGatewayType.TEMPORARY
        tx1.type = PaymentTransactionType.AUTHORIZE
        paymentTransactions = new ArrayList()
        paymentTransactions.add(tx1)
        context.seedData.order.payments.add(tx1.orderPayment)
        stateConfiguration.put(ValidateAndConfirmPaymentActivity.ROLLBACK_TRANSACTIONS,paymentTransactions)

        mockOrderPaymentService = Mock()
        mockOrderService = Mock()
        order = context.seedData.order
        mockOrderPaymentService.createTransaction() >> { new PaymentTransactionImpl() }
        RollbackHandler rollbackHandler = new ConfirmPaymentsRollbackHandler().with {
            paymentConfigurationServiceProvider = mockPaymentConfigurationServiceProvider
            transactionToPaymentRequestDTOService = mockOrderToPaymentRequestDTOService
            orderPaymentService = mockOrderPaymentService
            paymentGatewayCheckoutService = mockPaymentGatewayCheckoutService
            orderService = mockOrderService
            it
        }

        when: "rollbackState is executed"
        rollbackHandler.rollbackState(activity, context, stateConfiguration)

        then: "No exceptions are encountered and the orderService and orderPaymentService successfully saves the results"
        1 * mockPaymentGatewayCheckoutService.markPaymentAsInvalid(_)
        1 * mockOrderService.save(_,_)
        // Once to add the transaction, once to save after being marked as invalid
        2 * mockOrderPaymentService.save(_) >> { args -> return args[0] }
        order.getPayments().size() == 1
    }
}
