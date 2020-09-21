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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import com.ultracommerce.common.copy.CreateResponse;
import com.ultracommerce.common.copy.MultiTenantCopyContext;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransform;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransformMember;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import com.ultracommerce.common.presentation.AdminPresentation;
import com.ultracommerce.common.presentation.AdminPresentationClass;
import com.ultracommerce.common.presentation.client.VisibilityEnum;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * 
 * @author jfischer
 *
 */
@Entity
@Table(name = "UC_OFFER_ITEM_CRITERIA")
@Inheritance(strategy=InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="ucOffers")
@AdminPresentationClass(friendlyName = "OfferItemCriteriaImpl_baseOfferItemCriteria")
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.SANDBOX, skipOverlaps=true),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_CATALOG)
})
public class OfferItemCriteriaImpl implements OfferItemCriteria {
    
    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator= "OfferItemCriteriaId")
    @GenericGenerator(
        name="OfferItemCriteriaId",
        strategy="com.ultracommerce.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="OfferItemCriteriaImpl"),
            @Parameter(name="entity_name", value="com.ultracommerce.core.offer.domain.OfferItemCriteriaImpl")
        }
    )
    @Column(name = "OFFER_ITEM_CRITERIA_ID")
    @AdminPresentation(friendlyName = "OfferItemCriteriaImpl_Item_Criteria_Id", group = "OfferItemCriteriaImpl_Description", visibility = VisibilityEnum.HIDDEN_ALL)
    protected Long id;
    
    @Column(name = "QUANTITY", nullable=false)
    @AdminPresentation(friendlyName = "OfferItemCriteriaImpl_Quantity", group = "OfferItemCriteriaImpl_Description", visibility =VisibilityEnum.HIDDEN_ALL)
    protected Integer quantity;
    
    @Lob
    @Type(type = "org.hibernate.type.MaterializedClobType")
    @Column(name = "ORDER_ITEM_MATCH_RULE", length = Integer.MAX_VALUE - 1)
    @AdminPresentation(friendlyName = "OfferItemCriteriaImpl_Order_Item_Match_Rule", group = "OfferItemCriteriaImpl_Description", visibility = VisibilityEnum.HIDDEN_ALL)
    protected String orderItemMatchRule;

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
    public void setQuantity(Integer receiveQuantity) {
        this.quantity = receiveQuantity;
    }

    @Override
    public String getMatchRule() {
        return orderItemMatchRule;
    }

    @Override
    public void setMatchRule(String matchRule) {
        this.orderItemMatchRule = matchRule;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(id)
            .append(orderItemMatchRule)
            .append(quantity)
            .build();
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && getClass().isAssignableFrom(o.getClass())) {
            OfferItemCriteriaImpl that = (OfferItemCriteriaImpl) o;
            return new EqualsBuilder()
                .append(this.id, that.id)
                .append(this.orderItemMatchRule, that.orderItemMatchRule)
                .append(this.quantity, that.quantity)
                .build();
        }

        return false;
    }

    @Override
    public <G extends OfferItemCriteria> CreateResponse<G> createOrRetrieveCopyInstance(MultiTenantCopyContext context) throws CloneNotSupportedException {
        CreateResponse<G> createResponse = context.createOrRetrieveCopyInstance(this);
        if (createResponse.isAlreadyPopulated()) {
            return createResponse;
        }
        OfferItemCriteria cloned = createResponse.getClone();
        cloned.setQuantity(quantity);
        cloned.setMatchRule(orderItemMatchRule);
       return createResponse;
    }
}
