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
package com.ultracommerce.core.offer.service;

import com.ultracommerce.common.util.TransactionUtils;
import com.ultracommerce.core.offer.dao.OfferAuditDao;
import com.ultracommerce.core.offer.domain.OfferAudit;
import com.ultracommerce.core.order.domain.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.annotation.Resource;


/**
 *
 * @author Phillip Verheyden (phillipuniverse)
 */
@Service("ucOfferAuditService")
public class OfferAuditServiceImpl implements OfferAuditService {
    
    @Resource(name = "ucOfferAuditDao")
    protected OfferAuditDao offerAuditDao;
    
    @Override
    public OfferAudit readAuditById(Long offerAuditId) {
        return offerAuditDao.readAuditById(offerAuditId);
    }

    @Override
    @Transactional(TransactionUtils.DEFAULT_TRANSACTION_MANAGER)
    public OfferAudit save(OfferAudit offerAudit) {
        return offerAuditDao.save(offerAudit);
    }
    
    @Override
    @Transactional(TransactionUtils.DEFAULT_TRANSACTION_MANAGER)
    public void delete(OfferAudit offerAudit) {
        offerAuditDao.delete(offerAudit);
    }

    @Override
    public OfferAudit create() {
        return offerAuditDao.create();
    }

    @Override
    public Long countUsesByCustomer(Order order, Long customerId, Long offerId) {
        return offerAuditDao.countUsesByCustomer(order, customerId, offerId);
    }

    @Override
    public Long countUsesByCustomer(Order order, Long customerId, Long offerId, Long minimumDaysPerUsage) {
        return offerAuditDao.countUsesByCustomer(order, customerId, offerId, minimumDaysPerUsage);
    }

    @Override
    public Long countUsesByAccount(Order order, Long accountId, Long offerId, Long minimumDaysPerUsage) {
        return offerAuditDao.countUsesByAccount(order, accountId, offerId, minimumDaysPerUsage);
    }

    @Deprecated
    @Override
    public Long countUsesByCustomer(Long customerId, Long offerId) {
        return offerAuditDao.countUsesByCustomer(customerId, offerId);
    }

    @Override
    public Long countOfferCodeUses(Order order, Long offerCodeId) {
        return offerAuditDao.countOfferCodeUses(order, offerCodeId);
    }
    
    @Deprecated
    @Override
    public Long countOfferCodeUses(Long offerCodeId) {
        return offerAuditDao.countOfferCodeUses(offerCodeId);
    }

    @Override
    public List<OfferAudit> readOfferAuditsByOrderId(Long orderId) {
        return offerAuditDao.readOfferAuditsByOrderId(orderId);
    }


}
