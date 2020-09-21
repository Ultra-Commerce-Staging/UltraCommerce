/*
 * #%L
 * UltraCommerce Framework
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
package com.ultracommerce.core.checkout.service;

import com.ultracommerce.common.event.UltraApplicationEventPublisher;
import com.ultracommerce.common.event.OrderSubmittedEvent;
import com.ultracommerce.core.checkout.service.exception.CheckoutException;
import com.ultracommerce.core.checkout.service.workflow.CheckoutResponse;
import com.ultracommerce.core.checkout.service.workflow.CheckoutSeed;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.service.OrderService;
import com.ultracommerce.core.order.service.exception.RequiredAttributeNotProvidedException;
import com.ultracommerce.core.order.service.type.OrderStatus;
import com.ultracommerce.core.pricing.service.exception.PricingException;
import com.ultracommerce.core.workflow.ActivityMessages;
import com.ultracommerce.core.workflow.ProcessContext;
import com.ultracommerce.core.workflow.Processor;
import com.ultracommerce.core.workflow.WorkflowException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Resource;

@Service("ucCheckoutService")
public class CheckoutServiceImpl implements CheckoutService {

    @Resource(name="ucCheckoutWorkflow")
    protected Processor<CheckoutSeed, CheckoutSeed> checkoutWorkflow;

    @Autowired
    @Qualifier("ucApplicationEventPublisher")
    protected UltraApplicationEventPublisher eventPublisher;

    @Resource(name="ucOrderService")
    protected OrderService orderService;
    
    /**
     * Map of locks for given order ids. This lock map ensures that only a single request can handle a particular order
     * at a time
     */
    protected static ConcurrentMap<Long, Object> lockMap = new ConcurrentHashMap<>();

    @Override
    public CheckoutResponse performCheckout(Order order) throws CheckoutException {
        //Immediately fail if another thread is currently attempting to check out the order
        Object lockObject = putLock(order.getId());
        if (lockObject != null) {
            throw new CheckoutException("This order is already in the process of being submitted, unable to checkout order -- id: " + order.getId(), new CheckoutSeed(order, new HashMap<String, Object>()));
        }

        // Immediately fail if this order has already been checked out previously
        if (hasOrderBeenCompleted(order)) {
            throw new CheckoutException("This order has already been submitted or cancelled, unable to checkout order -- id: " + order.getId(), new CheckoutSeed(order, new HashMap<String, Object>()));
        }
        
        CheckoutSeed seed = null;
        try {
            // Do a final save of the order before going through with the checkout workflow
            order = orderService.save(order, false);
            seed = new CheckoutSeed(order, new HashMap<String, Object>());

            ProcessContext<CheckoutSeed> context = checkoutWorkflow.doActivities(seed);

            // We need to pull the order off the seed and save it here in case any activity modified the order.
            order = orderService.save(seed.getOrder(), false);
            order.getOrderMessages().addAll(((ActivityMessages) context).getActivityMessages());
            seed.setOrder(order);

            OrderSubmittedEvent event = new OrderSubmittedEvent(this, seed.getOrder().getId(), seed.getOrder().getOrderNumber());
            eventPublisher.publishEvent(event);

            return seed;
        } catch (PricingException e) {
            throw new CheckoutException("Unable to checkout order -- id: " + order.getId(), e, seed);
        } catch (WorkflowException e) {
            throw new CheckoutException("Unable to checkout order -- id: " + order.getId(), e.getRootCause(), seed);
        } catch (RequiredAttributeNotProvidedException e) {
            throw new CheckoutException("Unable to checkout order -- id: " + order.getId(), e.getCause(), seed);
        } finally {
            // The order has completed processing, remove the order from the processing map
            removeLock(order.getId());
        }
    }
    
    /**
     * Checks if the <b>order</b> has already been gone through the checkout workflow.
     * 
     * @param order
     * @return
     */
    protected boolean hasOrderBeenCompleted(Order order) {
        return (OrderStatus.SUBMITTED.equals(order.getStatus()) || OrderStatus.CANCELLED.equals(order.getStatus()));
    }

    /**
    * Get an object to lock on for the given order id
    * 
    * @param orderId
    * @return null if there was not already a lock object available. If an object was already in the map, this will return
    * that object, which means that there is already a thread attempting to go through the checkout workflow
    */
    protected Object putLock(Long orderId) {
        return lockMap.putIfAbsent(orderId, new Object());
    }
    
    /**
     * Done with processing the given orderId, remove the lock from the map
     * 
     * @param orderId
     */
    protected void removeLock(Long orderId) {
        lockMap.remove(orderId);
    }
    
}
