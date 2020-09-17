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
package com.ultracommerce.core.offer.domain;

import com.ultracommerce.common.currency.util.UltraCurrencyUtils;
import com.ultracommerce.common.currency.util.CurrencyCodeIdentifiable;
import com.ultracommerce.common.money.Money;
import com.ultracommerce.common.persistence.DefaultPostLoaderDao;
import com.ultracommerce.common.persistence.PostLoaderDao;
import com.ultracommerce.common.presentation.AdminPresentation;
import com.ultracommerce.common.presentation.AdminPresentationClass;
import com.ultracommerce.common.presentation.AdminPresentationToOneLookup;
import com.ultracommerce.common.presentation.PopulateToOneFieldsEnum;
import com.ultracommerce.common.presentation.client.SupportedFieldType;
import com.ultracommerce.common.presentation.override.AdminPresentationMergeEntry;
import com.ultracommerce.common.presentation.override.AdminPresentationMergeOverride;
import com.ultracommerce.common.presentation.override.AdminPresentationMergeOverrides;
import com.ultracommerce.common.presentation.override.PropertyType;
import com.ultracommerce.common.util.HibernateUtils;
import com.ultracommerce.core.offer.service.type.OfferAdjustmentType;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.domain.OrderImpl;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Parameter;
import org.hibernate.proxy.HibernateProxy;

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
import javax.persistence.Transient;

@Entity
@Table(name = "UC_ORDER_ADJUSTMENT")
@Inheritance(strategy=InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "ucOrderElements")
@AdminPresentationMergeOverrides(
    {
        @AdminPresentationMergeOverride(name = "", mergeEntries =
            @AdminPresentationMergeEntry(propertyType = PropertyType.AdminPresentation.READONLY,
                                            booleanOverrideValue = true))
    }
)
@AdminPresentationClass(populateToOneFields = PopulateToOneFieldsEnum.TRUE, friendlyName = "OrderAdjustmentImpl_baseOrderAdjustment")
public class OrderAdjustmentImpl implements OrderAdjustment, CurrencyCodeIdentifiable {

    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator= "OrderAdjustmentId")
    @GenericGenerator(
        name="OrderAdjustmentId",
        strategy="com.ultracommerce.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="OrderAdjustmentImpl"),
            @Parameter(name="entity_name", value="com.ultracommerce.core.offer.domain.OrderAdjustmentImpl")
        }
    )
    @Column(name = "ORDER_ADJUSTMENT_ID")
    protected Long id;

    @ManyToOne(targetEntity = OrderImpl.class)
    @JoinColumn(name = "ORDER_ID")
    @Index(name="ORDERADJUST_ORDER_INDEX", columnNames={"ORDER_ID"})
    @AdminPresentation(excluded = true)
    protected Order order;

    @ManyToOne(targetEntity = OfferImpl.class, optional=false)
    @JoinColumn(name = "OFFER_ID")
    @Index(name="ORDERADJUST_OFFER_INDEX", columnNames={"OFFER_ID"})
    @AdminPresentation(friendlyName = "OrderAdjustmentImpl_Offer", order=1000,
            prominent = true, gridOrder = 1000)
    @AdminPresentationToOneLookup()
    protected Offer offer;

    @Column(name = "ADJUSTMENT_REASON", nullable=false)
    @AdminPresentation(friendlyName = "OrderAdjustmentImpl_Order_Adjustment_Reason", order=2000)
    protected String reason;

    @Column(name = "ADJUSTMENT_VALUE", nullable=false, precision=19, scale=5)
    @AdminPresentation(friendlyName = "OrderAdjustmentImpl_Order_Adjustment_Value", order=3000,
            fieldType = SupportedFieldType.MONEY, prominent = true,
            gridOrder = 2000)
    protected BigDecimal value = Money.ZERO.getAmount();

    @Column(name = "IS_FUTURE_CREDIT")
    @AdminPresentation(friendlyName = "OrderAdjustmentImpl_isFutureCredit", order = 4000,
            showIfProperty="admin.showIfProperty.offerAdjustmentType")
    protected Boolean isFutureCredit = false;

    @Transient
    protected Offer deproxiedOffer;

    @Override
    public void init(Order order, Offer offer, String reason){
        this.order = order;
        this.offer = offer;
        this.reason = reason;
        if (offer != null) {
            this.isFutureCredit = OfferAdjustmentType.FUTURE_CREDIT.equals(offer.getAdjustmentType());
        }
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
    public Order getOrder() {
        return order;
    }

    @Override
    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public Offer getOffer() {
        if (deproxiedOffer == null) {
            PostLoaderDao postLoaderDao = DefaultPostLoaderDao.getPostLoaderDao();

            if (postLoaderDao != null && offer.getId() != null) {
                Long id = offer.getId();
                deproxiedOffer = postLoaderDao.find(OfferImpl.class, id);
            } else if (offer instanceof HibernateProxy) {
                deproxiedOffer = HibernateUtils.deproxy(offer);
            } else {
                deproxiedOffer = offer;
            }
        }

        return deproxiedOffer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
        deproxiedOffer = null;
    }

    @Override
    public String getReason() {
        return reason;
    }

    @Override
    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public Money getValue() {
        return value == null ? null : UltraCurrencyUtils.getMoney(value, getOrder().getCurrency());
    }

    @Override
    public void setValue(Money value) {
        this.value = value.getAmount();
    }

    @Override
    public String getCurrencyCode() {
        if (order.getCurrency() != null) {
            return order.getCurrency().getCurrencyCode();
        }
        return null;
    }

    @Override
    public Boolean isFutureCredit() {
        if (isFutureCredit == null) {
            return false;
        }
        return isFutureCredit;
    }

    @Override
    public void setFutureCredit(Boolean futureCredit) {
        isFutureCredit = futureCredit;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((offer == null) ? 0 : offer.hashCode());
        result = prime * result + ((order == null) ? 0 : order.hashCode());
        result = prime * result + ((reason == null) ? 0 : reason.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        result = prime * result + ((isFutureCredit == null) ? 0 : isFutureCredit.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!getClass().isAssignableFrom(obj.getClass())) {
            return false;
        }
        OrderAdjustmentImpl other = (OrderAdjustmentImpl) obj;

        if (id != null && other.id != null) {
            return id.equals(other.id);
        }

        if (offer == null) {
            if (other.offer != null) {
                return false;
            }
        } else if (!offer.equals(other.offer)) {
            return false;
        }
        if (order == null) {
            if (other.order != null) {
                return false;
            }
        } else if (!order.equals(other.order)) {
            return false;
        }
        if (reason == null) {
            if (other.reason != null) {
                return false;
            }
        } else if (!reason.equals(other.reason)) {
            return false;
        }
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        if (isFutureCredit != other.isFutureCredit) {
            return false;
        }
        return true;
    }

}
