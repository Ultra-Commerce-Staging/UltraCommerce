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

import com.ultracommerce.common.web.UltraRequestContext;
import com.ultracommerce.core.order.domain.NullOrderImpl;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.domain.OrderPersistedEntityListener;
import com.ultracommerce.core.order.domain.OrderPersistedEvent;
import com.ultracommerce.core.order.service.type.OrderStatus;
import com.ultracommerce.profile.core.domain.Customer;
import com.ultracommerce.profile.web.core.CustomerState;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;


/**
 * {@link ApplicationListener} responsible for updating {@link CartState} with a new version that was persisted.
 * 
 * @author Phillip Verheyden (phillipuniverse)
 * 
 * @see {@link OrderPersistedEntityListener}
 * @see {@link OrderPersistedEvent}
 */
@Component("ucCartStateRefresher")
public class CartStateRefresher implements ApplicationListener<OrderPersistedEvent> {

    /**
     * <p>Resets {@link CartState} with the newly persisted Order. If {@link CartState} was empty, this will only update it if
     * the {@link Order} that has been persisted is the {@link OrderStatus#IN_PROCESS} {@link Order} for the active
     * {@link Customer} (as determined by {@link CustomerState#getCustomer()}. If {@link CartState} was <b>not</b> empty,
     * then it will be replaced only if this newly persisted {@link Order} has the same id.</p>
     * 
     * <p>This ensures that whatever is returned from {@link CartState#getCart()} will always be the most up-to-date
     * database version (meaning, safe to write to the DB).</p>
     */
    @Override
    public void onApplicationEvent(final OrderPersistedEvent event) {
        WebRequest request = UltraRequestContext.getUltraRequestContext().getWebRequest();
        if (request != null) {
             Order dbOrder = event.getOrder();
            //Update the cart state ONLY IF the IDs of the newly persisted order and whatever is already in CartState match
            boolean emptyCartState = CartState.getCart() == null || CartState.getCart() instanceof NullOrderImpl;
            if (emptyCartState) {
                //If cart state is empty, set it to this newly persisted order if it's the active Customer's cart
                if (CustomerState.getCustomer() != null && CustomerState.getCustomer().getId().equals(dbOrder.getCustomer().getId())
                        && OrderStatus.IN_PROCESS.equals(dbOrder.getStatus())) {
                    CartState.setCart(dbOrder);
                }
            } else if (CartState.getCart().getId().equals(dbOrder.getId())) {
                CartState.setCart(dbOrder);
            }
        }
    }

}
