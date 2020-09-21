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
import com.ultracommerce.core.order.domain.FulfillmentGroupFeeImpl
import com.ultracommerce.core.order.domain.FulfillmentGroupImpl
import com.ultracommerce.core.order.domain.FulfillmentGroupItemImpl
import com.ultracommerce.core.order.domain.TaxDetailImpl
import com.ultracommerce.core.pricing.service.workflow.TotalActivity


class TotalActivitySpec extends BasePricingActivitySpec{

    /*
     * setTaxSums(order)
     *      Exit cases:
     *          1. order.taxOverride = true
     *              Code Coverage Notes:
     *                  order.fulfillmentGroups -> FG.taxes != null
     *                  FG.fulfillmentGroupItems -> FGI.taxes != null
     *                  FG.fulfillmentGroupFees -> FGF.taxes != null
     *          Result:
     *              FG.totalFulfillmentGroupTax == 0
     *              FG.totalItemTax == 0
     *              FG.totalFeeTax == 0
     *              order.totalTax == 0
     *          2. order.taxOverride = false
     *              Code Coverage Notes:
     *                  order.fulfillmentGroups -> FG.taxes != null
     *                  FG.fulfillmentGroupItems -> FGI.taxes != null
     *                  FG.fulfillmentGroupFees -> FGF.taxes != null
     *          Result:
     *              FG.totalFulfillmentGroupTax == Sum of FG.taxes
     *              FG.totalItemTax == sum of FGI.taxes
     *              FG.totalFeeTax == sum of FGF.taxes
     *              FG.totalTax == sum of totalFulfillmentGroupTax, totalItemTax, and totalFeeTax
     *              order.totalTax == sum of FG.totalTax
     *              
     * execute(context)
     *      This method will need the following:
     *          Order
     *              subTotal -> Money
     *              orderAdjustments
     *                  OrderAdjustment
     *              totalShipping -> Money
     *              totalTax -> derived from when setTaxSums is run
     *              fulfillmentGroups
     *                  FulfillmentGroup
     *                      merchandiseTotal -> Money
     *                      shippingPrice -> Money (Deprecated)
     *                      totalTax -> derived from when setTaxSums is run
     *                      fulfillmentGroupFees
     *                          FulfillmentGroupFee
     *                              amount -> Money
     *                              taxes
     *                                  TaxDetail
     *                                      amount -> Money
     *                      fulfillmentGroupItems
     *                          FulfillmentGroupItem
     *                              taxes
     *                                  TaxDetail
     *                                      amount -> Money
     *                      taxes
     *                          TaxDetail
     *                              amount -> Money            
     */
    FulfillmentGroup fulfillmentGroup1
    
    def setup() {
        context.seedData.subTotal = new Money("10.00")
        context.seedData.totalShipping = new Money("1.99")
        fulfillmentGroup1 = new FulfillmentGroupImpl().with() {
            order = context.seedData
            merchandiseTotal = new Money("10.00")
            shippingPrice = new Money("1.99")
            fulfillmentGroupFees = [new FulfillmentGroupFeeImpl().with() {
                amount = new Money("0.50")
                taxes = [new TaxDetailImpl().with() {
                    amount = new Money("0.20")
                    it
                }
                ]
                it
            }
            ]
            fulfillmentGroupItems = [new FulfillmentGroupItemImpl().with() {
                taxes = [new TaxDetailImpl().with() {
                    amount = new Money("0.20")
                    it   
                }
                ]
                it  
            }
            ]
            taxes = [new TaxDetailImpl().with() {
                amount = new Money("0.20")
                it
            }
            ]
            it
        }
        fulfillmentGroup1.fulfillmentGroupFees.get(0).fulfillmentGroup = fulfillmentGroup1
        context.seedData.fulfillmentGroups = [fulfillmentGroup1]
    }
    
    def "Test TotalActivity with valid data"() {
        activity = new TotalActivity()
        
        when: "I execute TotalActivity"
        context = activity.execute(context)
        
        then: "The sum of all the fees, taxes, and totals should add up to be 13.09"
        context.seedData.total.amount == 13.09
    }
    
    def "Test TotalActivity with taxOverride enabled"() {
        setup: "Activate taxOverride in the order"
        context.seedData.taxOverride = true
        activity = new TotalActivity()
        
        when: "I execute TotalActivity"
        context = activity.execute(context)
        
        then: "The sum of all the fees, and totals, not including taxes, should add up to be 12.49"
        context.seedData.total.amount == 12.49
    }
}
