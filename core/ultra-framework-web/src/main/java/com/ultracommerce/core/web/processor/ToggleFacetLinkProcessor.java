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

package com.ultracommerce.core.web.processor;

import org.apache.commons.lang.ArrayUtils;
import com.ultracommerce.common.web.UltraRequestContext;
import com.ultracommerce.core.search.domain.SearchCriteria;
import com.ultracommerce.core.search.domain.SearchFacetResultDTO;
import com.ultracommerce.core.web.service.SearchFacetDTOService;
import com.ultracommerce.core.web.util.ProcessorUtils;
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
 * A Thymeleaf processor that processes the value attribute on the element it's tied to
 * with a predetermined value based on the SearchFacetResultDTO object that is passed into this
 * processor. 
 * 
 * @author apazzolini
 */
@Component("ucToggleFacetLinkProcessor")
@ConditionalOnTemplating
public class ToggleFacetLinkProcessor extends AbstractUltraAttributeModifierProcessor {

    @Resource(name = "ucSearchFacetDTOService")
    protected SearchFacetDTOService facetService;

    @Override
    public String getName() {
        return "togglefacetlink";
    }
    
    @Override
    public int getPrecedence() {
        return 10000;
    }

    @Override
    public UltraAttributeModifier getModifiedAttributes(String tagName, Map<String, String> tagAttributes, String attributeName, String attributeValue, UltraTemplateContext context) {
        UltraRequestContext ucContext = UltraRequestContext.getUltraRequestContext();
        HttpServletRequest request = ucContext.getRequest();

        String baseUrl = request.getRequestURL().toString();
        Map<String, String[]> params = new HashMap<>(request.getParameterMap());

        SearchFacetResultDTO result = (SearchFacetResultDTO) context.parseExpression(attributeValue);

        String key = facetService.getUrlKey(result);
        String value = facetService.getValue(result);
        String[] paramValues = params.get(key);

        if (ArrayUtils.contains(paramValues, facetService.getValue(result))) {
            paramValues = (String[]) ArrayUtils.removeElement(paramValues, facetService.getValue(result));
        } else {
            paramValues = (String[]) ArrayUtils.add(paramValues, value);
        }

        params.remove(SearchCriteria.PAGE_NUMBER);
        params.put(key, paramValues);

        String url = ProcessorUtils.getUrl(baseUrl, params);
        Map<String, String> newAttributes = new HashMap<>();
        newAttributes.put("href", url);
        return new UltraAttributeModifier(newAttributes);
    }

}
