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

import com.ultracommerce.common.site.domain.Site;
import com.ultracommerce.common.site.domain.Theme;
import com.ultracommerce.common.site.domain.ThemeDTO;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * Returns null for the Site (typical for non-multi-site implementations of
 * Ultra Commerce.
 *
 * @author bpolster
 */
public class NullUltraThemeResolver implements UltraThemeResolver {
    private final Theme theme = new ThemeDTO();

    @Override
    public Theme resolveTheme(HttpServletRequest request, Site site) {
        return resolveTheme(new ServletWebRequest(request));
    }
    
    @Override
    public Theme resolveTheme(WebRequest request) {
        return theme;
    }
}
