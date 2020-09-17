/*
 * #%L
 * UltraCommerce Framework Web
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
package com.ultracommerce.core.web.order;

import org.aspectj.lang.ProceedingJoinPoint;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.profile.core.domain.Customer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class OrderStateAOP implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    public Object processOrderRetrieval(ProceedingJoinPoint call) throws Throwable {
        Object returnValue;
        /*
         * we retrieve the OrderState instance directly from the application
         * context, as this bean has a request scope.
         */
        OrderState orderState = (OrderState) applicationContext.getBean("ucOrderState");
        Customer customer = (Customer) call.getArgs()[0];
        Order order = orderState.getOrder(customer);
        if (order != null) {
            returnValue = order;
        } else {
            returnValue = call.proceed();
            returnValue = orderState.setOrder(customer, (Order) returnValue);
        }

        return returnValue;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
