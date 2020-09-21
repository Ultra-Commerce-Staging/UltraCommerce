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
$(document).ready(function() {
	
	$('body').on('click', '.modal-footer button.btn-primary', function() {
		$(this).closest('div.modal').find('form').submit();
	});
	
	$('body').on('shown', '.modal', function () {
		$("html").css({ overflow: 'hidden' });
	});
	
	$('body').on('hide', '.modal', function () {
		$("html").css({ overflow: 'auto' });
		UCAdmin.entityForm.visitedTabs.removeModalTabs();
	});
	
	$('body').on('click', 'a.modal-view', function() {
    	UCAdmin.showLinkAsModal($(this).attr('href'));
		return false;
	});
	
});
