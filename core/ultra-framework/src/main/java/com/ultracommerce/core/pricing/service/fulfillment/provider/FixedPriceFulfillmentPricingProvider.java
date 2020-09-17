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
package com.ultracommerce.core.pricing.service.fulfillment.provider;

import com.ultracommerce.common.money.Money;
import com.ultracommerce.common.vendor.service.exception.FulfillmentPriceException;
import com.ultracommerce.core.order.domain.FulfillmentGroup;
import com.ultracommerce.core.order.domain.FulfillmentOption;
import com.ultracommerce.core.order.fulfillment.domain.FixedPriceFulfillmentOption;

import java.util.HashMap;
import java.util.Set;

/**
 * Processor used in conjunction with {@link FixedPriceFulfillmentOption}. Simply takes the
 * flat rate defined on the option and sets that to the total shipping price of the {@link FulfillmentGroup}
 * 
 * @author Phillip Verheyden
 * @see {@link FixedPriceFulfillmentOption}
 */
public class FixedPriceFulfillmentPricingProvider implements FulfillmentPricingProvider {

    @Override
    public boolean canCalculateCostForFulfillmentGroup(FulfillmentGroup fulfillmentGroup, FulfillmentOption option) {
        return (option instanceof FixedPriceFulfillmentOption);
    }

    @Override
    public FulfillmentGroup calculateCostForFulfillmentGroup(FulfillmentGroup fulfillmentGroup) throws FulfillmentPriceException {
        if (canCalculateCostForFulfillmentGroup(fulfillmentGroup, fulfillmentGroup.getFulfillmentOption())) {
            Money price = ((FixedPriceFulfillmentOption)fulfillmentGroup.getFulfillmentOption()).getPrice();
            fulfillmentGroup.setRetailShippingPrice(price);
            fulfillmentGroup.setSaleShippingPrice(price);
            fulfillmentGroup.setShippingPrice(price);
            return fulfillmentGroup;
        }

        throw new IllegalArgumentException("Cannot estimate shipping cost for the fulfillment option: "
                + fulfillmentGroup.getFulfillmentOption().getClass().getName());
    }

    @Override
    public FulfillmentEstimationResponse estimateCostForFulfillmentGroup(FulfillmentGroup fulfillmentGroup, Set<FulfillmentOption> options) throws FulfillmentPriceException {

        FulfillmentEstimationResponse response = new FulfillmentEstimationResponse();
        HashMap<FulfillmentOption, Money> shippingPrices = new HashMap<FulfillmentOption, Money>();
        response.setFulfillmentOptionPrices(shippingPrices);

        for (FulfillmentOption option : options) {
            if (canCalculateCostForFulfillmentGroup(fulfillmentGroup, option)) {
                Money price = ((FixedPriceFulfillmentOption) option).getPrice();
                shippingPrices.put(option, price);
            }
        }

        return response;
    }

}
