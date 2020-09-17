/*
 * #%L
 * UltraCommerce Framework Web
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
package com.ultracommerce.core.web.controller.catalog;

import org.apache.commons.lang3.StringUtils;
import com.ultracommerce.common.template.TemplateType;
import com.ultracommerce.common.web.UltraRequestContext;
import com.ultracommerce.common.web.TemplateTypeAware;
import com.ultracommerce.common.web.controller.UltraAbstractController;
import com.ultracommerce.common.web.deeplink.DeepLinkService;
import com.ultracommerce.core.catalog.domain.Sku;
import com.ultracommerce.core.web.catalog.SkuHandlerMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class works in combination with the SkuHandlerMapping which finds a category based upon
 * the passed in URL.
 *
 * @author Joshua Skorton (jskorton)
 */
public class UltraSkuController extends UltraAbstractController implements Controller, TemplateTypeAware {
    
    protected String defaultSkuView = "catalog/sku";
    protected static String MODEL_ATTRIBUTE_NAME = "sku";
    protected static String ALL_SKUS_ATTRIBUTE_NAME = "ucAllDisplayedSkus";
    
    @Autowired(required = false)
    @Qualifier("ucSkuDeepLinkService")
    protected DeepLinkService<Sku> deepLinkService;

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView model = new ModelAndView();
        Sku sku = (Sku) request.getAttribute(SkuHandlerMapping.CURRENT_SKU_ATTRIBUTE_NAME);
        assert(sku != null);
        
        model.addObject(MODEL_ATTRIBUTE_NAME, sku);
        Set<Sku> allSkusSet = new HashSet<Sku>();
        allSkusSet.add(sku);
        model.addObject(ALL_SKUS_ATTRIBUTE_NAME, new HashSet<Sku>(allSkusSet));

        addDeepLink(model, deepLinkService, sku);

        if (StringUtils.isNotEmpty(sku.getDisplayTemplate())) {
            model.setViewName(sku.getDisplayTemplate());    
        } else {
            model.setViewName(getDefaultSkuView());
        }
        return model;
    }

    public String getDefaultSkuView() {
        return defaultSkuView;
    }

    public void setDefaultSkuView(String defaultSkuView) {
        this.defaultSkuView = defaultSkuView;
    }
    
    @Override
    public String getExpectedTemplateName(HttpServletRequest request) {
        UltraRequestContext context = UltraRequestContext.getUltraRequestContext();
        if (context != null) {
            Sku sku = (Sku) context.getRequest().getAttribute(SkuHandlerMapping.CURRENT_SKU_ATTRIBUTE_NAME);
            if (sku != null && sku.getDisplayTemplate() != null) {
                return sku.getDisplayTemplate();
            }
        }
        return getDefaultSkuView();
    }

    @Override
    public TemplateType getTemplateType(HttpServletRequest request) {
        return TemplateType.SKU;
    }

}
