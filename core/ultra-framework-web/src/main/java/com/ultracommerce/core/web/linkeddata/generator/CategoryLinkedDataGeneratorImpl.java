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

import com.ultracommerce.common.util.UCArrayUtils;
import com.ultracommerce.common.util.TypedTransformer;
import com.ultracommerce.core.catalog.domain.Category;
import com.ultracommerce.core.catalog.domain.CategoryProductXref;
import com.ultracommerce.core.catalog.domain.Product;
import com.ultracommerce.core.web.catalog.CategoryHandlerMapping;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * This generator generates structured data specific to the category pages.
 * <p>
 * See <a href="http://schema.org/ItemList" target="_blank">http://schema.org/ItemList</a> 
 * and <a href="http://schema.org/ListItem" target="_blank">http://schema.org/ListItem</a>
 *
 * @author Jacob Mitash
 * @author Nathan Moore (nathanmoore).
 */
@Service(value = "ucCategoryLinkedDataGenerator")
public class CategoryLinkedDataGeneratorImpl extends AbstractLinkedDataGenerator {

    @Override
    public boolean canHandle(final HttpServletRequest request) {
        return request.getAttribute(CategoryHandlerMapping.CURRENT_CATEGORY_ATTRIBUTE_NAME) != null;
    }

    @Override
    protected JSONArray getLinkedDataJsonInternal(final String url, final HttpServletRequest request,
                                                  final JSONArray schemaObjects) throws JSONException {
        final JSONObject categoryData = new JSONObject();
        
        categoryData.put("@context", getStructuredDataContext());
        categoryData.put("@type", "ItemList");

        addCategoryProductData(request, categoryData);
        
        extensionManager.getProxy().addCategoryData(request, categoryData);

        schemaObjects.put(categoryData);

        return schemaObjects;

    }
    
    protected void addCategoryProductData(final HttpServletRequest request, final JSONObject categoryData) throws JSONException {
        final List<Product> products = getProducts(request);
        final JSONArray itemList = new JSONArray();

        for (int i = 0; i < products.size(); i++) {
            JSONObject item = new JSONObject();
            item.put("@type", "ListItem");
            item.put("position", i + 1);
            item.put("url", products.get(i).getUrl());
            itemList.put(item);

            extensionManager.getProxy().addCategoryProductData(request, categoryData);
        }

        categoryData.put("itemListElement", itemList);
    }

    protected List<Product> getProducts(final HttpServletRequest request) {
        final Category category = (Category) request.getAttribute(CategoryHandlerMapping.CURRENT_CATEGORY_ATTRIBUTE_NAME);

        return UCArrayUtils.collect(category.getActiveProductXrefs().toArray(), new TypedTransformer<Product>() {
            @Override
            public Product transform(Object input) {
                return ((CategoryProductXref) input).getProduct();
            }
        });
    }
}
