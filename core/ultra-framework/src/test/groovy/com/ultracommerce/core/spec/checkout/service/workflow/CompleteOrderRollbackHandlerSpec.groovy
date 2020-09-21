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
package com.ultracommerce.core.spec.checkout.service.workflow

import com.ultracommerce.core.checkout.service.workflow.CompleteOrderRollbackHandler
import com.ultracommerce.core.order.service.type.OrderStatus
import com.ultracommerce.core.workflow.state.RollbackHandler

class CompleteOrderRollbackHandlerSpec extends BaseCheckoutRollbackSpec {

    def "Test that seed data has been sent to correct values"() {
        RollbackHandler rollbackHandler = new CompleteOrderRollbackHandler()

        when: "rollbackState is executed"
        rollbackHandler.rollbackState(activity, context, stateConfiguration)

        then: "seedData's order status is set to IN_Process, and its order number and submite date are nulled"
        context.seedData.order.status == OrderStatus.IN_PROCESS
        context.seedData.order.orderNumber == null
        context.seedData.order.submitDate == null
    }
}
