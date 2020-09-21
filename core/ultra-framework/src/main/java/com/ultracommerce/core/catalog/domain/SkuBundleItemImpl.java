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
package com.ultracommerce.core.catalog.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import com.ultracommerce.common.copy.CreateResponse;
import com.ultracommerce.common.copy.MultiTenantCopyContext;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransform;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransformMember;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import com.ultracommerce.common.money.Money;
import com.ultracommerce.common.persistence.DefaultPostLoaderDao;
import com.ultracommerce.common.persistence.PostLoaderDao;
import com.ultracommerce.common.presentation.AdminPresentation;
import com.ultracommerce.common.presentation.AdminPresentationToOneLookup;
import com.ultracommerce.common.presentation.RequiredOverride;
import com.ultracommerce.common.presentation.ValidationConfiguration;
import com.ultracommerce.common.presentation.client.SupportedFieldType;
import com.ultracommerce.common.presentation.client.VisibilityEnum;
import com.ultracommerce.common.util.HibernateUtils;
import com.ultracommerce.core.catalog.service.dynamic.DynamicSkuPrices;
import com.ultracommerce.core.catalog.service.dynamic.SkuPricingConsiderationContext;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @deprecated instead, use the ProductType Module's Product Add-Ons to build and configure bundles
 */
@Deprecated
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "UC_SKU_BUNDLE_ITEM")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "ucProducts")
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.SANDBOX, skipOverlaps=true),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_CATALOG)
})
public class SkuBundleItemImpl implements SkuBundleItem, SkuBundleItemAdminPresentation {

    private static final long serialVersionUID = 1L;

    /** The id. */
    @Id
    @GeneratedValue(generator = "SkuBundleItemId")
    @GenericGenerator(
        name="SkuBundleItemId",
        strategy="com.ultracommerce.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name = "segment_value", value = "SkuBundleItemImpl"),
            @Parameter(name = "entity_name", value = "com.ultracommerce.core.catalog.domain.SkuBundleItemImpl")
        }
    )
    @Column(name = "SKU_BUNDLE_ITEM_ID")
    @AdminPresentation(friendlyName = "SkuBundleItemImpl_ID", group = GroupName.General, visibility = VisibilityEnum.HIDDEN_ALL)
    protected Long id;

    @Column(name = "QUANTITY", nullable=false)
    @AdminPresentation(friendlyName = "bundleItemQuantity",
        group = GroupName.General, order = FieldOrder.QUANTITY,
        prominent = true, gridOrder = FieldOrder.QUANTITY,
        requiredOverride = RequiredOverride.REQUIRED)
    protected Integer quantity;

    @Column(name = "ITEM_SALE_PRICE", precision=19, scale=5)
    @AdminPresentation(friendlyName = "bundleItemSalePrice",
        group = GroupName.General, order = FieldOrder.ITEM_SALE_PRICE,
        prominent = true, gridOrder = FieldOrder.ITEM_SALE_PRICE,
        tooltip="bundleItemSalePriceTooltip", 
        fieldType = SupportedFieldType.MONEY)
    protected BigDecimal itemSalePrice;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = ProductBundleImpl.class, optional = false, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "PRODUCT_BUNDLE_ID", referencedColumnName = "PRODUCT_ID")
    protected ProductBundle bundle;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = SkuImpl.class, optional = false)
    @JoinColumn(name = "SKU_ID", referencedColumnName = "SKU_ID")
    @AdminPresentation(friendlyName = "Sku",
        group = GroupName.General, order = FieldOrder.SKU,
        prominent = true, gridOrder = FieldOrder.SKU,
        validationConfigurations = @ValidationConfiguration(validationImplementation = "ucProductBundleSkuBundleItemValidator"))
    @AdminPresentationToOneLookup()
    protected Sku sku;

    /** The display order. */
    @Column(name = "SEQUENCE", precision = 10, scale = 6)
    @AdminPresentation(visibility = VisibilityEnum.HIDDEN_ALL)
    protected BigDecimal sequence;

    @Transient
    protected DynamicSkuPrices dynamicPrices = null;

    @Transient
    protected Sku deproxiedSku = null;

    @Transient
    protected ProductBundle deproxiedBundle = null;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Integer getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
   
    public Money getDynamicSalePrice(Sku sku, BigDecimal salePrice) {   
        Money returnPrice = null;
        
        if (SkuPricingConsiderationContext.hasDynamicPricing()) {
            if (dynamicPrices != null) {
                returnPrice = dynamicPrices.getSalePrice();
            } else {
                dynamicPrices = SkuPricingConsiderationContext.getDynamicSkuPrices(sku);
                if (SkuPricingConsiderationContext.isPricingConsiderationActive()) {
                    returnPrice = new Money(salePrice);
                } else {
                    returnPrice = dynamicPrices.getSalePrice();
                }
            }
        } else {
            if (salePrice != null) {
                returnPrice = new Money(salePrice,Money.defaultCurrency());
            }
        }
        
        return returnPrice;   
    }
    @Override
    public void setSalePrice(Money salePrice) {
        if (salePrice != null) {
            this.itemSalePrice = salePrice.getAmount();
        } else {
            this.itemSalePrice = null;
        }
    }


    @Override
    public Money getSalePrice() {
        if (itemSalePrice == null) {
            return getSku().getSalePrice();
        } else {
            return getDynamicSalePrice(getSku(), itemSalePrice);
        }
    }

    @Override
    public Money getRetailPrice() {
         return getSku().getRetailPrice();
     }

    @Override
    public ProductBundle getBundle() {
        // We deproxy the bundle to allow logic introduced by filters to still take place (this can be an issue since
        // the bundle is lazy loaded).
        if(deproxiedBundle == null) {
            PostLoaderDao postLoaderDao = DefaultPostLoaderDao.getPostLoaderDao();
            Long id = bundle.getId();
            if (postLoaderDao != null && id != null) {
                deproxiedBundle = postLoaderDao.findSandboxEntity(ProductBundleImpl.class, id);
            } else if (bundle instanceof HibernateProxy) {
                deproxiedBundle = HibernateUtils.deproxy(bundle);
            } else {
                deproxiedBundle = bundle;
            }
        }
        if (deproxiedBundle instanceof HibernateProxy) {
            deproxiedBundle = HibernateUtils.deproxy(bundle);
        }
        return deproxiedBundle;
    }

    @Override
    public void setBundle(ProductBundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public Sku getSku() {
        // We deproxy the sku to allow logic introduced by filters to still take place (this can be an issue since
        // the sku is lazy loaded).
        if (deproxiedSku == null) {
            PostLoaderDao postLoaderDao = DefaultPostLoaderDao.getPostLoaderDao();
            Long id = sku.getId();
            if (postLoaderDao != null && id != null) {
                deproxiedSku = postLoaderDao.findSandboxEntity(SkuImpl.class, id);
            } else if (sku instanceof HibernateProxy) {
                deproxiedSku = HibernateUtils.deproxy(sku);
            } else {
                deproxiedSku = sku;
            }
        }
        if (deproxiedSku instanceof HibernateProxy) {
            deproxiedSku = HibernateUtils.deproxy(sku);
        }
        return deproxiedSku;
    }

    @Override
    public void setSku(Sku sku) {
        this.sku = sku;
    }

    @Override
    public BigDecimal getSequence() {
        return sequence;
    }

    @Override
    public void setSequence(BigDecimal sequence) {
        this.sequence = sequence;
    }

    @Override
    public void clearDynamicPrices() {
        dynamicPrices = null;
        getSku().clearDynamicPrices();
    }

    @Override
    public <G extends SkuBundleItem> CreateResponse<G> createOrRetrieveCopyInstance(MultiTenantCopyContext context) throws CloneNotSupportedException {
        CreateResponse<G> createResponse = context.createOrRetrieveCopyInstance(this);
        if (createResponse.isAlreadyPopulated()) {
            return createResponse;
        }
        SkuBundleItem cloned = createResponse.getClone();
        cloned.setQuantity(quantity);
        cloned.setSalePrice(getSalePrice());
        cloned.setSequence(sequence);
        if (sku != null) {
            cloned.setSku(sku.createOrRetrieveCopyInstance(context).getClone());
        }
        if (bundle != null) {
            cloned.setBundle((ProductBundle) bundle.createOrRetrieveCopyInstance(context).getClone());
        }
        return createResponse;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        }
        SkuBundleItemImpl rhs = (SkuBundleItemImpl) obj;
        return new EqualsBuilder()
                .append(this.id, rhs.id)
                .append(this.quantity, rhs.quantity)
                .append(this.itemSalePrice, rhs.itemSalePrice)
                .append(this.bundle, rhs.bundle)
                .append(this.sku, rhs.sku)
                .append(this.sequence, rhs.sequence)
                .append(this.dynamicPrices, rhs.dynamicPrices)
                .append(this.deproxiedSku, rhs.deproxiedSku)
                .append(this.deproxiedBundle, rhs.deproxiedBundle)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(quantity)
                .append(itemSalePrice)
                .append(bundle)
                .append(sku)
                .append(sequence)
                .append(dynamicPrices)
                .append(deproxiedSku)
                .append(deproxiedBundle)
                .toHashCode();
    }
}
