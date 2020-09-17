/*
 * #%L
 * UltraCommerce Profile Web
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
package com.ultracommerce.profile.web.site.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Cookie used to protected against session fixation attacks
 * 
 * @see SessionFixationProtectionFilter
 * 
 * @author Andre Azzolini (apazzolini)
 *
 * @deprecated Use {@link org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy} instead
 */
@Deprecated
public class SessionFixationProtectionCookie {
    protected final Log logger = LogFactory.getLog(getClass());

    public static final String COOKIE_NAME = "ActiveID";

}
