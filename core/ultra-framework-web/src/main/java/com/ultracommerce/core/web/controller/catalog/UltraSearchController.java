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
package com.ultracommerce.core.web.controller.catalog;

import org.apache.commons.lang.StringUtils;
import com.ultracommerce.common.exception.ServiceException;
import com.ultracommerce.common.security.service.ExploitProtectionService;
import com.ultracommerce.common.util.UrlUtil;
import com.ultracommerce.common.web.UltraRequestContext;
import com.ultracommerce.core.catalog.domain.Product;
import com.ultracommerce.core.search.domain.SearchCriteria;
import com.ultracommerce.core.search.domain.SearchResult;
import com.ultracommerce.core.search.redirect.domain.SearchRedirect;
import com.ultracommerce.core.search.redirect.service.SearchRedirectService;
import com.ultracommerce.core.search.service.SearchService;
import com.ultracommerce.core.web.service.SearchFacetDTOService;
import com.ultracommerce.core.web.util.ProcessorUtils;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handles searching the catalog for a given search term. Will apply product search criteria
 * such as filters, sorts, and pagination if applicable
 * 
 * @author Andre Azzolini (apazzolini)
 */
public class UltraSearchController extends AbstractCatalogController {

    @Resource(name = "ucSearchService")
    protected SearchService searchService;

    @Resource(name = "ucExploitProtectionService")
    protected ExploitProtectionService exploitProtectionService;
    
    @Resource(name = "ucSearchFacetDTOService")
    protected SearchFacetDTOService facetService;
    @Resource(name = "ucSearchRedirectService")
    protected SearchRedirectService searchRedirectService;
    protected static String searchView = "catalog/search";
    
    protected static String PRODUCTS_ATTRIBUTE_NAME = "products";
    protected static String FACETS_ATTRIBUTE_NAME = "facets";  
    protected static String PRODUCT_SEARCH_RESULT_ATTRIBUTE_NAME = "result";  
    protected static String ACTIVE_FACETS_ATTRIBUTE_NAME = "activeFacets";  
    protected static String ORIGINAL_QUERY_ATTRIBUTE_NAME = "originalQuery";  
    protected static String ALL_PRODUCTS_ATTRIBUTE_NAME = "ucAllDisplayedProducts";
    protected static String ALL_SKUS_ATTRIBUTE_NAME = "ucAllDisplayedSkus";

    public String search(Model model, HttpServletRequest request, HttpServletResponse response,String query) throws ServletException, IOException, ServiceException {

        if (request.getParameterMap().containsKey("facetField")) {
            // If we receive a facetField parameter, we need to convert the field to the 
            // product search criteria expected format. This is used in multi-facet selection. We 
            // will send a redirect to the appropriate URL to maintain canonical URLs
            
            String fieldName = request.getParameter("facetField");
            List<String> activeFieldFilters = new ArrayList<String>();
            Map<String, String[]> parameters = new HashMap<String, String[]>(request.getParameterMap());
            
            for (Iterator<Entry<String,String[]>> iter = parameters.entrySet().iterator(); iter.hasNext();){
                Map.Entry<String, String[]> entry = iter.next();
                String key = entry.getKey();
                if (key.startsWith(fieldName + "-")) {
                    activeFieldFilters.add(key.substring(key.indexOf('-') + 1));
                    iter.remove();
                }
            }
            
            parameters.remove(SearchCriteria.PAGE_NUMBER);
            parameters.put(fieldName, activeFieldFilters.toArray(new String[activeFieldFilters.size()]));
            parameters.remove("facetField");
            
            String newUrl = ProcessorUtils.getUrl(request.getRequestURL().toString(), parameters);
            return "redirect:" + newUrl;
        } else {
            // Else, if we received a GET to the category URL (either the user performed a search or we redirected
            // from the POST method, we can actually process the results
            SearchRedirect handler = searchRedirectService.findSearchRedirectBySearchTerm(query);
                   
            if (handler != null) {
                String contextPath = request.getContextPath();
                String url = UrlUtil.fixRedirectUrl(contextPath, handler.getUrl());
                response.sendRedirect(url);
                return null;
            }

            if (StringUtils.isNotEmpty(query)) {
                SearchCriteria searchCriteria = facetService.buildSearchCriteria(request);

                if (StringUtils.isEmpty(searchCriteria.getQuery())) {
                    // if our query is empty or null, we want to redirect.
                    return "redirect:/";
                }

                SearchResult result = getSearchService().findSearchResults(searchCriteria);
                
                facetService.setActiveFacetResults(result.getFacets(), request);
                
                model.addAttribute(PRODUCTS_ATTRIBUTE_NAME, result.getProducts());
                model.addAttribute(FACETS_ATTRIBUTE_NAME, result.getFacets());
                model.addAttribute(PRODUCT_SEARCH_RESULT_ATTRIBUTE_NAME, result);
                model.addAttribute(ORIGINAL_QUERY_ATTRIBUTE_NAME, query);
                if (result.getProducts() != null) {
                    model.addAttribute(ALL_PRODUCTS_ATTRIBUTE_NAME, new HashSet<Product>(result.getProducts()));
                }

            }
            
        }

        updateQueryRequestAttribute(query);

        return getSearchView();
    }

    public String getSearchView() {
        return searchView;
    }

    protected void updateQueryRequestAttribute(String query) {
        if (StringUtils.isNotEmpty(query)) {
            UltraRequestContext brc = UltraRequestContext.getUltraRequestContext();
            if (brc != null && brc.getAdditionalProperties() != null) {
                brc.getAdditionalProperties().put("ucSearchKeyword", query);
            }
        }
    }

    protected SearchService getSearchService() {
        return searchService;
    }

}

