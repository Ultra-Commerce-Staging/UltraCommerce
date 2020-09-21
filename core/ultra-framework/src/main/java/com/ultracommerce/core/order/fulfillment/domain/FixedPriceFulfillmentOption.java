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
package com.ultracommerce.core.order.fulfillment.domain;

import com.ultracommerce.common.currency.domain.UltraCurrency;
import com.ultracommerce.common.money.Money;
import com.ultracommerce.core.order.domain.FulfillmentOption;
import com.ultracommerce.core.pricing.service.fulfillment.provider.FixedPriceFulfillmentPricingProvider;

import java.io.Serializable;

/**
 * Used in conjunction with the {@link FixedPriceFulfillmentPricingProvider} to allow for a single price
 * for fulfilling an order (e.g. $5 shipping)
 * 
 * @author Phillip Verheyden
 * @see {@link FixedPriceFulfillmentPricingProvider}
 */
public interface FixedPriceFulfillmentOption extends FulfillmentOption, Serializable {
    
    public Money getPrice();
    
    public void setPrice(Money price);
    public UltraCurrency getCurrency();

    public void setCurrency(UltraCurrency currency);
    
}
