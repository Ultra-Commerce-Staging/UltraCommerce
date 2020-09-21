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
package com.ultracommerce.core.workflow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.core.workflow.state.RollbackStateLocal;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("ucDefaultErrorHandler")
public class DefaultErrorHandler implements ErrorHandler {

    private static final Log LOG = LogFactory.getLog(DefaultErrorHandler.class);
    @SuppressWarnings("unused")
    private String name;
    
    protected List<String> unloggedExceptionClasses = new ArrayList<>();

    /* (non-Javadoc)
     * @see com.ultracommerce.core.workflow.ErrorHandler#handleError(com.ultracommerce.core.workflow.ProcessContext, java.lang.Throwable)
     */
    @Override
    public void handleError(ProcessContext context, Throwable th) throws WorkflowException {
        context.stopProcess();

        boolean shouldLog = true;
        Throwable cause = th;
        while (true) {
            if (unloggedExceptionClasses.contains(cause.getClass().getName())) {
                shouldLog = false;
                break;
            }
            cause = cause.getCause();
            if (cause == null) {
                break;
            }
        }
        if (shouldLog) {
            LOG.error(String.format("An error occurred during the %s workflow", RollbackStateLocal.getRollbackStateLocal().getWorkflowId()), th);
        }
        
        throw new WorkflowException(th);
    }

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.BeanNameAware#setBeanName(java.lang.String)
     */
    @Override
    public void setBeanName(String name) {
        this.name = name;
    }

    public List<String> getUnloggedExceptionClasses() {
        return unloggedExceptionClasses;
    }

    public void setUnloggedExceptionClasses(List<String> unloggedExceptionClasses) {
        this.unloggedExceptionClasses = unloggedExceptionClasses;
    }
    
}
