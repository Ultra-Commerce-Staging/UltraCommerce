/*-
 * #%L
 * ultra-marketplace
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
package com.ultracommerce.test.helper;

import com.ultracommerce.common.exception.ExceptionHelper;
import com.ultracommerce.common.persistence.TargetModeType;
import com.ultracommerce.openadmin.server.service.persistence.Persistable;
import com.ultracommerce.openadmin.web.filter.UltraAdminRequestFilter;
import org.springframework.web.context.request.ServletWebRequest;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Avoid stale state redirects for the purposes of MockMVC testing
 *
 * @author Jeff Fischer
 */
public class TestAdminRequestFilter extends UltraAdminRequestFilter {

    @Override
    public void doFilterInternalUnlessIgnored(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws IOException, ServletException {
        try {
            persistenceThreadManager.operation(TargetModeType.SANDBOX, new Persistable<Void, RuntimeException>() {
                @Override
                public Void execute() {
                    try {
                        requestProcessor.process(new ServletWebRequest(request, response));
                        filterChain.doFilter(request, response);
                        return null;
                    } catch (Exception e) {
                        throw ExceptionHelper.refineException(e);
                    }
                }
            });
        } finally {
            requestProcessor.postProcess(new ServletWebRequest(request, response));
        }
    }
}
