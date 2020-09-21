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

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

import com.ultracommerce.common.util.ApplicationContextHolder;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class CustomerAddressPersistedEntityListener {
    /**
     * Invoked on PostPersist, PostUpdate, and PostRemove. The default implementation is to simply publish a Spring event
     * to the ApplicationContext after the transaction has completed.
     * 
     * @param entity the newly-persisted CustomerAddress
     * @see CustomerPersistedEvent
     */
    @PostPersist
    @PostUpdate
    @PostRemove
    public void customerAddressUpdated(final Object entity) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                    ApplicationContextHolder.getApplicationContext().publishEvent(new CustomerPersistedEvent(((CustomerAddress) entity).getCustomer()));
                }
            });
        }
    }
}
