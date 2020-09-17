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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.notification.service.type.Notification;
import com.ultracommerce.common.notification.service.type.SMSNotification;
import org.springframework.stereotype.Service;

/**
 * @author Nick Crum ncrum
 */
@Service("ucSMSNotificationService")
public class DefaultSMSNotificationServiceImpl implements NotificationService {

    protected final Log LOG = LogFactory.getLog(DefaultSMSNotificationServiceImpl.class);

    @Override
    public boolean canHandle(Class<? extends Notification> clazz) {
        return SMSNotification.class.isAssignableFrom(clazz);
    }

    @Override
    public void sendNotification(Notification notification) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Attempt to send sms notification of type " + notification.getType().getFriendlyType());
        }
    }
}
