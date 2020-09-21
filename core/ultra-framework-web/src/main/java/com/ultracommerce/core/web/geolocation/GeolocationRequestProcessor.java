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
package com.ultracommerce.core.web.geolocation;

import org.apache.commons.lang3.StringUtils;
import com.ultracommerce.common.util.UCRequestUtils;
import com.ultracommerce.common.web.AbstractUltraWebRequestProcessor;
import com.ultracommerce.common.web.UltraRequestContext;
import com.ultracommerce.core.geolocation.GeolocationDTO;
import com.ultracommerce.core.geolocation.GeolocationService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

@Component("ucGeolocationRequestProcessor")
public class GeolocationRequestProcessor extends AbstractUltraWebRequestProcessor {

    public static final String FORWARD_HEADER = "X-FORWARDED-FOR";
    public static final String GEOLOCATON_ATTRIBUTE_NAME = "_ucGeolocationAttribute";
    protected static final String UC_RULE_MAP_PARAM = "ucRuleMap";

    @Resource(name="ucGeolocationService")
    protected GeolocationService geolocationService;

    @Resource
    protected Environment env;

    @Override
    public void process(WebRequest request) {
        if (isGeolocationEnabled()) {
            if (request instanceof ServletWebRequest) {
                ServletWebRequest servletWebRequest = (ServletWebRequest) request;
                GeolocationDTO location = (GeolocationDTO) UCRequestUtils.getSessionAttributeIfOk(request, GEOLOCATON_ATTRIBUTE_NAME);
                if (location == null) {
                    String ipAddress = getIPAddress(servletWebRequest);
                    location = geolocationService.getLocationData(ipAddress);
                    UCRequestUtils.setSessionAttributeIfOk(request, GEOLOCATON_ATTRIBUTE_NAME, location);
                }
                UltraRequestContext.getUltraRequestContext().getAdditionalProperties().put(GEOLOCATON_ATTRIBUTE_NAME, location);

                Map<String, Object> ruleMap = getRuleMapFromRequest(request);
                ruleMap.put(GEOLOCATON_ATTRIBUTE_NAME, location);
                request.setAttribute(UC_RULE_MAP_PARAM, ruleMap, WebRequest.SCOPE_REQUEST);
            }
        }
    }

    protected boolean isGeolocationEnabled() {
        return env.getProperty("geolocation.api.enabled", Boolean.class, false);
    }

    protected String getIPAddress(ServletWebRequest request) {
        String ipAddress = request.getHeader(FORWARD_HEADER);
        if (StringUtils.isEmpty(ipAddress)) {
            ipAddress = request.getRequest().getRemoteAddr();
        }
        return ipAddress;
    }

    protected Map<String,Object> getRuleMapFromRequest(WebRequest request) {
        Map<String,Object> ruleMap = (Map<String, Object>) request.getAttribute(UC_RULE_MAP_PARAM, WebRequest.SCOPE_REQUEST);
        if (ruleMap == null) {
            ruleMap = new HashMap<>();
        }
        return ruleMap;
    }
}
