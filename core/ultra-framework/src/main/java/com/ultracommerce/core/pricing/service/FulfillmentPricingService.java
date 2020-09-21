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

import com.ultracommerce.common.vendor.service.exception.FulfillmentPriceException;
import com.ultracommerce.core.order.domain.FulfillmentGroup;
import com.ultracommerce.core.order.domain.FulfillmentOption;
import com.ultracommerce.core.pricing.service.fulfillment.provider.FulfillmentEstimationResponse;
import com.ultracommerce.core.pricing.service.fulfillment.provider.FulfillmentPricingProvider;
import com.ultracommerce.core.pricing.service.workflow.FulfillmentGroupMerchandiseTotalActivity;

import java.util.List;
import java.util.Set;

/**
 * This service can be used in a couple of different ways. First, this is used in the pricing workflow and specifically
 * {@link FulfillmentGroupMerchandiseTotalActivity} to calculate costs for {@link FulfillmentGroup}s in an {@link Order}. This can
 * also be injected in a controller to provide estimations for various {@link FulfillmentOption}s to display to the user
 * before an option is actually selected.
 * 
 * @author Phillip Verheyden
 */
public interface FulfillmentPricingService {

    /**
     * Called during the Pricing workflow to determine the cost for the {@link FulfillmentGroup}. This will loop through
     * {@link #getProcessors()} and call {@link FulfillmentPricingProvider#calculateCostForFulfillmentGroup(FulfillmentGroup)}
     * on the first processor that returns true from {@link FulfillmentPricingProvider#canCalculateCostForFulfillmentGroup(FulfillmentGroup)}
     * 
     * @param fulfillmentGroup
     * @return the updated </b>fulfillmentGroup</b> with its shippingPrice set
     * @throws FulfillmentPriceException if <b>fulfillmentGroup</b> does not have a FulfillmentOption associated to it or
     * if there was no processor found to calculate costs for <b>fulfillmentGroup</b>
     * @see {@link FulfillmentPricingProvider}
     */
    public FulfillmentGroup calculateCostForFulfillmentGroup(FulfillmentGroup fulfillmentGroup) throws FulfillmentPriceException;

    /**
     * This provides an estimation for a {@link FulfillmentGroup} with a {@link FulfillmentOption}. The main use case for this method
     * is in a view cart controller that wants to provide estimations for different {@link FulfillmentOption}s before the user
     * actually selects one. This uses {@link #getProviders()} to allow third-party integrations to respond to
     * estimations, and returns the first processor that returns true from {@link FulfillmentPricingProvider#canCalculateCostForFulfillmentGroup(FulfillmentGroup, FulfillmentOption)}.
     * 
     * @param fulfillmentGroup
     * @param options
     * @return the price estimation for a particular {@link FulfillmentGroup} with a candidate {@link FulfillmentOption}
     * @throws FulfillmentPriceException if no processor was found to estimate costs for <b>fulfillmentGroup</b> with the given <b>option</b>
     * @see {@link FulfillmentPricingProvider}
     */
    public FulfillmentEstimationResponse estimateCostForFulfillmentGroup(FulfillmentGroup fulfillmentGroup, Set<FulfillmentOption> options) throws FulfillmentPriceException;
    
    public List<FulfillmentPricingProvider> getProviders();

}
