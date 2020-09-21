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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import com.ultracommerce.common.copy.CreateResponse;
import com.ultracommerce.common.copy.MultiTenantCopyContext;
import com.ultracommerce.common.currency.domain.UltraCurrency;
import com.ultracommerce.common.currency.domain.UltraCurrencyImpl;
import com.ultracommerce.common.currency.util.UltraCurrencyUtils;
import com.ultracommerce.common.money.Money;
import com.ultracommerce.common.presentation.AdminPresentation;
import com.ultracommerce.common.presentation.AdminPresentationClass;
import com.ultracommerce.core.order.domain.FulfillmentOptionImpl;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 * @author Phillip Verheyden
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "UC_FULFILLMENT_OPTION_FIXED")
@AdminPresentationClass(friendlyName = "Fixed Price Fulfillment")
public class FixedPriceFulfillmentOptionImpl extends FulfillmentOptionImpl implements FixedPriceFulfillmentOption {

    private static final long serialVersionUID = 2L;

    @Column(name = "PRICE", precision=19, scale=5, nullable=false)
    @AdminPresentation(friendlyName = "FixedPriceFulfillmentOptionImpl_price", order = Presentation.FieldOrder.DESCRIPTION + 1000)
    protected BigDecimal price;
    
    @ManyToOne(targetEntity = UltraCurrencyImpl.class)
    @JoinColumn(name = "CURRENCY_CODE")
    @AdminPresentation(excluded = true)
    protected UltraCurrency currency;

    @Override
    public Money getPrice() {
        return price == null ? null : UltraCurrencyUtils.getMoney(price, getCurrency());
    }

    @Override
    public void setPrice(Money price) {
        this.price = Money.toAmount(price);
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
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || !getClass().isAssignableFrom(o.getClass())) {
            return false;
        }

        final FixedPriceFulfillmentOptionImpl that = (FixedPriceFulfillmentOptionImpl) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(getPrice(), that.getPrice())
                .append(getCurrency(), that.getCurrency())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(getPrice())
                .append(getCurrency())
                .toHashCode();
    }

    @Override
    public CreateResponse<FixedPriceFulfillmentOption> createOrRetrieveCopyInstance(MultiTenantCopyContext context)
            throws CloneNotSupportedException {
        CreateResponse<FixedPriceFulfillmentOption> createResponse = super.createOrRetrieveCopyInstance(context);
        if (createResponse.isAlreadyPopulated()) {
            return createResponse;
        }
        FixedPriceFulfillmentOption myClone = createResponse.getClone();
        myClone.setPrice(getPrice());
        myClone.setCurrency(currency);

        return createResponse;
    }
}
