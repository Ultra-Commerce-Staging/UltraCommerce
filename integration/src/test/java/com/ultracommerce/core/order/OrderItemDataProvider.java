/*
 * #%L
 * UltraCommerce Integration
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
package com.ultracommerce.core.order;

import com.ultracommerce.common.money.Money;
import com.ultracommerce.core.order.domain.DiscreteOrderItemImpl;
import com.ultracommerce.core.order.domain.GiftWrapOrderItemImpl;
import com.ultracommerce.core.order.domain.OrderItemImpl;
import org.testng.annotations.DataProvider;

import java.math.BigDecimal;

public class OrderItemDataProvider {

    @DataProvider(name = "basicDiscreteOrderItem")
    public static Object[][] provideBasicDiscreteSalesOrderItem() {
        OrderItemImpl soi = new DiscreteOrderItemImpl();
        soi.setRetailPrice(new Money(BigDecimal.valueOf(10.25)));
        soi.setQuantity(3);
        return new Object[][] { { soi } };
    }

    @DataProvider(name = "basicGiftWrapOrderItem")
    public static Object[][] provideBasicGiftWrapSalesOrderItem() {
        OrderItemImpl soi = new GiftWrapOrderItemImpl();
        soi.setRetailPrice(new Money(BigDecimal.valueOf(1.25)));
        soi.setQuantity(1);
        return new Object[][] { { soi } };
    }
}
