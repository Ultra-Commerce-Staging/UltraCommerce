/*
 * #%L
 * UltraCommerce Framework Web
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
package com.ultracommerce.core.web.controller.contact;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.exception.ServiceException;
import com.ultracommerce.common.notification.service.NotificationDispatcher;
import com.ultracommerce.common.notification.service.type.EmailNotification;
import com.ultracommerce.common.notification.service.type.NotificationEventType;
import com.ultracommerce.common.web.controller.UltraAbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import java.util.HashMap;

/**
 * @author Nick Crum ncrum
 */
public class UltraContactUsController extends UltraAbstractController {

    protected static final Log LOG = LogFactory.getLog(UltraContactUsController.class);

    @Value("${site.emailAddress}")
    protected String targetEmailAddress;

    @Autowired
    @Qualifier("ucNotificationDispatcher")
    protected NotificationDispatcher notificationDispatcher;

    public String sendConfirmationEmail(String name, String emailAddress, String comments) {
        HashMap<String, Object> context = new HashMap<String, Object>();
        context.put("name", name);
        context.put("comments", comments);
        context.put("emailAddress", emailAddress);
        try {
            notificationDispatcher.dispatchNotification(new EmailNotification(targetEmailAddress, NotificationEventType.CONTACT_US, context));
        } catch (ServiceException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Unable to send contact us email", e);
            }
            return "redirect:" + getPath();
        }

        return getSuccessView();

    }

    public String index() {
        return getView();
    }

    protected String getPath() {
        return "/contactus";
    }

    protected String getView() {
        return "contactus/contactus";
    }

    protected String getSuccessView() {
        return "contactus/success";
    }
}
