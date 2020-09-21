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
package com.ultracommerce.common.web;

import com.ultracommerce.common.exception.SiteNotFoundException;
import com.ultracommerce.common.site.domain.Site;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * Returns null for the Site (typical for non-multi-site implementations of
 * Ultra Commerce.
 *
 * @author bpolster
 */
public class NullUltraSiteResolver implements UltraSiteResolver {

    @Override
    public Site resolveSite(HttpServletRequest request) {
        return resolveSite(new ServletWebRequest(request));
    }
    
    @Override
    public Site resolveSite(WebRequest request) {
        return resolveSite(request, false);
    }

    @Override
    public Site resolveSite(WebRequest request, boolean allowNullSite) throws SiteNotFoundException {
        return null;
    }
    
}
