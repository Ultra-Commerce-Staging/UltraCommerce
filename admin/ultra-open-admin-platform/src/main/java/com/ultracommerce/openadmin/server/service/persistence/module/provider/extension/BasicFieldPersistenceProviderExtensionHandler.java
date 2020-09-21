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
package com.ultracommerce.openadmin.server.service.persistence.module.provider.extension;

import com.ultracommerce.common.extension.ExtensionHandler;
import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.openadmin.dto.Property;
import com.ultracommerce.openadmin.server.service.persistence.module.provider.request.ExtractValueRequest;

import java.io.Serializable;

/**
 * For internal usage. Allows extending API calls without subclassing the entity.
 *
 * @author Jeff Fischer
 */
public interface BasicFieldPersistenceProviderExtensionHandler extends ExtensionHandler {

    ExtensionResultStatusType transformId(ExtractValueRequest request, Property property, ExtensionResultHolder<Serializable> resultHolder);

    ExtensionResultStatusType transformForeignKey(ExtractValueRequest request, Property property, ExtensionResultHolder<Serializable> resultHolder);

    public static final int DEFAULT_PRIORITY = Integer.MAX_VALUE;

}
