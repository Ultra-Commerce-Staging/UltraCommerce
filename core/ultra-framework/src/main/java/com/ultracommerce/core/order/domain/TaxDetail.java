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
package com.ultracommerce.core.order.domain;

import com.ultracommerce.common.config.domain.ModuleConfiguration;
import com.ultracommerce.common.copy.MultiTenantCloneable;
import com.ultracommerce.common.currency.domain.UltraCurrency;
import com.ultracommerce.common.money.Money;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The Interface TaxDetail. A TaxDetail object stores relevant tax information 
 * including a tax type, amount, and rate.
 *
 */
public interface TaxDetail extends Serializable, MultiTenantCloneable<TaxDetail> {

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public Long getId();

    /**
     * Sets the id.
     * 
     * @param id the new id
     */
    public void setId(Long id);
    
    /**
     * Gets the tax type
     * 
     * @return the tax type
     */
    public TaxType getType();

    /**
     * Sets the tax type
     * 
     * @param type the tax type
     */
    public void setType(TaxType type);

    /**
     * Gets the tax amount
     * 
     * @return the tax amount
     */
    public Money getAmount();

    /**
     * Sets the tax amount
     * 
     * @param amount the tax amount
     */
    public void setAmount(Money amount);

    /**
     * Gets the tax rate
     * 
     * @return the rate
     */
    public BigDecimal getRate();

    /**
     * Sets the tax rate.
     * 
     * @param name the tax rate
     */
    public void setRate(BigDecimal rate);
    
    public UltraCurrency getCurrency();

    public void setCurrency(UltraCurrency currency);

    /**
     * Returns the configuration of the module that was used to calculate taxes. Allows 
     * for tracking, especially when more than one module may be used by the system.
     * @return
     */
    public ModuleConfiguration getModuleConfiguration();

    /**
     * Sets the module configuration that was used to calculate taxes.  Allows for tracking 
     * of which module was used, especially in cases where more than one module is available 
     * over time.
     * @param config
     */
    public void setModuleConfiguration(ModuleConfiguration config);

    /**
     * Optionally sets the name of the tax jurisdiction.
     * @param jurisdiction
     */
    public void setJurisdictionName(String jurisdiction);

    /**
     * Returns the name of the tax jurisdiction. May return null.
     * @return
     */
    public String getJurisdictionName();

    /**
     * Sets the name of the tax, if applicable.
     * @param taxName
     */
    public void setTaxName(String taxName);

    /**
     * Gets the name of the tax. May return null.
     * @return
     */
    public String getTaxName();

    /**
     * Sets the region, as a string. Typically this will be a 
     * State, Province, or County.
     * @param region
     */
    public void setRegion(String region);

    /**
     * Returns the name of the region used for tax calculation. May 
     * return null.
     * @return
     */
    public String getRegion();

    /**
     * Sets the country used for tax calculation.
     * @param country
     */
    public void setCountry(String country);

    /**
     * Returns the country, as a string, used for tax calculation. 
     * May return null.
     * @return
     */
    public String getCountry();

}
