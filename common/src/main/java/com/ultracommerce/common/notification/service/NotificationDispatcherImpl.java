/*
 * #%L
 * UltraCommerce Common Libraries
 * %%
 * Copyright (C) 2009 - 2017 Ultra Commerce
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
package com.ultracommerce.common.notification.service;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.exception.ServiceException;
import com.ultracommerce.common.notification.service.type.Notification;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author Nick Crum ncrum
 */
@Service("ucNotificationDispatcher")
public class NotificationDispatcherImpl implements NotificationDispatcher {

    protected final Log LOG = LogFactory.getLog(NotificationDispatcherImpl.class);

    protected final List<NotificationService> notificationServices;

    public NotificationDispatcherImpl(List<NotificationService> notificationServices) {
        this.notificationServices = notificationServices;
    }

    @Override
    public void dispatchNotification(Notification notification) throws ServiceException {
        if (CollectionUtils.isEmpty(notificationServices)) {
            throw new ServiceException("No notification services injected to handle notifications");
        }

        if (notification == null) {
            throw new ServiceException("NULL Notification provided to dispatcher, unable to send notification");
        }

        if (notification.getType() == null) {
            throw new ServiceException("Cannot dispatch Notification with no NotificationEventType specified");
        }

        if (notification.getContext() == null) {
            throw new ServiceException("Cannot dispatch Notification with no context specified");
        }

        try {
            for (NotificationService notificationService: ListUtils.emptyIfNull(notificationServices)) {
                if (notificationService.canHandle(notification.getClass())) {
                    notificationService.sendNotification(notification);
                }
            }
        } catch (Exception e) {
            /*
                We are rethrowing any Exception thrown by the NotificationServices as a ServiceException.
                This allows us to be sure that any mistake in dispatching notifications does not cause the
                overarching flow to fail. Instead, the ServiceException must be caught and handled appropriately.
             */
            throw new ServiceException(e);
        }
    }
}
