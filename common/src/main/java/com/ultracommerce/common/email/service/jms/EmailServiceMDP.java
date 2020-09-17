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
package com.ultracommerce.common.email.service.jms;

import com.ultracommerce.common.email.service.exception.EmailException;
import com.ultracommerce.common.email.service.message.MessageCreator;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.MailSendException;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.HashMap;

/**
 * @author jfischer
 */
public class EmailServiceMDP implements MessageListener {

    @Resource(name = "ucMessageCreator")
    private MessageCreator messageCreator;

    /*
     * (non-Javadoc)
     * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
     */
    @SuppressWarnings("unchecked")
    public void onMessage(Message message) {
        try {
            HashMap props = (HashMap) ((ObjectMessage) message).getObject();
            messageCreator.sendMessage(props);
        } catch (MailAuthenticationException e) {
            throw new EmailException(e);
        } catch (MailPreparationException e) {
            throw new EmailException(e);
        } catch (MailParseException e) {
            throw new EmailException(e);
        } catch (MailSendException e) {
            /*
             * TODO find the specific exception that results from the smtp
             * server being down, and throw this as an EmailException.
             * Otherwise, log and then swallow this exception, as it may have
             * been possible that this email was actually sent.
             */
            throw new EmailException(e);
        } catch (JMSException e) {
            throw new EmailException(e);
        }
    }

}
