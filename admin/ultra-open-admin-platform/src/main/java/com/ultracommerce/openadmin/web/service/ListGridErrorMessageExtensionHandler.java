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
package com.ultracommerce.openadmin.web.service;

import com.ultracommerce.common.extension.ExtensionHandler;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.openadmin.dto.Entity;
import com.ultracommerce.openadmin.web.form.component.ListGridRecord;

/**
 * An extension handler to allow a custom error key or error message to be added to the ListGridRecord.
 * @author kellytisdell
 *
 */
public interface ListGridErrorMessageExtensionHandler extends ExtensionHandler {

    /**
     * Allows the extension handler to determine a custom error message or error message key for the entity. 
     * Implementors should determine if they can handle the entity in question. If not, they should return 
     * ExtensionResultStatusType.NOT_HANDLED.
     * 
     * Otherwise, they should either set the error message or the error key on the ListGrid on the entity. If both 
     * are set the error message will win.
     * 
     * Implementors can use the UltraRequestContext to try to determine Locale, or get a MessageSource, etc.
     * 
     * @param entity
     * @param lgr
     * @return
     */
    public ExtensionResultStatusType determineErrorMessageForEntity(Entity entity, ListGridRecord lgr);

    /**
     * Allows the extension handler to determine a custom status message for the entity.
     *
     * @param entity
     * @param lgr
     * @return
     */
    public ExtensionResultStatusType determineStatusMessageForEntity(Entity entity, ListGridRecord lgr);

}
