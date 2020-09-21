/*
 * #%L
 * UltraCommerce CMS Module
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
package com.ultracommerce.cms.page.message.jms;

import com.ultracommerce.cms.page.domain.Page;
import com.ultracommerce.cms.page.message.ArchivedPagePublisher;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * JMS implementation of ArchivedPagePublisher.
 * Intended usage is to notify other VMs that a pageDTO needs to be
 * evicted from cache.   This occurs when the page is marked as
 * archived - typically because a replacemet page has been
 * promoted to production.
 *
 * Utilizes Spring JMS template pattern where template and destination
 * are configured via Spring.
 *
 * Created by bpolster.
 */
public class JMSArchivedPagePublisher implements ArchivedPagePublisher {

    private JmsTemplate archivePageTemplate;

    private Destination archivePageDestination;

    @Override
    public void processPageArchive(final Page page, final String basePageKey) {
        archivePageTemplate.send(archivePageDestination, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(basePageKey);
            }
        });
    }

    public JmsTemplate getArchivePageTemplate() {
        return archivePageTemplate;
    }

    public void setArchivePageTemplate(JmsTemplate archivePageTemplate) {
        this.archivePageTemplate = archivePageTemplate;
    }

    public Destination getArchivePageDestination() {
        return archivePageDestination;
    }

    public void setArchivePageDestination(Destination archivePageDestination) {
        this.archivePageDestination = archivePageDestination;
    }
}
