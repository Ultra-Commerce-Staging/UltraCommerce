/*
 * #%L
 * UltraCommerce CMS Module
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
package com.ultracommerce.cms.url.domain;

import com.ultracommerce.cms.url.type.URLRedirectType;
import com.ultracommerce.common.copy.MultiTenantCloneable;

import java.io.Serializable;

public interface URLHandler extends Serializable, MultiTenantCloneable<URLHandler> {

    public abstract Long getId();

    public abstract void setId(Long id);

    public abstract String getIncomingURL();

    public abstract void setIncomingURL(String incomingURL);

    public abstract String getNewURL();

    public abstract void setNewURL(String newURL);

    public abstract URLRedirectType getUrlRedirectType();

    public abstract void setUrlRedirectType(URLRedirectType redirectType);

    /**
     * Indicates if the value returned by <code>getIncomingURL()</code> is a regex expression
     * rather than a concrete URI.  Default is false.
     *
     * @return
     */
    public abstract boolean isRegexHandler();

    /**
     * Indicates if the value set by the method <code>setIncomingURL(String)</code> should be treated as a regex
     * expression rather than as a concrete URI.
     *
     * @param regexHandler
     */
    public abstract void setRegexHandler(Boolean regexHandler);

    /**
     * @Deprecated use {@link #setRegexHandler(Boolean regexHandler)} 
     */
    @Deprecated
    public abstract void setRegexHandler(boolean regexHandler);
}
