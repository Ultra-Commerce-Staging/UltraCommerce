/*
 * #%L
 * UltraCommerce CMS Module
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

package com.ultracommerce.core.web.processor;

import com.ultracommerce.common.web.UltraRequestContext;
import com.ultracommerce.core.catalog.domain.Category;
import com.ultracommerce.core.catalog.domain.Product;
import com.ultracommerce.core.catalog.service.CatalogURLService;
import com.ultracommerce.presentation.condition.ConditionalOnTemplating;
import com.ultracommerce.presentation.dialect.AbstractUltraAttributeModifierProcessor;
import com.ultracommerce.presentation.model.UltraAttributeModifier;
import com.ultracommerce.presentation.model.UltraTemplateContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * For use with category and product entities.   Creates a relative URL using the
 * current URI appended with the url-key (or last fragment of the url).
 * 
 * Takes in a category or product object as a parameter.
 * 
 * Uses the current request for the baseURI.
 * 
 * This implementation will also a categoryId or productId to the end of the URL it generates.
 * 
 * @author bpolster
 */
@Component("ucCatalogRelativeHrefProcessor")
@ConditionalOnTemplating
public class CatalogRelativeHrefProcessor extends AbstractUltraAttributeModifierProcessor {

    private static final String RHREF = "rhref";
    private static final String HREF = "href";

    @Resource(name = "ucCatalogURLService")
    protected CatalogURLService catalogURLService;

    @Override
    public String getName() {
        return "RHREF";
    }
    
    @Override
    public int getPrecedence() {
        return 0;
    }

    protected String buildRelativeHref(String tagName, Map<String, String> tagAttributes, String attributeName, String attributeValue, UltraTemplateContext context) {
        Object result = context.parseExpression(attributeValue);
        HttpServletRequest request = UltraRequestContext.getUltraRequestContext().getRequest();
        String currentUrl = request.getRequestURI();

        if (request.getQueryString() != null) {
            currentUrl = currentUrl + "?" + request.getQueryString();
        }

        if (result instanceof Product) {
            return catalogURLService.buildRelativeProductURL(currentUrl, (Product) result);
        } else if (result instanceof Category) {
            return catalogURLService.buildRelativeCategoryURL(currentUrl, (Category) result);
        }
        return "";
    }

    @Override
    public UltraAttributeModifier getModifiedAttributes(String tagName, Map<String, String> tagAttributes, String attributeName, String attributeValue, UltraTemplateContext context) {
        String relativeHref = buildRelativeHref(tagName, tagAttributes, attributeName, attributeValue, context);
        Map<String, String> newAttributes = new HashMap<>();
        newAttributes.put(HREF, relativeHref);
        return new UltraAttributeModifier(newAttributes);
    }

}
