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

import com.ultracommerce.common.extension.AbstractExtensionHandler;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.core.catalog.domain.Product;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Nathan Moore (nathanmoore).
 */
public abstract class AbstractLinkedDataGeneratorExtensionHandler extends AbstractExtensionHandler implements LinkedDataGeneratorExtensionHandler {

    @Override
    public ExtensionResultStatusType addDefaultData(final HttpServletRequest request, final JSONArray defaultData)
            throws JSONException {
        return ExtensionResultStatusType.NOT_HANDLED; 
    }

    @Override
    public ExtensionResultStatusType addBreadcrumbData(final HttpServletRequest request, final JSONObject breadcrumbData)
            throws JSONException {
        return ExtensionResultStatusType.NOT_HANDLED; 
    }

    @Override
    public ExtensionResultStatusType addBreadcrumbListItemData(final HttpServletRequest request, final JSONObject breadcrumbData)
            throws JSONException {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType addBreadcrumbItemData(final HttpServletRequest request, final JSONObject breadcrumbData)
            throws JSONException {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType addHomepageData(final HttpServletRequest request, final JSONArray homepageData)
            throws JSONException {
        return ExtensionResultStatusType.NOT_HANDLED; 
    }

    @Override
    public ExtensionResultStatusType addWebSiteData(final HttpServletRequest request, final JSONObject homepageData)
            throws JSONException {
        return ExtensionResultStatusType.NOT_HANDLED; 
    }

    @Override
    public ExtensionResultStatusType addOrganizationData(final HttpServletRequest request, final JSONObject homepageData) 
            throws JSONException {
        return ExtensionResultStatusType.NOT_HANDLED; 
    }

    @Override
    public ExtensionResultStatusType addContactData(final HttpServletRequest request, final JSONObject homepageData)
            throws JSONException {
        return ExtensionResultStatusType.NOT_HANDLED; 
    }

    @Override
    public ExtensionResultStatusType addSocialMediaData(final HttpServletRequest request, final JSONArray homepageData)
            throws JSONException {
        return ExtensionResultStatusType.NOT_HANDLED; 
    }

    @Override
    public ExtensionResultStatusType addPotentialActionsData(final HttpServletRequest request, final JSONObject homepageData) 
            throws JSONException {
        return ExtensionResultStatusType.NOT_HANDLED; 
    }

    @Override
    public ExtensionResultStatusType addCategoryData(final HttpServletRequest request, final JSONObject categoryData)
            throws JSONException {
        return ExtensionResultStatusType.NOT_HANDLED; 
    }

    @Override
    public ExtensionResultStatusType addCategoryProductData(final HttpServletRequest request, final JSONObject categoryData) 
            throws JSONException {
        return ExtensionResultStatusType.NOT_HANDLED; 
    }

    @Override
    public ExtensionResultStatusType addReviewData(final HttpServletRequest request, final Product product, final JSONObject reviewData) 
            throws JSONException {
        return ExtensionResultStatusType.NOT_HANDLED; 
    }

    @Override
    public ExtensionResultStatusType addAggregateReviewData(final HttpServletRequest request, final Product product, final JSONObject reviewData)
            throws JSONException {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType addProductData(final HttpServletRequest request, final Product product, final JSONObject productData) 
            throws JSONException {
        return ExtensionResultStatusType.NOT_HANDLED; 
    }

    @Override
    public ExtensionResultStatusType addSkuData(final HttpServletRequest request, final Product product, final JSONObject skuData) 
            throws JSONException {
        return ExtensionResultStatusType.NOT_HANDLED; 
    }

    @Override
    public ExtensionResultStatusType addAggregateSkuData(final HttpServletRequest request, final Product product, final JSONObject skuData)
            throws JSONException {
        return ExtensionResultStatusType.NOT_HANDLED;
    }
}
