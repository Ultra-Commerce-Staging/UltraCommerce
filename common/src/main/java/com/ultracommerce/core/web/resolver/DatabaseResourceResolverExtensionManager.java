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
package com.ultracommerce.core.web.resolver;

import com.ultracommerce.common.extension.ExtensionManager;
import com.ultracommerce.presentation.condition.ConditionalOnTemplating;
import org.springframework.stereotype.Service;


/**
 * @author Andre Azzolini (apazzolini), bpolster
 */
@Service("ucDatabaseResourceResolverExtensionManager")
@ConditionalOnTemplating
public class DatabaseResourceResolverExtensionManager extends ExtensionManager<DatabaseResourceResolverExtensionHandler> {

    public DatabaseResourceResolverExtensionManager() {
        super(DatabaseResourceResolverExtensionHandler.class);
    }

    /**
     * By default, this manager will allow other handlers to process the method when a handler returns
     * HANDLED.
     */
    @Override
    public boolean continueOnHandled() {
        return false;
    }
}