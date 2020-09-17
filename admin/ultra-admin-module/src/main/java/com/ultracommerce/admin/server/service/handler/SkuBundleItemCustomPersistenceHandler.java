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
package com.ultracommerce.admin.server.service.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.exception.ServiceException;
import com.ultracommerce.common.presentation.client.PersistencePerspectiveItemType;
import com.ultracommerce.core.catalog.domain.ProductOptionValue;
import com.ultracommerce.core.catalog.domain.Sku;
import com.ultracommerce.core.catalog.domain.SkuBundleItem;
import com.ultracommerce.core.catalog.domain.SkuBundleItemImpl;
import com.ultracommerce.core.catalog.domain.SkuProductOptionValueXref;
import com.ultracommerce.openadmin.dto.ClassMetadata;
import com.ultracommerce.openadmin.dto.CriteriaTransferObject;
import com.ultracommerce.openadmin.dto.DynamicResultSet;
import com.ultracommerce.openadmin.dto.Entity;
import com.ultracommerce.openadmin.dto.FieldMetadata;
import com.ultracommerce.openadmin.dto.FilterAndSortCriteria;
import com.ultracommerce.openadmin.dto.ForeignKey;
import com.ultracommerce.openadmin.dto.MergedPropertyType;
import com.ultracommerce.openadmin.dto.PersistencePackage;
import com.ultracommerce.openadmin.dto.PersistencePerspective;
import com.ultracommerce.openadmin.dto.Property;
import com.ultracommerce.openadmin.server.dao.DynamicEntityDao;
import com.ultracommerce.openadmin.server.service.handler.CustomPersistenceHandlerAdapter;
import com.ultracommerce.openadmin.server.service.persistence.module.InspectHelper;
import com.ultracommerce.openadmin.server.service.persistence.module.RecordHelper;
import com.ultracommerce.openadmin.server.service.persistence.module.criteria.FilterMapping;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * Overridden to provide the option values field on the SkuBundleItem list
 *
 * @author Phillip Verheyden (phillipuniverse)
 */
@Component("ucSkuBundleItemCustomPersistenceHandler")
public class SkuBundleItemCustomPersistenceHandler extends CustomPersistenceHandlerAdapter {

    private static final Log LOG = LogFactory.getLog(SkuBundleItemCustomPersistenceHandler.class);

    @Resource(name = "ucSkuCustomPersistenceHandler")
    protected SkuCustomPersistenceHandler skuPersistenceHandler;

    @Override
    public Boolean canHandleInspect(PersistencePackage persistencePackage) {
        return canHandle(persistencePackage);
    }

    @Override
    public Boolean canHandleFetch(PersistencePackage persistencePackage) {
        return canHandle(persistencePackage);
    }

    protected Boolean canHandle(PersistencePackage persistencePackage) {
        String className = persistencePackage.getCeilingEntityFullyQualifiedClassname();
        try {
            return SkuBundleItem.class.isAssignableFrom(Class.forName(className));
        } catch (ClassNotFoundException e) {
            LOG.warn("Could not find the class " + className + ", skipping the inventory custom persistence handler");
            return false;
        }
    }

    @Override
    public DynamicResultSet inspect(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, InspectHelper helper) throws ServiceException {
        try {
            PersistencePerspective persistencePerspective = persistencePackage.getPersistencePerspective();
            Map<MergedPropertyType, Map<String, FieldMetadata>> allMergedProperties = new HashMap<MergedPropertyType, Map<String, FieldMetadata>>();

            //retrieve the default properties for Inventory
            Map<String, FieldMetadata> properties = helper.getSimpleMergedProperties(SkuBundleItem.class.getName(), persistencePerspective);

            //add in the consolidated product options field
            FieldMetadata options = skuPersistenceHandler.createConsolidatedOptionField(SkuBundleItemImpl.class);
            options.setOrder(3);
            properties.put(SkuCustomPersistenceHandler.CONSOLIDATED_PRODUCT_OPTIONS_FIELD_NAME, options);

            allMergedProperties.put(MergedPropertyType.PRIMARY, properties);
            Class<?>[] entityClasses = dynamicEntityDao.getAllPolymorphicEntitiesFromCeiling(SkuBundleItem.class);
            ClassMetadata mergedMetadata = helper.buildClassMetadata(entityClasses, persistencePackage, allMergedProperties);

            return new DynamicResultSet(mergedMetadata, null, null);

        } catch (Exception e) {
            String className = persistencePackage.getCeilingEntityFullyQualifiedClassname();
            ServiceException ex = new ServiceException("Unable to retrieve inspection results for " + className, e);
            LOG.error("Unable to retrieve inspection results for " + className, ex);
            throw ex;
        }
    }

    @Override
    public DynamicResultSet fetch(PersistencePackage persistencePackage, CriteriaTransferObject cto, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        String ceilingEntityFullyQualifiedClassname = persistencePackage.getCeilingEntityFullyQualifiedClassname();
        try {
            PersistencePerspective persistencePerspective = persistencePackage.getPersistencePerspective();
            ForeignKey foreignKey = (ForeignKey) persistencePerspective.getPersistencePerspectiveItems().get(PersistencePerspectiveItemType.FOREIGNKEY);

            // sort it
            if (foreignKey != null && foreignKey.getSortField() != null) {
                FilterAndSortCriteria sortCriteria = cto.get(foreignKey.getSortField());
                sortCriteria.setSortAscending(foreignKey.getSortAscending());

            }
            //get the default properties from Inventory and its subclasses
            Map<String, FieldMetadata> originalProps = helper.getSimpleMergedProperties(SkuBundleItem.class.getName(), persistencePerspective);

            //Pull back the Inventory based on the criteria from the client
            List<FilterMapping> filterMappings = helper.getFilterMappings(persistencePerspective, cto, ceilingEntityFullyQualifiedClassname, originalProps);

            //attach the product option criteria
            skuPersistenceHandler.applyProductOptionValueCriteria(filterMappings, cto, persistencePackage, "sku");

            List<Serializable> records = helper.getPersistentRecords(persistencePackage.getCeilingEntityFullyQualifiedClassname(), filterMappings, cto.getFirstResult(), cto.getMaxResults());
            //Convert Skus into the client-side Entity representation
            Entity[] payload = helper.getRecords(originalProps, records);

            int totalRecords = helper.getTotalRecords(persistencePackage.getCeilingEntityFullyQualifiedClassname(), filterMappings);

            for (int i = 0; i < payload.length; i++) {
                Entity entity = payload[i];
                SkuBundleItem bundleItem = (SkuBundleItem) records.get(i);
                Sku bundleSku = bundleItem.getSku();
                entity.findProperty("sku").setDisplayValue(bundleSku.getName());
                List<ProductOptionValue> productOptionValues = new ArrayList<>();
                for(SkuProductOptionValueXref skuProductOptionValueXref : bundleSku.getProductOptionValueXrefs()) {
                    productOptionValues.add(skuProductOptionValueXref.getProductOptionValue());
                }
                Property optionsProperty = skuPersistenceHandler.getConsolidatedOptionProperty(productOptionValues);
                entity.addProperty(optionsProperty);
            }

            return new DynamicResultSet(null, payload, totalRecords);
        } catch (Exception e) {
            throw new ServiceException("There was a problem fetching inventory. See server logs for more details", e);
        }
    }

}
