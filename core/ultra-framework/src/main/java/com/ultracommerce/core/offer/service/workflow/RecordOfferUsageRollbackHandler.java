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
import com.ultracommerce.core.offer.domain.OfferAudit;
import com.ultracommerce.core.offer.service.OfferAuditService;
import com.ultracommerce.core.workflow.Activity;
import com.ultracommerce.core.workflow.ProcessContext;
import com.ultracommerce.core.workflow.state.RollbackFailureException;
import com.ultracommerce.core.workflow.state.RollbackHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;


/**
 * Rolls back audits that were saved in the database from {@link RecordOfferUsageActivity}.
 *
 * @author Phillip Verheyden (phillipuniverse)
 * @see {@link RecordOfferUsageActivity}
 */
@Component("ucRecordOfferUsageRollbackHandler")
public class RecordOfferUsageRollbackHandler implements RollbackHandler<ProcessContext<CheckoutSeed>> {

    @Resource(name = "ucOfferAuditService")
    protected OfferAuditService offerAuditService;
    
    @Override
    public void rollbackState(Activity<ProcessContext<CheckoutSeed>> activity, ProcessContext<CheckoutSeed> processContext, Map<String, Object> stateConfiguration) throws RollbackFailureException {
        List<OfferAudit> audits = (List<OfferAudit>) stateConfiguration.get(RecordOfferUsageActivity.SAVED_AUDITS);
        
        for (OfferAudit audit : audits) {
            offerAuditService.delete(audit);
        }
    }
    
}
