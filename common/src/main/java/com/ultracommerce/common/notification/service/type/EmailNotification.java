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
package com.ultracommerce.common.notification.service.type;

import com.ultracommerce.common.email.service.message.Attachment;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Nick Crum ncrum
 */
public class EmailNotification extends Notification {

    protected String emailAddress;
    protected List<Attachment> attachments = new ArrayList<Attachment>();

    public EmailNotification() {
        super();
    }

    public EmailNotification(NotificationEventType notificationEventType, Map<String, Object> context) {
        super(notificationEventType, context);
    }

    public EmailNotification(String emailAddress, NotificationEventType notificationEventType, Map<String, Object> context) {
        super(notificationEventType, context);
        this.emailAddress = emailAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }
}
