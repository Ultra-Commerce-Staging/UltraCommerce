/*
 * #%L
 * UltraCommerce Framework
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

import com.ultracommerce.common.presentation.AdminPresentation;
import com.ultracommerce.common.presentation.client.VisibilityEnum;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="UC_ADMIN_USER_ADDTL_FIELDS")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="ucAdminSecurityVolatile")
public class AdminUserAttributeImpl implements AdminUserAttribute {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(generator= "AdminUserAttributeId")
    @GenericGenerator(
        name="AdminUserAttributeId",
        strategy="com.ultracommerce.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="AdminUserAttributeImpl"),
            @Parameter(name="entity_name", value="com.ultracommerce.openadmin.server.security.domain.AdminUserAttributeImpl")
        }
    )
    @Column(name = "ATTRIBUTE_ID")
    protected Long id;
    
    @Column(name = "FIELD_NAME", nullable = false)
    @Index(name="ADMINUSERATTRIBUTE_NAME_INDEX", columnNames = { "NAME" })
    @AdminPresentation(visibility = VisibilityEnum.HIDDEN_ALL)
    protected String name;

    @Column(name = "FIELD_VALUE")
    protected String value;

    @ManyToOne(targetEntity = AdminUserImpl.class, optional = false)
    @JoinColumn(name = "ADMIN_USER_ID")
    @Index(name="ADMINUSERATTRIBUTE_INDEX", columnNames = { "ADMIN_USER_ID" })
    protected AdminUser adminUser;
    
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public AdminUser getAdminUser() {
        return adminUser;
    }

    @Override
    public void setAdminUser(AdminUser adminUser) {
        this.adminUser = adminUser;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((adminUser == null) ? 0 : adminUser.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!getClass().isAssignableFrom(obj.getClass()))
            return false;
        AdminUserAttributeImpl other = (AdminUserAttributeImpl) obj;

        if (id != null && other.id != null) {
            return id.equals(other.id);
        }

        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (adminUser == null) {
            if (other.adminUser != null)
                return false;
        } else if (!adminUser.equals(other.adminUser))
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }
}
