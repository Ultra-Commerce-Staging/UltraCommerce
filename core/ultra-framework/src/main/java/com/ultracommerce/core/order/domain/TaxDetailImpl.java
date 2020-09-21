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

import com.ultracommerce.common.config.domain.AbstractModuleConfiguration;
import com.ultracommerce.common.config.domain.ModuleConfiguration;
import com.ultracommerce.common.copy.CreateResponse;
import com.ultracommerce.common.copy.MultiTenantCopyContext;
import com.ultracommerce.common.currency.domain.UltraCurrency;
import com.ultracommerce.common.currency.domain.UltraCurrencyImpl;
import com.ultracommerce.common.currency.util.UltraCurrencyUtils;
import com.ultracommerce.common.money.Money;
import com.ultracommerce.common.presentation.AdminPresentation;
import com.ultracommerce.common.presentation.AdminPresentationClass;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="UC_TAX_DETAIL")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="ucOrderElements")
@AdminPresentationClass(friendlyName = "TaxDetailImpl_baseTaxDetail")
public class TaxDetailImpl implements TaxDetail {
    
    private static final long serialVersionUID = -4036994446393527252L;

    @Id
    @GeneratedValue(generator = "TaxDetailId")
    @GenericGenerator(
        name="TaxDetailId",
        strategy="com.ultracommerce.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="TaxDetailImpl"),
            @Parameter(name="increment_size", value="150"),
            @Parameter(name="entity_name", value="com.ultracommerce.core.catalog.domain.TaxDetailImpl")
        }
    )
    @Column(name = "TAX_DETAIL_ID")
    protected Long id;
    
    @Column(name = "TYPE")
    @AdminPresentation(friendlyName = "TaxDetailImpl_Tax_Type", order=1, group = "TaxDetailImpl_Tax_Detail")
    protected String type;
    
    @Column(name = "AMOUNT", precision=19, scale=5)
    @AdminPresentation(friendlyName = "TaxDetailImpl_Tax_Amount", order=2, group = "TaxDetailImpl_Tax_Detail")
    protected BigDecimal amount;
    
    @Column(name = "RATE", precision=19, scale=5)
    @AdminPresentation(friendlyName = "TaxDetailImpl_Tax_Rate", order = 3, group = "TaxDetailImpl_Tax_Detail")
    protected BigDecimal rate;
    
    @Column(name = "JURISDICTION_NAME")
    @AdminPresentation(friendlyName = "TaxDetailImpl_Tax_Jurisdiction_Name", order = 4, group = "TaxDetailImpl_Tax_Detail")
    protected String jurisdictionName;

    @Column(name = "TAX_COUNTRY")
    @AdminPresentation(friendlyName = "TaxDetailImpl_Tax_Country", order = 5, group = "TaxDetailImpl_Tax_Detail")
    protected String country;
    
    @Column(name = "TAX_REGION")
    @AdminPresentation(friendlyName = "TaxDetailImpl_Tax_Region", order = 6, group = "TaxDetailImpl_Tax_Detail")
    protected String region;

    @Column(name = "TAX_NAME")
    @AdminPresentation(friendlyName = "TaxDetailImpl_Tax_Name", order = 7, group = "TaxDetailImpl_Tax_Detail")
    protected String taxName;

    @ManyToOne(targetEntity = UltraCurrencyImpl.class)
    @JoinColumn(name = "CURRENCY_CODE")
    @AdminPresentation(friendlyName = "TaxDetailImpl_Currency_Code", order = 1, group = "FixedPriceFulfillmentOptionImpl_Details", prominent = true)
    protected UltraCurrency currency;

    @ManyToOne(targetEntity = AbstractModuleConfiguration.class)
    @JoinColumn(name = "MODULE_CONFIG_ID")
    protected ModuleConfiguration moduleConfiguation;

    public TaxDetailImpl() {
        
    }
    
    public TaxDetailImpl(TaxType type, Money amount, BigDecimal rate) {
        this.type = type.getType();
        this.amount = amount.getAmount();
        this.rate = rate;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public TaxType getType() {
        return TaxType.getInstance(this.type);
    }

    @Override
    public void setType(TaxType type) {
        this.type = type.getType();
    }

    @Override
    public Money getAmount() {
        return UltraCurrencyUtils.getMoney(amount, currency);
    }

    @Override
    public void setAmount(Money amount) {
        this.amount = amount.getAmount();
    }

    @Override
    public BigDecimal getRate() {
        return rate;
    }

    @Override
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    @Override
    public UltraCurrency getCurrency() {
        return currency;
    }

    @Override
    public void setCurrency(UltraCurrency currency) {
        this.currency = currency;
    }

    @Override
    public ModuleConfiguration getModuleConfiguration() {
        return this.moduleConfiguation;
    }

    @Override
    public void setModuleConfiguration(ModuleConfiguration config) {
        this.moduleConfiguation = config;
    }

    @Override
    public void setJurisdictionName(String jurisdiction) {
        this.jurisdictionName = jurisdiction;
    }

    @Override
    public String getJurisdictionName() {
        return this.jurisdictionName;
    }

    @Override
    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    @Override
    public String getTaxName() {
        return this.taxName;
    }

    @Override
    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public String getRegion() {
        return this.region;
    }

    @Override
    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String getCountry() {
        return this.country;
    }
    
    @Override
    public CreateResponse<TaxDetail> createOrRetrieveCopyInstance(MultiTenantCopyContext context) throws CloneNotSupportedException {
        CreateResponse<TaxDetail> createResponse = context.createOrRetrieveCopyInstance(this);
        if (createResponse.isAlreadyPopulated()) {
            return createResponse;
        }
        TaxDetail cloned = createResponse.getClone();
        cloned.setAmount(new Money(amount));
        cloned.setCountry(country);
        cloned.setCurrency(currency);
        cloned.setJurisdictionName(jurisdictionName);
        cloned.setModuleConfiguration(moduleConfiguation);
        cloned.setRate(rate);
        cloned.setRegion(region);
        cloned.setRegion(region);
        cloned.setTaxName(taxName);
        cloned.setType(getType());
        return  createResponse;
    }
}
