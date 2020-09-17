/*
 * #%L
 * UltraCommerce Profile
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
package com.ultracommerce.profile.core.dao;

import com.ultracommerce.common.persistence.EntityConfiguration;
import com.ultracommerce.profile.core.domain.CustomerPayment;
import com.ultracommerce.profile.core.domain.CustomerPaymentImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository("ucCustomerPaymentDao")
public class CustomerPaymentDaoImpl implements CustomerPaymentDao {

    @PersistenceContext(unitName = "ucPU")
    protected EntityManager em;

    @Resource(name = "ucEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    @Override
    public List<CustomerPayment> readCustomerPaymentsByCustomerId(Long customerId) {
        Query query = em.createNamedQuery("UC_READ_CUSTOMER_PAYMENTS_BY_CUSTOMER_ID");
        query.setParameter("customerId", customerId);
        return query.getResultList();
    }

    @Override
    public CustomerPayment save(CustomerPayment customerPayment) {
        return em.merge(customerPayment);
    }

    @Override
    public CustomerPayment readCustomerPaymentByToken(String token) {
        Query query = em.createNamedQuery("UC_READ_CUSTOMER_PAYMENT_BY_TOKEN");
        query.setParameter("token", token);
        CustomerPayment payment = null;
        try{
            payment = (CustomerPayment) query.getSingleResult();
        } catch (NoResultException e) {
           //do nothing
        }
        return  payment;
    }

    @Override
    public CustomerPayment readCustomerPaymentById(Long customerPaymentId) {
        return (CustomerPayment) em.find(CustomerPaymentImpl.class, customerPaymentId);
    }

    @Override
    public void deleteCustomerPaymentById(Long customerPaymentId) {
        CustomerPayment customerPayment = readCustomerPaymentById(customerPaymentId);
        if (customerPayment != null) {
            em.remove(customerPayment);
        }
    }

    @Override
    public void deleteCustomerPaymentByToken(String token) {
        CustomerPayment customerPayment = readCustomerPaymentByToken(token);
        if (customerPayment != null) {
            em.remove(customerPayment);
        }
    }

    @Override
    public CustomerPayment create() {
        return (CustomerPayment) entityConfiguration.createEntityInstance(CustomerPayment.class.getName());
    }

}
