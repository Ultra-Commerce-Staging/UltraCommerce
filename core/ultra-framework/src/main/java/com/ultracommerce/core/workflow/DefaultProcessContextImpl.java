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

import com.ultracommerce.core.order.service.call.ActivityMessageDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Default ProcessContext implementation
 * @author "Priyesh Patel"
 *
 * @param <T> SeedData
 */

public class DefaultProcessContextImpl<T> implements ProcessContext<T>, ActivityMessages {
    public final static long serialVersionUID = 1L;
    protected T seedData;
    protected boolean stopEntireProcess = false;
    
    protected List<ActivityMessageDTO> activityMessages = new ArrayList<ActivityMessageDTO>();

    public boolean stopProcess() {
        this.stopEntireProcess = true;
        return stopEntireProcess;
    }

    public boolean isStopped() {
        return stopEntireProcess;
    }

    public T getSeedData() {
        return seedData;
    }

    public void setSeedData(T seedObject) {
        seedData = (T) seedObject;
    }

    public List<ActivityMessageDTO> getActivityMessages() {
        return activityMessages;
    }

    public void setActivityMessages(List<ActivityMessageDTO> activityMessages) {
        this.activityMessages = activityMessages;
    }
}
