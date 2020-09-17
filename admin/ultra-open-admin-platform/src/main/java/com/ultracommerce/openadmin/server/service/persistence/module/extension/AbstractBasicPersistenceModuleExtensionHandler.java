/*
 * #%L
 * UltraCommerce Framework Web
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
package com.ultracommerce.openadmin.server.service.persistence.module.extension;

import com.ultracommerce.common.extension.AbstractExtensionHandler;
import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.openadmin.dto.FieldMetadata;
import com.ultracommerce.openadmin.dto.PersistencePackage;
import com.ultracommerce.openadmin.server.service.persistence.module.BasicPersistenceModule;

import java.io.Serializable;
import java.util.Map;

/**
 * Convenience implementation of interface so that subclasses do not have to implement uninteresting methods
 *
 * @see com.ultracommerce.openadmin.server.service.persistence.module.extension.BasicPersistenceModuleExtensionHandler
 * @author Jeff Fischer
 */
public class AbstractBasicPersistenceModuleExtensionHandler extends AbstractExtensionHandler implements BasicPersistenceModuleExtensionHandler {

    @Override
    public ExtensionResultStatusType rebalanceForUpdate(BasicPersistenceModule basicPersistenceModule,
                                                        PersistencePackage persistencePackage, Serializable instance,
                                                        Map<String, FieldMetadata> mergedProperties,
                                                        Object primaryKey, ExtensionResultHolder<Serializable> resultHolder) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType rebalanceForAdd(BasicPersistenceModule basicPersistenceModule,
                                                     PersistencePackage persistencePackage, Serializable instance,
                                                     Map<String, FieldMetadata> mergedProperties,
                                                     ExtensionResultHolder<Serializable> resultHolder) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }
}
