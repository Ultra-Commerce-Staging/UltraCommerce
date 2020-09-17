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
package com.ultracommerce.core.web.order.service;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import com.ultracommerce.common.payment.PaymentAdditionalFieldType;
import com.ultracommerce.common.payment.PaymentGatewayType;
import com.ultracommerce.common.payment.PaymentType;
import com.ultracommerce.core.order.domain.FulfillmentGroup;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.service.FulfillmentGroupService;
import com.ultracommerce.core.payment.domain.OrderPayment;
import com.ultracommerce.core.payment.domain.PaymentTransaction;
import com.ultracommerce.core.payment.service.OrderPaymentService;
import com.ultracommerce.core.web.order.CartState;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.annotation.Resource;

/**
 * @author Chris Kittrell (ckittrell)
 */
@Service("ucCartStateService")
public class CartStateServiceImpl implements CartStateService {

    @Resource(name = "ucFulfillmentGroupService")
    protected FulfillmentGroupService fulfillmentGroupService;

    @Resource(name = "ucOrderPaymentService")
    protected OrderPaymentService orderPaymentService;

    @Override
    public boolean cartHasPopulatedOrderInfo() {
        Order cart = CartState.getCart();
        return StringUtils.isNotBlank(cart.getEmailAddress());
    }

    @Override
    public boolean cartHasPopulatedBillingAddress() {
        Order cart = CartState.getCart();

        List<OrderPayment> orderPayments = orderPaymentService.readPaymentsForOrder(cart);
        for (OrderPayment payment : CollectionUtils.emptyIfNull(orderPayments)) {
            boolean isCreditCardPayment = PaymentType.CREDIT_CARD.equals(payment.getType());
            boolean paymentHasBillingAddress = (payment.getBillingAddress() != null);

            if (payment.isActive() && isCreditCardPayment && paymentHasBillingAddress) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean cartHasPopulatedShippingAddress() {
        Order cart = CartState.getCart();

        for (FulfillmentGroup fulfillmentGroup : CollectionUtils.emptyIfNull(cart.getFulfillmentGroups())) {
            if (fulfillmentGroupService.isShippable(fulfillmentGroup.getType())) {
                if (fulfillmentGroup.getAddress() != null && fulfillmentGroup.getFulfillmentOption() != null) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean cartHasCreditCardPaymentWithSameToken(String paymentToken) {
        Order cart = CartState.getCart();

        List<OrderPayment> orderPayments = orderPaymentService.readPaymentsForOrder(cart);
        for (OrderPayment orderPayment : orderPayments) {
            if (orderPayment.isActive() && PaymentType.CREDIT_CARD.equals(orderPayment.getType())) {
                List<PaymentTransaction> transactions = orderPayment.getTransactions();
                for (PaymentTransaction transaction : transactions) {
                    String orderPaymentToken = transaction.getAdditionalFields().get(PaymentAdditionalFieldType.TOKEN.getType());

                    if (ObjectUtils.equals(orderPaymentToken, paymentToken)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public boolean cartHasTemporaryCreditCard() {
        Order cart = CartState.getCart();

        List<OrderPayment> orderPayments = orderPaymentService.readPaymentsForOrder(cart);
        for (OrderPayment payment : CollectionUtils.emptyIfNull(orderPayments))  {
            boolean isCreditCartPayment = PaymentType.CREDIT_CARD.equals(payment.getType());
            boolean isTemporaryPaymentGateway = PaymentGatewayType.TEMPORARY.equals(payment.getGatewayType());

            if (payment.isActive() && isCreditCartPayment && isTemporaryPaymentGateway) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean cartHasCreditCardPayment() {
        Order cart = CartState.getCart();

        List<OrderPayment> orderPayments = orderPaymentService.readPaymentsForOrder(cart);
        for (OrderPayment payment : CollectionUtils.emptyIfNull(orderPayments))  {
            boolean isCreditCartPayment = PaymentType.CREDIT_CARD.equals(payment.getType());

            if (payment.isActive() && isCreditCartPayment) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean cartHasThirdPartyPayment() {
        Order cart = CartState.getCart();

        List<OrderPayment> orderPayments = orderPaymentService.readPaymentsForOrder(cart);
        for (OrderPayment payment : CollectionUtils.emptyIfNull(orderPayments))  {
            if (payment.isActive() && PaymentType.THIRD_PARTY_ACCOUNT.equals(payment.getType())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean cartHasUnconfirmedCreditCard() {
        return getUnconfirmedCCFromCart() != null;
    }

    protected OrderPayment getUnconfirmedCCFromCart() {
        OrderPayment unconfirmedCC = null;

        Order cart = CartState.getCart();
        List<OrderPayment> orderPayments = orderPaymentService.readPaymentsForOrder(cart);
        for (OrderPayment payment : CollectionUtils.emptyIfNull(orderPayments))  {
            boolean isCreditCartPayment = PaymentType.CREDIT_CARD.equals(payment.getType());
            boolean isTemporaryPaymentGateway = PaymentGatewayType.TEMPORARY.equals(payment.getGatewayType());

            if (payment.isActive() && isCreditCartPayment && !isTemporaryPaymentGateway) {
                unconfirmedCC = payment;
            }
        }
        return unconfirmedCC;
    }

}
