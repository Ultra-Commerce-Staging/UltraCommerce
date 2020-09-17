/*
 * #%L
 * UltraCommerce Framework Web
 * %%
 * Copyright (C) 2009 - 2017 Ultra Commerce
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
package com.ultracommerce.core.web.device;

import com.ultracommerce.common.admin.condition.ConditionalOnNotAdmin;
import com.ultracommerce.common.web.UltraRequestContext;
import com.ultracommerce.common.web.device.WebRequestDeviceType;
import com.ultracommerce.common.web.filter.FilterOrdered;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.Ordered;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceResolver;
import org.springframework.mobile.device.DeviceResolverRequestFilter;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This filter adds the device type that made the request to the request context.
 * 
 * @author Nathan Moore (nathanmoore).
 */
@Component("ucDeviceResolverRequestFilter")
@ConditionalOnNotAdmin
public class UltraDeviceResolverRequestFilter extends DeviceResolverRequestFilter implements Ordered {

    @Autowired
    @Qualifier("ucDeviceResolver")
    private DeviceResolver deviceResolver;

    /**
     * Called early in the post security chain to ensure that the device type can be used with greatest flexibility.
     * 
     * @return
     */
    @Override
    public int getOrder() {
        return FilterOrdered.POST_SECURITY_HIGH + 10;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain)
            throws ServletException, IOException {
        resolveDeviceType(request);
        filterChain.doFilter(request, response);
    }
    
    protected void resolveDeviceType(final HttpServletRequest request) {
        final Device device = deviceResolver.resolveDevice(request);
        String type = "UNKNOWN";
        
        if (device != null) {
            if (device.isMobile()) {
                type = "MOBILE";
            } else if (device.isTablet()) {
                type = "TABLET";
            } else if (device.isNormal()) {
                type = "NORMAL";
            }
        }

        UltraRequestContext.getUltraRequestContext().getAdditionalProperties().put(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, WebRequestDeviceType.getInstance(type));
    }
    
}
