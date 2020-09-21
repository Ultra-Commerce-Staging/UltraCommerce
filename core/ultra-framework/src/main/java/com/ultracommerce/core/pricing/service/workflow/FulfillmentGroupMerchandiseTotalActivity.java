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
package com.ultracommerce.core.pricing.service.workflow;

import com.ultracommerce.common.currency.util.UltraCurrencyUtils;
import com.ultracommerce.common.money.Money;
import com.ultracommerce.core.order.domain.FulfillmentGroup;
import com.ultracommerce.core.order.domain.FulfillmentGroupItem;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.domain.OrderItem;
import com.ultracommerce.core.workflow.BaseActivity;
import com.ultracommerce.core.workflow.ProcessContext;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Called during the pricing workflow to set the merchandise total for each FulfillmentGroup
 * in an Order. This activity should come before any activity dealing with pricing FulfillmentGroups
 * 
 * @author Phillip Verheyden
 * @see {@link FulfillmentGroup#setMerchandiseTotal(Money)}, {@link FulfillmentGroup#getMerchandiseTotal()}
 */
@Component("ucFulfillmentGroupMerchandiseTotalActivity")
public class FulfillmentGroupMerchandiseTotalActivity extends BaseActivity<ProcessContext<Order>> {

    public static final int ORDER = 4000;
    
    public FulfillmentGroupMerchandiseTotalActivity() {
        setOrder(ORDER);
    }
    
    @Override
    public ProcessContext<Order> execute(ProcessContext<Order> context) throws Exception {
        Order order = context.getSeedData();

        for(FulfillmentGroup fulfillmentGroup : order.getFulfillmentGroups()) {
            Money merchandiseTotal = UltraCurrencyUtils.getMoney(BigDecimal.ZERO, fulfillmentGroup.getOrder().getCurrency());
            for(FulfillmentGroupItem fulfillmentGroupItem : fulfillmentGroup.getFulfillmentGroupItems()) {
                OrderItem item = fulfillmentGroupItem.getOrderItem();
                merchandiseTotal = merchandiseTotal.add(item.getTotalPrice());
            }
            fulfillmentGroup.setMerchandiseTotal(merchandiseTotal);
        }
        context.setSeedData(order);

        return context;
    }

}
