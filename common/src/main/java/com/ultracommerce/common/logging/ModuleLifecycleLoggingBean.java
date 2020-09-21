/*
 * #%L
 * UltraCommerce Common Libraries
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
package com.ultracommerce.common.logging;

import com.ultracommerce.common.module.UltraModuleRegistration;

import javax.annotation.PostConstruct;

/**
 * A simple bean that when declared in app context will cause a lifecycle
 * logging message to appear in the logging output.
 *
 * @author Jeff Fischer
 * @see {@link UltraModuleRegistration}
 */
public class ModuleLifecycleLoggingBean {

    private String moduleName;
    private LifeCycleEvent lifeCycleEvent;
    
    public ModuleLifecycleLoggingBean() {
    }
    
    public ModuleLifecycleLoggingBean(String moduleName, LifeCycleEvent lifeCycleEvent) {
        this.moduleName = moduleName;
        this.lifeCycleEvent = lifeCycleEvent;
    }

    /**
     * Initialize the bean and cause the logging message to take place
     */
    @PostConstruct
    public void init() {
        if (moduleName == null || lifeCycleEvent == null) {
            throw new IllegalArgumentException("Must supply the moduleName and lifeCycleEvent properties!");
        }
        SupportLogger logger = SupportLogManager.getLogger(moduleName, ModuleLifecycleLoggingBean.class);
        logger.lifecycle(lifeCycleEvent, "");
    }

    /**
     * Retrieve the type of life cycle event for this logging message
     *
     * @return life cycle event type
     */
    public LifeCycleEvent getLifeCycleEvent() {
        return lifeCycleEvent;
    }

    /**
     * Set the type of life cycle event for this logging message
     *
     * @param lifeCycleEvent life cycle event type
     */
    public void setLifeCycleEvent(LifeCycleEvent lifeCycleEvent) {
        this.lifeCycleEvent = lifeCycleEvent;
    }

    /**
     * The name of the module that this log message applies to
     *
     * @return the module name for this logging message
     */
    public String getModuleName() {
        return moduleName;
    }

    /**
     * Set the name of the module that this log message applies to
     *
     * @param moduleName the module name for this logging message
     */
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
}
