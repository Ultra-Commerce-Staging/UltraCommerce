/*
 * #%L
 * UltraCommerce Common Libraries
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
package com.ultracommerce.common.presentation.client;

/**
 * Created by IntelliJ IDEA.
 * User: jfischer
 * Date: 9/27/11
 * Time: 1:26 PM
 * To change this template use File | Settings | File Templates.
 */
public enum VisibilityEnum {
    HIDDEN_ALL,
    VISIBLE_ALL,
    FORM_HIDDEN,
    GRID_HIDDEN,
    NOT_SPECIFIED,
    /**
     * This will ensure that the field is shown on the the entity form regardless of whether or not this field is
     * actually a member of the entity. Mainly used in {@link CustomPersistenceHandler}s for psuedo-fields that are built
     * manually and you still want user input from (like selecting {@link ProductOption}s to associate to a {@link Sku}
     */
    FORM_EXPLICITLY_SHOWN,
    FORM_EXPLICITLY_HIDDEN
}
