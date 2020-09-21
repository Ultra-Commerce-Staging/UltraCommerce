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
package com.ultracommerce.core.web.breadcrumbs;

import com.ultracommerce.common.breadcrumbs.dto.BreadcrumbDTO;
import com.ultracommerce.common.breadcrumbs.dto.BreadcrumbDTOType;
import com.ultracommerce.common.breadcrumbs.service.BreadcrumbHandlerDefaultPriorities;
import com.ultracommerce.common.breadcrumbs.service.BreadcrumbServiceExtensionManager;
import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.common.web.UltraRequestContext;
import com.ultracommerce.core.catalog.domain.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;


/**
 * Adds a product breadcrumb using the product on the UltraRequestContext.
 * 
 * @author bpolster
 */
@Service("ucProductBreadcrumbServiceExtensionHandler")
public class ProductBreadcrumbServiceExtensionHandler extends AbstractBreadcrumbServiceExtensionHandler {

    @Resource(name = "ucBreadcrumbServiceExtensionManager")
    protected BreadcrumbServiceExtensionManager extensionManager;

    @PostConstruct
    public void init() {
        if (isEnabled()) {
            extensionManager.registerHandler(this);
        }
    }

    public ExtensionResultStatusType modifyBreadcrumbList(String url, Map<String, String[]> params,
            ExtensionResultHolder<List<BreadcrumbDTO>> holder) {
        Product product = determineProduct(url, params, holder);

        if (product != null) {
            BreadcrumbDTO productDto = new BreadcrumbDTO();
            productDto.setText(getNameForProductLink(product));
            productDto.setLink(buildLink(url, params));
            productDto.setType(BreadcrumbDTOType.PRODUCT);
            holder.getResult().add(0, productDto);
        }

        updateContextMap(url, params, holder);

        return ExtensionResultStatusType.HANDLED_CONTINUE;
    }

    protected Product determineProduct(String url, Map<String, String[]> params,
            ExtensionResultHolder<List<BreadcrumbDTO>> holder) {
        UltraRequestContext brc = UltraRequestContext.getUltraRequestContext();
        if (brc != null) {
            return (Product) brc.getRequestAttribute("currentProduct"); // see ProductHandlerMapping
        }
        return null;
    }

    protected String getNameForProductLink(Product product) {
        return product.getName();
    }

    /**
     * Remove the productId and the last fragment of the URL     
     * 
     * @param params
     * @param holder
     */
    protected void updateContextMap(String origUrl, Map<String, String[]> params, ExtensionResultHolder<List<BreadcrumbDTO>> holder) {
        Map<String, Object> contextMap = holder.getContextMap();

        if (params != null && params.containsKey(getProductIdParam())) {
            params.remove(getProductIdParam());
            contextMap.put(BreadcrumbServiceExtensionManager.CONTEXT_PARAM_STRIPPED_PARAMS, params);
        }

        int pos = origUrl.lastIndexOf("/");
        if (pos > 0) {
            String newUrl = origUrl.substring(0, pos);
            contextMap.put(BreadcrumbServiceExtensionManager.CONTEXT_PARAM_STRIPPED_URL, newUrl);
        }
    }

    protected String getProductIdParam() {
        return "productId";
    }

    @Override
    public int getDefaultPriority() {
        return BreadcrumbHandlerDefaultPriorities.PRODUCT_CRUMB;
    }
}
