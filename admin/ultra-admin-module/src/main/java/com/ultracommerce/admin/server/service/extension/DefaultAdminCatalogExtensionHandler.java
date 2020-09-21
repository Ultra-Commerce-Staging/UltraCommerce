/*
 * #%L
 * UltraCommerce Admin Module
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

import com.ultracommerce.common.extension.AbstractExtensionHandler;
import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.core.catalog.domain.Product;
import com.ultracommerce.core.catalog.domain.ProductOptionValue;
import com.ultracommerce.core.catalog.domain.Sku;
import com.ultracommerce.core.catalog.service.CatalogService;
import org.springframework.stereotype.Component;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Default implementation used by the core framework.
 *
 * @see com.ultracommerce.admin.server.service.extension.AdminCatalogServiceExtensionHandler
 * @author Jeff Fischer
 */
@Component("ucDefaultAdminCatalogExtensionHandler")
public class DefaultAdminCatalogExtensionHandler extends AbstractExtensionHandler implements AdminCatalogServiceExtensionHandler {

    @Resource(name = "ucAdminCatalogServiceExtensionManager")
    protected AdminCatalogServiceExtensionManager extensionManager;

    @Resource(name = "ucCatalogService")
    protected CatalogService catalogService;

    @PostConstruct
    public void init() {
        if (isEnabled()) {
            extensionManager.registerHandler(this);
        }
    }

    /**
     * Simply iterates through the permutations based on the product options and persists them
     * as new {@link com.ultracommerce.core.catalog.domain.Sku} instances in the {@link com.ultracommerce.core.catalog.domain.Product}
     *
     * @param product
     * @param permutationsToGenerate
     * @param erh
     * @return
     */
    @Override
    public ExtensionResultStatusType persistSkuPermutation(Product product, List<List<ProductOptionValue>>
            permutationsToGenerate, ExtensionResultHolder<Integer> erh) {
        int numPermutationsCreated = 0;
        //For each permutation, I need them to map to a specific Sku
        for (List<ProductOptionValue> permutation : permutationsToGenerate) {
            if (permutation.isEmpty()) continue;
            Sku permutatedSku = catalogService.createSku();
            permutatedSku.setProduct(product);
            permutatedSku.setProductOptionValues(permutation);
            permutatedSku = catalogService.saveSku(permutatedSku);
            product.getAdditionalSkus().add(permutatedSku);
            numPermutationsCreated++;
        }
        if (numPermutationsCreated != 0) {
            catalogService.saveProduct(product);
        }
        erh.setResult(numPermutationsCreated);
        return ExtensionResultStatusType.HANDLED;
    }

    @Override
    public int getPriority() {
        return DEFAULT_PRIORITY;
    }
}
