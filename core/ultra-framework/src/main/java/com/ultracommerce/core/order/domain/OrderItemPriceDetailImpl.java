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

import com.ultracommerce.common.copy.CreateResponse;
import com.ultracommerce.common.copy.MultiTenantCopyContext;
import com.ultracommerce.common.currency.domain.UltraCurrency;
import com.ultracommerce.common.currency.util.UltraCurrencyUtils;
import com.ultracommerce.common.currency.util.CurrencyCodeIdentifiable;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransform;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransformMember;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import com.ultracommerce.common.money.Money;
import com.ultracommerce.common.presentation.AdminPresentation;
import com.ultracommerce.common.presentation.AdminPresentationCollection;
import com.ultracommerce.common.presentation.client.AddMethodType;
import com.ultracommerce.common.presentation.client.VisibilityEnum;
import com.ultracommerce.common.presentation.override.AdminPresentationMergeEntry;
import com.ultracommerce.common.presentation.override.AdminPresentationMergeOverride;
import com.ultracommerce.common.presentation.override.AdminPresentationMergeOverrides;
import com.ultracommerce.common.presentation.override.PropertyType;
import com.ultracommerce.core.offer.domain.OrderItemPriceDetailAdjustment;
import com.ultracommerce.core.offer.domain.OrderItemPriceDetailAdjustmentImpl;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "UC_ORDER_ITEM_PRICE_DTL")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "ucOrderElements")
@AdminPresentationMergeOverrides(
    {
        @AdminPresentationMergeOverride(name = "", mergeEntries =
            @AdminPresentationMergeEntry(propertyType = PropertyType.AdminPresentation.READONLY,
                                            booleanOverrideValue = true))
    }
)
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_SITE)
})
public class OrderItemPriceDetailImpl implements OrderItemPriceDetail, CurrencyCodeIdentifiable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "OrderItemPriceDetailId")
    @GenericGenerator(
        name="OrderItemPriceDetailId",
        strategy="com.ultracommerce.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="OrderItemPriceDetailImpl"),
            @Parameter(name="entity_name", value="com.ultracommerce.core.order.domain.OrderItemPriceDetailImpl")
        }
    )
    @Column(name = "ORDER_ITEM_PRICE_DTL_ID")
    @AdminPresentation(friendlyName = "OrderItemPriceDetailImpl_Id", group = "OrderItemPriceDetailImpl_Primary_Key", visibility = VisibilityEnum.HIDDEN_ALL)
    protected Long id;

    @ManyToOne(targetEntity = OrderItemImpl.class)
    @JoinColumn(name = "ORDER_ITEM_ID")
    @AdminPresentation(excluded = true)
    protected OrderItem orderItem;

    @OneToMany(mappedBy = "orderItemPriceDetail", targetEntity = OrderItemPriceDetailAdjustmentImpl.class, cascade = { CascadeType.ALL }, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "ucOrderElements")
    @AdminPresentationCollection(addType = AddMethodType.PERSIST, friendlyName = "OrderItemPriceDetailImpl_orderItemPriceDetailAdjustments")
    protected List<OrderItemPriceDetailAdjustment> orderItemPriceDetailAdjustments = new ArrayList<OrderItemPriceDetailAdjustment>();

    @Column(name = "QUANTITY", nullable=false)
    @AdminPresentation(friendlyName = "OrderItemPriceDetailImpl_quantity", order = 5, group = "OrderItemPriceDetailImpl_Pricing", prominent = true)
    protected int quantity;

    @Column(name = "USE_SALE_PRICE")
    @AdminPresentation(friendlyName = "OrderItemPriceDetailImpl_useSalePrice", order = 5, group = "OrderItemPriceDetailImpl_Pricing", prominent = true)
    protected Boolean useSalePrice = true;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public OrderItem getOrderItem() {
        return orderItem;
    }

    @Override
    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    @Override
    public List<OrderItemPriceDetailAdjustment> getOrderItemPriceDetailAdjustments() {
        return orderItemPriceDetailAdjustments;
    }

    @Override
    public List<OrderItemPriceDetailAdjustment> getFutureCreditOrderItemPriceDetailAdjustments() {
        List<OrderItemPriceDetailAdjustment> orderDiscountAdjustments = new ArrayList<>();
        for (OrderItemPriceDetailAdjustment adjustment : orderItemPriceDetailAdjustments) {
            if (adjustment.isFutureCredit()) {
                orderDiscountAdjustments.add(adjustment);
            }
        }
        return orderDiscountAdjustments;
    }

    @Override
    public void setOrderItemAdjustments(List<OrderItemPriceDetailAdjustment> orderItemPriceDetailAdjustments) {
        this.orderItemPriceDetailAdjustments = orderItemPriceDetailAdjustments;
        
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    protected UltraCurrency getCurrency() {
        return getOrderItem().getOrder().getCurrency();
    }

    @Override
    public Money getAdjustmentValue() {
        Money adjustmentValue = UltraCurrencyUtils.getMoney(BigDecimal.ZERO, getCurrency());
        for (OrderItemPriceDetailAdjustment adjustment : orderItemPriceDetailAdjustments) {
            if (!adjustment.isFutureCredit()) {
                adjustmentValue = adjustmentValue.add(adjustment.getValue());
            }
        }
        return adjustmentValue;
    }

    @Override
    public Money getFutureCreditAdjustmentValue() {
        Money adjustmentValue = UltraCurrencyUtils.getMoney(BigDecimal.ZERO, getCurrency());
        for (OrderItemPriceDetailAdjustment adjustment : orderItemPriceDetailAdjustments) {
            if (adjustment.isFutureCredit()) {
                adjustmentValue = adjustmentValue.add(adjustment.getValue());
            }
        }
        return adjustmentValue;
    }

    @Override
    public Money getTotalAdjustmentValue() {
        return getAdjustmentValue().multiply(quantity);
    }

    @Override
    public Money getFutureCreditTotalAdjustmentValue() {
        return getFutureCreditAdjustmentValue().multiply(quantity);
    }

    @Override
    public Money getTotalAdjustedPrice() {
        Money basePrice = orderItem.getPriceBeforeAdjustments(getUseSalePrice());
        return basePrice.multiply(quantity).subtract(getTotalAdjustmentValue());
    }

    @Override
    public boolean getUseSalePrice() {
        if (useSalePrice == null) {
            return false;
        } else {
            return useSalePrice;
        }
    }

    @Override
    public void setUseSalePrice(boolean useSalePrice) {
        this.useSalePrice = Boolean.valueOf(useSalePrice);
    }

    @Override
    public String getCurrencyCode() {
        if (getCurrency() != null) {
            return getCurrency().getCurrencyCode();
        }
        return null;
    }

    @Override
    public <G extends OrderItemPriceDetail> CreateResponse<G> createOrRetrieveCopyInstance(MultiTenantCopyContext context) throws CloneNotSupportedException {
        CreateResponse<G> createResponse = context.createOrRetrieveCopyInstance(this);
        if (createResponse.isAlreadyPopulated()) {
            return createResponse;
        }
        OrderItemPriceDetail cloned = createResponse.getClone();
        cloned.setQuantity(quantity);
        cloned.setUseSalePrice(useSalePrice);
        // dont clone
        cloned.setOrderItem(orderItem);
        for(OrderItemPriceDetailAdjustment entry : orderItemPriceDetailAdjustments){
            OrderItemPriceDetailAdjustment clonedEntry = entry.createOrRetrieveCopyInstance(context).getClone();
            clonedEntry.setOrderItemPriceDetail(cloned);
            cloned.getOrderItemPriceDetailAdjustments().add(clonedEntry);
        }
        return  createResponse;
    }
}
