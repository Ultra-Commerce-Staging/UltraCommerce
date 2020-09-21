/*
 * #%L
 * UltraCommerce Open Admin Platform
 * %%
 * Copyright (C) 2009 - 2018 Ultra Commerce
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
package com.ultracommerce.openadmin.server.service.extension;

import com.ultracommerce.common.extension.ExtensionHandler;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.openadmin.dto.CriteriaTransferObject;
import com.ultracommerce.openadmin.server.domain.PersistencePackageRequest;

/**
 * @author Jacob Mitash
 */
public interface CriteriaTransferObjectExtensionHandler extends ExtensionHandler {

    /**
     * Allows modification of the criteria transfer object for a fetch
     * @param request the persistence package request the {@code CriteriaTransferObject} was built from
     * @param cto the criteria transfer object to modify
     */
    ExtensionResultStatusType modifyFetchCriteriaTransferObject(PersistencePackageRequest request,
                                                                CriteriaTransferObject cto);
}
