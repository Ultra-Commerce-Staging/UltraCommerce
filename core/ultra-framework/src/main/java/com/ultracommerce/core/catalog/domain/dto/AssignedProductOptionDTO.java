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
package com.ultracommerce.core.catalog.domain.dto;

import com.ultracommerce.core.catalog.domain.ProductOptionValue;
import com.ultracommerce.core.catalog.domain.Sku;

/**
 * DTO used to carry back the found {@link com.ultracommerce.core.catalog.domain.ProductOptionValue#getId()} and
 * {@link com.ultracommerce.core.catalog.domain.ProductOption#getAttributeName()} on a given
 * {@link com.ultracommerce.core.catalog.domain.Product}
 *
 * @author Jerry Ocanas (jocanas)
 */
public class AssignedProductOptionDTO {

    private Long productId;
    private String productOptionAttrName;
    private ProductOptionValue productOptionValue;
    private Sku sku;

    public AssignedProductOptionDTO(Long productId, String productOptionAttrName, ProductOptionValue productOptionValue, Sku sku) {
        this.productId = productId;
        this.productOptionAttrName = productOptionAttrName;
        this.productOptionValue = productOptionValue;
        this.sku = sku;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductOptionAttrName() {
        return productOptionAttrName;
    }

    public void setProductOptionAttrName(String productOptionAttrName) {
        this.productOptionAttrName = productOptionAttrName;
    }

    public ProductOptionValue getProductOptionValue() {
        return productOptionValue;
    }

    public void setProductOptionValue(ProductOptionValue productOptionValue) {
        this.productOptionValue = productOptionValue;
    }

    public Sku getSku() {
        return sku;
    }

    public void setSku(Sku sku) {
        this.sku = sku;
    }
}
