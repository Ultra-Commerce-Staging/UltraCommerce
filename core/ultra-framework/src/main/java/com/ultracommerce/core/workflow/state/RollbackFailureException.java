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
package com.ultracommerce.core.workflow.state;

import com.ultracommerce.core.workflow.Activity;
import com.ultracommerce.core.workflow.ProcessContext;
import com.ultracommerce.core.workflow.WorkflowException;

import java.util.Map;

/**
 * This exception is thrown to indicate a problem while trying to rollback
 * state for any and all activities during a failed workflow. Only those
 * activities that register their state with the ProcessContext will have
 * their state rolled back.
 *
 * @author Jeff Fischer
 */
public class RollbackFailureException extends WorkflowException {

    private static final long serialVersionUID = 1L;

    protected Activity<? extends ProcessContext<?>> activity;
    protected ProcessContext<?> processContext;
    protected Map<String, Object> stateItems;
    protected Throwable originalWorkflowException;

    public RollbackFailureException() {
    }

    public RollbackFailureException(Throwable cause) {
        super(cause);
    }
    
    public RollbackFailureException(Throwable rollbackFailureCause, Throwable originalWorkflowException) {
        super(rollbackFailureCause);
        this.originalWorkflowException = originalWorkflowException;
    }

    public RollbackFailureException(String message) {
        super(message);
    }

    public RollbackFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public Activity<? extends ProcessContext<?>> getActivity() {
        return activity;
    }

    public void setActivity(Activity<? extends ProcessContext<?>> activity) {
        this.activity = activity;
    }

    public ProcessContext<?> getProcessContext() {
        return processContext;
    }

    public void setProcessContext(ProcessContext<?> processContext) {
        this.processContext = processContext;
    }

    public Map<String, Object> getStateItems() {
        return stateItems;
    }

    public void setStateItems(Map<String, Object> stateItems) {
        this.stateItems = stateItems;
    }
    
    public void setOriginalWorkflowException(Throwable originalWorkflowException) {
        this.originalWorkflowException = originalWorkflowException;
    }
    
    public Throwable getOriginalWorkflowException() {
        return originalWorkflowException;
    }
}
