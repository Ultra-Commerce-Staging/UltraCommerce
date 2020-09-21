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
package com.ultracommerce.admin.web.controller.extension;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.common.util.UCSystemProperty;
import com.ultracommerce.core.catalog.domain.Product;
import com.ultracommerce.core.catalog.domain.Sku;
import com.ultracommerce.core.catalog.service.CatalogService;
import com.ultracommerce.openadmin.server.service.JSCompatibilityHelper;
import com.ultracommerce.openadmin.web.controller.AbstractAdminTranslationControllerExtensionHandler;
import com.ultracommerce.openadmin.web.controller.AdminTranslationControllerExtensionManager;
import com.ultracommerce.openadmin.web.form.TranslationForm;
import org.springframework.stereotype.Component;

/**
 * @author Andre Azzolini (apazzolini)
 */
@Component("ucAdminProductTranslationExtensionHandler")
public class AdminProductTranslationExtensionHandler extends AbstractAdminTranslationControllerExtensionHandler {
    
    @Resource(name = "ucCatalogService")
    protected CatalogService catalogService;

    @Resource(name = "ucAdminTranslationControllerExtensionManager")
    protected AdminTranslationControllerExtensionManager extensionManager;

    @PostConstruct
    public void init() {
        if (isEnabled()) {
            extensionManager.registerHandler(this);
        }
    }

    protected boolean getTranslationEnabled() {
        return UCSystemProperty.resolveBooleanSystemProperty("i18n.translation.enabled");
    }

    /**
     * If we are trying to translate a field on Product that starts with "defaultSku.", we really want to associate the
     * translation with Sku, its associated id, and the property name without "defaultSku."
     */
    @Override
    public ExtensionResultStatusType applyTransformation(TranslationForm form) {
        if (getTranslationEnabled()) {
            String defaultSkuPrefix = "defaultSku.";
            String unencodedPropertyName = JSCompatibilityHelper.unencode(form.getPropertyName());
            if (form.getCeilingEntity().equals(Product.class.getName()) && unencodedPropertyName.startsWith(defaultSkuPrefix)) {
                Product p = catalogService.findProductById(Long.parseLong(form.getEntityId()));
                form.setCeilingEntity(Sku.class.getName());
                form.setEntityId(String.valueOf(p.getDefaultSku().getId()));
                form.setPropertyName(unencodedPropertyName.substring(defaultSkuPrefix.length()));
            }
        }
        
        return ExtensionResultStatusType.HANDLED;
    }
    
}
