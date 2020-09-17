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
package com.ultracommerce.core.catalog.service;

import com.ultracommerce.core.catalog.domain.PromotableProduct;
import com.ultracommerce.core.catalog.domain.RelatedProductDTO;

import java.util.List;

/**
 * Interface for finding related products.   Could be extended to support more complex
 * related product functions.    
 * 
 * @author bpolster
 *
 */
public interface RelatedProductsService {   
    
    /**
     * Uses the data in the passed in DTO to return a list of relatedProducts.
     * 
     * For example, upSale, crossSale, or featured products.
     * 
     * @param relatedProductDTO
     * @return
     */
    public List<? extends PromotableProduct> findRelatedProducts(RelatedProductDTO relatedProductDTO);
}
