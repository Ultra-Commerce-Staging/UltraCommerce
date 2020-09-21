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
import com.ultracommerce.core.catalog.domain.Category;
import com.ultracommerce.core.catalog.service.CatalogService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * This handler mapping works with the Category entity to determine if a category has been configured for
 * the passed in URL.   
 * 
 * If the URL matches a valid Category then this mapping returns the handler configured via the 
 * controllerName property or ucCategoryController by default. 
 *
 * @author bpolster
 * @since 2.0
 * @see com.ultracommerce.core.catalog.domain.Category
 * @see CatalogService
 */
public class CategoryHandlerMapping extends UCAbstractHandlerMapping {

    private static final Log LOG = LogFactory.getLog(CategoryHandlerMapping.class);

    public static final String CURRENT_CATEGORY_ATTRIBUTE_NAME = "category";

    protected String defaultTemplateName = "catalog/category";

    private String controllerName = "ucCategoryController";

    @Resource(name = "ucCatalogService")
    private CatalogService catalogService;

    @Value("${request.uri.encoding}")
    public String charEncoding;

    @Override
    protected Object getHandlerInternal(HttpServletRequest request) throws Exception {
        Category category = null;
        if (allowCategoryResolutionUsingIdParam()) {
            category = findCategoryUsingIdParam(request);
        }

        if (category == null) {
            category = findCategoryUsingUrl(request);
        }

        if (category != null) {
            request.setAttribute(CURRENT_CATEGORY_ATTRIBUTE_NAME, category);
            return controllerName;
        }
            
        return null;
    }

    protected Category findCategoryUsingIdParam(HttpServletRequest request) throws ServletRequestBindingException {
        Long categoryId = ServletRequestUtils.getLongParameter(request, "categoryId");

        if (categoryId != null) {
            Category category = catalogService.findCategoryById(categoryId);
            if (category != null && LOG.isDebugEnabled()) {
                LOG.debug("Obtained the category using ID=" + categoryId);
            }
            return category;
        }

        return null;
    }

    protected Category findCategoryUsingUrl(HttpServletRequest request) throws UnsupportedEncodingException {
        String requestUri = URLDecoder.decode(UCRequestUtils.getRequestURIWithoutContext(request), charEncoding);

        Category category = catalogService.findCategoryByURI(requestUri);
        if (category != null && LOG.isDebugEnabled()) {
            LOG.debug("Obtained the category using URI=" + requestUri);
        }

        return category;
    }

    public String getDefaultTemplateName() {
        return defaultTemplateName;
    }

    public void setDefaultTemplateName(String defaultTemplateName) {
        this.defaultTemplateName = defaultTemplateName;
    }
}
