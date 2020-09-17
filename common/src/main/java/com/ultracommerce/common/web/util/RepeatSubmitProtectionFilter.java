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
package com.ultracommerce.common.web.util;

import com.ultracommerce.common.util.UCRequestUtils;
import com.ultracommerce.common.web.UltraRequestContext;
import org.springframework.web.context.request.ServletWebRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RepeatSubmitProtectionFilter implements Filter {

    private final Map<String, List<String>> requests = new HashMap<String, List<String>>(100);

    @Override
    public void destroy() {
        //do nothing
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        boolean useSession = true;
        if (UltraRequestContext.getUltraRequestContext() != null
                && UltraRequestContext.getUltraRequestContext().getWebRequest() != null) {
            if (!UCRequestUtils.isOKtoUseSession(UltraRequestContext.getUltraRequestContext().getWebRequest())) {
                useSession = false;
            }
        } else if (!UCRequestUtils.isOKtoUseSession(new ServletWebRequest((HttpServletRequest) request))) {
            useSession = false;
        }

        if (useSession) {
            String sessionId;
            String requestURI;
            synchronized(requests) {
                sessionId = ((HttpServletRequest) request).getSession().getId();
                requestURI = ((HttpServletRequest) request).getRequestURI();
                if (requests.containsKey(sessionId) && requests.get(sessionId).contains(requestURI)) {
                    //we are currently already processing this request
                    ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_NO_CONTENT);
                    return;
                }
                List<String> myRequests = requests.get(sessionId);
                if (myRequests == null) {
                    myRequests = new ArrayList<String>();
                    requests.put(sessionId, myRequests);
                }
                myRequests.add(requestURI);
            }
            try {
                chain.doFilter(request, response);
            } finally {
                synchronized (requests) {
                    List<String> myRequests = requests.get(sessionId);
                    myRequests.remove(requestURI);
                    if (myRequests.isEmpty()) {
                        requests.remove(sessionId);
                    }
                }
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        //do nothing
    }

}
