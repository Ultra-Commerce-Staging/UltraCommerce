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
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * Responsible for returning the theme used by Ultra Commerce for the current request.
 * For a single site installation, this should return a theme whose path and name are empty string.
 *
 * @author bpolster
 */
public interface UltraThemeResolver {
    
    public static final String BRC_THEME_CHANGE_STATUS = "themeChangeStatus";
    
    /**
     * 
     * @deprecated Use {@link #resolveTheme(WebRequest)} instead
     */
    @Deprecated
    public Theme resolveTheme(HttpServletRequest request, Site site);
    
    public Theme resolveTheme(WebRequest request);
}
