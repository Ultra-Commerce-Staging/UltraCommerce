/*
 * #%L
 * UltraCommerce Framework
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
package com.ultracommerce.core.registration;

import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.extension.PostUpdateOrderExtensionHandler;
import com.ultracommerce.core.order.extension.PostUpdateOrderExtensionManager;
import com.ultracommerce.core.order.service.OrderService;
import com.ultracommerce.profile.core.domain.Customer;
import com.ultracommerce.profile.core.service.CustomerService;
import com.ultracommerce.profile.core.service.listener.PostRegistrationObserver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component("ucMergeOrdersByEmailPostRegistrationObserver")
public class MergeOrdersByEmailPostRegistrationObserver implements PostRegistrationObserver {

    @Value("${merge.orders.after.registration:false}")
    private boolean enabled;

    @Resource(name = "ucCustomerService")
    private CustomerService customerService;

    @Resource(name = "ucOrderService")
    private OrderService orderService;

    @Resource(name = "ucPostUpdateOrderExtensionManager")
    private PostUpdateOrderExtensionManager extensionManager;

    @PostConstruct
    protected void init() {
        if (enabled) {
            customerService.addPostRegisterListener(this);
        }
    }

    @Override
    public void processRegistrationEvent(Customer customer) {
        List<Order> orders = orderService.findOrdersByEmail(customer.getEmailAddress());
        List<Order> updOrders = new ArrayList<>();
        for (Order o : orders) {
            if (!o.getCustomer().isRegistered()) {
                o.setCustomer(customer);
                updOrders.add(o);
            }
        }
        List<PostUpdateOrderExtensionHandler> handlers = extensionManager.getHandlers();
        for (PostUpdateOrderExtensionHandler h : handlers) {
            ExtensionResultStatusType status = h.postUpdateAll(updOrders);
            if (!extensionManager.shouldContinue(status, null, null, null)) {
                break;
            }
        }
    }
}
