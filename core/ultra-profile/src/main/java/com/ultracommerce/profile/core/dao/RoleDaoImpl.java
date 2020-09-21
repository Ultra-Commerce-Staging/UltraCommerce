/*
 * #%L
 * UltraCommerce Profile
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
package com.ultracommerce.profile.core.dao;

import com.ultracommerce.common.persistence.EntityConfiguration;
import com.ultracommerce.profile.core.domain.CustomerRole;
import com.ultracommerce.profile.core.domain.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository("ucRoleDao")
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext(unitName = "ucPU")
    protected EntityManager em;

    @Resource(name = "ucEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    @SuppressWarnings("unchecked")
    public List<CustomerRole> readCustomerRolesByCustomerId(Long customerId) {
        Query query = em.createNamedQuery("UC_READ_CUSTOMER_ROLES_BY_CUSTOMER_ID");
        query.setParameter("customerId", customerId);
        return query.getResultList();
    }
    
    public Role readRoleByName(String name) {
        Query query = em.createNamedQuery("UC_READ_ROLE_BY_NAME");
        query.setParameter("name", name);
        return (Role) query.getSingleResult();
    }

    public void addRoleToCustomer(CustomerRole customerRole) {
        em.persist(customerRole);
    }
    
    public void removeCustomerRolesByCustomerId(Long customerId) {
    	List<CustomerRole> roles = readCustomerRolesByCustomerId(customerId);
    	for (CustomerRole r : roles) {
    		em.remove(r);
    	}
    }
}
