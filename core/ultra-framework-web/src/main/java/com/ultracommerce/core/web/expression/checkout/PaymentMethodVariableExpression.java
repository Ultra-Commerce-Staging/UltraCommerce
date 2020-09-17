/*
 * #%L
 * UltraCommerce Framework Web
 * %%
 * Copyright (C) 2009 - 2017 Ultra Commerce
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
package com.ultracommerce.core.web.expression.checkout;

import com.ultracommerce.common.payment.PaymentAdditionalFieldType;
import com.ultracommerce.common.payment.PaymentType;
import com.ultracommerce.common.payment.dto.PaymentRequestDTO;
import com.ultracommerce.common.util.UCPaymentMethodUtils;
import com.ultracommerce.common.web.UltraRequestContext;
import com.ultracommerce.common.web.expression.UltraVariableExpression;
import com.ultracommerce.common.web.payment.controller.PaymentGatewayAbstractController;
import com.ultracommerce.core.order.domain.NullOrderImpl;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.payment.domain.OrderPayment;
import com.ultracommerce.core.payment.domain.PaymentTransaction;
import com.ultracommerce.core.payment.service.OrderPaymentService;
import com.ultracommerce.core.payment.service.OrderToPaymentRequestDTOService;
import com.ultracommerce.core.web.order.CartState;
import com.ultracommerce.core.web.order.service.CartStateService;
import com.ultracommerce.presentation.condition.ConditionalOnTemplating;
import org.springframework.stereotype.Component;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Chris Kittrell (ckittrell)
 */
@Component("ucPaymentMethodVariableExpression")
@ConditionalOnTemplating
public class PaymentMethodVariableExpression implements UltraVariableExpression {

    @Resource(name = "ucCartStateService")
    protected CartStateService cartStateService;

    @Resource(name = "ucOrderPaymentService")
    protected OrderPaymentService orderPaymentService;

    @Resource(name = "ucOrderToPaymentRequestDTOService")
    protected OrderToPaymentRequestDTOService orderToPaymentRequestDTOService;

    @Override
    public String getName() {
        return "paymentMethod";
    }

    public PaymentRequestDTO getPaymentRequestDTO() {
        Order cart = CartState.getCart();

        return isNullOrder(cart) ? null : orderToPaymentRequestDTOService.translateOrder(cart);
    }

    protected boolean isNullOrder(Order cart) {
        return cart == null || (cart instanceof NullOrderImpl);
    }

    public boolean cartContainsThirdPartyPayment() {
        return cartStateService.cartHasThirdPartyPayment();
    }

    public boolean cartContainsCreditCardPayment() {
        return cartStateService.cartHasCreditCardPayment();
    }

    public boolean cartContainsTemporaryCreditCard() {
        return cartStateService.cartHasTemporaryCreditCard();
    }

    public boolean orderContainsCODPayment(Order order) {
        return orderContainsPaymentOfType(order, PaymentType.COD);
    }

    public boolean orderContainsCreditCardPayment(Order order) {
        return orderContainsPaymentOfType(order, PaymentType.CREDIT_CARD);
    }

    public boolean orderContainsThirdPartyPayment(Order order) {
        return orderContainsPaymentOfType(order, PaymentType.THIRD_PARTY_ACCOUNT);
    }

    public boolean orderContainsPaymentOfType(Order order, PaymentType paymentType) {
        List<OrderPayment> orderPayments = orderPaymentService.readPaymentsForOrder(order);

        for (OrderPayment payment : orderPayments) {
            boolean isActive = payment.isActive();
            boolean isOfCorrectType = paymentType.equals(payment.getType());

            if (isActive && isOfCorrectType) {
                return true;
            }
        }
        return false;
    }

    /**
     * A helper method used to construct a list of Credit Card Expiration Months
     * Useful for expiration dropdown menus.
     * Will use locale to determine language if a locale is available.
     *
     * @return List containing expiration months of the form "01 - January"
     */
    public List<String> getExpirationMonthOptions() {
        return UCPaymentMethodUtils.getExpirationMonthOptions();
    }

    /**
     * A helper method used to construct a list of Credit Card Expiration Years
     * Useful for expiration dropdown menus.
     *
     * @return List of the next ten years starting with the current year.
     */
    public List<String> getExpirationYearOptions() {
        return UCPaymentMethodUtils.getExpirationYearOptions();
    }

    public String getCreditCardTypeFromCart() {
        return getCartOrderPaymentProperty(PaymentAdditionalFieldType.CARD_TYPE.getType());
    }

    public String getCreditCardLastFourFromCart() {
        return getCartOrderPaymentProperty(PaymentAdditionalFieldType.LAST_FOUR.getType());
    }

    public String getCreditCardExpDateFromCart() {
        return getCartOrderPaymentProperty(PaymentAdditionalFieldType.EXP_DATE.getType());
    }

    protected String getCartOrderPaymentProperty(String propertyName) {
        Order cart = CartState.getCart();
        List<OrderPayment> orderPayments = orderPaymentService.readPaymentsForOrder(cart);
        for (OrderPayment orderPayment : orderPayments) {
            if (orderPayment.isActive() && PaymentType.CREDIT_CARD.equals(orderPayment.getType())) {
                List<PaymentTransaction> transactions = orderPayment.getTransactions();
                for (PaymentTransaction transaction : transactions) {
                    return transaction.getAdditionalFields().get(propertyName);
                }
            }
        }
        return null;
    }

    /**
     * This method is responsible for gathering any Payment Processing Errors that may have been stored
     * as a Redirect Attribute when attempting to checkout.
     */
    public String getPaymentProcessingError() {
        UltraRequestContext ucContext = UltraRequestContext.getUltraRequestContext();
        HttpServletRequest request = ucContext.getRequest();

        return request.getParameter(PaymentGatewayAbstractController.PAYMENT_PROCESSING_ERROR);
    }

}
