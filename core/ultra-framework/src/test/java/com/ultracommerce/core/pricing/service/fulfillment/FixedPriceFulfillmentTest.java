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
package com.ultracommerce.core.pricing.service.fulfillment;

import junit.framework.TestCase;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import com.ultracommerce.common.money.Money;
import com.ultracommerce.core.order.domain.FulfillmentGroup;
import com.ultracommerce.core.order.domain.FulfillmentGroupImpl;
import com.ultracommerce.core.order.domain.FulfillmentOption;
import com.ultracommerce.core.order.fulfillment.domain.FixedPriceFulfillmentOption;
import com.ultracommerce.core.order.fulfillment.domain.FixedPriceFulfillmentOptionImpl;
import com.ultracommerce.core.pricing.service.fulfillment.provider.FixedPriceFulfillmentPricingProvider;
import com.ultracommerce.core.pricing.service.fulfillment.provider.FulfillmentEstimationResponse;


/**
 * 
 *
 * @author Phillip Verheyden (phillipuniverse)
 */
public class FixedPriceFulfillmentTest extends TestCase {

    public void testNullFulfillmentOptionInEstimation() throws Exception {
        Set<FulfillmentOption> options = new HashSet<FulfillmentOption>();
        FixedPriceFulfillmentOption option1 = new FixedPriceFulfillmentOptionImpl();
        option1.setPrice(new Money(BigDecimal.ONE));
        FixedPriceFulfillmentOption option2= new FixedPriceFulfillmentOptionImpl();
        option2.setPrice(new Money(BigDecimal.TEN));
        
        options.add(option1);
        options.add(option2);
        
        FixedPriceFulfillmentPricingProvider provider = new FixedPriceFulfillmentPricingProvider();
        FulfillmentGroup fg = new FulfillmentGroupImpl();
        FulfillmentEstimationResponse response = provider.estimateCostForFulfillmentGroup(fg, options);
        
        for (Entry<? extends FulfillmentOption, Money> entry : response.getFulfillmentOptionPrices().entrySet()) {
            assertEquals(((FixedPriceFulfillmentOption) entry.getKey()).getPrice(), entry.getValue()); 
        }
    }

}
