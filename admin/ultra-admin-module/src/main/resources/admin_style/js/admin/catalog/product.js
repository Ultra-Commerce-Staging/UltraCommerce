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
(function($, UCAdmin) {
    
    // Add utility functions for products to the UCAdmin object
    UCAdmin.product = {
            
        refreshSkusGrid : function($container, listGridUrl) {
            UC.ajax({
                url : listGridUrl,
                type : "GET"
            }, function(data) {
                UCAdmin.listGrid.replaceRelatedCollection($(data));
            });
        }

    };

    $.each(['com.ultracommerce.core.catalog.domain.Product'], function(idx, clazz) {

        UCAdmin.addDependentFieldHandler(
            clazz,
            '#field-hasPromotionMessageOverrides',
            '.listgrid-container#promotionMessageOverrides',
            'true'
        );
    });
    
})(jQuery, UCAdmin);

$(document).ready(function() {
    
    $('body').on('click', 'button.generate-skus', function() {
        var $skuGenerationButton = $(this);
        var $container = $skuGenerationButton.closest('div.listgrid-container');

        $skuGenerationButton.prop("disabled", true);
        UCAdmin.listGrid.showAlert($container, "Generating SKUs...", {
            alertType: 'save-alert',
            clearOtherAlerts: true,
            autoClose: 8000
        });
        
        UC.ajax({
            url : $(this).data('actionurl'),
            type : "GET"
        }, function(data) {
            var alertType = data.error ? 'error-alert' : data.skusGenerated > 0 ? 'save-alert' : 'error-alert';
            
            UCAdmin.listGrid.showAlert($container, data.message, {
                alertType: alertType,
                clearOtherAlerts: true,
                autoClose: 5000
            });
            
            if (data.skusGenerated > 0) {
                UCAdmin.product.refreshSkusGrid($container, data.listGridUrl);
            }

            $skuGenerationButton.prop("disabled", false);
        });
        
        return false;
    });

    $('body').on('change', "input[name=\"fields['defaultCategory'].value\"]", function(event, fields) {
        var $fieldBox = $(event.target).closest('.field-group');
        var $prefix = $fieldBox.find('input.generated-url-prefix');

        if (!$prefix.length) {
            $prefix = $fieldBox.append($('<input>', {
                'type'  : "hidden",
                'class' : "generated-url-prefix"
            })).find('input.generated-url-prefix');
        }

        $prefix.val(fields['url']);
    });
});
