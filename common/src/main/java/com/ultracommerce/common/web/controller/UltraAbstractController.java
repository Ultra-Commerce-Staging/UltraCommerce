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
package com.ultracommerce.common.web.controller;

import org.apache.commons.lang3.StringUtils;
import com.ultracommerce.common.web.UltraRequestContext;
import com.ultracommerce.common.web.deeplink.DeepLink;
import com.ultracommerce.common.web.deeplink.DeepLinkService;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * An abstract controller that provides convenience methods and resource declarations for its  children
 * 
 * Operations that are shared between all controllers belong here.   To use composition rather than
 * extension, implementors can utilize UltraControllerUtility.
 * 
 * @see UltraControllerUtility
 * 
 * @author apazzolini
 * @author bpolster
 */
public abstract class UltraAbstractController {
    
    
    /**
     * A helper method that returns whether or not the given request was invoked via an AJAX call
     * 
     * @param request
     * @return - whether or not it was an AJAX request
     */
    protected boolean isAjaxRequest(HttpServletRequest request) {
        return UltraControllerUtility.isAjaxRequest(request);       
    }
    
    /**
     * Returns the current servlet context path. This will return a "/" if the application is
     * deployed as root. If it's not deployed as root, it will return the context path BOTH a 
     * leading slash but without a trailing slash.
     * 
     * @param request
     * @return the context path
     */
    protected String getContextPath(HttpServletRequest request) {
        String ctxPath = request.getContextPath();
        if (StringUtils.isBlank(ctxPath)) {
            return "/";
        } else {
            if (ctxPath.charAt(0) != '/') {
                ctxPath = '/' + ctxPath;
            }
            if (ctxPath.charAt(ctxPath.length() - 1) != '/') {
                ctxPath = ctxPath + '/';
            }
            
            return ctxPath;
        }
        
    }
    
    protected <T> void addDeepLink(ModelAndView model, DeepLinkService<T> service, T item) {
        if (service == null) {
            return;
        }

        UltraRequestContext brc = UltraRequestContext.getUltraRequestContext();
        if (brc.getSandBox() != null) {
            List<DeepLink> links = service.getLinks(item);
            if (links.size() == 1) {
                model.addObject("adminDeepLink", links.get(0));
            } else {
                model.addObject("adminDeepLink", links);
            }
        }
    }
    
    /**
     * Typically, controller methods are set to return a String that points to the necessary template path.
     * 
     * However, there may be occasions where the error state for a controller action should instead return
     * JSON instead of a fully rendered template. This convenience method will achieve that by setting the
     * appropriate headers and serializing the given map.
     * 
     * @param response
     * @param responseMap
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    protected String jsonResponse(HttpServletResponse response, Map<?, ?> responseMap) 
            throws JsonGenerationException, JsonMappingException, IOException {
        response.setHeader("Content-Type", "application/json");
        new ObjectMapper().writeValue(response.getWriter(), responseMap);
        return null;
    }

}
