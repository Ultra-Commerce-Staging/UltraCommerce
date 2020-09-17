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
package com.ultracommerce.openadmin.server.security.external;

import com.ultracommerce.common.security.UltraExternalAuthenticationUserDetails;
import com.ultracommerce.common.site.domain.Site;
import com.ultracommerce.openadmin.server.security.service.AdminSecurityService;
import com.ultracommerce.openadmin.server.security.service.user.AdminUserProvisioningService;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;

import java.util.Collection;

import javax.annotation.Resource;

/**
 * This is used to map LDAP principal and authorities into UC security model.
 * 
 * @author Kelly Tisdell
 *
 */
public class UltraAdminLdapUserDetailsMapper extends LdapUserDetailsMapper {

    @Resource(name = "ucAdminSecurityService")
    protected AdminSecurityService securityService;

    @Resource(name = "ucAdminUserProvisioningService")
    protected AdminUserProvisioningService provisioningService;
    
    @Override
    public UserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {
        String email = (String) ctx.getObjectAttribute("mail");
        String firstName = (String) ctx.getObjectAttribute("givenName");
        String lastName = (String) ctx.getObjectAttribute("sn");
        
        UltraExternalAuthenticationUserDetails details = new UltraExternalAuthenticationUserDetails(username, "", authorities);
        details.setEmail(email);
        details.setFirstName(firstName);
        details.setLastName(lastName);
        details.setSite(determineSite(ctx, username, authorities));

        return provisioningService.provisionAdminUser(details);
    }

    /**
     * Allows for a hook to determine the Multi-Tenant site for this user from the ctx, username, and authorities. Default is 
     * to return null (no site).  Implementors may wish to subclass this to determine the Site from the context.
     * 
     * If the user is not associated with the current site, or if there is a problem determining the Site, an instance of 
     * <code>org.springframework.security.core.AuthenticationException</code> should be thrown.
     * 
     * @return
     */
    protected Site determineSite(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {
        return null;
    }
}
