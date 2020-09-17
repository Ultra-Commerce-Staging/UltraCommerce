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
package com.ultracommerce.common.i18n.dao;

import com.ultracommerce.common.extension.ExtensionHandler;
import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.extension.ExtensionResultStatusType;

import javax.persistence.EntityManager;


/**
 * @author Andre Azzolini (apazzolini)
 */
public interface TranslationDaoExtensionHandler extends ExtensionHandler {
    
    /**
     * If there is a different id that should be used for a translation lookup instead of the given entityId,
     * the handler should place the result in the {@link ExtensionResultHolder} argument.
     * 
     * @param erh
     * @param em
     * @param clazz
     * @param entityId
     * @return the status of the call to the given extension handler
     */
    public ExtensionResultStatusType overrideRequestedId(ExtensionResultHolder erh, EntityManager em, 
            Class<?> clazz, Long entityId);

}
