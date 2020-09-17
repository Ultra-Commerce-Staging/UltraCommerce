/*
 * #%L
 * UltraCommerce Profile
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
package com.ultracommerce.profile.core.event;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.event.AbstractUltraApplicationEventListener;
import com.ultracommerce.common.exception.ServiceException;
import com.ultracommerce.common.notification.service.NotificationDispatcher;
import com.ultracommerce.common.notification.service.type.EmailNotification;
import com.ultracommerce.common.notification.service.type.NotificationEventType;
import com.ultracommerce.common.notification.service.type.SMSNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Nick Crum ncrum
 */
@Component("ucNotificationForgotUsernameEventListener")
public class NotificationForgotUsernameEventListener extends AbstractUltraApplicationEventListener<ForgotUsernameEvent> {

    protected static final String ACTIVE_USERNAMES_CONTEXT_KEY = "activeUsernames";
    protected final Log LOG = LogFactory.getLog(NotificationForgotUsernameEventListener.class);

    @Autowired
    @Qualifier("ucNotificationDispatcher")
    protected NotificationDispatcher notificationDispatcher;

    @Override
    protected void handleApplicationEvent(ForgotUsernameEvent event) {
        Map<String, Object> context = createContext(event);

        try {
            notificationDispatcher.dispatchNotification(new EmailNotification(event.getEmailAddress(), NotificationEventType.FORGOT_USERNAME, context));
        } catch (ServiceException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Unable to send a forgot username email for " + event.getEmailAddress(), e);
            }
        }

        try {
            notificationDispatcher.dispatchNotification(new SMSNotification(event.getPhoneNumber(), NotificationEventType.FORGOT_USERNAME, context));
        } catch (ServiceException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Unable to send a forgot username sms for " + event.getPhoneNumber(), e);
            }
        }
    }

    protected Map<String, Object> createContext(ForgotUsernameEvent event) {
        Map<String, Object> context = new HashMap<>();
        context.put(ACTIVE_USERNAMES_CONTEXT_KEY, event.getActiveUsernames());
        return MapUtils.unmodifiableMap(context);
    }

    @Override
    public boolean isAsynchronous() {
        return true;
    }
}
