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
package com.ultracommerce.admin.server.service.extension;

import com.ultracommerce.common.extension.ExtensionHandler;
import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.core.catalog.domain.Product;
import com.ultracommerce.core.catalog.domain.ProductOptionValue;

import java.util.List;


/**
 * Extension handler for {@link com.ultracommerce.admin.server.service.AdminCatalogService}
 *
 * @author Jeff Fischer
 */
public interface AdminCatalogServiceExtensionHandler extends ExtensionHandler {

    public static final int DEFAULT_PRIORITY = 1000;

    /**
     * Customize the persistence of generated sku permutations based on product options.
     *
     * @param product
     * @param permutationsToGenerate
     * @param erh
     * @return
     */
    ExtensionResultStatusType persistSkuPermutation(Product product, List<List<ProductOptionValue>> permutationsToGenerate, ExtensionResultHolder<Integer> erh);

}
