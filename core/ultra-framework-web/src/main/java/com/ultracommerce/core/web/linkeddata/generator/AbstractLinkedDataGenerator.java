/*
 * #%L
 * UltraCommerce Framework Web
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

import com.ultracommerce.common.breadcrumbs.service.BreadcrumbService;
import com.ultracommerce.common.web.BaseUrlResolver;
import com.ultracommerce.common.web.UltraRequestContext;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 
 * 
 * @author Nathan Moore (nathanmoore).
 */
public abstract class AbstractLinkedDataGenerator implements LinkedDataGenerator {
    protected static final String DEFAULT_STRUCTURED_CONTENT_CONTEXT = "http://schema.org/";

    @Autowired
    protected Environment environment;

    @Resource(name = "ucBaseUrlResolver")
    protected BaseUrlResolver baseUrlResolver;

    @Resource(name = "ucBreadcrumbService")
    protected BreadcrumbService breadcrumbService;
    
    @Resource(name = "ucLinkedDataGeneratorExtensionManager")
    protected LinkedDataGeneratorExtensionManager extensionManager;

    @Override
    public abstract boolean canHandle(final HttpServletRequest request);

    @Override
    public void getLinkedDataJSON(final String url, final HttpServletRequest request, final JSONArray schemaObjects) throws JSONException {
        getLinkedDataJsonInternal(url, request, schemaObjects);
    }
    
    protected abstract JSONArray getLinkedDataJsonInternal(final String url, final HttpServletRequest request, 
                                                           final JSONArray schemaObjects) throws JSONException;

    protected String getRequestUri() {
        final HttpServletRequest request = UltraRequestContext.getUltraRequestContext().getRequest();

        return request.getRequestURI();
    }

    protected static Map<String, String[]> getRequestParams() {
        Map<String, String[]> params = new HashMap<>();

        if (UltraRequestContext.getRequestParameterMap() != null) {
            params = new HashMap<>(UltraRequestContext.getRequestParameterMap());
        }

        return params;
    }

    protected String getSiteBaseUrl() {
        return baseUrlResolver.getSiteBaseUrl();
    }

    @Override
    public String getStructuredDataContext() {
        return environment.getProperty("structured.data.context", DEFAULT_STRUCTURED_CONTENT_CONTEXT);
    }
}
