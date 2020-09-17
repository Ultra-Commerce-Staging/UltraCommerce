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
import com.ultracommerce.core.order.domain.FulfillmentOption;
import com.ultracommerce.core.pricing.service.FulfillmentPricingService;

import java.util.Map;

/**
 * DTO to allow FulfillmentProcessors to respond to estimation requests for a particular FulfillmentGroup
 * for a particular FulfillmentOptions
 * 
 * @author Phillip Verheyden
 * @see {@link FulfillmentPricingProvider}, {@link FulfillmentPricingService}
 */
public class FulfillmentEstimationResponse {

    protected Map<? extends FulfillmentOption, Money> fulfillmentOptionPrices;

    public Map<? extends FulfillmentOption, Money> getFulfillmentOptionPrices() {
        return fulfillmentOptionPrices;
    }

    public void setFulfillmentOptionPrices(Map<? extends FulfillmentOption, Money> fulfillmentOptionPrices) {
        this.fulfillmentOptionPrices = fulfillmentOptionPrices;
    }
}
