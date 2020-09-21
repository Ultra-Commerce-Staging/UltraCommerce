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
package com.ultracommerce.openadmin.server.security.service.user;

import org.apache.commons.lang.StringUtils;
import com.ultracommerce.common.security.UltraExternalAuthenticationUserDetails;
import com.ultracommerce.openadmin.server.security.domain.AdminRole;
import com.ultracommerce.openadmin.server.security.domain.AdminUser;
import com.ultracommerce.openadmin.server.security.domain.AdminUserImpl;
import com.ultracommerce.openadmin.server.security.external.AdminExternalLoginUserExtensionManager;
import com.ultracommerce.openadmin.server.security.service.AdminSecurityHelper;
import com.ultracommerce.openadmin.server.security.service.AdminSecurityService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;

/**
 * This component allows for the default provisioning of an AdminUser and roles in the Ultra database, based on the 
 * external authentication of a user (e.g. LDAP or custom external authentication provider).
 * 
 * @author Kelly Tisdell
 *
 */
@Service("ucAdminUserProvisioningService")
public class AdminUserProvisioningServiceImpl implements AdminUserProvisioningService {

    @Resource(name = "ucAdminSecurityService")
    protected AdminSecurityService securityService;

    @Resource(name = "ucAdminExternalLoginExtensionManager")
    protected AdminExternalLoginUserExtensionManager adminExternalLoginExtensionManager;

    @Resource(name="ucAdminSecurityHelper")
    protected AdminSecurityHelper adminSecurityHelper;

    protected Map<String, String[]> roleNameSubstitutions;

    @Override
    public AdminUserDetails provisionAdminUser(
            final UltraExternalAuthenticationUserDetails details) {
        final HashSet<AdminRole> parsedRoles = parseAdminRoles(details);
        final Set<SimpleGrantedAuthority> adminUserAuthorities = 
                extractAdminUserAuthorities(parsedRoles);
        final AdminUser adminUser = getAdminUser(details, parsedRoles);

        return createDetails(adminUser, details, adminUserAuthorities);
    }
    
    protected HashSet<AdminRole> parseAdminRoles(
            final UltraExternalAuthenticationUserDetails details) {
        final HashSet<String> parsedRoleNames = parseRolesFromUserDetails(details);
        final HashSet<AdminRole> parsedRoles = new HashSet<>();
        final List<AdminRole> adminRoles = securityService.readAllAdminRoles();

        if (adminRoles != null) {
            for (AdminRole role : adminRoles) {
                if (parsedRoleNames.contains(role.getName())) {
                    parsedRoles.add(role);
                }
            }
        }
        
        return parsedRoles;
    } 

    /**
     * Extracts the {@code SimpleGrantedAuthority}s for the given List of {@code AdminRole}s. In addition, this will handle
     * populating the default roles. This method returns a Set in order to avoid the duplication between the permissions of different roles.
     *
     * @param parsedRoles a List of AdminRole
     * @return a Set of unique authorities for the given roles
     */
    protected Set<SimpleGrantedAuthority> extractAdminUserAuthorities(HashSet<AdminRole> parsedRoles) {
        List<SimpleGrantedAuthority> adminUserAuthorities = new ArrayList<>();
        addPermissions(parsedRoles, adminUserAuthorities);
        convertPermissionPrefixToRole(adminUserAuthorities);

        return new HashSet<>(adminUserAuthorities);
    }
    
    protected void addPermissions(final HashSet<AdminRole> parsedRoles,
            final List<SimpleGrantedAuthority> adminUserAuthorities) {
        for (final String perm : AdminSecurityService.DEFAULT_PERMISSIONS) {
            adminUserAuthorities.add(new SimpleGrantedAuthority(perm));
        }

        for (final AdminRole role : parsedRoles) {
            adminSecurityHelper
                    .addAllPermissionsToAuthorities(adminUserAuthorities, role.getAllPermissions());
        }
    }
    
    protected void convertPermissionPrefixToRole(
            final List<SimpleGrantedAuthority> adminUserAuthorities) {
        // Spring security expects everything to begin with ROLE_ for things like hasRole() 
        // expressions so this adds additional authorities with those mappings, as well as new ones 
        // with ROLE_ instead of PERMISSION_.
        // At the end of this, given a permission set like: PERMISSION_ALL_PRODUCT
        // The following authorities will appear in the final list to Spring security:
        // PERMISSION_ALL_PRODUCT, ROLE_PERMISSION_ALL_PRODUCT, ROLE_ALL_PRODUCT
        final ListIterator<SimpleGrantedAuthority> it = adminUserAuthorities.listIterator();
        
        while (it.hasNext()) {
            final SimpleGrantedAuthority auth = it.next();
            
            if (auth.getAuthority().startsWith(AdminUserDetailsServiceImpl.LEGACY_ROLE_PREFIX)) {
                it.add(new SimpleGrantedAuthority(
                        AdminUserDetailsServiceImpl.DEFAULT_SPRING_SECURITY_ROLE_PREFIX + auth
                                .getAuthority()));
                it.add(new SimpleGrantedAuthority(auth.getAuthority()
                        .replaceAll(AdminUserDetailsServiceImpl.LEGACY_ROLE_PREFIX,
                                AdminUserDetailsServiceImpl.DEFAULT_SPRING_SECURITY_ROLE_PREFIX)));
            }
        }
    }
    
    protected AdminUser getAdminUser(final UltraExternalAuthenticationUserDetails details,
            final HashSet<AdminRole> parsedRoles) {
        AdminUser adminUser = securityService.readAdminUserByUserName(details.getUsername());
        if (adminUser == null) {
            adminUser = new AdminUserImpl();
            adminUser.setLogin(details.getUsername());
        }

        if (StringUtils.isNotBlank(details.getEmail())) {
            adminUser.setEmail(details.getEmail());
        }

        StringBuilder name = new StringBuilder();
        if (StringUtils.isNotBlank(details.getFirstName())) {
            name.append(details.getFirstName()).append(" ");
        }
        if (StringUtils.isNotBlank(details.getLastName())) {
            name.append(details.getLastName());
        }

        String fullName = name.toString();
        if (StringUtils.isNotBlank(fullName)) {
            adminUser.setName(fullName);
        } else {
            adminUser.setName(details.getUsername());
        }

        // set the roles for the admin user to our new set of roles
        adminUser.setAllRoles(new HashSet<>(parsedRoles));

        //Add optional support for things like Multi-Tenant, etc...
        adminExternalLoginExtensionManager.getProxy()
                .performAdditionalAuthenticationTasks(adminUser, details);

        //Save the user data and all of the roles...
        return securityService.saveAdminUser(adminUser);
    }
    
    protected AdminUserDetails createDetails(final AdminUser adminUser, 
            final UltraExternalAuthenticationUserDetails details,
            final Set<SimpleGrantedAuthority> adminUserAuthorities) {
        return new AdminUserDetails(adminUser.getId(), details.getUsername(), "", true, true, true,
                true, adminUserAuthorities);
    } 

    /**
     * Uses the provided role name substitutions to map the LDAP roles to Ultra roles.
     *
     * @param details the auth details
     * @return a Set of unique Ultra role names
     */
    protected HashSet<String> parseRolesFromUserDetails(UltraExternalAuthenticationUserDetails details) {
        HashSet<String> newRoles = new HashSet<>();

        if (roleNameSubstitutions != null && !roleNameSubstitutions.isEmpty()) {
            for (GrantedAuthority authority : details.getAuthorities()) {
                if (roleNameSubstitutions.containsKey(authority.getAuthority())) {
                    String[] roles = roleNameSubstitutions.get(authority.getAuthority());
                    for (String role : roles) {
                        newRoles.add(role.trim());
                    }
                } else {
                    newRoles.add(authority.getAuthority());
                }
            }
        } else {
            for (GrantedAuthority authority : details.getAuthorities()) {
                newRoles.add(authority.getAuthority());
            }
        }
        return newRoles;
    }

    /**
     * This allows you to declaratively set a map containing values that will substitute role names from LDAP to Ultra roles names in cases that they might be different.
     * For example, if you have a role specified in LDAP under "memberOf" with a DN of "Marketing Administrator", you might want to
     * map that to the role "ADMIN".  By default the prefix "ROLE_" will be pre-pended to this name. So to configure this, you would specify:
     *
     * <bean class="org.ultra.loadtest.web.security.ActiveDirectoryUserDetailsContextMapper">
     *     <property name="roleMappings">
     *         <map>
     *             <entry key="Marketing_Administrator" value="ROLE_CATALOG_ADMIN"/>
     *         </map>
     *     </property>
     * </bean>
     *
     * With this configuration, all roles returned by LDAP that have a DN of "Marketing Administrator" will be converted to "ADMIN"
     * @param roleNameSubstitutions
     */
    public void setRoleNameSubstitutions(Map<String, String[]> roleNameSubstitutions) {
        this.roleNameSubstitutions = roleNameSubstitutions;
    }
}
