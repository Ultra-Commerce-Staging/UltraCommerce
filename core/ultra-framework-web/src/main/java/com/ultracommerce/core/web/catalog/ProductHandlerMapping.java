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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.util.UCRequestUtils;
import com.ultracommerce.common.web.UCAbstractHandlerMapping;
import com.ultracommerce.core.catalog.domain.Product;
import com.ultracommerce.core.catalog.service.CatalogService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * This handler mapping works with the Product entity to determine if a product has been configured for
 * the passed in URL.   
 * 
 * If the URL matches a valid Product then this mapping returns the handler configured via the 
 * controllerName property or ucProductController by default. 
 *
 * @author bpolster
 * @since 2.0
 * @see com.ultracommerce.core.catalog.domain.Product
 * @see CatalogService
 */
public class ProductHandlerMapping extends UCAbstractHandlerMapping {

    private static final Log LOG = LogFactory.getLog(ProductHandlerMapping.class);

    public static final String CURRENT_PRODUCT_ATTRIBUTE_NAME = "currentProduct";

    protected String defaultTemplateName = "catalog/product";

    private final String controllerName = "ucProductController";

    @Resource(name = "ucCatalogService")
    protected CatalogService catalogService;

    @Value("${request.uri.encoding}")
    public String charEncoding;

    @Override
    protected Object getHandlerInternal(HttpServletRequest request) throws Exception {
        if (shouldSkipExecution(request)) {
            return null;
        }

        Product product = null;
        if (allowProductResolutionUsingIdParam()) {
            product = findProductUsingIdParam(request);
        }

        if (product == null) {
            product = findProductUsingUrl(request);
        }

        if (product != null) {
            request.setAttribute(CURRENT_PRODUCT_ATTRIBUTE_NAME, product);
            return controllerName;
        }

        return null;
    }

    public boolean shouldSkipExecution(HttpServletRequest request) throws ServletRequestBindingException {
        if (allowCategoryResolutionUsingIdParam()
                && ServletRequestUtils.getLongParameter(request, "categoryId") != null) {
            return true;
        }

        return false;
    }

    protected Product findProductUsingIdParam(HttpServletRequest request) throws ServletRequestBindingException {
        Long productId = ServletRequestUtils.getLongParameter(request, "productId");

        if (productId != null) {
            Product product = catalogService.findProductById(productId);
            if (product != null && LOG.isDebugEnabled()) {
                LOG.debug("Obtained the product using id=" + productId);
            }
            return product;
        }

        return null;
    }

    protected Product findProductUsingUrl(HttpServletRequest request) throws UnsupportedEncodingException {
        String requestUri = URLDecoder.decode(UCRequestUtils.getRequestURIWithoutContext(request), charEncoding);

        Product product = catalogService.findProductByURI(requestUri);
        if (product != null && LOG.isDebugEnabled()) {
            LOG.debug("Obtained the product using URI=" + requestUri);
        }

        return product;
    }

    public String getDefaultTemplateName() {
        return defaultTemplateName;
    }

    public void setDefaultTemplateName(String defaultTemplateName) {
        this.defaultTemplateName = defaultTemplateName;
    }

}
