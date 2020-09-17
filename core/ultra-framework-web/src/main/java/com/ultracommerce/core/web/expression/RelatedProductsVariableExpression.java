/*
 * #%L
 * UltraCommerce Framework Web
 * %%
 * Copyright (C) 2009 - 2017 Ultra Commerce
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
package com.ultracommerce.core.web.expression;

import com.ultracommerce.common.web.expression.UltraVariableExpression;
import com.ultracommerce.core.catalog.domain.Product;
import com.ultracommerce.core.catalog.domain.PromotableProduct;
import com.ultracommerce.core.catalog.domain.RelatedProductDTO;
import com.ultracommerce.core.catalog.domain.RelatedProductTypeEnum;
import com.ultracommerce.core.catalog.service.RelatedProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nick Crum ncrum
 */
@Component("ucRelatedProductsVariableExpression")
public class RelatedProductsVariableExpression implements UltraVariableExpression {

    private final RelatedProductsService relatedProductsService;

    @Autowired
    public RelatedProductsVariableExpression(RelatedProductsService relatedProductsService) {
        this.relatedProductsService = relatedProductsService;
    }

    @Override
    public String getName() {
        return "related_products";
    }

    public List<Product> findByProduct(Long productId) {
        return getRelatedProducts(productId, null, null, null);
    }

    public List<Product> findByProduct(Long productId, Integer quantity) {
        return getRelatedProducts(productId, null, quantity, null);
    }

    public List<Product> findByCategory(Long categoryId, Integer quantity) {
        return getRelatedProducts(null, categoryId, quantity, null);
    }

    public List<Product> findByProduct(Long productId, Integer quantity, String type) {
        return getRelatedProducts(productId, null, quantity, type);
    }

    public List<Product> findByCategory(Long categoryId, Integer quantity, String type) {
        return getRelatedProducts(null, categoryId, quantity, type);
    }

    public List<Product> getRelatedProducts(Long productId, Long categoryId, Integer quantity, String type) {
        RelatedProductDTO relatedProductDTO = new RelatedProductDTO();
        relatedProductDTO.setProductId(productId);
        relatedProductDTO.setCategoryId(categoryId);

        if (quantity != null) {
            relatedProductDTO.setQuantity(quantity);
        }

        if (type != null) {
            relatedProductDTO.setType(RelatedProductTypeEnum.getInstance(type));
        }
        List<? extends PromotableProduct> relatedProducts = relatedProductsService.findRelatedProducts(relatedProductDTO);
        return buildProductList(relatedProducts);
    }

    protected List<Product> buildProductList(List<? extends PromotableProduct> relatedProducts) {
        List<Product> productList = new ArrayList<>();
        if (relatedProducts != null) {
            for (PromotableProduct promProduct : relatedProducts) {
                productList.add(promProduct.getRelatedProduct());
            }
        }
        return productList;
    }
}
