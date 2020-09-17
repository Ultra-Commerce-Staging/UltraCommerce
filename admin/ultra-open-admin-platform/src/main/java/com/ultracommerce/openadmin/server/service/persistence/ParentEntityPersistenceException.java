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

/**
 * Specific class of PersistenceException used in FieldPersistenceProviders that attempt to perform their own persistence
 * operations in addition to the normal entity field population duties.
 *
 * @author Jeff Fischer
 */
public class ParentEntityPersistenceException extends PersistenceException {

    public ParentEntityPersistenceException(Throwable cause) {
        super(cause);
    }

    public ParentEntityPersistenceException(String message) {
        super(message);
    }

    public ParentEntityPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
