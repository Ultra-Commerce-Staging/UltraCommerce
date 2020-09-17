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
package com.ultracommerce.common.site.domain;

import java.io.Serializable;

/**
 * Created by bpolster.
 */
public interface Theme extends Serializable {
    
    /**
     * @return the id
     */
    public Long getId();

    /**
     * Sets the id
     * @param id
     */
    public void setId(Long id);

    public String getName();

    public void setName(String name);

    /**
     * The display name for a site.  Returns blank if no theme if no path is available.   Should return
     * a path that does not start with "/" and that does not ends with a "/".
     * @return
     */
    public String getPath();

    /**
     * Sets the path of the theme.
     * @param path
     */
    public void setPath(String path);
}
