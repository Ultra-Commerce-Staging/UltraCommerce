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
package com.ultracommerce.openadmin.server.service.persistence.extension;

import com.ultracommerce.common.extension.ExtensionHandler;
import com.ultracommerce.common.extension.ExtensionResultStatusType;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Allows for custom conditions to avoid ArchiveStatusPersistenceEventHandler's preFetch functionality
 *
 * @author Chris Kittrell
 */
public interface ArchiveStatusPersistenceEventHandlerExtensionHandler extends ExtensionHandler {

    /**
     * @param entity
     * @return
     */
    ExtensionResultStatusType isArchivable(Class<?> entity, AtomicBoolean isArchivable);

    public static final int DEFAULT_PRIORITY = Integer.MAX_VALUE;
}
