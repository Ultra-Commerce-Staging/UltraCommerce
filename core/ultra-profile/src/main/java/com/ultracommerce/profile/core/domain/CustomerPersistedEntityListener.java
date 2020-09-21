/*
 * #%L
 * UltraCommerce Profile
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
package com.ultracommerce.profile.core.domain;

import com.ultracommerce.common.util.ApplicationContextHolder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;


/**
 * The main function of this entity listener is to publish a Spring event that the customer has been persisted. This is
 * necessary in order to update the current customer in the application
 *
 * @author Phillip Verheyden (phillipuniverse)
 * 
 * @see {@link ApplicationEventPublisher#publishEvent(org.springframework.context.ApplicationEvent)}
 * @see {@link CustomerPersistedEvent}
 * @see {@link com.ultracommerce.profile.web.core.CustomerStateRefresher}
 * @see {@link com.ultracommerce.profile.web.core.CustomerState}
 */
public class CustomerPersistedEntityListener {
    
    /**
     * Invoked on both the PostPersist and PostUpdate. The default implementation is to simply publish a Spring event
     * to the ApplicationContext after the transaction has completed.
     * 
     * @param entity the newly-persisted Customer
     * @see CustomerPersistedEvent
     */
    @PostPersist
    @PostUpdate
    public void customerUpdated(final Object entity) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                    ApplicationContextHolder.getApplicationContext().publishEvent(new CustomerPersistedEvent((Customer) entity));
                }
            });
        }
    }
    
}
