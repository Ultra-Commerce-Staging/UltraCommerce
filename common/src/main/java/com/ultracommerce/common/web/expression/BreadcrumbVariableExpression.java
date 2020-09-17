/*
 * #%L
 * UltraCommerce Common Libraries
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
/**
 * 
 */
package com.ultracommerce.common.web.expression;

import com.ultracommerce.common.breadcrumbs.dto.BreadcrumbDTO;
import com.ultracommerce.common.breadcrumbs.service.BreadcrumbService;
import com.ultracommerce.common.web.UltraRequestContext;
import com.ultracommerce.presentation.condition.ConditionalOnTemplating;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * 
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
@Component("ucBreadcrumbVariableExpression")
@ConditionalOnTemplating
public class BreadcrumbVariableExpression implements UltraVariableExpression {

    @Resource(name = "ucBreadcrumbService")
    protected BreadcrumbService breadcrumbService;
    
    @Override
    public String getName() {
        return "breadcrumbs";
    }

    public List<BreadcrumbDTO> getBreadcrumbs() {
        String baseUrl = getBaseUrl();
        Map<String, String[]> params = getParams();
        return breadcrumbService.buildBreadcrumbDTOs(baseUrl, params);
    }

    protected String getBaseUrl() {
        UltraRequestContext brc = UltraRequestContext.getUltraRequestContext();
        
        if (brc != null) {
            return brc.getRequest().getRequestURI();
        }
        return "";
    }
    
    protected Map<String, String[]> getParams() {
        Map<String, String[]> paramMap = new HashMap<>();
        UltraRequestContext brc = UltraRequestContext.getUltraRequestContext();
        
        if (brc != null) {
            paramMap = UltraRequestContext.getRequestParameterMap();
            if (paramMap != null) {
                paramMap = new HashMap<>(paramMap);
            }
        }
        return paramMap;
    }
}
