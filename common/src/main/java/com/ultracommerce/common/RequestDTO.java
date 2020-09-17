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
package com.ultracommerce.common;

import java.util.Map;

/**
 * Created by bpolster.
 */
public interface RequestDTO {

    /**
     * @return  returns the request not including the protocol, domain, or query string
     */
    public String getRequestURI();

    /**
     * @return Returns the URL and parameters.
     */
    public String getFullUrLWithQueryString();

    /**
     * @return true if this request came in through HTTPS
     */
    public Boolean isSecure();

    Map<String, String> getRequestContextAttributes();

    void setRequestContextAttributes(Map<String, String> requestContextAttributes);

    /**
     * By default, converts the properties on 
     * @return
     */
    public Map<String, Object> getProperties();
}
