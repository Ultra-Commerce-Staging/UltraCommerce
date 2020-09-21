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

/**
 * A Null instance of a URLHandler.   Used by the default URLHandlerServiceImpl implementation to
 * cache misses (e.g. urls  that are not being handled by forwards and redirects.
 *
 * @author bpolster
 */
public class NullURLHandler extends URLHandlerDTO {

    private static final long serialVersionUID = 1L;

    public NullURLHandler() {
        this(null, null);
    }
    
    public NullURLHandler(String newUrl, URLRedirectType redirectType) {
        super(newUrl, redirectType);
    }
}
