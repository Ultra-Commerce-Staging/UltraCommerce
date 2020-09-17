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
/**
 * @author Austin Rooke (austinrooke)
 */
package com.ultracommerce.core.spec.pricing.service.workflow

import com.ultracommerce.common.money.Money
import com.ultracommerce.core.order.domain.FulfillmentGroupImpl
import com.ultracommerce.core.order.domain.FulfillmentGroupItemImpl
import com.ultracommerce.core.order.domain.OrderItem
import com.ultracommerce.core.order.domain.OrderItemImpl
import com.ultracommerce.core.pricing.service.workflow.FulfillmentGroupMerchandiseTotalActivity



class FulfillmentGroupMerchandiseTotalActivitySpec extends BasePricingActivitySpec {
    /*
     * This activity will require the following:
     * Order
     *      FulfillmentGroup
     *          FulfillmentGroupItem
     *              OrderItem
     *                  totalPrice <- some value (Money)
     * May want to have multiple FulfillmentGroupItems for a good test
     */
    def setup() {
        OrderItem orderItem1 = new OrderItemImpl().with {
            salePrice = new Money('5.00')
            quantity = 1
            order = context.seedData
            it
        }
        OrderItem orderItem2 = new OrderItemImpl().with {
            salePrice = new Money('3.00')
            quantity = 5
            order = context.seedData
            it
        }
        context.seedData.fulfillmentGroups = [
            new FulfillmentGroupImpl().with() {
                fulfillmentGroupItems << new FulfillmentGroupItemImpl().with() {
                    orderItem = orderItem1
                    quantity = 1
                    it
                }
                fulfillmentGroupItems << new FulfillmentGroupItemImpl().with() {
                    orderItem = orderItem2
                    quantity = 2
                    it
                }
                order = context.seedData
                it
            },
            new FulfillmentGroupImpl().with() {
                fulfillmentGroupItems << new FulfillmentGroupItemImpl().with() {
                    orderItem = orderItem2
                    quantity = 3
                    it
                }
                order = context.seedData
                it
            }
        ]
    }

    def "Test FulfillmentGroupMerchandiseTotalActivity with valid data"() {
        activity = new FulfillmentGroupMerchandiseTotalActivity()

        when: "I execute FulfillmentGroupMerchandiseTotalActivity"
        context = activity.execute(context)

        then: "fulfillmentGroup1 should have a merchandiseTotal of 20 and fulfillmentGroup2 should have 15"
        context.seedData.fulfillmentGroups.get(0).merchandiseTotal.amount == 20.00
        context.seedData.fulfillmentGroups.get(1).merchandiseTotal.amount == 15.00
    }
}
