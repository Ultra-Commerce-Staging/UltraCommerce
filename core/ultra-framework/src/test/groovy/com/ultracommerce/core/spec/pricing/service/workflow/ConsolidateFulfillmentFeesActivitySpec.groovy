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
import com.ultracommerce.core.catalog.domain.SkuFeeImpl
import com.ultracommerce.core.catalog.domain.SkuImpl
import com.ultracommerce.core.catalog.service.type.SkuFeeType
import com.ultracommerce.core.order.domain.BundleOrderItemImpl
import com.ultracommerce.core.order.domain.FulfillmentGroupFeeImpl
import com.ultracommerce.core.order.domain.FulfillmentGroupImpl
import com.ultracommerce.core.order.domain.FulfillmentGroupItemImpl
import com.ultracommerce.core.order.domain.Order
import com.ultracommerce.core.order.service.FulfillmentGroupService
import com.ultracommerce.core.pricing.service.workflow.ConsolidateFulfillmentFeesActivity

class ConsolidateFulfillmentFeesActivitySpec extends BasePricingActivitySpec {

    /*
     * The code coverage on this spec is only 69.4% due to not knowing what the format of
     * SkuFee expression statements are for the method shouldApplyFeeToFulfillmentGroup
     * to be further tested.
     * 
     * If someone, whom knows this information, would like to write a test to up the code
     * coverage, please do so.
     */
    FulfillmentGroupService mockFulfillmentGroupService
    Order order
    def setup() {
        //Setup a valid FulfillmentGroup with a FulfillmentItem inside
        // and place it inside the context.seedData order object
        order = context.seedData
        context.seedData.fulfillmentGroups = [
            new FulfillmentGroupImpl().with() {
                fulfillmentGroupItems = [
                    new FulfillmentGroupItemImpl().with() {
                        orderItem = new BundleOrderItemImpl().with() {
                            sku = new SkuImpl().with() {
                                id = 1
                                retailPrice = new Money('1.00')
                                fees = [
                                    new SkuFeeImpl().with() {
                                        feeType = SkuFeeType.FULFILLMENT
                                        name = 'Test'
                                        taxable = true
                                        amount = new Money('1.00')
                                        it
                                    }
                                ] as List
                                it
                            }
                            it
                        }
                        it
                    }
                ]
                it
            }
        ]
    }

    def "Test a valid run with valid data"() {
        mockFulfillmentGroupService = Mock()

        activity = new ConsolidateFulfillmentFeesActivity().with() {
            fulfillmentGroupService = mockFulfillmentGroupService
            it
        }

        when: "I execute ConsolidateFulfillmentfeesActivity"
        context = activity.execute(context)

        then: "FulfillmentGroupService's createFulfillmentGroupFee and save methods should run once"
        1 * mockFulfillmentGroupService.createFulfillmentGroupFee() >> { new FulfillmentGroupFeeImpl() }
        1 * mockFulfillmentGroupService.save(_)
        order == context.seedData
    }
}
