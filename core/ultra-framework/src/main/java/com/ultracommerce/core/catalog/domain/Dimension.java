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
import com.ultracommerce.common.util.DimensionUnitOfMeasureType;
import com.ultracommerce.common.vendor.service.type.ContainerShapeType;
import com.ultracommerce.common.vendor.service.type.ContainerSizeType;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Dimension implements Serializable, MultiTenantCloneable<Dimension> {

    private static final long serialVersionUID = 1L;

    @Column(name = "WIDTH")
    @AdminPresentation(friendlyName = "ProductDimension_Product_Width",
            group = SkuAdminPresentation.GroupName.ShippingDimensions, order = SkuAdminPresentation.FieldOrder.WIDTH)
    protected BigDecimal width;

    @Column(name = "HEIGHT")
    @AdminPresentation(friendlyName = "ProductDimension_Product_Height",
            group = SkuAdminPresentation.GroupName.ShippingDimensions, order = SkuAdminPresentation.FieldOrder.HEIGHT)
    protected BigDecimal height;

    @Column(name = "DEPTH")
    @AdminPresentation(friendlyName = "ProductDimension_Product_Depth",
        group = SkuAdminPresentation.GroupName.ShippingDimensions, order = SkuAdminPresentation.FieldOrder.DEPTH)
    protected BigDecimal depth;

    @Column(name = "GIRTH")
    @AdminPresentation(friendlyName = "ProductDimension_Product_Girth",
        group = SkuAdminPresentation.GroupName.ShippingDimensions, order = SkuAdminPresentation.FieldOrder.GIRTH)
    protected BigDecimal girth;

    @Column(name = "CONTAINER_SIZE")
    @AdminPresentation(friendlyName = "ProductDimension_Product_Container_Size",
        group = SkuAdminPresentation.GroupName.ShippingContainer, order = SkuAdminPresentation.FieldOrder.CONTAINER_SIZE,
        fieldType = SupportedFieldType.ULTRA_ENUMERATION, 
        ultraEnumeration = "com.ultracommerce.common.vendor.service.type.ContainerSizeType",
        hideEnumerationIfEmpty = true)
    protected String size;

    @Column(name = "CONTAINER_SHAPE")
    @AdminPresentation(friendlyName = "ProductDimension_Product_Container_Shape",
        group = SkuAdminPresentation.GroupName.ShippingContainer, order = SkuAdminPresentation.FieldOrder.CONTAINER_SHAPE,
        fieldType = SupportedFieldType.ULTRA_ENUMERATION,
        ultraEnumeration = "com.ultracommerce.common.vendor.service.type.ContainerShapeType",
        hideEnumerationIfEmpty = true)
    protected String container;

    @Column(name = "DIMENSION_UNIT_OF_MEASURE")
    @AdminPresentation(friendlyName = "ProductDimension_Product_Dimension_Units",
        group = SkuAdminPresentation.GroupName.ShippingDimensions, order = SkuAdminPresentation.FieldOrder.DIMENSION_UNIT_OF_MEASURE,
        fieldType = SupportedFieldType.ULTRA_ENUMERATION, 
        ultraEnumeration = "com.ultracommerce.common.util.DimensionUnitOfMeasureType",
        defaultValue = "CENTIMETERS")
    protected String dimensionUnitOfMeasure;

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(final BigDecimal width) {
        this.width = width;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(final BigDecimal height) {
        this.height = height;
    }

    public BigDecimal getDepth() {
        return depth;
    }

    public void setDepth(final BigDecimal depth) {
        this.depth = depth;
    }

    /**
     * Returns the product dimensions as a String (assumes measurements are in
     * inches)
     * @return a String value of the product dimensions
     */
    public String getDimensionString() {
        return height + "Hx" + width + "Wx" + depth + "D\"";
    }

    public BigDecimal getGirth() {
        return girth;
    }

    public void setGirth(final BigDecimal girth) {
        this.girth = girth;
    }

    public ContainerSizeType getSize() {
        return ContainerSizeType.getInstance(size);
    }

    public void setSize(final ContainerSizeType size) {
        if (size != null) {
            this.size = size.getType();
        }
    }

    public ContainerShapeType getContainer() {
        return ContainerShapeType.getInstance(container);
    }

    public void setContainer(final ContainerShapeType container) {
        if (container != null) {
            this.container = container.getType();
        }
    }

    public DimensionUnitOfMeasureType getDimensionUnitOfMeasure() {
        return DimensionUnitOfMeasureType.getInstance(dimensionUnitOfMeasure);
    }

    public void setDimensionUnitOfMeasure(final DimensionUnitOfMeasureType dimensionUnitOfMeasure) {
        if (dimensionUnitOfMeasure != null) {
            this.dimensionUnitOfMeasure = dimensionUnitOfMeasure.getType();
        }
    }

    @Override
    public <G extends Dimension> CreateResponse<G> createOrRetrieveCopyInstance(MultiTenantCopyContext context) throws CloneNotSupportedException {
        CreateResponse<G> createResponse = context.createOrRetrieveCopyInstance(this);
        Dimension clone = createResponse.getClone();
        if (container != null) {
            clone.setContainer(getContainer());
        }
        clone.setWidth(width);
        clone.setDepth(depth);
        clone.setGirth(girth);
        clone.setHeight(height);
        if (size != null) {
            clone.setSize(getSize());
        }
        if (dimensionUnitOfMeasure != null) {
            clone.setDimensionUnitOfMeasure(getDimensionUnitOfMeasure());
        }
        return createResponse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!getClass().isAssignableFrom(o.getClass())) return false;

        Dimension dimension = (Dimension) o;

        if (container != null ? !container.equals(dimension.container) : dimension.container != null) return false;
        if (depth != null ? !depth.equals(dimension.depth) : dimension.depth != null) return false;
        if (dimensionUnitOfMeasure != null ? !dimensionUnitOfMeasure.equals(dimension.dimensionUnitOfMeasure) :
                dimension.dimensionUnitOfMeasure != null)
            return false;
        if (girth != null ? !girth.equals(dimension.girth) : dimension.girth != null) return false;
        if (height != null ? !height.equals(dimension.height) : dimension.height != null) return false;
        if (size != null ? !size.equals(dimension.size) : dimension.size != null) return false;
        if (width != null ? !width.equals(dimension.width) : dimension.width != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = width != null ? width.hashCode() : 0;
        result = 31 * result + (height != null ? height.hashCode() : 0);
        result = 31 * result + (depth != null ? depth.hashCode() : 0);
        result = 31 * result + (girth != null ? girth.hashCode() : 0);
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + (container != null ? container.hashCode() : 0);
        result = 31 * result + (dimensionUnitOfMeasure != null ? dimensionUnitOfMeasure.hashCode() : 0);
        return result;
    }
}
