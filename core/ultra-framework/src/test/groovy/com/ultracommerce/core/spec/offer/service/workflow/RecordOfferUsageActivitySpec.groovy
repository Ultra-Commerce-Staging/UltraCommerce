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
package com.ultracommerce.core.spec.offer.service.workflow

import com.ultracommerce.core.offer.domain.*
import com.ultracommerce.core.offer.service.OfferAuditService
import com.ultracommerce.core.offer.service.OfferService
import com.ultracommerce.core.offer.service.workflow.RecordOfferUsageActivity
import com.ultracommerce.core.order.domain.Order
import com.ultracommerce.core.spec.checkout.service.workflow.BaseCheckoutActivitySpec
import com.ultracommerce.core.workflow.state.ActivityStateManagerImpl
import com.ultracommerce.core.workflow.state.RollbackStateLocal

/**
 * @author Elbert Bautista (elbertbautista)
 */
class RecordOfferUsageActivitySpec extends BaseCheckoutActivitySpec {

    Set<Offer> appliedOffers = new HashSet<Offer>()

    def setup() {
        def rollbackStateLocal = new RollbackStateLocal();
        rollbackStateLocal.setThreadId("SPOCK_THREAD");
        rollbackStateLocal.setWorkflowId("TEST");
        RollbackStateLocal.setRollbackStateLocal(rollbackStateLocal);

        new ActivityStateManagerImpl().init()

        Offer testOffer = new OfferImpl()
        testOffer.id = 1
        appliedOffers << testOffer
    }

    def "Test Offer Audits are registered with the Activity State Manager"() {
        setup: "I have one offer on the order"

        //Initiate Mocks
        OfferAudit offerAudit = new OfferAuditImpl()

        OfferAuditService mockAuditService = Mock()
        mockAuditService.create() >> offerAudit
        mockAuditService.save(_) >> {OfferAudit audit -> audit}

        OfferService mockOfferService = Mock()
        mockOfferService.getUniqueOffersFromOrder(_) >> {Order order -> appliedOffers}
        mockOfferService.getOffersRetrievedFromCodes(*_) >> new HashMap<Offer, OfferCode>()

        activity = new RecordOfferUsageActivity().with {
            offerService = mockOfferService
            offerAuditService = mockAuditService
            it
        }

        when: "I execute the RecordOfferUsageActivity"
        context = activity.execute(context);

        then: "There should be one OfferAudit in the rollback state"
        Stack<ActivityStateManagerImpl.StateContainer> containers = ActivityStateManagerImpl.stateManager.stateMap.get("SPOCK_THREAD_TEST")
        containers.size() == 1
        Map<String, Object> stateItems = containers.pop().getStateItems()
        List<OfferAudit> audits = stateItems.get(RecordOfferUsageActivity.SAVED_AUDITS)
        audits.size() == 1
        audits.get(0) == offerAudit
    }

}
