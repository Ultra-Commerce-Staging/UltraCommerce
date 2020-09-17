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

import com.ultracommerce.common.extension.AbstractExtensionHandler;
import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.core.offer.domain.OfferCode;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.service.call.OrderItemRequestDTO;
import com.ultracommerce.profile.core.domain.Customer;

import java.util.List;

/**
 * @author bpolster
 */
public abstract class AbstractOrderServiceExtensionHandler extends AbstractExtensionHandler implements
        OrderServiceExtensionHandler {
    
    @Override
    public ExtensionResultStatusType attachAdditionalDataToNewNamedCart(Customer customer, Order cart) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType preValidateCartOperation(Order cart, ExtensionResultHolder erh) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType preValidateUpdateQuantityOperation(Order cart, OrderItemRequestDTO dto, 
            ExtensionResultHolder erh) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }
    
    @Override
    public ExtensionResultStatusType attachAdditionalDataToOrder(Order order, boolean priceOrder) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType addOfferCodes(Order order, List<OfferCode> offerCodes, boolean priceOrder) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType findCartForCustomerWithEnhancements(Customer customer, ExtensionResultHolder erh) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType findCartForCustomerWithEnhancements(Customer customer, Order candidateCart, ExtensionResultHolder erh) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

}
