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
package com.ultracommerce.common.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * Works in conjection with <code>com.ultracommerce.common.event.UltraApplicationEventMulticaster</code> except 
 * the event listener can indicate if the event should be run in a background thread.  If no TaskExecutor is 
 * configured on the UltraApplicationEventMulticaster then it will be executed synchronously, regardless of 
 * of whether an event listener is configured.
 * 
 * @author Kelly Tisdell
 *
 * @param <ApplicationEvent>
 */
public interface UltraApplicationListener<E extends ApplicationEvent> extends ApplicationListener<E> {

    /**
     * Indicates if this application listener should be run in a background thread if a TaskExecutor is configured. 
     * If no TaskExecutor is configure with a bean name of "ucApplicationEventMulticastTaskExecutor" then this will 
     * be run synchronously regardless. Generally, this should return false as the default implementation has no 
     * reliability guarantees associated with Asynch processing.  
     * However, for convenience this allows asynch processing for situations that don't require 
     * guaranteed processing.  For example, publishing statistics or log events in a background thread would be 
     * candidates for background processing. Handling important database updates would not.
     * 
     * @see com.ultracommerce.common.event.UltraApplicationEventMulticaster
     * 
     * @return
     */
    public boolean isAsynchronous();

}
