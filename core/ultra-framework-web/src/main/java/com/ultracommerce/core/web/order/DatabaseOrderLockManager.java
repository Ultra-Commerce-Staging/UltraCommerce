/*
 * #%L
 * UltraCommerce Framework Web
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
package com.ultracommerce.core.web.order;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.util.UCSystemProperty;
import com.ultracommerce.core.order.domain.NullOrderImpl;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.domain.OrderLock;
import com.ultracommerce.core.order.service.OrderLockManager;
import com.ultracommerce.core.order.service.OrderService;

import javax.annotation.Resource;

/**
 * An implementation of the {@link OrderLockManager} that relies on the database to provide synchronization
 * for locks on {@link Order}s. This class leverages the {@link OrderLock} domain object to provide this
 * functionality.
 * 
 * @author Andre Azzolini (apazzolini)
 */
public class DatabaseOrderLockManager implements OrderLockManager {

    protected static final Log LOG = LogFactory.getLog(DatabaseOrderLockManager.class);
    
    @Resource(name = "ucOrderService")
    protected OrderService orderService;

    @Override
    public Object acquireLock(Order order) {
        if (order == null || order instanceof NullOrderImpl) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Thread[" + Thread.currentThread().getId() + "] Attempted to grab a lock for a NullOrderImpl. ");
            }
            return order;
        }

        boolean lockAcquired = false;
        int count = 0;
        while (!lockAcquired) {
            try {
                lockAcquired = orderService.acquireLock(order);
            } catch (Exception e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Couldn't acquire lock - that's ok, we'll retry shortly", e);
                }
            }

            if (!lockAcquired) {
                count++;
                if (count >= getDatabaseLockAcquisitionNumRetries()) {
                    LOG.warn(String.format("Exceeded max retries to attempt to acquire a lock on current Order (%s)", order.getId()));
                    throw new RuntimeException("Exceeded max retries to attempt to acquire a lock on current Order");
                }
                try {
                    long msToSleep = getDatabaseLockPollingIntervalMs();

                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Thread[" + Thread.currentThread().getId() + "] Could not acquire order lock for order[" +
                                order.getId() + "] - sleeping for " + msToSleep + " ms");
                    }
                    Thread.sleep(msToSleep);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return order;
    }

    @Override
    public Object acquireLockIfAvailable(Order order) {
        if (order == null || order instanceof NullOrderImpl) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Attempted to grab a lock - order was null or it was a NullOrderImpl. Not blocking");
            }
            return order;
        }

        boolean lockAcquired = orderService.acquireLock(order); 
        return lockAcquired ? order : null;
    }

    @Override
    public void releaseLock(Object lockObject) {
        Order order = (Order) lockObject;
        if (order instanceof NullOrderImpl) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Thread[" + Thread.currentThread().getId() + "] Attempted to release a lock for a NullOrderImpl");
            }
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Thread[" + Thread.currentThread().getId() + "] releasing lock for order[" + order.getId() + "]");
            }
            orderService.releaseLock(order);
        }
    }

    protected long getDatabaseLockPollingIntervalMs() {
        return UCSystemProperty.resolveLongSystemProperty("order.lock.databaseLockPollingIntervalMs");
    }
    
    protected int getDatabaseLockAcquisitionNumRetries() {
        return UCSystemProperty.resolveIntSystemProperty("order.lock.databaseLockAcquisitionNumRetries", 5);
    }

    @Override
    public boolean isActive() {
        return true;
    }
}
