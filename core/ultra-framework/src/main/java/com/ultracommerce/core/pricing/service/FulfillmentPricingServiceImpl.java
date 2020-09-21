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
package com.ultracommerce.core.pricing.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.money.Money;
import com.ultracommerce.common.vendor.service.exception.FulfillmentPriceException;
import com.ultracommerce.core.order.domain.FulfillmentGroup;
import com.ultracommerce.core.order.domain.FulfillmentOption;
import com.ultracommerce.core.order.service.FulfillmentGroupService;
import com.ultracommerce.core.pricing.service.fulfillment.provider.FulfillmentEstimationResponse;
import com.ultracommerce.core.pricing.service.fulfillment.provider.FulfillmentPricingProvider;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service("ucFulfillmentPricingService")
public class FulfillmentPricingServiceImpl implements FulfillmentPricingService {
    
    protected static final Log LOG  = LogFactory.getLog(FulfillmentPricingServiceImpl.class);

    @Resource(name = "ucFulfillmentPricingProviders")
    protected List<FulfillmentPricingProvider> providers;
    
    @Resource(name = "ucFulfillmentGroupService")
    protected FulfillmentGroupService fulfillmentGroupService;

    @Override
    public FulfillmentGroup calculateCostForFulfillmentGroup(FulfillmentGroup fulfillmentGroup) throws FulfillmentPriceException {

        if (fulfillmentGroup.getFulfillmentOption() == null) {
            //There is no shipping option yet. We'll simply set the shipping price to zero for now, and continue.
            fulfillmentGroup.setRetailFulfillmentPrice(Money.ZERO);
            fulfillmentGroup.setFulfillmentPrice(Money.ZERO);
            fulfillmentGroup.setSaleFulfillmentPrice(Money.ZERO);
            return fulfillmentGroup;
        }
        
        for (FulfillmentPricingProvider provider : providers) {
            if (provider.canCalculateCostForFulfillmentGroup(fulfillmentGroup, fulfillmentGroup.getFulfillmentOption())) {
                return provider.calculateCostForFulfillmentGroup(fulfillmentGroup);
            }
        }

        throw new FulfillmentPriceException("No valid processor was found to calculate the FulfillmentGroup cost with " +
                "FulfillmentOption id: " + fulfillmentGroup.getFulfillmentOption().getId() + 
                        " and name: " + fulfillmentGroup.getFulfillmentOption().getName());
    }
    
    @Override
    public FulfillmentEstimationResponse estimateCostForFulfillmentGroup(FulfillmentGroup fulfillmentGroup, Set<FulfillmentOption> options) throws FulfillmentPriceException {
        FulfillmentEstimationResponse response = new FulfillmentEstimationResponse();
        HashMap<FulfillmentOption, Money> prices = new HashMap<FulfillmentOption, Money>();
        response.setFulfillmentOptionPrices(prices);
        for (FulfillmentPricingProvider provider : providers) {
            //Leave it up to the providers to determine if they can respond to a pricing estimate.  If they can't, or if one or more of the options that are passed in can't be responded
            //to, then the response from the pricing provider should not include the options that it could not respond to.
            try {
                FulfillmentEstimationResponse processorResponse = provider.estimateCostForFulfillmentGroup(fulfillmentGroup, options);
                if (processorResponse != null
                        && processorResponse.getFulfillmentOptionPrices() != null
                        && processorResponse.getFulfillmentOptionPrices().size() > 0) {
                    prices.putAll(processorResponse.getFulfillmentOptionPrices());
                }
            } catch (FulfillmentPriceException e) {
                //Shouldn't completely fail the rest of the estimation on a pricing exception. Another provider might still
                //be able to respond
                String errorMessage = "FulfillmentPriceException thrown when trying to estimate fulfillment costs from ";
                errorMessage += provider.getClass().getName();
                errorMessage += ". Underlying message was: " + e.getMessage();
                LOG.error(errorMessage);
            }
        }

        return response;
    }

    @Override
    public List<FulfillmentPricingProvider> getProviders() {
        return providers;
    }

    public void setProviders(List<FulfillmentPricingProvider> providers) {
        this.providers = providers;
    }

}
