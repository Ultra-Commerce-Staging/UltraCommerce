/*
 * #%L
 * UltraCommerce Open Admin Platform
 * %%
 * Copyright (C) 2009 - 2017 Ultra Commerce
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

import com.ultracommerce.openadmin.dto.CriteriaTransferObject;
import com.ultracommerce.openadmin.dto.FieldMetadata;
import com.ultracommerce.openadmin.dto.PersistencePackage;
import com.ultracommerce.openadmin.server.service.persistence.module.criteria.FilterMapping;

import java.util.List;
import java.util.Map;

/**
 * POJO for useful params dictating {@link FetchWrapper} behavior
 *
 * @author Jeff Fischer
 */
public class FetchRequest {

    protected PersistencePackage persistencePackage;
    protected CriteriaTransferObject cto;
    protected String ceilingEntity;
    protected List<FilterMapping> filterMappings;

    public FetchRequest(PersistencePackage persistencePackage,
                        CriteriaTransferObject cto,
                        String ceilingEntity,
                        List<FilterMapping> filterMappings) {
        this.persistencePackage = persistencePackage;
        this.cto = cto;
        this.ceilingEntity = ceilingEntity;
        this.filterMappings = filterMappings;
    }

    /**
     * Object describing the overall fetch request
     *
     * @return
     */
    public PersistencePackage getPersistencePackage() {
        return persistencePackage;
    }

    public void setPersistencePackage(PersistencePackage persistencePackage) {
        this.persistencePackage = persistencePackage;
    }

    /**
     * Object describing query filters and sorting. Generally passed from the client UI.
     *
     * @return
     */
    public CriteriaTransferObject getCto() {
        return cto;
    }

    public void setCto(CriteriaTransferObject cto) {
        this.cto = cto;
    }

    /**
     * The top entity in the inheritance hierarchy.
     *
     * @return
     */
    public String getCeilingEntity() {
        return ceilingEntity;
    }

    public void setCeilingEntity(String ceilingEntity) {
        this.ceilingEntity = ceilingEntity;
    }

    /**
     * The various fetch query filter restrictions
     *
     * @return
     */
    public List<FilterMapping> getFilterMappings() {
        return filterMappings;
    }

    public void setFilterMappings(List<FilterMapping> filterMappings) {
        this.filterMappings = filterMappings;
    }

}
