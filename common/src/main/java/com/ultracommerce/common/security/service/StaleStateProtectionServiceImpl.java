/*
 * #%L
 * UltraCommerce Common Libraries
 * %%
 * Copyright (C) 2009 - 2013 Ultra Commerce
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
package com.ultracommerce.common.security.service;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.security.RandomGenerator;
import com.ultracommerce.common.util.UCRequestUtils;
import com.ultracommerce.common.util.StringUtil;
import com.ultracommerce.common.util.UrlUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @see StaleStateProtectionService
 * @author jfischer
 */
@Service("ucStaleStateProtectionService")
public class StaleStateProtectionServiceImpl implements StaleStateProtectionService {

    public static final String STATEVERSIONTOKEN = "stateVersionToken";
    public static final String STATECHANGENOTIFICATIONTOKEN = "stateChangeNotificationToken";
    public static final String STATEVERSIONTOKENPARAMETER = "stateVersionToken";
    private static final Log LOG = LogFactory.getLog(StaleStateProtectionServiceImpl.class);

    @Value("${stale.state.protection.enabled:false}")
    protected boolean staleStateProtectionEnabled = false;

    @Override
    public void compareToken(String passedToken) {
        if (staleStateProtectionEnabled) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            if (!getStateVersionToken().equals(passedToken) && request.getAttribute(getStateVersionTokenParameter()) == null) {
                throw new StaleStateServiceException("Page version token mismatch (" + passedToken + "). The request likely came from a stale page.");
            } else {
                request.setAttribute(getStateVersionTokenParameter(), "passed");
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Validated page version token");
                }
            }
        }
    }

    @Override
    public String getStateVersionToken(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (UCRequestUtils.isOKtoUseSession(new ServletWebRequest(request))) {
            HttpSession session = request.getSession();
            String token = (String) session.getAttribute(STATEVERSIONTOKEN);
            if (StringUtils.isEmpty(token)) {
                try {
                    token = RandomGenerator.generateRandomId("SHA1PRNG", 32);
                } catch (NoSuchAlgorithmException e) {
                    LOG.error("Unable to generate random number", e);
                    throw new RuntimeException("Unable to generate random number", e);
                }
                session.setAttribute(STATEVERSIONTOKEN, token);
            }
            return token;
        }
        return null;
    }

    @Override
    public void invalidateState() {
        invalidateState(false);
    }

    @Override
    public void invalidateState(boolean notify) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (UCRequestUtils.isOKtoUseSession(new ServletWebRequest(request))) {
            HttpSession session = request.getSession();
            session.removeAttribute(STATEVERSIONTOKEN);
            if (notify) {
                getStateVersionToken();
                request.setAttribute(STATECHANGENOTIFICATIONTOKEN, "true");
            }
        }
    }

    @Override
    public boolean sendRedirectOnStateChange(HttpServletResponse response, String... stateChangeParams) throws IOException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (UCRequestUtils.isOKtoUseSession(new ServletWebRequest(request))) {
            String notification = (String) request.getAttribute(STATECHANGENOTIFICATIONTOKEN);
            if (Boolean.valueOf(notification)) {
                String uri = request.getRequestURI();
                Enumeration<String> params = request.getParameterNames();
                StringBuilder sb = new StringBuilder();
                Arrays.sort(stateChangeParams);
                while (params.hasMoreElements()) {
                    String param = params.nextElement();
                    if (!param.equals(STATEVERSIONTOKEN) && Arrays.binarySearch(stateChangeParams, param) < 0) {
                        if (sb.length() == 0) {
                            sb.append("?");
                        } else {
                            sb.append("&");
                        }
                        sb.append(param);
                        sb.append("=");
                        sb.append(request.getParameter(param));
                    }
                }
                uri = uri + sb.toString();
                String encoded = response.encodeRedirectURL(uri);
                try {
                    UrlUtil.validateUrl(encoded, request);
                } catch (IOException e) {
                    LOG.error("SECURITY FAILURE Bad redirect location: " + StringUtil.sanitize(encoded), e);
                    response.sendError(403);
                }
                response.sendRedirect(encoded);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isEnabled() {
        return staleStateProtectionEnabled;
    }

    @Override
    public String getStateVersionTokenParameter() {
        return STATEVERSIONTOKENPARAMETER;
    }
}
