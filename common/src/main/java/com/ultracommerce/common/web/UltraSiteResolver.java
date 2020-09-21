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
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * Responsible for returning the site used by Ultra Commerce for the current request.
 * For a single site installation, this will typically return null.
 *
 * @author bpolster
 */
public interface UltraSiteResolver  {

    public static final String SELECTED_SITE_URL_PARAM = "selectedSite";

    /**
     * 
     * @deprecated Use {@link #resolveSite(WebRequest)} instead
     */
    @Deprecated
    public Site resolveSite(HttpServletRequest request) throws SiteNotFoundException;

    /**
     * @see #resolveSite(WebRequest, boolean)
     */
    public Site resolveSite(WebRequest request) throws SiteNotFoundException;

    /**
     * Resolves a site for the given WebRequest. Implementations should throw a {@link SiteNotFoundException}
     * when a site could not be resolved unless the allowNullSite parameter is set to true.
     * 
     * @param request
     * @param allowNullSite
     * @return the resolved {@link Site}
     * @throws SiteNotFoundException
     */
    public Site resolveSite(final WebRequest request, final boolean allowNullSite) throws SiteNotFoundException;
}
