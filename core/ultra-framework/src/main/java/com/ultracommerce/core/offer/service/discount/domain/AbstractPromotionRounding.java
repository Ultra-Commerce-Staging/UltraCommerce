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
package com.ultracommerce.core.offer.service.discount.domain;

import java.math.RoundingMode;


public abstract class AbstractPromotionRounding implements PromotionRounding {

    protected boolean roundOfferValues = true;
    protected int roundingScale = 2;
    protected RoundingMode roundingMode = RoundingMode.HALF_EVEN;

    /**
     * It is sometimes problematic to offer percentage-off offers with regards to rounding. For example,
     * consider an item that costs 9.99 and has a 50% promotion. To be precise, the offer value is 4.995,
     * but this may be a strange value to display to the user depending on the currency being used.
     */
    public boolean isRoundOfferValues() {
        return roundOfferValues;
    }

    /**
     * @see #isRoundOfferValues()
     * 
     * @param roundingScale
     */
    public void setRoundingScale(int roundingScale) {
        this.roundingScale = roundingScale;
    }

    /**
     * @see #isRoundOfferValues()
     * 
     * @param roundingMode
     */
    public void setRoundingMode(RoundingMode roundingMode) {
        this.roundingMode = roundingMode;
    }

    @Override
    public RoundingMode getRoundingMode() {
        return roundingMode;
    }

    @Override
    public int getRoundingScale() {
        return roundingScale;
    }

}
