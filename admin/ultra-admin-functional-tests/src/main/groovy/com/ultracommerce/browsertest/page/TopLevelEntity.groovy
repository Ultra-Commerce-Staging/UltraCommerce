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
package com.ultracommerce.browsertest.page

import geb.Page
import geb.navigator.NonEmptyNavigator


/**
 * Represents a top-level entity page like viewing a list of Categories at '/admin/category' or viewing a list of Prodcuts
 * at /admin/product.
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
class TopLevelEntity extends AdminPage {

    static at = { addButton.displayed }
        
    static content = {
        // cannot use multi-page for EntityTypeSelection and EntityFormPage since both require modal waiting
        addButton { $("button.add-main-entity") }
        mainGrid { module ListGridModule, $('.listgrid-container') }
    }
    
    def findModule(String containingText) {
        $('#sideMenu .uc-module .title', text: contains(containingText))
    }
    
    def expandModule(String containingText) {
        findModule(containingText).click()
    }
    
    def findSectionLink(String containingText) {
        $('#sideMenu .uc-module .content a', text: contains(containingText))
    }
    
    def findSectionLink(String containingText, NonEmptyNavigator containingModule) {
        containingModule.closest('.uc-module').find('.content a', text: contains(containingText))
    }
    
}
