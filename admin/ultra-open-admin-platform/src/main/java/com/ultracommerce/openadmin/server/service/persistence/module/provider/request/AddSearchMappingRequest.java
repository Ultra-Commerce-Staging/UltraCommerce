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

import com.ultracommerce.openadmin.dto.CriteriaTransferObject;
import com.ultracommerce.openadmin.dto.FieldMetadata;
import com.ultracommerce.openadmin.dto.PersistencePerspective;
import com.ultracommerce.openadmin.server.service.persistence.module.DataFormatProvider;
import com.ultracommerce.openadmin.server.service.persistence.module.FieldManager;
import com.ultracommerce.openadmin.server.service.persistence.module.RecordHelper;
import com.ultracommerce.openadmin.server.service.persistence.module.criteria.RestrictionFactory;

import java.util.Map;

/**
 * Contains the requested ctoConverter, cto and support classes.
 *
 * @author Jeff Fischer
 */
public class AddSearchMappingRequest {

    private final PersistencePerspective persistencePerspective;
    private final CriteriaTransferObject requestedCto;
    private final String ceilingEntityFullyQualifiedClassname;
    private final Map<String, FieldMetadata> mergedProperties;
    private final String propertyName;
    private final FieldManager fieldManager;
    private final DataFormatProvider dataFormatProvider;
    private final RecordHelper recordHelper;
    private final RestrictionFactory restrictionFactory;

    public AddSearchMappingRequest(PersistencePerspective persistencePerspective, CriteriaTransferObject
            requestedCto, String ceilingEntityFullyQualifiedClassname, Map<String, FieldMetadata> mergedProperties,
                                   String propertyName, FieldManager fieldManager,
                                   DataFormatProvider dataFormatProvider, RecordHelper recordHelper,
                                   RestrictionFactory restrictionFactory) {
        this.persistencePerspective = persistencePerspective;
        this.requestedCto = requestedCto;
        this.ceilingEntityFullyQualifiedClassname = ceilingEntityFullyQualifiedClassname;
        this.mergedProperties = mergedProperties;
        this.propertyName = propertyName;
        this.fieldManager = fieldManager;
        this.dataFormatProvider = dataFormatProvider;
        this.recordHelper = recordHelper;
        this.restrictionFactory = restrictionFactory;
    }

    public PersistencePerspective getPersistencePerspective() {
        return persistencePerspective;
    }

    public CriteriaTransferObject getRequestedCto() {
        return requestedCto;
    }

    public String getCeilingEntityFullyQualifiedClassname() {
        return ceilingEntityFullyQualifiedClassname;
    }

    public Map<String, FieldMetadata> getMergedProperties() {
        return mergedProperties;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public FieldManager getFieldManager() {
        return fieldManager;
    }
    
    public DataFormatProvider getDataFormatProvider() {
        return dataFormatProvider;
    }
    
    public RecordHelper getRecordHelper() {
        return recordHelper;
    }

    public RestrictionFactory getRestrictionFactory() {
        return restrictionFactory;
    }
}
