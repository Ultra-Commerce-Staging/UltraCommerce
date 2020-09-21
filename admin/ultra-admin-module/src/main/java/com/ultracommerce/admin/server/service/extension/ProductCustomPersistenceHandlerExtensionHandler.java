/*
 * #%L
 * UltraCommerce Framework
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
package com.ultracommerce.admin.server.service.extension;

import com.ultracommerce.common.exception.ServiceException;
import com.ultracommerce.common.extension.ExtensionHandler;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.core.catalog.domain.Product;
import com.ultracommerce.openadmin.dto.CriteriaTransferObject;
import com.ultracommerce.openadmin.dto.FieldMetadata;
import com.ultracommerce.openadmin.dto.PersistencePackage;

import java.util.Map;

/**
 * Extension handler for {@link com.ultracommerce.admin.server.service.handler.ProductCustomPersistenceHandler}
 *
 * @author Jeff Fischer
 */
public interface ProductCustomPersistenceHandlerExtensionHandler extends ExtensionHandler {

    /**
     * Perform any special handling for the parent category of a product during a product add
     *
     * @param product
     * @return
     */
    ExtensionResultStatusType manageParentCategoryForAdd(PersistencePackage persistencePackage, Product product) throws ServiceException;

    /**
     * Perform any special handling for the parent category of a product during a product update
     *
     * @param product
     * @return
     */
    ExtensionResultStatusType manageParentCategoryForUpdate(PersistencePackage persistencePackage, Product product) throws ServiceException;

    /**
     * Perform any special handling for the remove
     *
     * @param product
     * @return
     */
    ExtensionResultStatusType manageRemove(PersistencePackage persistencePackage, Product product) throws ServiceException;

    /**
     * Perform any special metadata handling for the inspect
     *
     * @param metadata
     * @return
     */
    ExtensionResultStatusType manageInspect(Map<String, FieldMetadata> metadata) throws ServiceException;

    /**
     * Perform any special handling for field on the product
     *
     * @param persistencePackage
     * @param product
     * @return
     * @throws ServiceException
     */
    ExtensionResultStatusType manageFields(PersistencePackage persistencePackage, Product product) throws ServiceException;


    /**
     * Setup any special state to influence the fetch results
     *
     * @return
     * @throws ServiceException
     */
    ExtensionResultStatusType initiateFetchState() throws ServiceException;

    /**
     * Cleanup any special state started by {@link #initiateFetchState()}
     *
     * @return
     * @throws ServiceException
     */
    ExtensionResultStatusType endFetchState() throws ServiceException;

    /**
     * Perform any special filtering for the fetch
     *
     * @param cto
     * @return
     * @throws ServiceException
     */
    ExtensionResultStatusType manageAdditionalFilterMappings(CriteriaTransferObject cto) throws ServiceException;
}
