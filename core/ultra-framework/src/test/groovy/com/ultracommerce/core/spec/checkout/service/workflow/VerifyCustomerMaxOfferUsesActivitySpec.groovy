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
package com.ultracommerce.core.spec.checkout.service.workflow

import com.ultracommerce.core.offer.domain.Offer
import com.ultracommerce.core.offer.domain.OfferCode
import com.ultracommerce.core.offer.domain.OfferCodeImpl
import com.ultracommerce.core.offer.domain.OfferImpl
import com.ultracommerce.core.offer.service.OfferAuditService
import com.ultracommerce.core.offer.service.OfferService
import com.ultracommerce.core.offer.service.exception.OfferMaxUseExceededException
import com.ultracommerce.core.offer.service.workflow.VerifyCustomerMaxOfferUsesActivity
import com.ultracommerce.core.order.domain.Order

class VerifyCustomerMaxOfferUsesActivitySpec extends BaseCheckoutActivitySpec {

    Set<Offer> appliedOffers
    OfferService mockOfferService
    OfferAuditService mockOfferAuditService

    def setup() {
        Offer testOffer = new OfferImpl()
        testOffer.maxUsesPerCustomer = 2
        appliedOffers = [testOffer] as Set

        mockOfferService = Mock()
        mockOfferService.getUniqueOffersFromOrder(_) >> {Order order -> appliedOffers}

        mockOfferAuditService = Mock()

        OfferCode offerCode = new OfferCodeImpl()
        offerCode.maxUses = 2
        context.seedData.order.addOfferCode(offerCode)
    }

    def "Test that exception is thrown when one customer has used an offer more times than is allowed"() {
        setup:
        mockOfferAuditService.countUsesByCustomer(_,_,_,_) >> 3
        mockOfferAuditService.countOfferCodeUses(_,_) >> 1
        activity = new VerifyCustomerMaxOfferUsesActivity().with {
            offerService = mockOfferService
            offerAuditService = mockOfferAuditService
            it
        }

        when: "I execute the VerifyCustomerMaxOfferUsesActivity"
        context = activity.execute(context)

        then: "OfferMaxUseExceededException gets thrown"
        thrown(OfferMaxUseExceededException)
    }

    def "Test that exception is thrown when an offer code has been used the maximum number of times"() {
        setup:
        mockOfferAuditService.countUsesByCustomer(_,_,_,_) >> 1
        mockOfferAuditService.countOfferCodeUses(_,_) >> 3
        activity = new VerifyCustomerMaxOfferUsesActivity().with {
            offerService = mockOfferService
            offerAuditService = mockOfferAuditService
            it
        }

        when: "I execute the VerifyCustomerMaxOfferUsesActivity"
        context = activity.execute(context)

        then: "OfferMaxUseExceededException gets thrown"
        thrown(OfferMaxUseExceededException)
    }

    def "Test that no exception is thrown on valid state"() {
        setup:
        mockOfferAuditService.countUsesByCustomer(_,_,_,_) >> 1
        mockOfferAuditService.countOfferCodeUses(_,_) >> 1
        activity = new VerifyCustomerMaxOfferUsesActivity().with {
            offerService = mockOfferService
            offerAuditService = mockOfferAuditService
            it
        }

        when: "I execute the VerifyCustomerMaxOfferUsesActivity"
        context = activity.execute(context)

        then: "No exception gets thrown"
        notThrown(OfferMaxUseExceededException)
    }
}
