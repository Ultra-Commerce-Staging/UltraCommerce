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
import com.ultracommerce.core.catalog.domain.Category;
import com.ultracommerce.openadmin.dto.PersistencePackage;

/**
 * Extension handler for {@link com.ultracommerce.admin.server.service.handler.CategoryCustomPersistenceHandler}
 *
 * @author Jeff Fischer
 */
public interface CategoryCustomPersistenceHandlerExtensionHandler extends ExtensionHandler {

    /**
     * Perform any special handling for the parent category during a category add
     *
     * @param category
     * @return
     */
    ExtensionResultStatusType manageParentCategoryForAdd(PersistencePackage persistencePackage, Category category) throws ServiceException;

    /**
     * Perform any special handling for the parent category during a category update
     *
     * @param category
     * @return
     */
    ExtensionResultStatusType manageParentCategoryForUpdate(PersistencePackage persistencePackage, Category category) throws ServiceException;
}
