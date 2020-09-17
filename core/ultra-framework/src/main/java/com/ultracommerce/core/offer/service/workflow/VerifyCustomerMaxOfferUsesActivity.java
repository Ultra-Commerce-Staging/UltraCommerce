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
package com.ultracommerce.core.offer.service.workflow;

import com.ultracommerce.core.checkout.service.workflow.CheckoutSeed;
import com.ultracommerce.core.offer.domain.Offer;
import com.ultracommerce.core.offer.domain.OfferCode;
import com.ultracommerce.core.offer.service.OfferAuditService;
import com.ultracommerce.core.offer.service.OfferService;
import com.ultracommerce.core.offer.service.exception.OfferMaxUseExceededException;
import com.ultracommerce.core.offer.service.type.CustomerMaxUsesStrategyType;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.workflow.BaseActivity;
import com.ultracommerce.core.workflow.ProcessContext;
import org.springframework.stereotype.Component;

import java.util.Set;

import javax.annotation.Resource;

/**
 * <p>Checks the offers being used in the order to make sure that the customer
 * has not exceeded the max uses for the {@link Offer}.</p>
 * 
 * This will also verify that max uses for any {@link OfferCode}s that were used to retrieve the {@link Offer}s.
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
@Component("ucVerifyCustomerMaxOfferUsesActivity")
public class VerifyCustomerMaxOfferUsesActivity extends BaseActivity<ProcessContext<CheckoutSeed>> {

    public static final int ORDER = 1000;

    @Resource(name="ucOfferAuditService")
    protected OfferAuditService offerAuditService;
    
    @Resource(name = "ucOfferService")
    protected OfferService offerService;

    public VerifyCustomerMaxOfferUsesActivity() {
        setOrder(ORDER);
    }

    @Override
    public ProcessContext<CheckoutSeed> execute(ProcessContext<CheckoutSeed> context) throws Exception {
        Order order = context.getSeedData().getOrder();
        Set<Offer> appliedOffers = offerService.getUniqueOffersFromOrder(order);
        
        for (Offer offer : appliedOffers) {
            if (offer.isLimitedUsePerCustomer()) {
                CustomerMaxUsesStrategyType strategy = offer.getMaxUsesStrategyType();
                boolean checkUsingCustomer = (strategy == null || strategy.equals(CustomerMaxUsesStrategyType.CUSTOMER));
                Long currentUses;
                if (checkUsingCustomer) {
                    currentUses = offerAuditService.countUsesByCustomer(order, order.getCustomer().getId(), offer.getId(), offer.getMinimumDaysPerUsage());
                } else {
                    currentUses = offerAuditService.countUsesByAccount(order, order.getUltraAccountId(), offer.getId(), offer.getMinimumDaysPerUsage());
                }
                
                if (currentUses >= offer.getMaxUsesPerCustomer()) {
                    throw new OfferMaxUseExceededException("The customer has used this offer more than the maximum allowed number of times.");
                }
            }
        }
        
        //TODO: allow lenient checking on offer code usage
        for (OfferCode code : order.getAddedOfferCodes()) {
            if (code.isLimitedUse()) {
                Long currentCodeUses = offerAuditService.countOfferCodeUses(order, code.getId());
                if (currentCodeUses >= code.getMaxUses()) {
                    throw new OfferMaxUseExceededException("Offer code " + code.getOfferCode() + " with id " + code.getId()
                            + " has been than the maximum allowed number of times.");
                }
            }
        }
        
        return context;
    }
   
}
