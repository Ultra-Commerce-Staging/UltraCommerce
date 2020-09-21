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
package com.ultracommerce.core.web.payment.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.payment.dto.PaymentRequestDTO;
import com.ultracommerce.common.payment.service.CurrentOrderPaymentRequestService;
import com.ultracommerce.common.vendor.service.exception.PaymentException;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.domain.OrderAttribute;
import com.ultracommerce.core.order.domain.OrderAttributeImpl;
import com.ultracommerce.core.order.service.OrderService;
import com.ultracommerce.core.payment.service.OrderToPaymentRequestDTOService;
import com.ultracommerce.core.pricing.service.exception.PricingException;
import com.ultracommerce.core.web.order.CartState;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

/**
 *
 * @author Phillip Verheyden (phillipuniverse)
 */
@Service("ucDefaultCurrentPaymentRequestService")
public class DefaultCurrentOrderPaymentRequestService implements CurrentOrderPaymentRequestService {

    private static final Log LOG = LogFactory.getLog(DefaultCurrentOrderPaymentRequestService.class);

    @Resource(name = "ucOrderToPaymentRequestDTOService")
    protected OrderToPaymentRequestDTOService paymentRequestDTOService;

    @Resource(name = "ucOrderService")
    protected OrderService orderService;
    
    @Override
    public PaymentRequestDTO getPaymentRequestFromCurrentOrder() {
        Order currentCart = CartState.getCart();
        PaymentRequestDTO request = paymentRequestDTOService.translateOrder(currentCart);
        return request;
    }

    @Override
    public void addOrderAttributeToCurrentOrder(String orderAttributeKey, String orderAttributeValue) throws PaymentException {
        addOrderAttributeToOrder(null, orderAttributeKey, orderAttributeValue);
    }

    @Override
    public void addOrderAttributeToOrder(Long orderId, String orderAttributeKey, String orderAttributeValue) throws PaymentException {
        Order currentCart = CartState.getCart();
        Long currentCartId = currentCart.getId();
        
        if (orderId != null && !currentCartId.equals(orderId)) {
            logWarningIfCartMismatch(currentCartId, orderId);
            currentCart = orderService.findOrderById(orderId);
        }

        OrderAttribute orderAttribute = currentCart.getOrderAttributes().get(orderAttributeKey);

        if (orderAttribute == null) {
            orderAttribute = new OrderAttributeImpl();
        }
        orderAttribute.setName(orderAttributeKey);
        orderAttribute.setValue(orderAttributeValue);
        orderAttribute.setOrder(currentCart);
        currentCart.getOrderAttributes().put(orderAttributeKey, orderAttribute);

        try {
            orderService.save(currentCart, false);
        } catch (PricingException e) {
            throw new PaymentException(e);
        }
    }
    
    protected void logWarningIfCartMismatch(Long currentCartId, Long orderId) {
        if (LOG.isWarnEnabled()) {
            LOG.warn(String.format("The current cart resolved from cart state [%s] is not the same as the requested order ID [%s]. Session may have expired or local cart state was lost. This may need manual review.", currentCartId, orderId));
        }
    }

    @Override
    public String retrieveOrderAttributeFromCurrentOrder(String orderAttributeKey) {
        return retrieveOrderAttributeFromOrder(null, orderAttributeKey);
    }

    @Override
    public String retrieveOrderAttributeFromOrder(Long orderId, String orderAttributeKey) {
        Order currentCart = CartState.getCart();
        Long currentCartId = currentCart.getId();
        
        if (orderId != null && !currentCartId.equals(orderId)) {
            logWarningIfCartMismatch(currentCartId, orderId);
            currentCart = orderService.findOrderById(orderId);
        }

        if (currentCart.getOrderAttributes().containsKey(orderAttributeKey)) {
            return currentCart.getOrderAttributes().get(orderAttributeKey).getValue();
        }

        return null;
    }

}