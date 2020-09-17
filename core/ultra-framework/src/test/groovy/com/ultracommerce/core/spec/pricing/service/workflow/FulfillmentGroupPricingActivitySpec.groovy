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
import com.ultracommerce.core.order.domain.FulfillmentGroup
import com.ultracommerce.core.order.domain.FulfillmentGroupImpl
import com.ultracommerce.core.pricing.service.FulfillmentPricingService
import com.ultracommerce.core.pricing.service.workflow.FulfillmentGroupPricingActivity


class FulfillmentGroupPricingActivitySpec extends BasePricingActivitySpec {
    /*
     * This activity will need the following
     * 
     * Order
     *      FulfillmentGroup
     *          shippingOverride = false
     *          
     *      FulfillmentGroup
     *          fulfillmentPrice = some amount (Money)
     *          
     * Simple test to make sure that the branches are taken with valid data
     */
    FulfillmentPricingService mockFulfillmentPricingService
    def setup() {
        context.seedData.fulfillmentGroups = [
            null,
            new FulfillmentGroupImpl().with {
                shippingOverride = false
                order = context.seedData
                it
            },
            new FulfillmentGroupImpl().with {
                shippingOverride = true
                fulfillmentPrice = new Money('1.00')
                order = context.seedData
                it
            }
        ]
        mockFulfillmentPricingService = Mock()
    }

    def "Test FulfillmentGroupPricingActivity with valid data"() {
        FulfillmentGroup fulfillmentGroup1 = new FulfillmentGroupImpl().with {
            shippingOverride = true
            order = context.seedData
            fulfillmentPrice = new Money('1.00')
            it
        }
        activity = new FulfillmentGroupPricingActivity().with() {
            fulfillmentPricingService = mockFulfillmentPricingService
            it
        }

        when: "I execute FulfillmentGroupPricingActivity"
        context = activity.execute(context)

        then: "MockFulfillmentPricingService will be invoked once and the sum total of the charges will be set in the order"
        1 * mockFulfillmentPricingService.calculateCostForFulfillmentGroup(_) >> fulfillmentGroup1
        context.seedData.totalFulfillmentCharges.amount == 2.00
    }
}
