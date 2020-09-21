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
package com.ultracommerce.core.catalog.dao;

import com.ultracommerce.core.catalog.domain.Product;
import com.ultracommerce.core.catalog.domain.ProductOption;
import com.ultracommerce.core.catalog.domain.ProductOptionValue;
import com.ultracommerce.core.catalog.domain.dto.AssignedProductOptionDTO;

import java.util.List;

/**
 * 
 * @author Phillip Verheyden
 *
 */
public interface ProductOptionDao {
    
    public List<ProductOption> readAllProductOptions();
    
    public ProductOption readProductOptionById(Long id);
    
    public ProductOption saveProductOption(ProductOption option);
    
    public ProductOptionValue readProductOptionValueById(Long id);

    /**
     * Returns a list of {@link com.ultracommerce.core.catalog.domain.dto.AssignedProductOptionDTO}
     * found for given the productId.
     *
     * @param productId
     * @return
     */
    public List<AssignedProductOptionDTO> findAssignedProductOptionsByProductId(Long productId);

    /**
     * Returns a list of {@link com.ultracommerce.core.catalog.domain.dto.AssignedProductOptionDTO}
     * found for given the {@link com.ultracommerce.core.catalog.domain.Product}.
     *
     * @param product
     * @return
     */
    public List<AssignedProductOptionDTO> findAssignedProductOptionsByProduct(Product product);

    Long countAllowedValuesForProductOptionById(Long productOptionId);

    List<Long> readSkuIdsForProductOptionValues(Long productId, String attributeName, String attributeValue, List<Long> possibleSkuIds);

    public Long countProductsUsingProductOptionById(Long productOptionId);

    /**
     * Returns a paginated list of Product Ids that are using the passed in ProductOption ID
     *
     * @param productOptionId
     * @param start
     * @param pageSize
     * @return
     */
    public List<Long> findProductIdsUsingProductOptionById(Long productOptionId, int start, int pageSize);
}
