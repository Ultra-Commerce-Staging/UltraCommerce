/*
 * #%L
 * UltraCommerce Integration
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
package com.ultracommerce.core.workflow.state.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.core.checkout.service.workflow.CheckoutSeed;
import com.ultracommerce.core.workflow.Activity;
import com.ultracommerce.core.workflow.ProcessContext;
import com.ultracommerce.core.workflow.state.RollbackFailureException;
import com.ultracommerce.core.workflow.state.RollbackHandler;
import com.ultracommerce.core.workflow.state.RollbackStateLocal;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Demonstrate a call to a RollbackHandler
 *
 * @author Jeff Fischer
 */
public class TestRollbackHandler implements RollbackHandler<ProcessContext<CheckoutSeed>> {

    private static final Log LOG = LogFactory.getLog(TestRollbackHandler.class);

    @Override
    @Transactional("ucTransactionManager")
    public void rollbackState(Activity<ProcessContext<CheckoutSeed>> activity,
            ProcessContext<CheckoutSeed> processContext, Map<String, Object> stateConfiguration) throws RollbackFailureException {
        LOG.warn("******************* TestRollbackHandler Engaged *********************");
        LOG.warn("******************* Activity: " + activity.getBeanName() + " *********************");
        RollbackStateLocal rollbackStateLocal = RollbackStateLocal.getRollbackStateLocal();
        LOG.warn("******************* Workflow: " + rollbackStateLocal.getWorkflowId() + " *********************");
        LOG.warn("******************* Thread: " + rollbackStateLocal.getThreadId() + " *********************");
    }
}
