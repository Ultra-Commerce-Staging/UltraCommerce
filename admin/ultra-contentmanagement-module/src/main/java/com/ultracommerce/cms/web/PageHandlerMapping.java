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

import com.ultracommerce.cms.page.service.PageService;
import com.ultracommerce.cms.web.controller.UltraPageController;
import com.ultracommerce.common.RequestDTO;
import com.ultracommerce.common.TimeDTO;
import com.ultracommerce.common.page.dto.NullPageDTO;
import com.ultracommerce.common.page.dto.PageDTO;
import com.ultracommerce.common.time.SystemTime;
import com.ultracommerce.common.web.UCAbstractHandlerMapping;
import com.ultracommerce.common.web.UltraRequestContext;
import org.springframework.beans.factory.annotation.Value;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * This handler mapping works with the Page entity to determine if a page has been configured for
 * the passed in URL.   
 * 
 * If the URL represents a valid PageUrl, then this mapping returns 
 * 
 * Allows configuration of the controller name to use if a Page was found.
 *
 * @author bpolster
 * @since 2.0
 * @see com.ultracommerce.cms.page.domain.Page
 * @see UltraPageController
 */
public class PageHandlerMapping extends UCAbstractHandlerMapping {
    
    private final String controllerName="ucPageController";
    public static final String UC_RULE_MAP_PARAM = "ucRuleMap";

    // The following attribute is set in UltraProcessURLFilter
    public static final String REQUEST_DTO = "ucRequestDTO";
    
    @Resource(name = "ucPageService")
    private PageService pageService;
    
    public static final String PAGE_ATTRIBUTE_NAME = "UC_PAGE";

    @Value("${request.uri.encoding}")
    public String charEncoding;

    @Override
    protected Object getHandlerInternal(HttpServletRequest request) throws Exception {
        UltraRequestContext context = UltraRequestContext.getUltraRequestContext();
        if (context != null && context.getRequestURIWithoutContext() != null) {
            String requestUri = URLDecoder.decode(context.getRequestURIWithoutContext(), charEncoding);
            
            Boolean internalValidateFindPreviouslySet = false;
            PageDTO page;
            
            try {
                if (!UltraRequestContext.getUltraRequestContext().getInternalValidateFind()) {
                    UltraRequestContext.getUltraRequestContext().setInternalValidateFind(true);
                    internalValidateFindPreviouslySet = true;
                }
                page = pageService.findPageByURI(context.getLocale(), requestUri, buildMvelParameters(request), context.isSecure());

            } finally {
                if (internalValidateFindPreviouslySet) {
                    UltraRequestContext.getUltraRequestContext().setInternalValidateFind(false);
                }
            }

            if (page != null && ! (page instanceof NullPageDTO)) {
                context.getRequest().setAttribute(PAGE_ATTRIBUTE_NAME, page);
                return controllerName;
            }
        }
        return null;
    }
    
     /**
     * MVEL is used to process the content targeting rules.
     *
     *
     * @param request
     * @return
     */
    private Map<String,Object> buildMvelParameters(HttpServletRequest request) {
        TimeDTO timeDto = new TimeDTO(SystemTime.asCalendar());
        RequestDTO requestDto = (RequestDTO) request.getAttribute(REQUEST_DTO);

        Map<String, Object> mvelParameters = new HashMap<String, Object>();
        mvelParameters.put("time", timeDto);
        mvelParameters.put("request", requestDto);

        Map<String,Object> ucRuleMap = (Map<String,Object>) request.getAttribute(UC_RULE_MAP_PARAM);
        if (ucRuleMap != null) {
            for (String mapKey : ucRuleMap.keySet()) {
                mvelParameters.put(mapKey, ucRuleMap.get(mapKey));
            }
        }

        return mvelParameters;
    }
}
