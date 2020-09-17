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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import com.ultracommerce.common.copy.CreateResponse;
import com.ultracommerce.common.copy.MultiTenantCopyContext;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransform;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransformMember;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import com.ultracommerce.common.i18n.service.DynamicTranslationProvider;
import com.ultracommerce.common.presentation.AdminPresentation;
import com.ultracommerce.common.presentation.AdminPresentationClass;
import com.ultracommerce.common.presentation.client.SupportedFieldType;
import com.ultracommerce.core.order.service.type.FulfillmentType;
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

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "UC_FULFILLMENT_OPTION")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "ucOrderElements")
@AdminPresentationClass(friendlyName = "Fulfillment Option")
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.SANDBOX),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_SITE)
})
public class FulfillmentOptionImpl implements FulfillmentOption {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator= "FulfillmentOptionId")
    @GenericGenerator(
        name="FulfillmentOptionId",
        strategy="com.ultracommerce.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="FulfillmentOptionImpl"),
            @Parameter(name="entity_name", value="com.ultracommerce.core.order.domain.FulfillmentOptionImpl")
        }
    )
    @Column(name = "FULFILLMENT_OPTION_ID")
    protected Long id;
    
    @Column(name = "NAME")
    @AdminPresentation(friendlyName = "FulfillmentOptionImpl_name",
            order = Presentation.FieldOrder.NAME, prominent = true, gridOrder = 1000, translatable = true)
    protected String name;

    @Lob
    @Type(type = "org.hibernate.type.MaterializedClobType")
    @Column(name = "LONG_DESCRIPTION", length = Integer.MAX_VALUE - 1)
    @AdminPresentation(friendlyName = "FulfillmentOptionImpl_longDescription",
            order = Presentation.FieldOrder.DESCRIPTION, translatable = true)
    protected String longDescription;

    @Column(name = "USE_FLAT_RATES")
    @AdminPresentation(friendlyName = "FulfillmentOptionImpl_useFlatRates",
        order = Presentation.FieldOrder.FLATRATES)
    protected Boolean useFlatRates = true;

    @Column(name = "FULFILLMENT_TYPE", nullable = false)
    @AdminPresentation(friendlyName = "FulfillmentOptionImpl_fulfillmentType",
                       order = Presentation.FieldOrder.FULFILLMENT_TYPE,
                       fieldType = SupportedFieldType.ULTRA_ENUMERATION,
                       ultraEnumeration = "com.ultracommerce.core.order.service.type.FulfillmentType")
    protected String fulfillmentType;

    @Column(name = "TAX_CODE")
    @AdminPresentation(friendlyName = "FulfillmentOptionImpl_taxCode",
                       order = Presentation.FieldOrder.TAX_CODE)
    protected String taxCode;

    @Column(name = "TAXABLE")
    @AdminPresentation(friendlyName = "FulfillmentOptionImpl_taxable",
                       order = Presentation.FieldOrder.TAXABLE)
    protected Boolean taxable = false;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return DynamicTranslationProvider.getValue(this, "name", name);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getLongDescription() {
        return DynamicTranslationProvider.getValue(this, "longDescription", longDescription);
    }

    @Override
    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    @Override
    public Boolean getUseFlatRates() {
        return useFlatRates;
    }

    @Override
    public void setUseFlatRates(Boolean useFlatRates) {
        this.useFlatRates = useFlatRates;
    }

    @Override
    public FulfillmentType getFulfillmentType() {
        return FulfillmentType.getInstance(fulfillmentType);
    }

    @Override
    public void setFulfillmentType(FulfillmentType fulfillmentType) {
        this.fulfillmentType = (fulfillmentType == null) ? null : fulfillmentType.getType();
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public Boolean getTaxable() {
        return this.taxable;
    }

    @Override
    public void setTaxable(Boolean taxable) {
        this.taxable = taxable;
    }

    @Override
    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    @Override
    public String getTaxCode() {
        return this.taxCode;
    }

    @Override
    public <G extends FulfillmentOption> CreateResponse<G> createOrRetrieveCopyInstance(MultiTenantCopyContext context) throws CloneNotSupportedException {
        CreateResponse<G> createResponse = context.createOrRetrieveCopyInstance(this);
        if (createResponse.isAlreadyPopulated()) {
            return createResponse;
        }
        FulfillmentOption cloned = createResponse.getClone();
        cloned.setFulfillmentType(getFulfillmentType());
        cloned.setName(name);
        cloned.setTaxCode(taxCode);
        cloned.setUseFlatRates(useFlatRates);
        cloned.setTaxable(taxable);
        cloned.setLongDescription(longDescription);
        return createResponse;
    }

    public static class Presentation {
        public static class Group {
            public static class Name {
            }

            public static class Order {
            }
        }

        public static class FieldOrder {
            public static final int NAME = 1000;
            public static final int DESCRIPTION = 2000;
            public static final int FLATRATES = 9000;
            public static final int FULFILLMENT_TYPE = 10000;
            public static final int TAX_CODE = 1100;
            public static final int TAXABLE = 12000;
        }
    }
}
