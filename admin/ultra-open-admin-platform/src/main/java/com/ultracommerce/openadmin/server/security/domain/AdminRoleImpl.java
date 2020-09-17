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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.admin.domain.AdminMainEntity;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransform;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransformMember;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import com.ultracommerce.common.presentation.AdminPresentation;
import com.ultracommerce.common.presentation.AdminPresentationCollection;
import com.ultracommerce.common.presentation.AdminPresentationOperationTypes;
import com.ultracommerce.common.presentation.client.AddMethodType;
import com.ultracommerce.common.presentation.client.OperationType;
import com.ultracommerce.common.presentation.client.VisibilityEnum;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * 
 * @author jfischer
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "UC_ADMIN_ROLE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="ucAdminSecurityVolatile")
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_ADMINROLE),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.AUDITABLE_ONLY),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTI_PHASE_ADD)
})
public class AdminRoleImpl implements AdminRole, AdminRoleAdminPresentation, AdminMainEntity {

    private static final Log LOG = LogFactory.getLog(AdminRoleImpl.class);
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "AdminRoleId")
    @GenericGenerator(
        name="AdminRoleId",
        strategy="com.ultracommerce.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="AdminRoleImpl"),
            @Parameter(name="entity_name", value="com.ultracommerce.openadmin.server.security.domain.AdminRoleImpl")
        }
    )
    @Column(name = "ADMIN_ROLE_ID")
    @AdminPresentation(friendlyName = "AdminRoleImpl_Admin_Role_ID", visibility = VisibilityEnum.HIDDEN_ALL)
    protected Long id;

    @Column(name = "NAME", nullable=false)
    @AdminPresentation(friendlyName = "AdminRoleImpl_Name",
            group = GroupName.RoleDetails, order = FieldOrder.NAME)
    protected String name;

    @Column(name = "DESCRIPTION", nullable=false)
    @AdminPresentation(friendlyName = "AdminRoleImpl_Description",
            group = GroupName.RoleDetails, order = FieldOrder.DESCRIPTION,
            prominent = true)
    protected String description;

    /** All users that have this role */
    @ManyToMany(fetch = FetchType.LAZY, targetEntity = AdminUserImpl.class)
    @JoinTable(name = "UC_ADMIN_USER_ROLE_XREF", joinColumns = @JoinColumn(name = "ADMIN_ROLE_ID", referencedColumnName = "ADMIN_ROLE_ID"), inverseJoinColumns = @JoinColumn(name = "ADMIN_USER_ID", referencedColumnName = "ADMIN_USER_ID"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="ucAdminSecurityVolatile")
    @BatchSize(size = 50)
    protected Set<AdminUser> allUsers = new HashSet<AdminUser>();

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = AdminPermissionImpl.class)
    @JoinTable(name = "UC_ADMIN_ROLE_PERMISSION_XREF", joinColumns = @JoinColumn(name = "ADMIN_ROLE_ID", referencedColumnName = "ADMIN_ROLE_ID"), inverseJoinColumns = @JoinColumn(name = "ADMIN_PERMISSION_ID", referencedColumnName = "ADMIN_PERMISSION_ID"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="ucAdminSecurityVolatile")
    @BatchSize(size = 50)
    @AdminPresentationCollection(friendlyName = "permissionListTitle",
            group = GroupName.Permissions, order = FieldOrder.PERMISSIONS,
            addType = AddMethodType.LOOKUP,
            manyToField = "allRoles",
            customCriteria = "includeFriendlyOnly",
            operationTypes = @AdminPresentationOperationTypes(removeType = OperationType.NONDESTRUCTIVEREMOVE))
    protected Set<AdminPermission> allPermissions= new HashSet<AdminPermission>();


    @Override
    public Set<AdminPermission> getAllPermissions() {
        return allPermissions;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public Set<AdminUser> getAllUsers() {
        return allUsers;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    public void setAllPermissions(Set<AdminPermission> allPermissions) {
        this.allPermissions = allPermissions;
    }

    public void checkCloneable(AdminRole adminRole) throws CloneNotSupportedException, SecurityException, NoSuchMethodException {
        Method cloneMethod = adminRole.getClass().getMethod("clone", new Class[]{});
        if (cloneMethod.getDeclaringClass().getName().startsWith("com.ultracommerce") && !adminRole.getClass().getName().startsWith("com.ultracommerce")) {
            //subclass is not implementing the clone method
            throw new CloneNotSupportedException("Custom extensions and implementations should implement clone.");
        }
    }

    @Override
    public AdminRole clone() {
        AdminRole clone;
        try {
            clone = (AdminRole) Class.forName(this.getClass().getName()).newInstance();
            try {
                checkCloneable(clone);
            } catch (CloneNotSupportedException e) {
                LOG.warn("Clone implementation missing in inheritance hierarchy outside of Ultra: " + clone.getClass().getName(), e);
            }
            clone.setId(id);
            clone.setName(name);
            clone.setDescription(description);

            //don't clone the allUsers collection, as it would cause a recursion

            if (allPermissions != null) {
                for (AdminPermission permission : allPermissions) {
                    AdminPermission permissionClone = permission.clone();
                    clone.getAllPermissions().add(permissionClone);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return clone;
    }

    @Override
    public String getMainEntityName() {
        return getName();
    }

}
