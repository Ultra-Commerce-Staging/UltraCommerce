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

import com.ultracommerce.common.util.WeightUnitOfMeasureType;
import com.ultracommerce.core.catalog.domain.Sku;
import com.ultracommerce.core.order.domain.FulfillmentGroup;
import com.ultracommerce.core.order.domain.FulfillmentGroupItem;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.domain.OrderItem;
import com.ultracommerce.core.order.service.type.FulfillmentBandResultAmountType;

import java.math.BigDecimal;

/**
 * <p>This entity defines the bands that can be specified for {@link BandedWeightFulfillmentOption}. Bands
 * work on the cumulated weight of an {@link Order} and should be calculated as follows:</p>
 * <ol>
 *  <li>The weight of all of the {@link OrderItem}s (via the relationship to {@link Sku}) in a {@link FulfillmentGroup} (which
 *  is obtained through their relationship with {@link FulfillmentGroupItem} are summed together</li>
 *  <li>The {@link FulfillmentWeightBand} should be looked up by getting the closest band less
 *  than the sum of the weights</li>
 *  <li>If {@link #getResultAmountType()} returns {@link FulfillmentBandResultAmountType#RATE}, then
 *  the cost for the fulfillment group is whatever is defined in {@link #getResultAmount()}</li>
 *  <li>If {@link #getResultAmountType()} returns {@link FulfillmentBandResultAmountType#PERCENTAGE}, then
 *  the fulfillment cost is the percentage obtained by {@link #getResultAmount()} * retailPriceTotal</li>
 *  <li>If two bands have the same weight, the cheapest resulting amount is used</li>
 * </ol>
 * <p>Note: this implementation assumes that units of measurement (lb, kg, etc) are the same across the site implementation</p>
 *
 * @author Phillip Verheyden
 * 
 */
public interface FulfillmentWeightBand extends FulfillmentBand {

    public BigDecimal getMinimumWeight();
    
    public void setMinimumWeight(BigDecimal weight);
    
    public BandedWeightFulfillmentOption getOption();

    public void setOption(BandedWeightFulfillmentOption option);
    
    public WeightUnitOfMeasureType getWeightUnitOfMeasure();
    
    public void setWeightUnitOfMeasure(WeightUnitOfMeasureType weightUnitOfMeasure);

}
