/*
 * #%L
 * UltraCommerce Admin Module
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
$(document).ready(function() {
    
    $('body').on('click', 'button.show-category-list-view', function() {
        $('.category-list').show();
        $('.category-tree').hide();
        $('button.show-category-list-view').addClass('active');
        $('button.show-category-tree-view').removeClass('active');
    });
    
    $('body').on('click', 'button.show-category-tree-view', function() {
        $('.category-list').hide();
        $('.category-tree').show();
        $('button.show-category-list-view').removeClass('active');
        $('button.show-category-tree-view').addClass('active');
    });

    $('body').on('click', 'button.show-category-tree-view-modal', function() {
        var currentUrl = window.location.href;
        currentUrl = UCAdmin.history.getUrlWithParameter('tree', true, null, currentUrl);
        UCAdmin.showLinkAsModal(currentUrl);
        return false;
    });
    
});
