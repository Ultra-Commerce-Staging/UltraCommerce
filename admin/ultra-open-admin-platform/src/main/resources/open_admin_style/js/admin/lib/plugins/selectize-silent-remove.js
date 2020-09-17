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
Selectize.define('silent_remove', function(options){
    var self = this;

    // defang the internal search method when remove has been clicked
    this.on('item_remove', function(){
        this.plugin_silent_remove_in_remove = true;
    });

    this.search = (function() {
        var original = self.search;
        return function() {
            if (typeof(this.plugin_silent_remove_in_remove) != "undefined") {
                // re-enable normal searching
                delete this.plugin_silent_remove_in_remove;
                return {
                    items: {},
                    query: [],
                    tokens: []
                };
            }
            else {
                return original.apply(this, arguments);
            }
        };
    })();
});
