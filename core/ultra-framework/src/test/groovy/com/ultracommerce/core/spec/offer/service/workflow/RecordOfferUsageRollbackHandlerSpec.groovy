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

import com.ultracommerce.core.checkout.service.workflow.CheckoutSeed
import com.ultracommerce.core.offer.domain.OfferAudit
import com.ultracommerce.core.offer.domain.OfferAuditImpl
import com.ultracommerce.core.offer.service.OfferAuditService
import com.ultracommerce.core.offer.service.workflow.RecordOfferUsageActivity
import com.ultracommerce.core.offer.service.workflow.RecordOfferUsageRollbackHandler
import com.ultracommerce.core.spec.checkout.service.workflow.BaseCheckoutRollbackSpec
import com.ultracommerce.core.workflow.state.RollbackHandler

class RecordOfferUsageRollbackHandlerSpec extends BaseCheckoutRollbackSpec {

    //need to set up the list of OfferAudits in the stateConfiguration under RecordOfferUsageActivity.SAVED_AUDITS
    //then delete them

    def "Test that the SAVED_AUDITS in the stateConfiguration are deleted"() {
        setup:"Placing one OfferAudit into the stateConfiguration"
        OfferAudit offerAudit = new OfferAuditImpl()
        List<OfferAudit> offerAudits = new ArrayList()
        offerAudits.add(offerAudit)
        stateConfiguration = new HashMap<String, List>()
        stateConfiguration.put(RecordOfferUsageActivity.SAVED_AUDITS, offerAudits)
        OfferAuditService mockOfferAuditService = Mock()
        RollbackHandler<CheckoutSeed> rollbackHandler = new RecordOfferUsageRollbackHandler().with {
            offerAuditService = mockOfferAuditService
            it
        }

        when:"rollbackState is executed"
        rollbackHandler.rollbackState(activity, context, stateConfiguration)

        then:"stateConfiguration's savedAudit's should be empty"
        1 * mockOfferAuditService.delete(_)
    }


}
