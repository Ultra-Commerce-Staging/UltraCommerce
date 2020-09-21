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
(function($, UCAdmin) {
    
    var adminFormats = {
        ucDateFormat : "yy.mm.dd",
        ucTimeFormat : "HH:mm:ss",
        displayDateFormat : 'mm/dd/yy',
        displayTimeFormat : 'HH:mm:ss',
        displayDateDelimiter : '/'
    };

    // Add utility functions for dates to the UCAdmin object
    UCAdmin.dates = {
        /**
         * This function should be called for any element that wants to be a rulebuilder
         */
        initialize : function($element) {
            // Set the value of this datepicker to be the appropriately formatted one
            $element.val($element.val().indexOf(adminFormats.displayDateDelimiter)>=0?this.getDisplayDate(this.getServerDate($element.val())):this.getDisplayDate($element.val()));
            
            // Make it a date-time picker
            $element.datetimepicker({
                showSecond: true,
                timeFormat: 'HH:mm:ss'
            });
        },
        
        /**
         * serverDate should be in the Ultra datetime format, "yyyy.MM.dd HH:mm:ss" (Java spec)
         * returns the display format, "mm/dd/yy HH:mm:ss" (JavaScript spec)
         */
        getDisplayDate : function(serverDate) {
            var display = UC.dates.getDisplayDate(serverDate, adminFormats);
            return display === null || display === undefined || display.constructor !== {}.constructor ? display : display.displayDate + " " + display.displayTime;
        },
        
        /**
         * displayDate should be in the format "mm/dd/yy HH:mm" (JavaScript spec)
         * returns the server-expected format, "yyyy.MM.dd HH:mm:ss Z" (Java spec)
         */
        getServerDate : function(displayDate) {
            var server = UC.dates.getServerDate(displayDate, adminFormats);
            return server === null || server === undefined || server.constructor !== {}.constructor ? server : server.serverDate + " " + server.serverTime;
        },
        
        initializationHandler : function($container) {
            $container.find('.datepicker').each(function(index, element) {
                UCAdmin.dates.initialize($(element));
            });
        },
        
        postValidationSubmitHandler : function($form) {
            $form.find('.datepicker').each(function(index, element) {
                var $this = $(this);
                if ($this.closest('.entityFormTab').data('initialized') != 'true') {
                    UCAdmin.dates.initialize($this);
                }

                var name = $this.attr('name');

                var $hiddenClone = $('<input>', {
                    type: 'hidden',
                    name: name,
                    value: UCAdmin.dates.getServerDate($this.val()),
                    'class': 'datepicker-clone'
                });
              
                $(this).data('previous-name', name).removeAttr('name').after($hiddenClone);
            });
        },
        
        postFormSubmitHandler : function($form, data) {
            $form.find('.datepicker-clone').each(function(index, element) {
                var $clone = $(this);
                var inputName = $clone.attr('name');
                
                var $originalInput = $clone.siblings('.datepicker');
                
                $originalInput.attr('name', inputName);
                $originalInput.val($clone.val());
                $originalInput.attr('type', 'text');
                $clone.remove();
                
                UCAdmin.dates.initialize($originalInput);
            });
        }
    };
    
    UCAdmin.addInitializationHandler(UCAdmin.dates.initializationHandler);
    UCAdmin.addPostValidationSubmitHandler(UCAdmin.dates.postValidationSubmitHandler);
    UCAdmin.addPostFormSubmitHandler(UCAdmin.dates.postFormSubmitHandler);
            
})(jQuery, UCAdmin);

$(document).ready(function() {
  
    $('body').on('click', 'div.datepicker-container', function(event) {
        $(this).find('input').datepicker('show');
    });
    
});