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

import com.ultracommerce.core.offer.domain.OfferCodeImpl
import com.ultracommerce.core.offer.service.OfferService
import com.ultracommerce.core.order.service.OrderService
import com.ultracommerce.core.pricing.service.workflow.OfferActivity

class OfferActivitySpec extends BasePricingActivitySpec {

    OfferService mockOfferService
    OrderService mockOrderService

    def setup() {
        mockOfferService = Mock()
        mockOrderService = Mock()
    }

    def"Test a valid run with valid data"() {

        activity = new OfferActivity().with {
            offerService = mockOfferService
            orderService = mockOrderService
            it
        }

        when: "I execute the OfferActivity"
        context = activity.execute(context)

        then: "orderService's addOfferCodes should have run and offerService's buildOfferListForOrder as well as applyAndSaveOffersToOrder should have run"
        1 * mockOfferService.buildOfferCodeListForCustomer(_) >> [new OfferCodeImpl()]
        1 * mockOfferService.applyAndSaveOffersToOrder(_, _) >> context.seedData
        1 * mockOrderService.addOfferCodes(_, _, _) >> context.seedData
        context.seedData != null
    }
}
