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
 * Define whether a new collection member is searched for or constructed.
 *
 * @author Jeff Fischer
 */
public enum AddMethodType {

    //Collection member created from a normal add form dialog
    PERSIST,

    //Persist an empty entity first before presenting the user with an "edit" stage form. It's up to the implementation
    //to bypass validation, account for non-nullable db fields, etc... (if applicable). This is useful for collection
    //members that themselves have collections.
    PERSIST_EMPTY,

    //Collection member chosen from a lookup listgrid dialog
    LOOKUP,

    //Collection member chosen from a selectize dropdown
    SELECTIZE_LOOKUP,

    //Collection members can be updated via a lookup listgrid dialog
    LOOKUP_FOR_UPDATE

}
