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

import com.ultracommerce.common.copy.CreateResponse;
import com.ultracommerce.common.copy.MultiTenantCloneable;
import com.ultracommerce.common.copy.MultiTenantCopyContext;
import com.ultracommerce.common.presentation.AdminPresentation;
import com.ultracommerce.common.presentation.client.SupportedFieldType;
import com.ultracommerce.common.util.WeightUnitOfMeasureType;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 
 * @author jfischer
 *
 */
@Embeddable
public class Weight implements Serializable, MultiTenantCloneable<Weight> {

    private static final long serialVersionUID = 1L;

    @Column(name = "WEIGHT")
    @AdminPresentation(friendlyName = "ProductWeight_Product_Weight",
        group = SkuAdminPresentation.GroupName.ShippingFulfillment, order = SkuAdminPresentation.FieldOrder.WEIGHT)
    protected BigDecimal weight;

        
    @Column(name = "WEIGHT_UNIT_OF_MEASURE")
    @AdminPresentation(friendlyName = "ProductWeight_Product_Weight_Units",
        group = SkuAdminPresentation.GroupName.ShippingFulfillment, order = SkuAdminPresentation.FieldOrder.WEIGHT_UNIT_OF_MEASURE,
        fieldType= SupportedFieldType.ULTRA_ENUMERATION, 
        ultraEnumeration="com.ultracommerce.common.util.WeightUnitOfMeasureType",
        defaultValue = "KILOGRAMS")
    protected String weightUnitOfMeasure;

    public WeightUnitOfMeasureType getWeightUnitOfMeasure() {
        return WeightUnitOfMeasureType.getInstance(weightUnitOfMeasure);
    }

    public void setWeightUnitOfMeasure(WeightUnitOfMeasureType weightUnitOfMeasure) {
        if (weightUnitOfMeasure != null) {
            this.weightUnitOfMeasure = weightUnitOfMeasure.getType();
        }
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    @Override
    public <G extends Weight> CreateResponse<G> createOrRetrieveCopyInstance(MultiTenantCopyContext context) throws CloneNotSupportedException {
        CreateResponse<G> createResponse = context.createOrRetrieveCopyInstance(this);
        Weight clone = createResponse.getClone();
        clone.setWeight(weight);
        if (weightUnitOfMeasure != null) {
            clone.setWeightUnitOfMeasure(getWeightUnitOfMeasure());
        }
        return createResponse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!getClass().isAssignableFrom(o.getClass())) return false;

        Weight weight1 = (Weight) o;

        if (weight != null ? !weight.equals(weight1.weight) : weight1.weight != null) return false;
        if (weightUnitOfMeasure != null ? !weightUnitOfMeasure.equals(weight1.weightUnitOfMeasure) : weight1
                .weightUnitOfMeasure != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = weight != null ? weight.hashCode() : 0;
        result = 31 * result + (weightUnitOfMeasure != null ? weightUnitOfMeasure.hashCode() : 0);
        return result;
    }
}
