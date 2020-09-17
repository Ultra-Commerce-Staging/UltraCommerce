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
package com.ultracommerce.openadmin.server.service.persistence;

import com.ultracommerce.common.exception.ServiceException;
import com.ultracommerce.common.persistence.TargetModeType;
import com.ultracommerce.openadmin.dto.ClassMetadata;
import com.ultracommerce.openadmin.dto.CriteriaTransferObject;
import com.ultracommerce.openadmin.dto.FieldMetadata;
import com.ultracommerce.openadmin.dto.MergedPropertyType;
import com.ultracommerce.openadmin.dto.PersistencePackage;
import com.ultracommerce.openadmin.dto.PersistencePerspective;
import com.ultracommerce.openadmin.server.dao.DynamicEntityDao;
import com.ultracommerce.openadmin.server.service.handler.CustomPersistenceHandler;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public interface PersistenceManager {

    Class<?>[] getAllPolymorphicEntitiesFromCeiling(Class<?> ceilingClass);

    Class<?>[] getPolymorphicEntities(String ceilingEntityFullyQualifiedClassname) throws ClassNotFoundException;

    Map<String, FieldMetadata> getSimpleMergedProperties(String entityName, PersistencePerspective persistencePerspective) throws ClassNotFoundException, SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException;

    ClassMetadata buildClassMetadata(Class<?>[] entities, PersistencePackage persistencePackage, Map<MergedPropertyType, Map<String, FieldMetadata>> mergedProperties) throws IllegalArgumentException;

    PersistenceResponse inspect(PersistencePackage persistencePackage) throws ServiceException, ClassNotFoundException;

    PersistenceResponse fetch(PersistencePackage persistencePackage, CriteriaTransferObject cto) throws ServiceException;

    PersistenceResponse add(PersistencePackage persistencePackage) throws ServiceException;

    PersistenceResponse update(PersistencePackage persistencePackage) throws ServiceException;

    PersistenceResponse remove(PersistencePackage persistencePackage) throws ServiceException;

    void configureDynamicEntityDao(Class entityClass, TargetModeType targetMode);

    /**
     * This method produces a {@link DynamicEntityDao} with a ucPU-based standardEntityManager
     *  using the passed in {@link TargetModeType}
     */
    void configureDefaultDynamicEntityDao(TargetModeType targetModeType);

    DynamicEntityDao getDynamicEntityDao();

    void setDynamicEntityDao(DynamicEntityDao dynamicEntityDao);

    TargetModeType getTargetMode();

    void setTargetMode(TargetModeType targetMode);

    List<CustomPersistenceHandler> getCustomPersistenceHandlers();

    void setCustomPersistenceHandlers(List<CustomPersistenceHandler> customPersistenceHandlers);

    Class<?>[] getUpDownInheritance(Class<?> testClass);

    Class<?>[] getUpDownInheritance(String testClassname) throws ClassNotFoundException;

    String getIdPropertyName(String entityClass);

}
