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

import org.apache.commons.lang.StringUtils;
import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.core.catalog.domain.Category;
import com.ultracommerce.core.catalog.domain.CategoryXref;
import com.ultracommerce.core.catalog.service.CatalogService;
import com.ultracommerce.presentation.condition.ConditionalOnTemplating;
import com.ultracommerce.presentation.dialect.AbstractUltraVariableModifierProcessor;
import com.ultracommerce.presentation.model.UltraTemplateContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * A Thymeleaf processor that will add the desired categories to the model. It does this by
 * searching for the <b>parentCategory</b> by name> and adding up to <b>maxResults</b> subcategories under
 * the model attribute specified by <b>resultVar</b>
 * 
 * @param parentCategory (required) the name of the parent category to get subcategories from
 * @param resultVar (required) the model variable that the resulting list of categories should be set to
 * @param maxResults (optional) the maximum number of categories to return
 * 
 * @author apazzolini
 */
@Component("ucCategoriesProcessor")
@ConditionalOnTemplating
public class CategoriesProcessor extends AbstractUltraVariableModifierProcessor {

    @Resource(name = "ucCatalogService")
    protected CatalogService catalogService;

    @Resource(name = "ucCategoriesProcessorExtensionManager")
    protected CategoriesProcessorExtensionManager extensionManager;

    @Override
    public String getName() {
        return "categories";
    }
    
    @Override
    public int getPrecedence() {
        return 10000;
    }

    @Override
    public Map<String, Object> populateModelVariables(String tagName, Map<String, String> tagAttributes, UltraTemplateContext context) {
        String resultVar = tagAttributes.get("resultVar");
        String parentCategory = tagAttributes.get("parentCategory");
        String unparsedMaxResults = tagAttributes.get("maxResults");

        Map<String, Object> newModelVars = new HashMap<>();
        if (extensionManager != null) {
            ExtensionResultHolder holder = new ExtensionResultHolder();
            ExtensionResultStatusType result = extensionManager.getProxy().findAllPossibleChildCategories(parentCategory, unparsedMaxResults, holder);
            if (ExtensionResultStatusType.HANDLED.equals(result)) {
                newModelVars.put(resultVar, holder.getResult());
                return newModelVars;
            }
        }

        // TODO: Potentially write an algorithm that will pick the minimum depth category
        // instead of the first category in the list
        List<Category> categories = catalogService.findCategoriesByName(parentCategory);
        if (categories != null && categories.size() > 0) {
            // gets child categories in order ONLY if they are in the xref table and active
            List<CategoryXref> subcategories = categories.get(0).getChildCategoryXrefs();
            List<Category> results = Collections.emptyList();
            if (subcategories != null && !subcategories.isEmpty()) {
                results = new ArrayList<>(subcategories.size());
                if (StringUtils.isNotEmpty(unparsedMaxResults)) {
                    int maxResults = Integer.parseInt(unparsedMaxResults);
                    if (subcategories.size() > maxResults) {
                        subcategories = subcategories.subList(0, maxResults);
                    }
                }

                for (CategoryXref xref : subcategories) {
                    results.add(xref.getSubCategory());
                }
            }
            newModelVars.put(resultVar, results);
        }
        return newModelVars;
    }

}
