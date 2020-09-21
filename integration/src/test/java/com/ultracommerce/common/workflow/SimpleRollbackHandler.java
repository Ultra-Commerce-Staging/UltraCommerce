/*
 * #%L
 * UltraCommerce Integration
 * %%
 * Copyright (C) 2009 - 2018 Ultra Commerce
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
package com.ultracommerce.common.workflow;

import com.ultracommerce.core.workflow.Activity;
import com.ultracommerce.core.workflow.ProcessContext;
import com.ultracommerce.core.workflow.state.RollbackFailureException;
import com.ultracommerce.core.workflow.state.RollbackHandler;

import java.util.List;
import java.util.Map;

public class SimpleRollbackHandler implements RollbackHandler<ProcessContext<List<String>>> {

    @Override
    public void rollbackState(Activity<ProcessContext<List<String>>> activity, ProcessContext<List<String>> processContext, Map<String, Object> stateConfiguration) throws RollbackFailureException {
        processContext.getSeedData().add("Rollback" + ((SimpleActivity) activity).getName());
    }
    
}
