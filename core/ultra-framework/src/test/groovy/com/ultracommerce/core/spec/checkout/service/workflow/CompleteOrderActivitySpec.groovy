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
package com.ultracommerce.core.spec.checkout.service.workflow

import com.ultracommerce.core.checkout.service.workflow.CompleteOrderActivity
import com.ultracommerce.core.order.service.type.OrderStatus
/**
 * @author Elbert Bautista (elbertbautista)
 */
class CompleteOrderActivitySpec extends BaseCheckoutActivitySpec {

    def setup() {
        activity = new CompleteOrderActivity().with {
            it
        }
    }

    def "Test that the properties on the Order are properly set"() {
        when: "I execute the CompleteOrderActivity"
        context = activity.execute(context);

        then: "The status on the order is submitted, and the order number and submit date are set"
        context.seedData.order.id == 1
        context.seedData.order.status == OrderStatus.SUBMITTED
        context.seedData.order.orderNumber != null
        context.seedData.order.submitDate != null

    }

}
