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
package com.ultracommerce.openadmin.web.form.entity;



public class DefaultEntityFormActions {
    
    public static final EntityFormAction SAVE = new EntityFormAction(EntityFormAction.SAVE)
        .withButtonType("submit")
        .withButtonClass("submit-button primary")
        .withDisplayText("Save");

    public static final EntityFormAction DELETE = new EntityFormAction(EntityFormAction.DELETE)
        .withButtonClass("delete-button")
        .withDisplayText("Delete");
    
    public static final EntityFormAction PREVIEW = new EntityFormAction(EntityFormAction.PREVIEW)
        .withButtonClass("preview-button")
        .withDisplayText("Preview");
    
    public static final EntityFormAction DUPLICATE = new EntityFormAction(EntityFormAction.DUPLICATE)
            .withButtonClass("duplicate-button")
            .withDisplayText("Duplicate");

}
