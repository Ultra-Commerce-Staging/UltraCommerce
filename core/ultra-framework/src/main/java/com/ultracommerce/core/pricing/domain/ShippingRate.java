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
package com.ultracommerce.core.pricing.domain;

import java.io.Serializable;
import java.math.BigDecimal;

@Deprecated
public interface ShippingRate extends Serializable {

    public Long  getId();
    public void setId(Long id);
    public String getFeeType();
    public void setFeeType(String feeType);
    public String getFeeSubType();
    public void setFeeSubType(String feeSubType);
    public Integer getFeeBand();
    public void setFeeBand(Integer feeBand);
    public BigDecimal getBandUnitQuantity();
    public void setBandUnitQuantity(BigDecimal bandUnitQuantity);
    public BigDecimal getBandResultQuantity();
    public void setBandResultQuantity(BigDecimal bandResultQuantity);
    public Integer getBandResultPercent();
    public void setBandResultPercent(Integer bandResultPersent);

}
