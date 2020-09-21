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
package com.ultracommerce.openadmin.server.security.domain;

import com.ultracommerce.openadmin.server.security.service.type.PermissionType;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author jfischer
 *
 */
public interface AdminPermission extends Serializable {

    public void setId(Long id);
    public Long getId();
    public String getName();
    public void setName(String name);
    public String getDescription();
    public void setDescription(String description);
    public PermissionType getType();

    public void setType(PermissionType type);

    public List<AdminPermissionQualifiedEntity> getQualifiedEntities();

    public void setQualifiedEntities(List<AdminPermissionQualifiedEntity> qualifiedEntities);

    public Set<AdminUser> getAllUsers();

    public void setAllUsers(Set<AdminUser> allUsers);

    public AdminPermission clone();
    
    public Set<AdminRole> getAllRoles();
    public void setAllRoles(Set<AdminRole> allRoles);
    public List<AdminPermission> getAllChildPermissions();

    public List<AdminPermission> getAllParentPermissions();
    public Boolean isFriendly();
}
