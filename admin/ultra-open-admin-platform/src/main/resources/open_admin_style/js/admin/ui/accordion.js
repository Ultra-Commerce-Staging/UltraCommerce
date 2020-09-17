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
;(function($, window, undefined) {
    'use strict';

    $.fn.ultraAccordion = function(options) {

        var initNav = function() {
            $.each($('.uc-accordion li .content li'), function(key, value) {
                var module = $(value).parents('.uc-module');

                if ($(value).hasClass('active')) {
                    module.addClass('active');
                }
            });

            $.each($('.uc-module'), function(key, value) {
                if ($(value).hasClass('active')) {
                    $(value).children('.content').show();
                } else {
                    $(value).children('.content').hide();
                }
            });
        };

        // DRY up the logic used to determine if the event logic should execute.
        var hasHover = function(accordion) {
            return accordion.hasClass('hover') && !Modernizr.touch
        };

        $(document).on('click.fndtn', '.uc-accordion li .title', function() {
            var li = $(this).closest('li'), p = li.parent(), content = $(this).next();

            if (!hasHover(p)) {
                li.toggleClass('active');
                content.toggle();
            }
        });

        initNav();
    };

})(jQuery, this);
