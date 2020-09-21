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
package com.ultracommerce.cms.web.controller;

import com.ultracommerce.cms.page.service.PageService;
import com.ultracommerce.common.RequestDTO;
import com.ultracommerce.common.TimeDTO;
import com.ultracommerce.common.file.service.UltraFileUtils;
import com.ultracommerce.common.page.dto.PageDTO;
import com.ultracommerce.common.time.SystemTime;
import com.ultracommerce.common.web.BaseUrlResolver;
import com.ultracommerce.common.web.UltraRequestContext;
import com.ultracommerce.common.web.resource.UltraContextUtil;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class serves up the Robots.txt file.    The default contents can be overridden by 
 * adding a Page named "/robots.txt" in the UC admin or DB. 
 *
 * @author bpolster
 */
public class UltraRobotsController {

    public static final String UC_RULE_MAP_PARAM = "ucRuleMap";

    // The following attribute is set in UltraProcessURLFilter
    public static final String REQUEST_DTO = "ucRequestDTO";

    @Resource(name = "ucBaseUrlResolver")
    private BaseUrlResolver baseUrlResolver;

    @Resource(name = "ucPageService")
    private PageService pageService;
    
    @Resource(name = "ucUltraContextUtil")
    protected UltraContextUtil ucContextUtil;

    public String getRobotsFile(HttpServletRequest request, HttpServletResponse response) {
    	ucContextUtil.establishThinRequestContext();
    	
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        PageDTO page = pageService.findPageByURI(null,
                "/robots.txt", buildMvelParameters(request), isSecure(request));

        if (page != null && page.getPageFields().containsKey("body")) {
            String body = (String) page.getPageFields().get("body");
            body = body.replace("${siteBaseUrl}", baseUrlResolver.getSiteBaseUrl());
            return body;
        } else {
            return getDefaultRobotsTxt();
        }
    }
    
    public boolean isSecure(HttpServletRequest request) {
        boolean secure = false;
        if (request != null) {
             secure = ("HTTPS".equalsIgnoreCase(request.getScheme()) || request.isSecure());
        }
        return secure;
    }

    /**
     * Used to produce a working but simple robots.txt.    Can be overridden in code or by defining a page
     * managed in the Ultra CMS named  "/robots.txt"
     * 
     * @return
     */
    protected String getDefaultRobotsTxt() {
        StringBuilder sb = new StringBuilder();
        sb.append("# Using default Ultra Commerce robots.txt file").append("\n");
        sb.append("User-agent: *").append("\n");
        sb.append("Disallow:").append("\n");
        String fileLoc = UltraFileUtils.appendUnixPaths(baseUrlResolver.getSiteBaseUrl(), "/sitemap.xml.gz");

        sb.append("Sitemap:").append(fileLoc);
        return sb.toString();
    }

    /**
    *
    * @param request
    * @return
    */
    private Map<String, Object> buildMvelParameters(HttpServletRequest request) {
        TimeDTO timeDto = new TimeDTO(SystemTime.asCalendar());
        RequestDTO requestDto = (RequestDTO) request.getAttribute(REQUEST_DTO);

        Map<String, Object> mvelParameters = new HashMap<String, Object>();
        mvelParameters.put("time", timeDto);
        mvelParameters.put("request", requestDto);

        Map<String, Object> ucRuleMap = (Map<String, Object>) request.getAttribute(UC_RULE_MAP_PARAM);
        if (ucRuleMap != null) {
            for (String mapKey : ucRuleMap.keySet()) {
                mvelParameters.put(mapKey, ucRuleMap.get(mapKey));
            }
        }

        return mvelParameters;
    }
}
