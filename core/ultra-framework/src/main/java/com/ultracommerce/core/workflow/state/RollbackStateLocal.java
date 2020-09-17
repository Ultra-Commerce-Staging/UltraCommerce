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

import com.ultracommerce.common.classloader.release.ThreadLocalManager;

import java.util.Stack;

/**
 * Handles the identification of the outermost workflow and the current thread so that the StateManager can
 * operate on the appropriate RollbackHandlers.
 *
 * @author Jeff Fischer
 */
public class RollbackStateLocal {

    private static final ThreadLocal<Stack> THREAD_LOCAL = ThreadLocalManager.createThreadLocal(Stack.class, true);

    public static RollbackStateLocal getRollbackStateLocal() {
        return (RollbackStateLocal) THREAD_LOCAL.get().peek();
    }

    public static void setRollbackStateLocal(RollbackStateLocal rollbackStateLocal) {
        Stack localState = THREAD_LOCAL.get();
        localState.push(rollbackStateLocal);
    }
    
    public static void clearRollbackStateLocal() {
        Stack localState = THREAD_LOCAL.get();
        localState.pop();
    }

    private String threadId;
    private String workflowId;

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public String getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }
}
