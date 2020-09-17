/*
 * #%L
 * UltraCommerce Open Admin Platform
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
package com.ultracommerce.openadmin.web.filter;

import org.apache.commons.collections4.iterators.IteratorEnumeration;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.exception.ExceptionHelper;
import com.ultracommerce.common.exception.SiteNotFoundException;
import com.ultracommerce.common.persistence.TargetModeType;
import com.ultracommerce.common.security.service.StaleStateProtectionService;
import com.ultracommerce.common.security.service.StaleStateProtectionServiceImpl;
import com.ultracommerce.common.security.service.StaleStateServiceException;
import com.ultracommerce.common.web.UltraSiteResolver;
import com.ultracommerce.common.web.UltraWebRequestProcessor;
import com.ultracommerce.common.web.filter.FilterOrdered;
import com.ultracommerce.openadmin.security.ClassNameRequestParamValidationService;
import com.ultracommerce.openadmin.server.service.persistence.Persistable;
import com.ultracommerce.openadmin.server.service.persistence.PersistenceThreadManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

/**
 * Responsible for setting the necessary attributes on the UltraRequestContext
 * 
 * @author Andre Azzolini (apazzolini)
 */
@Component("ucAdminRequestFilter")
public class UltraAdminRequestFilter extends AbstractUltraAdminRequestFilter {

    private final Log LOG = LogFactory.getLog(UltraAdminRequestFilter.class);

    @Autowired
    @Qualifier("ucAdminRequestProcessor")
    protected UltraWebRequestProcessor requestProcessor;

    @Autowired
    @Qualifier("ucPersistenceThreadManager")
    protected PersistenceThreadManager persistenceThreadManager;

    @Autowired
    @Qualifier("ucClassNameRequestParamValidationService")
    protected ClassNameRequestParamValidationService validationService;

    @Autowired
    @Qualifier("ucStaleStateProtectionService")
    protected StaleStateProtectionService staleStateProtectionService;

    @Override
    public void doFilterInternalUnlessIgnored(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws IOException, ServletException {

        if (!validateClassNameParams(request)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if (!shouldProcessURL(request, request.getRequestURI())) {
            if (LOG.isTraceEnabled()) {
                LOG.trace("Process URL not processing URL " + request.getRequestURI());
            }
            filterChain.doFilter(request, response);
            return;
        }

        try {
            persistenceThreadManager.operation(TargetModeType.SANDBOX, new Persistable <Void, RuntimeException>() {
                @Override
                public Void execute() {
                    try {
                        requestProcessor.process(new ServletWebRequest(request, response));
                        if (!staleStateProtectionService.sendRedirectOnStateChange(
                                response,
                                UltraAdminRequestProcessor.SANDBOX_REQ_PARAM,
                                UltraAdminRequestProcessor.CATALOG_REQ_PARAM,
                                UltraAdminRequestProcessor.PROFILE_REQ_PARAM
                        )) {
                            filterChain.doFilter(request, response);
                        }
                        return null;
                    } catch (Exception e) {
                        throw ExceptionHelper.refineException(e);
                    }
                }
            });
        } catch (SiteNotFoundException e) {
            LOG.warn("Could not resolve a site for the given request, returning not found");
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (StaleStateServiceException e) {
            //catch state change attempts from a stale page
            forwardToConflictDestination(request,response);
        } finally {
            requestProcessor.postProcess(new ServletWebRequest(request, response));
        }
    }

    protected boolean validateClassNameParams(HttpServletRequest request) {
        String ceilingEntityClassname = request.getParameter("ceilingEntityClassname");
        String ceilingEntity = request.getParameter("ceilingEntity");
        String ceilingEntityFullyQualifiedClassname = request.getParameter("fields['ceilingEntityFullyQualifiedClassname'].value");
        String originalType = request.getParameter("fields['__originalType'].value");
        String entityType = request.getParameter("entityType");
        Map<String, String> params = new HashMap<>(2);
        params.put("ceilingEntityClassname", ceilingEntityClassname);
        params.put("entityType", entityType);
        params.put("ceilingEntity", ceilingEntity);
        params.put("ceilingEntityFullyQualifiedClassname", ceilingEntityFullyQualifiedClassname);
        params.put("__originalType", originalType);
        return validationService.validateClassNameParams(params, "ucPU");
    }

    /**
     * Forward the user to the conflict error page.
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void forwardToConflictDestination(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_CONFLICT);
        final Map reducedMap = new LinkedHashMap(request.getParameterMap());
        reducedMap.remove(UltraAdminRequestProcessor.CATALOG_REQ_PARAM);
        reducedMap.remove(UltraAdminRequestProcessor.PROFILE_REQ_PARAM);
        reducedMap.remove(UltraAdminRequestProcessor.SANDBOX_REQ_PARAM);
        reducedMap.remove(StaleStateProtectionServiceImpl.STATEVERSIONTOKENPARAMETER);
        reducedMap.remove(UltraSiteResolver.SELECTED_SITE_URL_PARAM);

        final HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(request) {
            @Override
            public String getParameter(String name) {
                Object temp = reducedMap.get(name);
                Object[] response = new Object[0];
                if (temp != null) {
                    ArrayUtils.addAll(response, temp);
                }
                if (ArrayUtils.isEmpty(response)) {
                    return null;
                } else {
                    return (String) response[0];
                }
            }

            @Override
            public Map getParameterMap() {
                return reducedMap;
            }

            @Override
            public Enumeration getParameterNames() {
                return new IteratorEnumeration(reducedMap.keySet().iterator());
            }

            @Override
            public String[] getParameterValues(String name) {
                return (String[]) reducedMap.get(name);
            }
        };
        requestProcessor.process(new ServletWebRequest(wrapper, response));
        wrapper.getRequestDispatcher("/sc_conflict").forward(wrapper, response);
    }

    @Override
    public int getOrder() {
        return FilterOrdered.POST_SECURITY_HIGH;
    }
}
