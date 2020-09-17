/*-
 * #%L
 * UltraCommerce Advanced CMS
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
package com.ultracommerce.core.order.event;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.event.AbstractUltraApplicationEventListener;
import com.ultracommerce.common.event.UltraApplicationEventMulticaster;
import com.ultracommerce.common.event.OrderSubmittedEvent;
import com.ultracommerce.common.exception.ServiceException;
import com.ultracommerce.common.notification.service.NotificationDispatcher;
import com.ultracommerce.common.notification.service.type.EmailNotification;
import com.ultracommerce.common.notification.service.type.NotificationEventType;
import com.ultracommerce.common.notification.service.type.SMSNotification;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

/**
 * This event listener is responsible for sending any notifications in response to an {@code OrderSubmittedEvent}. By
 * default, this listener will handle the event synchronously unless the {@link UltraApplicationEventMulticaster} is
 * injected and configured correctly to handle asynchronous events.
 *
 * @see com.ultracommerce.core.checkout.service.workflow.CompleteOrderActivity for where the event is typically published
 * @author Nick Crum ncrum
 */
@Component("ucNotificationOrderSubmittedEventListener")
public class NotificationOrderSubmittedEventListener extends AbstractUltraApplicationEventListener<OrderSubmittedEvent> {

    protected static final String ORDER_CONTEXT_KEY = "order";
    protected static final String CUSTOMER_CONTEXT_KEY = "customer";
    protected final Log LOG = LogFactory.getLog(NotificationOrderSubmittedEventListener.class);

    @Autowired
    @Qualifier("ucOrderService")
    protected OrderService orderService;

    @Autowired
    @Qualifier("ucNotificationDispatcher")
    protected NotificationDispatcher notificationDispatcher;

    @Override
    protected void handleApplicationEvent(OrderSubmittedEvent event) {
        Order order = orderService.findOrderById(event.getOrderId());
        if (order != null) {
            Map<String, Object> context = createContext(order);

            try {
                notificationDispatcher.dispatchNotification(new EmailNotification(order.getEmailAddress(), NotificationEventType.ORDER_CONFIRMATION, context));
            } catch (ServiceException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Failure to dispatch order confirmation email notification", e);
                }
            }

            try {
                notificationDispatcher.dispatchNotification(new SMSNotification(NotificationEventType.ORDER_CONFIRMATION, context));
            } catch (ServiceException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Failure to dispatch order confirmation sms notification", e);
                }
            }
        }
    }

    protected Map<String, Object> createContext(Order order) {
        Map<String, Object> context = new HashMap<>();
        if (order != null) {
            context.put(ORDER_CONTEXT_KEY, order);
            context.put(CUSTOMER_CONTEXT_KEY, order.getCustomer());
        }
        return MapUtils.unmodifiableMap(context);
    }

    @Override
    public boolean isAsynchronous() {
        return true;
    }
}
