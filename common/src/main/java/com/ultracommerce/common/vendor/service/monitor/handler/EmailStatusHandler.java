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
package com.ultracommerce.common.vendor.service.monitor.handler;

import com.ultracommerce.common.email.domain.EmailTarget;
import com.ultracommerce.common.email.service.EmailService;
import com.ultracommerce.common.email.service.info.EmailInfo;
import com.ultracommerce.common.vendor.service.monitor.StatusHandler;
import com.ultracommerce.common.vendor.service.type.ServiceStatusType;

import javax.annotation.Resource;

public class EmailStatusHandler implements StatusHandler {

    @Resource(name="ucEmailService")
    protected EmailService emailService;

    protected EmailInfo emailInfo;
    protected EmailTarget emailTarget;

    public void handleStatus(String serviceName, ServiceStatusType status) {
        String message = serviceName + " is reporting a status of " + status.getType();
        EmailInfo copy = emailInfo.clone();
        copy.setMessageBody(message);
        copy.setSubject(message);
        emailService.sendBasicEmail(copy, emailTarget, null);
    }

    public EmailInfo getEmailInfo() {
        return emailInfo;
    }

    public void setEmailInfo(EmailInfo emailInfo) {
        this.emailInfo = emailInfo;
    }

    public EmailTarget getEmailTarget() {
        return emailTarget;
    }

    public void setEmailTarget(EmailTarget emailTarget) {
        this.emailTarget = emailTarget;
    }

}
