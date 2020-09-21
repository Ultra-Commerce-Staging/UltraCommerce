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

import com.ultracommerce.common.exception.ServiceException;
import com.ultracommerce.common.presentation.client.OperationType;
import com.ultracommerce.common.util.dao.DynamicDaoHelperImpl;
import com.ultracommerce.openadmin.dto.ClassMetadata;
import com.ultracommerce.openadmin.dto.CriteriaTransferObject;
import com.ultracommerce.openadmin.dto.DynamicResultSet;
import com.ultracommerce.openadmin.dto.Entity;
import com.ultracommerce.openadmin.dto.FieldMetadata;
import com.ultracommerce.openadmin.dto.MergedPropertyType;
import com.ultracommerce.openadmin.dto.PersistencePackage;
import com.ultracommerce.openadmin.dto.PersistencePerspective;
import com.ultracommerce.openadmin.server.dao.DynamicEntityDao;
import com.ultracommerce.openadmin.server.service.persistence.PersistenceManager;
import com.ultracommerce.openadmin.server.service.persistence.module.InspectHelper;
import com.ultracommerce.openadmin.server.service.persistence.module.RecordHelper;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

/**
 * Convenience class for those {@link com.ultracommerce.openadmin.server.service.handler.CustomPersistenceHandler} implementations
 * that do not wish to implement all the methods of the interface.
 *
 * @author Jeff Fischer
 */
public class CustomPersistenceHandlerAdapter implements CustomPersistenceHandler {

    @Override
    public Boolean canHandleInspect(PersistencePackage persistencePackage) {
        return false;
    }

    @Override
    public Boolean canHandleFetch(PersistencePackage persistencePackage) {
        return false;
    }

    @Override
    public Boolean canHandleAdd(PersistencePackage persistencePackage) {
        return false;
    }

    @Override
    public Boolean canHandleRemove(PersistencePackage persistencePackage) {
        return false;
    }

    @Override
    public Boolean canHandleUpdate(PersistencePackage persistencePackage) {
        return false;
    }

    @Override
    public DynamicResultSet inspect(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, InspectHelper helper) throws ServiceException {
        throw new ServiceException("Inspect not supported");
    }

    @Override
    public DynamicResultSet fetch(PersistencePackage persistencePackage, CriteriaTransferObject cto, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        throw new ServiceException("Fetch not supported");
    }

    @Override
    public Entity add(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        throw new ServiceException("Add not supported");
    }

    @Override
    public void remove(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
       throw new ServiceException("Remove not supported");
    }

    @Override
    public Entity update(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        throw new ServiceException("Update not supported");
    }

    @Override
    public Boolean willHandleSecurity(PersistencePackage persistencePackage) {
        return false;
    }

    @Override
    public int getOrder() {
        return CustomPersistenceHandler.DEFAULT_ORDER;
    }

    /**
     * This is a helper method that can be invoked as a first step in a custom inspect phase
     */
    protected Map<String, FieldMetadata> getMetadata(PersistencePackage persistencePackage, InspectHelper helper)
            throws ServiceException {
        String entityName = persistencePackage.getCeilingEntityFullyQualifiedClassname();
        PersistencePerspective perspective = persistencePackage.getPersistencePerspective();
        return helper.getSimpleMergedProperties(entityName, perspective);
    }

    /**
     * This is a helper method that can be invoked as the last step in a custom inspect phase. It will assemble the
     * appropriate DynamicResultSet from the given parameters.
     */
    protected DynamicResultSet getResultSet(PersistencePackage persistencePackage, InspectHelper helper,
            Map<String, FieldMetadata> metadata) throws ServiceException {
        String entityName = persistencePackage.getCeilingEntityFullyQualifiedClassname();
        try {
            if (helper instanceof PersistenceManager) {
                Class<?>[] entities = ((PersistenceManager) helper).getPolymorphicEntities(entityName);
                Map<MergedPropertyType, Map<String, FieldMetadata>> allMergedProperties =
                        new HashMap<MergedPropertyType, Map<String, FieldMetadata>>();
                allMergedProperties.put(MergedPropertyType.PRIMARY, metadata);
                ClassMetadata mergedMetadata = helper.buildClassMetadata(entities, persistencePackage, allMergedProperties);
                DynamicResultSet results = new DynamicResultSet(mergedMetadata);
                return results;
            }
        } catch (ClassNotFoundException e) {
            throw new ServiceException(e);
        }
        return new DynamicResultSet();
    }

    protected String[] getPolymorphicClasses(Class<?> clazz, EntityManager em, boolean useCache) {
        DynamicDaoHelperImpl helper = new DynamicDaoHelperImpl();
        Class<?>[] classes = helper.getAllPolymorphicEntitiesFromCeiling(clazz, true, useCache);
        String[] result = new String[classes.length];
        for (int i = 0; i < classes.length; i++) {
            result[i] = classes[i].getName();
        }
        return result;
    }

    protected Class getClassForName(String ceilingEntityFullyQualifiedClassname) {
        try {
            return Class.forName(ceilingEntityFullyQualifiedClassname);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    protected boolean isAssignableFrom(String ceilingEntityFullyQualifiedClassname, Class targetClass) {
        try {
            Class<?> clazz = Class.forName(ceilingEntityFullyQualifiedClassname);
            return targetClass.isAssignableFrom(clazz);
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    protected boolean meetsCustomCriteria(PersistencePackage pkg, String[] customCriteria) {
        if (pkg.getCustomCriteria() == null) {
            return false;
        }
        for (String criteria : pkg.getCustomCriteria()) {
            if (criteria != null) {
                for (String search : customCriteria) {
                    if (criteria.equals(search)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    protected boolean isBasicOperation(PersistencePackage pkg) {
        return pkg.getPersistencePerspective().getOperationTypes().getAddType().equals(OperationType.BASIC);
    }

    protected boolean isMapOperation(PersistencePackage pkg) {
        return pkg.getPersistencePerspective().getOperationTypes().getAddType().equals(OperationType.MAP);
    }

    protected boolean isAdornedListOperation(PersistencePackage pkg) {
        return pkg.getPersistencePerspective().getOperationTypes().getAddType().equals(OperationType.ADORNEDTARGETLIST);
    }
}
