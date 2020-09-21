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

    UCAdmin.translations = {
        getProperties : function($container) {
            return {
                ceilingEntity : $container.find('.translation-ceiling').text(),
                entityId      : $container.find('.translation-id').text(),
                propertyName  : $container.find('.translation-property').text(),
                isRte         : $container.find('.translation-rte').text()
            };
        }
    };

})(jQuery, UCAdmin);

$(document).ready(function() {

    $('body').on('click', 'a.show-translations', function() {
        if ($(this).data('disabled') != 'disabled') {
            UCAdmin.showLinkAsModal($(this).attr('href'));
        }
    	return false;
    });

    $('body').on('click', 'button.translation-submit-button', function() {
	    var $form = $(this).closest('.modal').find('.modal-body form');

		UC.ajax({
			url: $form.attr('action'),
			type: "POST",
			data: UCAdmin.serialize($form)
		}, function(data) {
			//prevenSubmit is a hidden field whose value is sent from the translations controller, when there are errors
			//"data" contains the new copy of the form, validated
			var preventSubmit = $(data).find(".modal-body").find("input[name=preventSubmit]").attr("value");
			if (!preventSubmit){
              UCAdmin.listGrid.replaceRelatedCollection($(data));
              UCAdmin.hideCurrentModal();
			}else{
				var errorMapString = $(data).find(".modal-body").find("input[name=jsErrorMapString]").attr("value");
				var errorMap = JSON.parse(errorMapString);
				//clear all previous error spans
				$form.find("span.error").remove();
				errorMap.forEach(function(item){
					for (var name in item){
						var value = item[name];
						//"name" and "value" are the field name and the internationalized error message, respectively
						//now, build the jQuery search string to pinpoint the field's box, and add the error message right after the label
						var searchString = ".field-group[id=field-" + name + "]";
						$form.find(searchString).append("<span class='fieldError error errors'>" + value + "</span>");
					}
				});
			}
	    });

		return false;
    });

    $('body').on('click', 'button.translation-grid-add', function() {
        var $container = $(this).closest('.listgrid-container');
        var baseUrl = $container.find('.listgrid-header-wrapper table').data('currenturl');
        var properties = UCAdmin.translations.getProperties($container);

        UCAdmin.showLinkAsModal(baseUrl + '/add?' + $.param(properties));
        return false;
    });

    $('body').on('click', 'button.translation-grid-update', function() {
        var $container = $(this).closest('.listgrid-container');
        var $selectedRows = $container.find('table tr.selected');
        var baseUrl = $container.find('.listgrid-header-wrapper table').data('currenturl');
        var rowFields = UCAdmin.listGrid.getRowFields($selectedRows);
        var properties = UCAdmin.translations.getProperties($container);

        properties.localeCode = rowFields.localeCode;
        properties.translationId = rowFields.id;

        UCAdmin.showLinkAsModal(baseUrl + '/update?' + $.param(properties));
        return false;
    });

    $('body').on('click', 'button.translation-grid-remove', function() {
        var $container = $(this).closest('.listgrid-container');
        var $selectedRows = $container.find('table tr.selected');
        var baseUrl = $container.find('.listgrid-header-wrapper table').data('currenturl');
        var rowFields = UCAdmin.listGrid.getRowFields($selectedRows);
        var properties = UCAdmin.translations.getProperties($container);

        properties.translationId = rowFields.id;

        UC.ajax({
            url: baseUrl + '/delete',
            data: properties,
            type: "POST"
        }, function(data) {
            UCAdmin.listGrid.replaceRelatedCollection($(data));
        });

        return false;
    });

    $('body').on('click', 'button.translation-revert-button', function() {
        var $form = UCAdmin.getForm($(this));
        var currentAction = $form.attr('action');
        var revertUrl = currentAction + '/revert';

        UCAdmin.showActionSpinner($(this).closest('.entity-form-actions'));
		
		UC.ajax({
	        url: revertUrl,
	        type: "POST",
	        data: $form.serializeArray(),
	        complete: UCAdmin.hideActionSpinner
		}, function(data) {
			if (data.errors == undefined){
				//if there are no errors, hide the form and return to the translation listgrid
				UCAdmin.hideCurrentModal();
		        UCAdmin.listGrid.replaceRelatedCollection($(data));
			}else{
				var $modal = $form.closest('.modal');
				//add the error
				$modal.find('.modal-body .errors').text(data.errors.message);
				//the empty-section-tabs hide the error section, removing so the error is shown
				$modal.find('.empty-section-tabs').removeClass('empty-section-tabs');
			}
	    });
		
		return false;
    });
    
});
