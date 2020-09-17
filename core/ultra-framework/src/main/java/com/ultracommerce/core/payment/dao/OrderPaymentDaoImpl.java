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
package com.ultracommerce.core.payment.dao;

import com.ultracommerce.common.persistence.EntityConfiguration;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.payment.domain.OrderPayment;
import com.ultracommerce.core.payment.domain.OrderPaymentImpl;
import com.ultracommerce.core.payment.domain.PaymentLog;
import com.ultracommerce.core.payment.domain.PaymentTransaction;
import com.ultracommerce.core.payment.domain.PaymentTransactionImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository("ucOrderPaymentDao")
public class OrderPaymentDaoImpl implements OrderPaymentDao {

    @PersistenceContext(unitName = "ucPU")
    protected EntityManager em;

    @Resource(name = "ucEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    @Override
    public OrderPayment save(OrderPayment payment) {
        return em.merge(payment);
    }

    @Override
    public PaymentTransaction save(PaymentTransaction transaction) {
        return em.merge(transaction);
    }

    @Override
    public PaymentLog save(PaymentLog log) {
        return em.merge(log);
    }

    @Override
    public OrderPayment readPaymentById(Long paymentId) {
        return em.find(OrderPaymentImpl.class, paymentId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<OrderPayment> readPaymentsForOrder(Order order) {
        Query query = em.createNamedQuery("UC_READ_ORDERS_PAYMENTS_BY_ORDER_ID");
        query.setParameter("orderId", order.getId());
        return query.getResultList();
    }

    @Override
    public OrderPayment create() {
        return ((OrderPayment) entityConfiguration.createEntityInstance(OrderPayment.class.getName()));
    }

    @Override
    public PaymentTransaction createTransaction() {
        return entityConfiguration.createEntityInstance(PaymentTransaction.class.getName(), PaymentTransaction.class);
    }

    @Override
    public PaymentTransaction readTransactionById(Long transactionId) {
        return em.find(PaymentTransactionImpl.class, transactionId);
    }

    @Override
    public PaymentLog createLog() {
        return entityConfiguration.createEntityInstance(PaymentLog.class.getName(), PaymentLog.class);
    }

    @Override
    public void delete(OrderPayment paymentInfo) {
        if (!em.contains(paymentInfo)) {
            paymentInfo = readPaymentById(paymentInfo.getId());
        }
        em.remove(paymentInfo);
    }
}
