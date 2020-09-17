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
package com.ultracommerce.openadmin.server.service;

import com.ultracommerce.common.exception.ServiceException;
import com.ultracommerce.openadmin.dto.Entity;
import com.ultracommerce.openadmin.server.service.persistence.module.RecordHelper;


/**
 * Thrown when an entity fails validation when attempting to populate an actual Hibernate entity based on its DTO
 * representation
 *
 * @see {@link RecordHelper#createPopulatedInstance(java.io.Serializable, Entity, java.util.Map, Boolean)}
 * @author Phillip Verheyden (phillipuniverse)
 */
public class ValidationException extends ServiceException {

    private static final long serialVersionUID = 1L;
    
    protected Entity entity;

    public ValidationException(Entity entity) {
        super();
        setEntity(entity);
    }
    
    public ValidationException(Entity entity, String message) {
        super(message);
        setEntity(entity);
    }
    
    public void setEntity(Entity entity) {
        this.entity = entity;
    }
    
    public Entity getEntity() {
        return entity;
    }
    
}
