/*
 * #%L
 * UltraCommerce Framework
 * %%
 * Copyright (C) 2009 - 2017 Ultra Commerce
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
package com.ultracommerce.core.web.linkeddata.generator;

import com.ultracommerce.common.breadcrumbs.dto.BreadcrumbDTO;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * This generator runs for all pages.
 * <p>
 * Currently, it only generates breadcrumb structured data.
 * <p>
 * See <a href="http://schema.org/BreadcrumbList" target="_blank">http://schema.org/BreadcrumbList</a>.
 * 
 *
 * @author Jacob Mitash
 * @author Nathan Moore (nathanmoore).
 */
@Service(value = "ucDefaultLinkedDataGenerator")
public class DefaultLinkedDataGeneratorImpl extends AbstractLinkedDataGenerator {
    @Override
    public boolean canHandle(final HttpServletRequest request) {
        return true;
    }

    @Override
    protected JSONArray getLinkedDataJsonInternal(String url, final HttpServletRequest request, 
                                                  final JSONArray schemaObjects) throws JSONException {
        schemaObjects.put(addBreadcrumbData(request));
        
        extensionManager.getProxy().addDefaultData(request, schemaObjects);
        
        return schemaObjects;
    }

    /**
     * Generates an object representing the Schema.org BreadcrumbList
     *
     * @return JSON representation of BreadcrumbList from Schema.org
     */
    protected JSONObject addBreadcrumbData(final HttpServletRequest request) throws JSONException {
        final JSONObject breadcrumbObjects = new JSONObject();

        breadcrumbObjects.put("@context", getStructuredDataContext());
        breadcrumbObjects.put("@type", "BreadcrumbList");

        final String requestUri = getRequestUri();
        final Map<String, String[]> params = getRequestParams();

        final List<BreadcrumbDTO> breadcrumbs = breadcrumbService.buildBreadcrumbDTOs(requestUri, params);

        final JSONArray breadcrumbList = new JSONArray();
        int index = 1;

        for (final BreadcrumbDTO breadcrumb : breadcrumbs) {
            final JSONObject listItem = new JSONObject();
            listItem.put("@type", "ListItem");
            listItem.put("position", index);

            final JSONObject item = new JSONObject();
            item.put("@id", getSiteBaseUrl() + breadcrumb.getLink());
            item.put("name", breadcrumb.getText());

            extensionManager.getProxy().addBreadcrumbItemData(request, item);

            listItem.put("item", item);
            
            extensionManager.getProxy().addBreadcrumbListItemData(request, listItem);
            
            breadcrumbList.put(listItem);
            index++;
        }
        
        extensionManager.getProxy().addBreadcrumbData(request, breadcrumbObjects);

        breadcrumbObjects.put("itemListElement", breadcrumbList);

        return breadcrumbObjects;
    }
}
