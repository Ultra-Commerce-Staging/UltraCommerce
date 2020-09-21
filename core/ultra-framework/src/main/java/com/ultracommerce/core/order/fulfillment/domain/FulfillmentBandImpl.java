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

import com.ultracommerce.common.presentation.AdminPresentation;
import com.ultracommerce.common.presentation.client.SupportedFieldType;
import com.ultracommerce.core.order.service.type.FulfillmentBandResultAmountType;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.math.BigDecimal;

/**
 * 
 * @author Phillip Verheyden
 * @see {@link FulfillmentPriceBandImpl}, {@link FulfillmentWeightBandImpl}
 */
@MappedSuperclass
public abstract class FulfillmentBandImpl implements FulfillmentBand {

    private static final long serialVersionUID = 1L;

    @Column(name="RESULT_AMOUNT", precision=19, scale=5, nullable = false)
    protected BigDecimal resultAmount;
    
    @Column(name="RESULT_AMOUNT_TYPE", nullable = false)
    @AdminPresentation(friendlyName="Result Type", fieldType=SupportedFieldType.ULTRA_ENUMERATION, ultraEnumeration="com.ultracommerce.core.order.service.type.FulfillmentBandResultAmountType")
    protected String resultAmountType = FulfillmentBandResultAmountType.RATE.getType();

    @Override
    public BigDecimal getResultAmount() {
        return resultAmount;
    }

    @Override
    public void setResultAmount(BigDecimal resultAmount) {
        this.resultAmount = resultAmount;
    }

    @Override
    public FulfillmentBandResultAmountType getResultAmountType() {
        return FulfillmentBandResultAmountType.getInstance(resultAmountType);
    }

    @Override
    public void setResultAmountType(FulfillmentBandResultAmountType resultAmountType) {
        this.resultAmountType = resultAmountType.getType();
    }

}
