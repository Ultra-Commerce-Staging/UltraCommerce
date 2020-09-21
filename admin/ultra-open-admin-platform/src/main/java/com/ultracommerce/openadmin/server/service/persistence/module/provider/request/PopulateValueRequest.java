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
package com.ultracommerce.openadmin.server.service.persistence.module.provider.request;

import com.ultracommerce.openadmin.dto.BasicFieldMetadata;
import com.ultracommerce.openadmin.dto.Property;
import com.ultracommerce.openadmin.server.service.persistence.PersistenceManager;
import com.ultracommerce.openadmin.server.service.persistence.module.DataFormatProvider;
import com.ultracommerce.openadmin.server.service.persistence.module.FieldManager;

/**
 * Contains the requested value, instance and support classes.
 *
 * @author Jeff Fischer
 */
public class PopulateValueRequest {

    private final Boolean setId;
    private final FieldManager fieldManager;
    private final Property property;
    private final BasicFieldMetadata metadata;
    private final Class<?> returnType;
    private final String requestedValue;
    private final PersistenceManager persistenceManager;
    private final DataFormatProvider dataFormatProvider;
    private final Boolean isPreAdd;

    public PopulateValueRequest(Boolean setId, FieldManager fieldManager, Property property, BasicFieldMetadata metadata, Class<?> returnType, String requestedValue, PersistenceManager persistenceManager, DataFormatProvider dataFormatProvider, Boolean isPreAdd) {
        this.setId = setId;
        this.fieldManager = fieldManager;
        this.property = property;
        this.metadata = metadata;
        this.returnType = returnType;
        this.requestedValue = requestedValue;
        this.persistenceManager = persistenceManager;
        this.dataFormatProvider = dataFormatProvider;
        this.isPreAdd = isPreAdd;
    }

    public Boolean getSetId() {
        return setId != null && setId;
    }

    public FieldManager getFieldManager() {
        return fieldManager;
    }

    public Property getProperty() {
        return property;
    }

    public BasicFieldMetadata getMetadata() {
        return metadata;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public String getRequestedValue() {
        return requestedValue;
    }

    public PersistenceManager getPersistenceManager() {
        return persistenceManager;
    }

    public DataFormatProvider getDataFormatProvider() {
        return dataFormatProvider;
    }

    public Boolean getPreAdd() {
        return isPreAdd != null && isPreAdd;
    }
}
