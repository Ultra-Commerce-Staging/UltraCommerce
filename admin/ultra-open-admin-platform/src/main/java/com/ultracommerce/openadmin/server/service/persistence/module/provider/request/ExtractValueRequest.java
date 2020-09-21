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
import com.ultracommerce.openadmin.server.service.persistence.module.RecordHelper;

import java.io.Serializable;
import java.util.List;

/**
 * Contains the requested value, property and support classes.
 *
 * @author Jeff Fischer
 */
public class ExtractValueRequest {

    protected final List<Property> props;
    protected final FieldManager fieldManager;
    protected final BasicFieldMetadata metadata;
    protected final Object requestedValue;
    protected String displayVal;
    protected final PersistenceManager persistenceManager;
    protected final RecordHelper recordHelper;
    protected final Serializable entity;
    protected final String[] customCriteria;

    public ExtractValueRequest(List<Property> props, FieldManager fieldManager, BasicFieldMetadata metadata, 
            Object requestedValue, String displayVal, PersistenceManager persistenceManager, 
            RecordHelper recordHelper, Serializable entity, String[] customCriteria) {
        this.props = props;
        this.fieldManager = fieldManager;
        this.metadata = metadata;
        this.requestedValue = requestedValue;
        this.displayVal = displayVal;
        this.persistenceManager = persistenceManager;
        this.recordHelper = recordHelper;
        this.entity = entity;
        this.customCriteria = customCriteria;
    }

    public List<Property> getProps() {
        return props;
    }

    public FieldManager getFieldManager() {
        return fieldManager;
    }

    public BasicFieldMetadata getMetadata() {
        return metadata;
    }

    public Object getRequestedValue() {
        return requestedValue;
    }

    public String getDisplayVal() {
        return displayVal;
    }

    public PersistenceManager getPersistenceManager() {
        return persistenceManager;
    }

    public DataFormatProvider getDataFormatProvider() {
        return recordHelper;
    }
    
    public RecordHelper getRecordHelper() {
        return recordHelper;
    }

    public void setDisplayVal(String displayVal) {
        this.displayVal = displayVal;
    }
    
    public Serializable getEntity() {
        return entity;
    }

    public String[] getCustomCriteria() {
        return customCriteria;
    }
}
