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

import com.ultracommerce.common.encryption.EncryptionModule;
import com.ultracommerce.common.persistence.EntityConfiguration;
import com.ultracommerce.core.payment.domain.secure.BankAccountPayment;
import com.ultracommerce.core.payment.domain.secure.CreditCardPayment;
import com.ultracommerce.core.payment.domain.secure.GiftCardPayment;
import com.ultracommerce.core.payment.domain.secure.Referenced;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository("ucSecureOrderPaymentDao")
public class SecureOrderPaymentDaoImpl implements SecureOrderPaymentDao {

    @PersistenceContext(unitName = "ucSecurePU")
    protected EntityManager em;

    @Resource(name = "ucEncryptionModule")
    protected EncryptionModule encryptionModule;

    @Resource(name = "ucEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    public Referenced save(Referenced securePaymentInfo) {
        return em.merge(securePaymentInfo);
    }

    public BankAccountPayment createBankAccountPayment() {
        BankAccountPayment response = entityConfiguration.createEntityInstance(BankAccountPayment.class.getName(), BankAccountPayment.class);
        response.setEncryptionModule(encryptionModule);
        return response;
    }

    public GiftCardPayment createGiftCardPayment() {
        GiftCardPayment response = entityConfiguration.createEntityInstance(GiftCardPayment.class.getName(), GiftCardPayment.class);
        response.setEncryptionModule(encryptionModule);
        return response;
    }

    public CreditCardPayment createCreditCardPayment() {
        CreditCardPayment response = entityConfiguration.createEntityInstance(CreditCardPayment.class.getName(), CreditCardPayment.class);
        response.setEncryptionModule(encryptionModule);
        return response;
    }

    @SuppressWarnings("unchecked")
    public BankAccountPayment findBankAccountPayment(String referenceNumber) {
        Query query = em.createNamedQuery("UC_READ_BANK_ACCOUNT_BY_REFERENCE_NUMBER");
        query.setParameter("referenceNumber", referenceNumber);
        List<BankAccountPayment> infos = query.getResultList();
        BankAccountPayment response = (infos == null || infos.size() == 0) ? null : infos.get(0);
        if (response != null) {
            response.setEncryptionModule(encryptionModule);
        }
        return response;
    }

    @SuppressWarnings("unchecked")
    public CreditCardPayment findCreditCardPayment(String referenceNumber) {
        Query query = em.createNamedQuery("UC_READ_CREDIT_CARD_BY_REFERENCE_NUMBER");
        query.setParameter("referenceNumber", referenceNumber);
        List<CreditCardPayment> infos = query.getResultList();
        CreditCardPayment response = (infos == null || infos.size() == 0) ? null : infos.get(0);
        if (response != null) {
            response.setEncryptionModule(encryptionModule);
        }
        return response;
    }

    @SuppressWarnings("unchecked")
    public GiftCardPayment findGiftCardPayment(String referenceNumber) {
        Query query = em.createNamedQuery("UC_READ_GIFT_CARD_BY_REFERENCE_NUMBER");
        query.setParameter("referenceNumber", referenceNumber);
        List<GiftCardPayment> infos = query.getResultList();
        GiftCardPayment response = (infos == null || infos.size() == 0) ? null : infos.get(0);
        if (response != null) {
            response.setEncryptionModule(encryptionModule);
        }
        return response;
    }

    public void delete(Referenced securePayment) {
        if (!em.contains(securePayment)) {
            securePayment = em.find(securePayment.getClass(), securePayment.getId());
        }
        em.remove(securePayment);
    }

}