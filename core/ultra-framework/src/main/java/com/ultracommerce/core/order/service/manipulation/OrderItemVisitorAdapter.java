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
package com.ultracommerce.core.order.service.manipulation;

import com.ultracommerce.core.order.domain.BundleOrderItem;
import com.ultracommerce.core.order.domain.DiscreteOrderItem;
import com.ultracommerce.core.order.domain.DynamicPriceDiscreteOrderItem;
import com.ultracommerce.core.order.domain.GiftWrapOrderItem;
import com.ultracommerce.core.order.domain.OrderItem;
import com.ultracommerce.core.pricing.service.exception.PricingException;

public class OrderItemVisitorAdapter implements OrderItemVisitor {

    public void visit(BundleOrderItem bundleOrderItem) throws PricingException {
        //do nothing
    }

    public void visit(DiscreteOrderItem discreteOrderItem) throws PricingException {
        //do nothing
    }

    public void visit(DynamicPriceDiscreteOrderItem dynamicPriceDiscreteOrderItem) throws PricingException {
        //do nothing
    }

    public void visit(GiftWrapOrderItem giftWrapOrderItem) throws PricingException {
        //do nothing
    }

    public void visit(OrderItem orderItem) throws PricingException {
        //do nothing
    }

}
