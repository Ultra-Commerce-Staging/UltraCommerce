/*
 * #%L
 * UltraCommerce CMS Module
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
package com.ultracommerce.cms.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(PreviewTemplateController.REQUEST_MAPPING_PREFIX + "**")
public class PreviewTemplateController {
    private String templatePathPrefix = "templates";
    public static final String REQUEST_MAPPING_PREFIX = "/preview/";
    
    @RequestMapping
    public String displayPreview(HttpServletRequest httpServletRequest) {
        String requestURIPrefix = httpServletRequest.getContextPath() + REQUEST_MAPPING_PREFIX;
        String templatePath = httpServletRequest.getRequestURI().substring(requestURIPrefix.length() - 1);
        return templatePathPrefix + templatePath;
    }
    
}
