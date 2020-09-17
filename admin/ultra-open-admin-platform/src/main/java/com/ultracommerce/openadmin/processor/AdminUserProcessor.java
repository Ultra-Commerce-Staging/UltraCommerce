/*
 * #%L
 * UltraCommerce Open Admin Platform
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
package com.ultracommerce.openadmin.processor;


import com.ultracommerce.openadmin.server.security.domain.AdminUser;
import com.ultracommerce.openadmin.server.security.service.AdminSecurityService;
import com.ultracommerce.presentation.condition.ConditionalOnTemplating;
import com.ultracommerce.presentation.dialect.AbstractUltraVariableModifierProcessor;
import com.ultracommerce.presentation.dialect.UltraDialectPrefix;
import com.ultracommerce.presentation.model.UltraTemplateContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

/**
 * A Thymeleaf processor that will add the appropriate AdminUser to the model.
 *
 * @author elbertbautista
 */
@Component("ucAdminUserProcessor")
@ConditionalOnTemplating
public class AdminUserProcessor extends AbstractUltraVariableModifierProcessor {

    private static final String ANONYMOUS_USER_NAME = "anonymousUser";
    
    @Resource(name = "ucAdminSecurityService")
    protected AdminSecurityService securityService;

    @Override
    public String getName() {
        return "admin_user";
    }
    
    @Override
    public String getPrefix() {
        return UltraDialectPrefix.UC_ADMIN;
    }
    
    @Override
    public int getPrecedence() {
        return 10000; 
    }

    @Override
    public Map<String, Object> populateModelVariables(String tagName, Map<String, String> tagAttributes, UltraTemplateContext context) {
        String resultVar = tagAttributes.get("resultVar");

        Map<String, Object> newModelVars = new HashMap<>();
        AdminUser user = getPersistentAdminUser();
        if (user != null) {
            newModelVars.put(resultVar, user);
        }
        return newModelVars;
    }
    
    protected AdminUser getPersistentAdminUser() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        if (ctx != null) {
            Authentication auth = ctx.getAuthentication();
            if (auth != null && !auth.getName().equals(ANONYMOUS_USER_NAME)) {
                UserDetails temp = (UserDetails) auth.getPrincipal();

                return securityService.readAdminUserByUserName(temp.getUsername());
            }
        }

        return null;
    }

}
