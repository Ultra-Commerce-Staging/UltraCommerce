/*
 * #%L
 * UltraCommerce Framework Web
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
package com.ultracommerce.core.web.order.security.extension;

import com.ultracommerce.common.extension.ExtensionHandler;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Extension handler for actions that should take place after a user has authenticated on the front-end site.
 * 
 * @author Andre Azzolini (apazzolini)
 */
public interface AuthSuccessHandlerExtensionHandler extends ExtensionHandler {
    
    /**
     * Perform any necessary tasks before the merge cart processor executes.
     * 
     * @param request
     * @param response
     * @param authentication
     * @return
     */
    public ExtensionResultStatusType preMergeCartExecution(HttpServletRequest request, HttpServletResponse response, 
            Authentication authentication);

}
