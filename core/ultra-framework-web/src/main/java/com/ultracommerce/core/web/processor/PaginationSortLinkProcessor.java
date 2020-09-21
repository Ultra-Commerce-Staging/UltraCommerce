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

import org.apache.commons.lang3.StringUtils;
import com.ultracommerce.common.web.UltraRequestContext;
import com.ultracommerce.core.search.domain.SearchCriteria;
import com.ultracommerce.core.web.util.ProcessorUtils;
import com.ultracommerce.presentation.condition.ConditionalOnTemplating;
import com.ultracommerce.presentation.dialect.AbstractUltraAttributeModifierProcessor;
import com.ultracommerce.presentation.model.UltraAttributeModifier;
import com.ultracommerce.presentation.model.UltraTemplateContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Thymeleaf Processor that replaces the "href" attribute on an <a/> element, maintaining the current search criteria
 * of the request and adding (or replacing, if it exists) the sort parameter on the request.
 *
 * @author Joseph Fridye (jfridye)
 */
@Component("ucPaginationSortLinkProcessor")
@ConditionalOnTemplating
public class PaginationSortLinkProcessor extends AbstractUltraAttributeModifierProcessor {

    @Override
    public String getName() {
        return "pagination-sort-link";
    }
    
    @Override
    public int getPrecedence() {
        return 10000;
    }

    @Override
    public UltraAttributeModifier getModifiedAttributes(String tagName, Map<String, String> tagAttributes, String attributeName, String attributeValue, UltraTemplateContext context) {
        HttpServletRequest request = UltraRequestContext.getUltraRequestContext().getRequest();

        String baseUrl = request.getRequestURL().toString();

        Map<String, String[]> params = new HashMap<>(request.getParameterMap());

        String sort = attributeValue;

        if (StringUtils.isNotBlank(sort)) {
            params.put(SearchCriteria.SORT_STRING, new String[] { sort });
        } else {
            params.remove(SearchCriteria.SORT_STRING);
        }

        // If there is a page number parameter, remove it. This ensures that when the search results refresh the
        // first page of results will be displayed.
        params.remove(SearchCriteria.PAGE_NUMBER);

        String url = ProcessorUtils.getUrl(baseUrl, params);
        Map<String, String> newAttributes = new HashMap<>();
        newAttributes.put("href", url);
        return new UltraAttributeModifier(newAttributes);

    }
}
