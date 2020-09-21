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
package com.ultracommerce.openadmin.server.service.persistence.module;

import com.ultracommerce.common.exception.ServiceException;
import com.ultracommerce.common.presentation.client.OperationType;
import com.ultracommerce.openadmin.dto.CriteriaTransferObject;
import com.ultracommerce.openadmin.dto.DynamicResultSet;
import com.ultracommerce.openadmin.dto.Entity;
import com.ultracommerce.openadmin.dto.FieldMetadata;
import com.ultracommerce.openadmin.dto.MergedPropertyType;
import com.ultracommerce.openadmin.dto.PersistencePackage;
import com.ultracommerce.openadmin.dto.Property;
import com.ultracommerce.openadmin.server.service.persistence.PersistenceManager;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author jfischer
 *
 */
public interface PersistenceModule {

    public boolean isCompatible(OperationType operationType);
    
    public Entity add(PersistencePackage persistencePackage) throws ServiceException;
    
    public void updateMergedProperties(PersistencePackage persistencePackage, Map<MergedPropertyType, Map<String, FieldMetadata>> allMergedProperties) throws ServiceException;
    
    public void extractProperties(Class<?>[] inheritanceLine, Map<MergedPropertyType, Map<String, FieldMetadata>> mergedProperties, List<Property> properties);
    
    public Entity update(PersistencePackage persistencePackage) throws ServiceException;
    
    public void remove(PersistencePackage persistencePackage) throws ServiceException;
    
    public DynamicResultSet fetch(PersistencePackage persistencePackage, CriteriaTransferObject cto) throws ServiceException;
    
    public void setPersistenceManager(PersistenceManager persistenceManager);
    
}
