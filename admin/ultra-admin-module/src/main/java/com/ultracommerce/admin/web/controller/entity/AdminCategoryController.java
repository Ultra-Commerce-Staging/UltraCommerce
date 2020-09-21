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
package com.ultracommerce.admin.web.controller.entity;

import com.ultracommerce.common.presentation.client.SupportedFieldType;
import com.ultracommerce.core.catalog.domain.Category;
import com.ultracommerce.core.catalog.service.CatalogService;
import com.ultracommerce.openadmin.web.controller.entity.AdminBasicEntityController;
import com.ultracommerce.openadmin.web.form.entity.EntityForm;
import com.ultracommerce.openadmin.web.form.entity.Field;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Handles admin operations for the {@link Category} entity.
 * 
 * @author Andre Azzolini (apazzolini)
 */
@Controller("ucAdminCategoryController")
@RequestMapping("/" + AdminCategoryController.SECTION_KEY)
public class AdminCategoryController extends AdminBasicEntityController {
    
    public static final String SECTION_KEY = "category";
    
    @Resource(name = "ucCatalogService")
    protected CatalogService catalogService;

    @Override
    protected String getSectionKey(Map<String, String> pathVars) {
        //allow external links to work for ToOne items
        if (super.getSectionKey(pathVars) != null) {
            return super.getSectionKey(pathVars);
        }
        return SECTION_KEY;
    }



    @Override
    protected void modifyAddEntityForm(EntityForm ef, Map<String, String> pathVars) {
        Field overrideGeneratedUrl = ef.findField("overrideGeneratedUrl");
        overrideGeneratedUrl.setFieldType(SupportedFieldType.HIDDEN.toString().toLowerCase());
        boolean overriddenUrl = Boolean.parseBoolean(overrideGeneratedUrl.getValue());
        Field fullUrl = ef.findField("url");
        fullUrl.withAttribute("overriddenUrl", overriddenUrl)
            .withAttribute("sourceField", "name")
            .withAttribute("toggleField", "overrideGeneratedUrl")
            .withFieldType(SupportedFieldType.GENERATED_URL.toString().toLowerCase());
    }
}
