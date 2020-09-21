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

import com.ultracommerce.common.vendor.service.exception.FulfillmentPriceException;
import com.ultracommerce.core.order.domain.FulfillmentGroup;
import com.ultracommerce.core.order.domain.FulfillmentOption;
import com.ultracommerce.core.pricing.service.FulfillmentPricingService;
import com.ultracommerce.core.pricing.service.workflow.FulfillmentGroupPricingActivity;

import java.util.Set;

/**
 * Main extension interface to allow third-party integrations to respond to fulfillment pricing
 * 
 * @author Phillip Verheyden
 * @see {@link FulfillmentPricingService}, {@link FulfillmentGroupPricingActivity}
 */
public interface FulfillmentPricingProvider {

    /**
     * Calculates the total cost for this FulfillmentGroup. Specific configurations for calculating
     * this cost can come from {@link FulfillmentGroup#getFulfillmentOption()}. This method is invoked
     * during the pricing workflow and will only be called if {@link #canCalculateCostForFulfillmentGroup(FulfillmentGroup, FulfillmentOption)}
     * returns true. This should call {@link FulfillmentGroup#setShippingPrice(com.ultracommerce.common.money.Money)} to
     * set the shipping price on <b>fulfillmentGroup</b>
     * 
     * @param fulfillmentGroup - the {@link FulfillmentGroup} to calculate costs for
     * @return the modified {@link FulfillmentGroup} with correct pricing. This is typically <b>fulfillmentGroup</b> after it
     * has been modified
     */
    public FulfillmentGroup calculateCostForFulfillmentGroup(FulfillmentGroup fulfillmentGroup) throws FulfillmentPriceException;

    /**
     * Whether or not this processor can provide a cost calculate for the given FulfillmentGroup and the given
     * FulfillmentOption. This is not invoked directly by any workflow, but could instead be invoked via a controller
     * that wants to display pricing to a user before the user actually picks a FulfillmentOption. The controller would
     * inject an instance of FulfillmentPricingService  and thus indirectly invoke this method for a particular option.
     *
     * @param fulfillmentGroup
     * @param option - the candidate option a user might select based on the estimate
     * @return <b>true</b> if this processor can estimate the costs, <b>false</b> otherwise
     * @see {@link FulfillmentPricingService}, {@link FulfillmentOption}
     */
    public boolean canCalculateCostForFulfillmentGroup(FulfillmentGroup fulfillmentGroup, FulfillmentOption option);

    /**
     * Estimates the cost for the fulfilling the given fulfillment group
     * Estimates the cost for the fulfilling the given fulfillment group with the given options. The response should not include prices that the implementor of this interface
     * cannot respond to.  So, if the invoker of this method passes in several types of fulfillment options, the response should only contain prices for the fulfillment options
     * that will would cause a call to
     * {@link #canCalculateCostForFulfillmentGroup(com.ultracommerce.core.order.domain.FulfillmentGroup, com.ultracommerce.core.order.domain.FulfillmentOption)}
     * to return true.  This method may return null or it may return a non-null response with an empty map, indicating that no price estimate was available for the options given. This
     * method SHOULD NOT throw an exception if it encounters a FulfillmentOption that it can not price. It should simply ignore that option.
     * 
     * @param fulfillmentGroup - the group to estimate fulfillment costs for
     * @param options - the candidate options that a user might select
     * @return a DTO that represents pricing information that might be added to the fulfillment cost of <b>fulfillmentGroup</b> when
     * {@link #calculateCostForFulfillmentGroup(FulfillmentGroup)} is invoked during the pricing workflow
     * @see {@link FulfillmentPricingService}, {@link FulfillmentOption}
     */
    public FulfillmentEstimationResponse estimateCostForFulfillmentGroup(FulfillmentGroup fulfillmentGroup, Set<FulfillmentOption> options) throws FulfillmentPriceException;
    
}
