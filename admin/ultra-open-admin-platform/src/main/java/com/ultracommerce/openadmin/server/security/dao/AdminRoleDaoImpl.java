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
package com.ultracommerce.openadmin.server.security.dao;

import com.ultracommerce.common.persistence.EntityConfiguration;
import com.ultracommerce.openadmin.server.security.domain.AdminRole;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * 
 * @author jfischer
 *
 */
@Repository("ucAdminRoleDao")
public class AdminRoleDaoImpl implements AdminRoleDao {

    @PersistenceContext(unitName = "ucPU")
    protected EntityManager em;

    @Resource(name="ucEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    public void deleteAdminRole(AdminRole role) {
        if (!em.contains(role)) {
            role = readAdminRoleById(role.getId());
        }
        em.remove(role);
    }

    public AdminRole readAdminRoleById(Long id) {
        return (AdminRole) em.find(entityConfiguration.lookupEntityClass("com.ultracommerce.openadmin.server.security.domain.AdminRole"), id);
    }

    public AdminRole saveAdminRole(AdminRole role) {
        return em.merge(role);
    }

    @SuppressWarnings("unchecked")
    public List<AdminRole> readAllAdminRoles() {
        Query query = em.createNamedQuery("UC_READ_ALL_ADMIN_ROLES");
        List<AdminRole> roles = query.getResultList();
        return roles;
    }

}
