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

import com.ultracommerce.common.extension.ExtensionHandler;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.core.catalog.domain.Product;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.servlet.http.HttpServletRequest;

/**
 * Extension handler for extending functionality of {@link com.ultracommerce.core.web.linkeddata.generator.LinkedDataGenerator}.
 * Implementors should extend from {@link AbstractLinkedDataGeneratorExtensionHandler} to protect from API changes to this interface
 * 
 * @author Nathan Moore (nathanmoore).
 */
public interface LinkedDataGeneratorExtensionHandler extends ExtensionHandler{
    // default
    ExtensionResultStatusType addDefaultData(final HttpServletRequest request, final JSONArray schemaObjects) throws JSONException;
    ExtensionResultStatusType addBreadcrumbData(final HttpServletRequest request, final JSONObject breadcrumbData) throws JSONException;
    ExtensionResultStatusType addBreadcrumbListItemData(final HttpServletRequest request, final JSONObject breadcrumbData) throws JSONException;
    ExtensionResultStatusType addBreadcrumbItemData(final HttpServletRequest request, final JSONObject breadcrumbData) throws JSONException;
    // homepage
    ExtensionResultStatusType addHomepageData(final HttpServletRequest request, final JSONArray schemaObjects) throws JSONException;
    ExtensionResultStatusType addWebSiteData(final HttpServletRequest request, final JSONObject homepageData) throws JSONException;
    ExtensionResultStatusType addOrganizationData(final HttpServletRequest request, final JSONObject homepageData) throws JSONException;
    ExtensionResultStatusType addContactData(final HttpServletRequest request, final JSONObject homepageData) throws JSONException;
    ExtensionResultStatusType addSocialMediaData(final HttpServletRequest request, final JSONArray homepageData) throws JSONException;
    ExtensionResultStatusType addPotentialActionsData(final HttpServletRequest request, final JSONObject homepageData) throws JSONException;
    // category
    ExtensionResultStatusType addCategoryData(final HttpServletRequest request, final JSONObject categoryData) throws JSONException;
    ExtensionResultStatusType addCategoryProductData(final HttpServletRequest request, final JSONObject categoryData) throws JSONException;
    // product
    ExtensionResultStatusType addReviewData(final HttpServletRequest request, final Product product, final JSONObject reviewData) throws JSONException;
    ExtensionResultStatusType addAggregateReviewData(final HttpServletRequest request, final Product product, final JSONObject reviewData) throws JSONException;
    ExtensionResultStatusType addProductData(final HttpServletRequest request, final Product product, final JSONObject productData) throws JSONException;
    ExtensionResultStatusType addSkuData(final HttpServletRequest request, final Product product, final JSONObject skuData) throws JSONException;
    ExtensionResultStatusType addAggregateSkuData(final HttpServletRequest request, final Product product, final JSONObject skuData) throws JSONException;
}
