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
package com.ultracommerce.common.persistence;

import com.ultracommerce.common.copy.MultiTenantCopyContext;
import com.ultracommerce.common.extension.ExtensionHandler;
import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.extension.ExtensionResultStatusType;

/**
 * Allow other modules to contribute to the duplication lifecycle
 *
 * @author Jeff Fischer
 */
public interface EntityDuplicatorExtensionHandler extends ExtensionHandler {

    /**
     * Confirm whether or not duplication operation is allowed
     *
     * @param entity
     * @param resultHolder
     * @return
     */
    ExtensionResultStatusType validateDuplicate(Object entity, ExtensionResultHolder<Boolean> resultHolder);

    /**
     * Perform any required context and state setup before commencing with the duplication
     *
     * @param entity
     * @param resultHolder
     * @return
     */
    ExtensionResultStatusType setupDuplicate(Object entity, ExtensionResultHolder<MultiTenantCopyContext> resultHolder);

    /**
     * Tear down any expired context and state used during the duplication
     *
     * @return
     */
    ExtensionResultStatusType tearDownDuplicate();
}
