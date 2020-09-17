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
import com.ultracommerce.openadmin.dto.CriteriaTransferObject;
import com.ultracommerce.openadmin.dto.DynamicResultSet;
import com.ultracommerce.openadmin.dto.Entity;
import com.ultracommerce.openadmin.dto.PersistencePackage;

/**
 * Convenience adapter for PersistenceManagerEventHandler implementations that don't need to implement every
 * method of the interface.
 *
 * @author Jeff Fischer
 */
public class PersistenceManagerEventHandlerAdapter implements PersistenceManagerEventHandler {

    @Override
    public PersistenceManagerEventHandlerResponse postAdd(PersistenceManager persistenceManager, Entity entity, PersistencePackage persistencePackage) throws ServiceException {
        return new PersistenceManagerEventHandlerResponse().withEntity(entity).
                withStatus(PersistenceManagerEventHandlerResponse.PersistenceManagerEventHandlerResponseStatus.NOT_HANDLED);
    }

    @Override
    public PersistenceManagerEventHandlerResponse preFetch(PersistenceManager persistenceManager, PersistencePackage persistencePackage, CriteriaTransferObject cto) throws ServiceException {
        return new PersistenceManagerEventHandlerResponse().
                withStatus(PersistenceManagerEventHandlerResponse.PersistenceManagerEventHandlerResponseStatus.NOT_HANDLED);
    }

    @Override
    public PersistenceManagerEventHandlerResponse postFetch(PersistenceManager persistenceManager, DynamicResultSet resultSet, PersistencePackage persistencePackage,
                                      CriteriaTransferObject cto) throws ServiceException {
        return new PersistenceManagerEventHandlerResponse().withDynamicResultSet(resultSet).
                withStatus(PersistenceManagerEventHandlerResponse.PersistenceManagerEventHandlerResponseStatus.NOT_HANDLED);
    }

    @Override
    public PersistenceManagerEventHandlerResponse preAdd(PersistenceManager persistenceManager, PersistencePackage persistencePackage) throws ServiceException {
        return new PersistenceManagerEventHandlerResponse().
                withStatus(PersistenceManagerEventHandlerResponse.PersistenceManagerEventHandlerResponseStatus.NOT_HANDLED);
    }

    @Override
    public PersistenceManagerEventHandlerResponse preUpdate(PersistenceManager persistenceManager, PersistencePackage persistencePackage) throws ServiceException {
        return new PersistenceManagerEventHandlerResponse().
                withStatus(PersistenceManagerEventHandlerResponse.PersistenceManagerEventHandlerResponseStatus.NOT_HANDLED);
    }

    @Override
    public PersistenceManagerEventHandlerResponse postUpdate(PersistenceManager persistenceManager, Entity entity, PersistencePackage persistencePackage) throws ServiceException {
        return new PersistenceManagerEventHandlerResponse().withEntity(entity).
                withStatus(PersistenceManagerEventHandlerResponse.PersistenceManagerEventHandlerResponseStatus.NOT_HANDLED);
    }

    @Override
    public PersistenceManagerEventHandlerResponse preRemove(PersistenceManager persistenceManager, PersistencePackage persistencePackage) throws ServiceException {
        return new PersistenceManagerEventHandlerResponse().
                withStatus(PersistenceManagerEventHandlerResponse.PersistenceManagerEventHandlerResponseStatus.NOT_HANDLED);
    }

    @Override
    public PersistenceManagerEventHandlerResponse postRemove(PersistenceManager persistenceManager, PersistencePackage persistencePackage) throws ServiceException {
        return new PersistenceManagerEventHandlerResponse().
                withStatus(PersistenceManagerEventHandlerResponse.PersistenceManagerEventHandlerResponseStatus.NOT_HANDLED);
    }

    @Override
    public PersistenceManagerEventHandlerResponse postInspect(PersistenceManager persistenceManager, DynamicResultSet resultSet, PersistencePackage
            persistencePackage) throws ServiceException {
        return new PersistenceManagerEventHandlerResponse().withDynamicResultSet(resultSet).
                withStatus(PersistenceManagerEventHandlerResponse.PersistenceManagerEventHandlerResponseStatus.NOT_HANDLED);
    }

    @Override
    public PersistenceManagerEventHandlerResponse preInspect(PersistenceManager persistenceManager, PersistencePackage persistencePackage) throws ServiceException {
        return new PersistenceManagerEventHandlerResponse().
                withStatus(PersistenceManagerEventHandlerResponse.PersistenceManagerEventHandlerResponseStatus.NOT_HANDLED);
    }

    @Override
    public PersistenceManagerEventHandlerResponse processValidationError(PersistenceManager persistenceManager, Entity entity, PersistencePackage persistencePackage) throws ServiceException {
        return new PersistenceManagerEventHandlerResponse().
                        withStatus(PersistenceManagerEventHandlerResponse.PersistenceManagerEventHandlerResponseStatus.NOT_HANDLED);
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

}
