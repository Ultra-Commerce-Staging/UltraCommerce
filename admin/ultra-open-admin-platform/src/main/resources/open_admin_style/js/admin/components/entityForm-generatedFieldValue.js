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
    
    UCAdmin.generatedFieldValue = {
            
        registerFieldValueGenerator : function registerFieldValueGenerator($generatedFieldValueContainer) {
            var sourceFieldName = $generatedFieldValueContainer.data('source-field');
            var $sourceField = $generatedFieldValueContainer.closest('form').find('#field-' + sourceFieldName + ' input');

            $sourceField.on('keyup', function() {
                UCAdmin.generatedFieldValue.setGeneratedFieldValue($generatedFieldValueContainer);
            });

            UCAdmin.generatedFieldValue.setGeneratedFieldValue($generatedFieldValueContainer);
        },
        
        setGeneratedFieldValue : function setGeneratedFieldValue($generatedFieldValueContainer) {
            var sourceFieldName = $generatedFieldValueContainer.data('source-field');
            var $sourceField = $generatedFieldValueContainer.closest('form').find('#field-' + sourceFieldName + ' input');
            var $targetField = $generatedFieldValueContainer.find('input.target-field');
            var fieldValueFormatConversionHandlerName = $generatedFieldValueContainer.data('field-value-format-conversion-handler');

            var convertedFieldValue = executeFunctionByName(fieldValueFormatConversionHandlerName, window, $sourceField.val());

            $targetField.val(convertedFieldValue);
            
            $targetField.trigger('generated-fieldValue', [convertedFieldValue]);
        },
        
        unregisterFieldValueGenerator : function unregisterFieldValueGenrator($generatedFieldValueContainer) {
            var sourceFieldName = $generatedFieldValueContainer.data('source-field');
            $generatedFieldValueContainer.closest('form').find('#field-' + sourceFieldName + " input").off('keyup');
        }

    };

    UCAdmin.addInitializationHandler(function($container) {
        $container.find('div.generated-fieldValue-container').each(function(idx, el) {
            if ($(el).data('overridden-fieldValue') != true && !$(el).hasClass('disabledValueGeneration')) {
                UCAdmin.generatedFieldValue.registerFieldValueGenerator($(el));
            }
        });
    });

})(jQuery, UCAdmin);

$('body').on('click', 'a.override-generated-fieldValue', function(event) {
    event.preventDefault();
    var $this = $(this);
    
    var enabled = $this.data('enabled') == true;
	var $container = $this.closest('div.generated-fieldValue-container');
	
	if (enabled) {
	    $container.find('input').attr('readonly', 'readonly');
	    $this.text($this.data('disabled-text'));

        if (!$container.hasClass('disabledValueGeneration')) {
            UCAdmin.generatedFieldValue.registerFieldValueGenerator($container);
        } else {
            UCAdmin.generatedFieldValue.setGeneratedFieldValue($container);
        }
	} else {
	    $container.find('input').removeAttr('readonly');
	    $this.text($this.data('enabled-text'));
	    UCAdmin.generatedFieldValue.unregisterFieldValueGenerator($container);
	}
	
	$container.closest('form').find('#field-' + $container.data('toggle-field') + ' input').val(!enabled);
	
	$this.data('enabled', !enabled);
});

function executeFunctionByName(functionName, context /*, args */) {
    var args = [].slice.call(arguments).splice(2);
    var namespaces = functionName.split(".");
    var func = namespaces.pop();
    for(var i = 0; i < namespaces.length; i++) {
        context = context[namespaces[i]];
    }
    return context[func].apply(this, args);
}
