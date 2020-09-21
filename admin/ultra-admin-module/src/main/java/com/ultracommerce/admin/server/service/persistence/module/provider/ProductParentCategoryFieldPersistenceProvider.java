/*
 * #%L
 * UltraCommerce Admin Module
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
package com.ultracommerce.admin.server.service.persistence.module.provider;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import com.ultracommerce.admin.server.service.persistence.module.provider.extension
        .ProductParentCategoryFieldPersistenceProviderExtensionManager;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.common.service.ParentCategoryLegacyModeServiceImpl;
import com.ultracommerce.common.web.UltraRequestContext;
import com.ultracommerce.core.catalog.domain.Category;
import com.ultracommerce.core.catalog.domain.CategoryImpl;
import com.ultracommerce.core.catalog.domain.CategoryProductXref;
import com.ultracommerce.core.catalog.domain.Product;
import com.ultracommerce.openadmin.dto.Property;
import com.ultracommerce.openadmin.server.service.persistence.module.provider.FieldPersistenceProviderAdapter;
import com.ultracommerce.openadmin.server.service.persistence.module.provider.request.ExtractValueRequest;
import com.ultracommerce.openadmin.server.service.persistence.module.provider.request.PopulateValueRequest;
import com.ultracommerce.openadmin.server.service.type.MetadataProviderResponse;
import org.springframework.context.annotation.Scope;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

/**
 * This field persistence provider manages the default CategoryProductXref reference for a Product instance through
 * the "defaultCategory" pseudo field.
 *
 * @author Jeff Fischer
 */
@Component("ucProductParentCategoryFieldPersistenceProvider")
@Scope("prototype")
public class ProductParentCategoryFieldPersistenceProvider extends FieldPersistenceProviderAdapter {

    @Resource(name="ucProductParentCategoryFieldPersistenceProviderExtensionManager")
    protected ProductParentCategoryFieldPersistenceProviderExtensionManager extensionManager;

    @Override
    public MetadataProviderResponse populateValue(PopulateValueRequest populateValueRequest, Serializable instance) {
        if (!canHandlePersistence(populateValueRequest, instance)) {
            return MetadataProviderResponse.NOT_HANDLED;
        }
        boolean handled = false;
        if (extensionManager != null) {
            ExtensionResultStatusType result = extensionManager.getProxy().manageParentCategory(populateValueRequest.getProperty(), (Product) instance);
            handled = ExtensionResultStatusType.NOT_HANDLED != result;
        }
        if (!handled || UltraRequestContext.getUltraRequestContext().isProductionSandBox()) {
            Long requestedValue = null;
            if (StringUtils.isNotEmpty(populateValueRequest.getRequestedValue())) {
                requestedValue = Long.parseLong(populateValueRequest.getRequestedValue());
            }
            boolean dirty = checkDirtyState((Product) instance, requestedValue);
            if (dirty) {
                populateValueRequest.getProperty().setIsDirty(true);
                if (requestedValue != null) {
                    ((Product) instance).setCategory((Category) populateValueRequest.getPersistenceManager()
                            .getDynamicEntityDao().find(CategoryImpl.class, requestedValue));
                } else {
                    ((Product) instance).setCategory(null);
                }
            }
        }
        return MetadataProviderResponse.HANDLED_BREAK;
    }

    @Override
    public MetadataProviderResponse extractValue(ExtractValueRequest extractValueRequest, Property property) {
        if (!canHandleExtraction(extractValueRequest, property)) {
            return MetadataProviderResponse.NOT_HANDLED;
        }
        Category category = getDefaultCategory((Product) extractValueRequest.getEntity());
        if (category != null) {
            property.setValue(String.valueOf(category.getId()));
            property.setDisplayValue(category.getName());
        }
        return MetadataProviderResponse.HANDLED_BREAK;
    }

    protected boolean checkDirtyState(Product instance, Long checkValue) {
        boolean dirty = !(instance == null && checkValue == null) && (instance == null || checkValue == null);
        if (!dirty) {
            Long value = null;
            Category category = getDefaultCategory(instance);
            if (category != null) {
                value = category.getId();
            }
            dirty = value == null || !value.equals(checkValue);
        }
        return dirty;
    }

    protected Category getDefaultCategory(Product product) {
        Category response = null;
        List<CategoryProductXref> xrefs = product.getAllParentCategoryXrefs();
        if (!CollectionUtils.isEmpty(xrefs)) {
            for (CategoryProductXref xref : xrefs) {
                if (xref.getCategory().isActive() && xref.getDefaultReference() != null && xref.getDefaultReference()) {
                    response = xref.getCategory();
                    break;
                }
            }
        }
        return response;
    }

    protected boolean canHandlePersistence(PopulateValueRequest populateValueRequest, Serializable instance) {
        Property property = populateValueRequest.getProperty();
        return instance instanceof Product && property.getName().equals("defaultCategory") &&
                !ParentCategoryLegacyModeServiceImpl.getLegacyModeService().isLegacyMode();
    }

    protected boolean canHandleExtraction(ExtractValueRequest extractValueRequest, Property property) {
        return extractValueRequest.getEntity() instanceof Product && property.getName().equals("defaultCategory") &&
                !ParentCategoryLegacyModeServiceImpl.getLegacyModeService().isLegacyMode();
    }

    @Override
    public int getOrder() {
            return Ordered.HIGHEST_PRECEDENCE + 100;
        }
}
