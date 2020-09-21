/*
 * #%L
 * UltraCommerce Open Admin Platform
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

package com.ultracommerce.openadmin.server.service.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.config.domain.SystemProperty;
import com.ultracommerce.common.config.service.SystemPropertiesService;
import com.ultracommerce.common.exception.ServiceException;
import com.ultracommerce.openadmin.dto.Entity;
import com.ultracommerce.openadmin.dto.FieldMetadata;
import com.ultracommerce.openadmin.dto.PersistencePackage;
import com.ultracommerce.openadmin.dto.PersistencePerspective;
import com.ultracommerce.openadmin.server.dao.DynamicEntityDao;
import com.ultracommerce.openadmin.server.service.persistence.module.RecordHelper;
import org.springframework.stereotype.Component;

import java.util.Map;

import javax.annotation.Resource;

/**
 * Custom persistence handler for SystemProperty to ensure that the value is validated against the type appropriately.
 * 
 * @author Andre Azzolini (apazzolini)
 */
@Component("ucSystemPropertyCustomPersistenceHandler")
public class SystemPropertyCustomPersistenceHandler extends CustomPersistenceHandlerAdapter {
    private final Log LOG = LogFactory.getLog(SystemPropertyCustomPersistenceHandler.class);

    @Resource(name = "ucSystemPropertiesService")
    protected SystemPropertiesService spService;

    protected Boolean classMatches(PersistencePackage persistencePackage) {
        String ceilingEntityFullyQualifiedClassname = persistencePackage.getCeilingEntityFullyQualifiedClassname();
        return SystemProperty.class.getName().equals(ceilingEntityFullyQualifiedClassname);
    }

    @Override
    public Boolean canHandleAdd(PersistencePackage persistencePackage) {
        return classMatches(persistencePackage);
    }

    @Override
    public Boolean canHandleUpdate(PersistencePackage persistencePackage) {
        return classMatches(persistencePackage);
    }

    @Override
    public Entity update(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) 
            throws ServiceException {
        Entity entity = persistencePackage.getEntity();
        try {
            // Get an instance of SystemProperty with the updated values from the form
            PersistencePerspective persistencePerspective = persistencePackage.getPersistencePerspective();
            Map<String, FieldMetadata> adminProperties = helper.getSimpleMergedProperties(SystemProperty.class.getName(), persistencePerspective);
            Object primaryKey = helper.getPrimaryKey(entity, adminProperties);
            SystemProperty adminInstance = (SystemProperty) dynamicEntityDao.retrieve(Class.forName(entity.getType()[0]), primaryKey);
            adminInstance = (SystemProperty) helper.createPopulatedInstance(adminInstance, entity, adminProperties, false);

            // Verify that the value entered matches up with the type of this property
            Entity errorEntity = validateTypeAndValueCombo(adminInstance);
            if (errorEntity != null) {
                entity.setPropertyValidationErrors(errorEntity.getPropertyValidationErrors());
                return entity;
            }

            adminInstance = (SystemProperty) dynamicEntityDao.merge(adminInstance);

            // Fill out the DTO and add in the product option value properties to it
            return helper.getRecord(adminProperties, adminInstance, null, null);
        } catch (Exception e) {
            throw new ServiceException("Unable to perform fetch for entity: " + SystemProperty.class.getName(), e);
        }
    }

    @Override
    public Entity add(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        Entity entity = persistencePackage.getEntity();
        try {
            // Get an instance of SystemProperty with the updated values from the form
            PersistencePerspective persistencePerspective = persistencePackage.getPersistencePerspective();
            SystemProperty adminInstance = (SystemProperty) Class.forName(entity.getType()[0]).newInstance();
            Map<String, FieldMetadata> adminProperties = helper.getSimpleMergedProperties(SystemProperty.class.getName(), persistencePerspective);
            adminInstance = (SystemProperty) helper.createPopulatedInstance(adminInstance, entity, adminProperties, false);
            
            // Verify that the value entered matches up with the type of this property
            Entity errorEntity = validateTypeAndValueCombo(adminInstance);
            if (errorEntity != null) {
                entity.setPropertyValidationErrors(errorEntity.getPropertyValidationErrors());
                return entity;
            }
            
            adminInstance = (SystemProperty) dynamicEntityDao.merge(adminInstance);

            // Fill out the DTO and add in the product option value properties to it
            return helper.getRecord(adminProperties, adminInstance, null, null);
        } catch (Exception e) {
            throw new ServiceException("Unable to perform fetch for entity: " + SystemProperty.class.getName(), e);
        }
    }
    
    protected Entity validateTypeAndValueCombo(SystemProperty prop) {
        if (!spService.isValueValidForType(prop.getValue(), prop.getPropertyType())) {
            Entity errorEntity = new Entity();
            errorEntity.addValidationError("value", "valueIllegalForPropertyType");
            return errorEntity;
        }

        return null;
    }
}
