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
package com.ultracommerce.core.order.service;

import com.ultracommerce.common.extension.ExtensionHandler;
import com.ultracommerce.common.extension.ExtensionManager;
import com.ultracommerce.common.extension.ExtensionManagerOperation;
import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.core.offer.domain.OfferCode;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.service.call.OrderItemRequestDTO;
import com.ultracommerce.profile.core.domain.Customer;
import org.springframework.stereotype.Service;
import java.util.List;


/**
 * @author Andre Azzolini (apazzolini), bpolster
 */
@Service("ucOrderServiceExtensionManager")
public class OrderServiceExtensionManager extends ExtensionManager<OrderServiceExtensionHandler> implements OrderServiceExtensionHandler {

    public static final ExtensionManagerOperation attachAdditionalDataToNewNamedCart = new ExtensionManagerOperation() {
        @Override
        public ExtensionResultStatusType execute(ExtensionHandler handler, Object... params) {
            return ((OrderServiceExtensionHandler) handler).attachAdditionalDataToNewNamedCart((Customer) params[0], (Order) params[1]);
        }
    };

    public static final ExtensionManagerOperation preValidateCartOperation = new ExtensionManagerOperation() {
        @Override
        public ExtensionResultStatusType execute(ExtensionHandler handler, Object... params) {
            return ((OrderServiceExtensionHandler) handler).preValidateCartOperation((Order) params[0], (ExtensionResultHolder) params[1]);
        }
    };

    public static final ExtensionManagerOperation preValidateUpdateQuantityOperation = new ExtensionManagerOperation() {
        @Override
        public ExtensionResultStatusType execute(ExtensionHandler handler, Object... params) {
            return ((OrderServiceExtensionHandler) handler).preValidateUpdateQuantityOperation((Order) params[0], (OrderItemRequestDTO) params[1], (ExtensionResultHolder) params[2]);
        }
    };

    public static final ExtensionManagerOperation attachAdditionalDataToOrder = new ExtensionManagerOperation() {
        @Override
        public ExtensionResultStatusType execute(ExtensionHandler handler, Object... params) {
            return ((OrderServiceExtensionHandler) handler).attachAdditionalDataToOrder((Order) params[0], (Boolean) params[1]);
        }
    };

    public static final ExtensionManagerOperation addOfferCodes = new ExtensionManagerOperation() {
        @Override
        public ExtensionResultStatusType execute(ExtensionHandler handler, Object... params) {
            return ((OrderServiceExtensionHandler) handler).addOfferCodes((Order) params[0], (List<OfferCode>) params[1], (Boolean) params[2]);
        }
    };

    public static final ExtensionManagerOperation findStaleCacheAwareCartForCustomer = new ExtensionManagerOperation() {
        @Override
        public ExtensionResultStatusType execute(ExtensionHandler handler, Object... params) {
            return ((OrderServiceExtensionHandler) handler).findCartForCustomerWithEnhancements((Customer) params[0], (ExtensionResultHolder) params[1]);
        }
    };

    public static final ExtensionManagerOperation findStaleCacheAwareCartForCustomer2 = new ExtensionManagerOperation() {
        @Override
        public ExtensionResultStatusType execute(ExtensionHandler handler, Object... params) {
            return ((OrderServiceExtensionHandler) handler).findCartForCustomerWithEnhancements((Customer) params[0], (Order) params[1], (ExtensionResultHolder) params[2]);
        }
    };

    public OrderServiceExtensionManager() {
        super(OrderServiceExtensionHandler.class);
    }

    /**
     * By default,this extension manager will continue on handled allowing multiple handlers to interact with the order.
     */
    public boolean continueOnHandled() {
        return true;
    }

    @Override
    public ExtensionResultStatusType attachAdditionalDataToNewNamedCart(Customer customer, Order cart) {
        return execute(attachAdditionalDataToNewNamedCart, customer, cart);
    }

    @Override
    public ExtensionResultStatusType preValidateCartOperation(Order cart, ExtensionResultHolder erh) {
        return execute(preValidateCartOperation, cart, erh);
    }

    @Override
    public ExtensionResultStatusType preValidateUpdateQuantityOperation(Order cart, OrderItemRequestDTO dto, ExtensionResultHolder erh) {
        return execute(preValidateUpdateQuantityOperation, cart, dto, erh);
    }

    @Override
    public ExtensionResultStatusType attachAdditionalDataToOrder(Order order, boolean priceOrder) {
        return execute(attachAdditionalDataToOrder, order, priceOrder);
    }

    @Override
    public ExtensionResultStatusType addOfferCodes(Order order, List<OfferCode> offerCodes, boolean priceOrder) {
        return execute(addOfferCodes, order, offerCodes, priceOrder);
    }

    @Override
    public ExtensionResultStatusType findCartForCustomerWithEnhancements(Customer customer, ExtensionResultHolder erh) {
        return execute(findStaleCacheAwareCartForCustomer, customer, erh);
    }

    @Override
    public ExtensionResultStatusType findCartForCustomerWithEnhancements(Customer customer, Order candidateCart, ExtensionResultHolder erh) {
        return execute(findStaleCacheAwareCartForCustomer2, customer, candidateCart, erh);
    }

    @Override
    public boolean isEnabled() {
        //Not used
        return true;
    }
}
