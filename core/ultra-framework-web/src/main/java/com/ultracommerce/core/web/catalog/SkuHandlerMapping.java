/*
 * #%L
 * UltraCommerce Framework Web
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
package com.ultracommerce.core.web.catalog;

import com.ultracommerce.common.util.UCRequestUtils;
import com.ultracommerce.common.web.UCAbstractHandlerMapping;
import com.ultracommerce.core.catalog.domain.Sku;
import com.ultracommerce.core.catalog.service.CatalogService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * This handler mapping works with the Sku entity to determine if a sku has been configured for
 * the passed in URL.   
 * 
 * If the URL matches a valid Sku then this mapping returns the handler configured via the 
 * controllerName property or ucSkuController by default. 
 *
 * @author Joshua Skorton (jskorton)
 * @since 3.2
 * @see com.ultracommerce.core.catalog.domain.Sku
 * @see CatalogService
 */
public class SkuHandlerMapping extends UCAbstractHandlerMapping {

    public static final String CURRENT_SKU_ATTRIBUTE_NAME = "currentSku";

    protected String defaultTemplateName = "catalog/sku";

    private String controllerName="ucSkuController";

    @Resource(name = "ucCatalogService")
    private CatalogService catalogService;

    @Override
    protected Object getHandlerInternal(HttpServletRequest request) throws Exception {
        String requestURIWithoutContext = UCRequestUtils.getRequestURIWithoutContext(request);
        if (requestURIWithoutContext != null) {
            Sku sku = catalogService.findSkuByURI(requestURIWithoutContext);

            if (sku != null) {
                request.setAttribute(CURRENT_SKU_ATTRIBUTE_NAME, sku);
                return controllerName;
            }
        }

        return null;
    }

    public String getDefaultTemplateName() {
        return defaultTemplateName;
    }

    public void setDefaultTemplateName(String defaultTemplateName) {
        this.defaultTemplateName = defaultTemplateName;
    }

}
