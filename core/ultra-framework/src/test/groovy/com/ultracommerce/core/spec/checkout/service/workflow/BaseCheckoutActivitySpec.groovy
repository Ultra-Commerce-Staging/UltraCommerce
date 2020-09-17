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

import com.ultracommerce.core.checkout.service.workflow.CheckoutSeed
import com.ultracommerce.core.order.domain.Order
import com.ultracommerce.core.order.domain.OrderImpl
import com.ultracommerce.core.workflow.BaseActivity
import com.ultracommerce.core.workflow.DefaultProcessContextImpl
import com.ultracommerce.core.workflow.ProcessContext
import com.ultracommerce.profile.core.domain.Customer
import com.ultracommerce.profile.core.domain.CustomerImpl

import spock.lang.Specification

/**
 * @author Elbert Bautista (elbertbautista)
 */
class BaseCheckoutActivitySpec extends Specification {

    BaseActivity<ProcessContext<CheckoutSeed>> activity;
    ProcessContext<CheckoutSeed> context;

    def setup() {
        Customer customer = new CustomerImpl()
        customer.id = 1
        Order order = new OrderImpl()
        order.id = 1
        order.customer = customer
        context = new DefaultProcessContextImpl<CheckoutSeed>().with{
			seedData = new CheckoutSeed(order, new HashMap<String, Object>())
			it
		} 
    }

}
