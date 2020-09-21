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

import com.ultracommerce.common.persistence.TargetModeType;
import com.ultracommerce.openadmin.dto.PersistencePackage;
import org.springframework.stereotype.Service;

/**
 * @author Jeff Fischer
 */
@Service("ucPersistenceThreadManager")
public class PersistenceThreadManager {

    public <T, G extends Throwable> T operation(TargetModeType targetModeType, Persistable<T, G> persistable) throws G {
        try {
            PersistenceManagerFactory.startPersistenceManager(targetModeType);
            return persistable.execute();
        } finally {
            PersistenceManagerFactory.endPersistenceManager();
        }
    }

    public <T, G extends Throwable> T operation(TargetModeType targetModeType, PersistencePackage pkg, Persistable<T, G> persistable) throws G {
        try {
            String pkgEntityClassName = pkg.getCeilingEntityFullyQualifiedClassname();
            PersistenceManagerFactory.startPersistenceManager(pkgEntityClassName, targetModeType);
            return persistable.execute();
        } finally {
            PersistenceManagerFactory.endPersistenceManager();
        }
    }
}
