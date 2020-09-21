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

import org.apache.commons.lang3.StringUtils;
import com.ultracommerce.common.exception.ServiceException;
import com.ultracommerce.common.presentation.client.OperationType;
import com.ultracommerce.common.presentation.client.PersistencePerspectiveItemType;
import com.ultracommerce.common.sandbox.SandBoxHelper;
import com.ultracommerce.core.catalog.dao.ProductOptionDao;
import com.ultracommerce.core.catalog.domain.ProductOption;
import com.ultracommerce.openadmin.dto.CriteriaTransferObject;
import com.ultracommerce.openadmin.dto.DynamicResultSet;
import com.ultracommerce.openadmin.dto.Entity;
import com.ultracommerce.openadmin.dto.FieldMetadata;
import com.ultracommerce.openadmin.dto.PersistencePackage;
import com.ultracommerce.openadmin.dto.PersistencePerspective;
import com.ultracommerce.openadmin.dto.Property;
import com.ultracommerce.openadmin.server.dao.DynamicEntityDao;
import com.ultracommerce.openadmin.server.service.handler.CustomPersistenceHandlerAdapter;
import com.ultracommerce.openadmin.server.service.persistence.module.RecordHelper;
import org.springframework.stereotype.Component;

import java.util.Map;

import javax.annotation.Resource;

/**
 * 
 *  This class is used to prevent updates to Product Options if "Use in Sku generation" is true but no "Allowed Values" 
 *  have been set.
 * 
 *  @author Nathan Moore (nathanmoore)
 *  
 */
@Component("ucProductOptionsCustomPersistenceHandler")
public class ProductOptionsCustomPersistenceHandler extends CustomPersistenceHandlerAdapter {

    @Resource(name="ucProductOptionDao")
    protected ProductOptionDao productOptionDao;

    @Resource(name="ucSandBoxHelper")
    protected SandBoxHelper sandBoxHelper;

    @Override
    public Boolean canHandleUpdate(PersistencePackage persistencePackage) {
        String ceilingEntityFullyQualifiedClassname = persistencePackage.getCeilingEntityFullyQualifiedClassname();
        try {
            Class testClass = Class.forName(ceilingEntityFullyQualifiedClassname);
            return ProductOption.class.isAssignableFrom(testClass);
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public Boolean canHandleFetch(PersistencePackage persistencePackage) {
        return canHandleUpdate(persistencePackage) &&
                !persistencePackage.getPersistencePerspectiveItems().containsKey(PersistencePerspectiveItemType.ADORNEDTARGETLIST);
    }

    @Override
    public DynamicResultSet fetch(PersistencePackage persistencePackage, CriteriaTransferObject cto, DynamicEntityDao
            dynamicEntityDao, RecordHelper helper) throws ServiceException {
        DynamicResultSet response = helper.getCompatibleModule(OperationType.BASIC).fetch(persistencePackage, cto);
        for (Entity entity : response.getRecords()) {
            Property prop = entity.findProperty("useInSkuGeneration");
            if (prop != null && StringUtils.isEmpty(prop.getValue())) {
                prop.setValue("true");
            }
        }
        return response;
    }

    @Override
    public Entity update(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        Entity entity = persistencePackage.getEntity();
        try {
            PersistencePerspective persistencePerspective = persistencePackage.getPersistencePerspective();

            Map<String, FieldMetadata> adminProperties = helper.getSimpleMergedProperties(ProductOption.class.getName(), persistencePerspective);

            Object primaryKey = helper.getPrimaryKey(entity, adminProperties);
            ProductOption adminInstance = (ProductOption) dynamicEntityDao.retrieve(Class.forName(entity.getType()[0]), primaryKey);

            adminInstance = (ProductOption) helper.createPopulatedInstance(adminInstance, entity, adminProperties, false);
            // validate "Use in Sku generation"
            if (needsAllowedValue(adminInstance)) {
                String errorMessage = "Must add at least 1 Allowed Value when Product Option is used in Sku generation";
                entity.addValidationError("useInSkuGeneration", errorMessage);
                return entity;
            }
            
            adminInstance = (ProductOption) dynamicEntityDao.merge(adminInstance);
            return helper.getRecord(adminProperties, adminInstance, null, null);

        } catch (Exception e) {
            throw new ServiceException("Unable to update entity for " + entity.getType()[0], e);
        }
    }

    /**
     * This function checks if a Product Option's "Use in sku generation" field is set to true 
     * without any allowed values set. This is what we are trying to prevent from happening. 
     * If "Use in sku generation" is true and there are no Allowed Values, the functions returns true.
     * 
     * @param adminInstance: The Product Option being validated
     * @return boolean: Default is false. Returns whether the Product Option needs any Allowed Values .
     */
    protected boolean needsAllowedValue(ProductOption adminInstance) {
        // validate "Use in Sku generation"
        // Check during a save (not in a replay operation) if "use in sku generation" is true
        // and that there are allowed values set
        if (adminInstance.getUseInSkuGeneration() && !sandBoxHelper.isReplayOperation()) {
            Long count = productOptionDao.countAllowedValuesForProductOptionById(adminInstance.getId());
            return count.equals(0L);
        }
        // Else either there are allowed values and/or "use in sku generation" is false
        return false;
    }
}
