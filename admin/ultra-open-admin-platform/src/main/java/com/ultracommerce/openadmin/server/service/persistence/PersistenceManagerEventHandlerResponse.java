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

import com.ultracommerce.openadmin.dto.DynamicResultSet;
import com.ultracommerce.openadmin.dto.Entity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jeff Fischer
 */
public class PersistenceManagerEventHandlerResponse {

    protected Entity entity;
    protected DynamicResultSet dynamicResultSet;
    protected PersistenceManagerEventHandlerResponseStatus status;
    protected Map<String, Object> additionalData = new HashMap<String, Object>();

    public enum PersistenceManagerEventHandlerResponseStatus {
        HANDLED,NOT_HANDLED,HANDLED_BREAK
    }

    public PersistenceManagerEventHandlerResponse withEntity(Entity entity) {
        setEntity(entity);
        return this;
    }

    public PersistenceManagerEventHandlerResponse withStatus(PersistenceManagerEventHandlerResponseStatus status) {
        setStatus(status);
        return this;
    }

    public PersistenceManagerEventHandlerResponse withDynamicResultSet(DynamicResultSet dynamicResultSet) {
        setDynamicResultSet(dynamicResultSet);
        return this;
    }

    public PersistenceManagerEventHandlerResponse withAdditionalData(Map<String, Object> additionalData) {
        setAdditionalData(additionalData);
        return this;
    }

    public DynamicResultSet getDynamicResultSet() {
        return dynamicResultSet;
    }

    public void setDynamicResultSet(DynamicResultSet dynamicResultSet) {
        this.dynamicResultSet = dynamicResultSet;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public PersistenceManagerEventHandlerResponseStatus getStatus() {
        return status;
    }

    public void setStatus(PersistenceManagerEventHandlerResponseStatus status) {
        this.status = status;
    }

    public Map<String, Object> getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(Map<String, Object> additionalData) {
        this.additionalData = additionalData;
    }
}
