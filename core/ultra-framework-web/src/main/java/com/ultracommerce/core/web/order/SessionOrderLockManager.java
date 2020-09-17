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
import com.ultracommerce.common.util.UCRequestUtils;
import com.ultracommerce.common.util.EfficientLRUMap;
import com.ultracommerce.common.web.UltraRequestContext;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.service.OrderLockManager;
import org.springframework.context.ApplicationListener;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * An {@link HttpSession} based {@link OrderLockManager}. This implementation is less concerned with the given Order
 * and instead will lock on the user's session to serialize order modification requests.
 * 
 * @author Andre Azzolini (apazzolini)
 */
public class SessionOrderLockManager implements OrderLockManager, ApplicationListener<HttpSessionDestroyedEvent> {

    private static final Log LOG = LogFactory.getLog(SessionOrderLockManager.class);
    private static final Object LOCK = new Object();
    private static final EfficientLRUMap<String, ReentrantLock> SESSION_LOCKS;
    
    static {
        SESSION_LOCKS = new EfficientLRUMap<>(10000);
    }

    /**
     * Note that although this method accepts an {@link Order} parameter, it does not use it in any way. This 
     * session-based lock manager implementation will prevent all operations that are identified as requiring a
     * lock from happening in parallel. Instead, it will execute them sequentially as locks are released from 
     * previous implementations.
     */
    @Override
    public Object acquireLock(Order order) {
        ReentrantLock lockObject = getSessionLock();
        lockObject.lock();
        return lockObject;
    }

    @Override
    public Object acquireLockIfAvailable(Order order) {
        ReentrantLock lockObject = getSessionLock();
        boolean locked = lockObject.tryLock();
        return locked ? lockObject : null;
    }

    @Override
    public void releaseLock(Object lockObject) {
        ReentrantLock lock = (ReentrantLock) lockObject;
        lock.unlock();
    }
    
    @Override
    public void onApplicationEvent(HttpSessionDestroyedEvent event) {
        ReentrantLock lock = SESSION_LOCKS.remove(event.getSession().getId());
        if (lock != null && LOG.isDebugEnabled()) {
            LOG.debug("Destroyed lock due to session invalidation: " + lock.toString());
        }
    }

    protected HttpServletRequest getRequest() {
        if (RequestContextHolder.getRequestAttributes() == null) {
            return null;
        }
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    protected ReentrantLock getSessionLock() {
        if (!isActive()) {
            throw new IllegalStateException("This is currently a sessionless environment and session cannot be used " +
                    "to obtain a lock. Consider using a different implementation of OrderLockManager.");
        }

        HttpSession session = getRequest().getSession();
        ReentrantLock lock = SESSION_LOCKS.get(session.getId());

        if (lock == null) {
            // There was no session lock object. We'll need to create one. To do this, we have to synchronize the
            // creation globally, so that two threads don't create the session lock at the same time.
            synchronized (LOCK) {
                lock = SESSION_LOCKS.get(session.getId());
                if (lock == null) {
                    lock = new ReentrantLock();
                    SESSION_LOCKS.put(session.getId(), lock);
                }
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Created new lock object: " + lock.toString());
            }
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Returning previously created lock object: " + lock.toString());
            }
        }

        return lock;
    }

    @Override
    public boolean isActive() {
        if (getRequest() == null) {
            return false;
        }
        if (UltraRequestContext.getUltraRequestContext() != null
                && UltraRequestContext.getUltraRequestContext().getWebRequest() != null) {
            if (!UCRequestUtils.isOKtoUseSession(UltraRequestContext.getUltraRequestContext().getWebRequest())) {
                return false;
            }
        } else if (!UCRequestUtils.isOKtoUseSession(new ServletWebRequest(getRequest()))) {
            return false;
        }
        return true;
    }
}
