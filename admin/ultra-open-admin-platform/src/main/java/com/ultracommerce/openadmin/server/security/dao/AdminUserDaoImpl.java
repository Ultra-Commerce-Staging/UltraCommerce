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
import com.ultracommerce.common.persistence.Status;
import com.ultracommerce.common.util.dao.TypedQueryBuilder;
import com.ultracommerce.openadmin.server.security.domain.AdminUser;
import org.hibernate.jpa.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * 
 * @author jfischer
 *
 */
@Repository("ucAdminUserDao")
public class AdminUserDaoImpl implements AdminUserDao {

    @PersistenceContext(unitName = "ucPU")
    protected EntityManager em;

    @Resource(name="ucEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    public void deleteAdminUser(AdminUser user) {
        if (!em.contains(user)) {
            user = em.find(entityConfiguration.lookupEntityClass("com.ultracommerce.openadmin.server.security.domain.AdminUser", AdminUser.class), user.getId());
        }
        em.remove(user);
    }

    public AdminUser readAdminUserById(Long id) {
        return em.find(entityConfiguration.lookupEntityClass("com.ultracommerce.openadmin.server.security.domain.AdminUser", AdminUser.class), id);
    }
    
    @Override
    public List<AdminUser> readAdminUsersByIds(Set<Long> ids) {
        TypedQueryBuilder<AdminUser> tqb = new TypedQueryBuilder<AdminUser>(AdminUser.class, "au");

        if (ids != null && !ids.isEmpty()) {
            tqb.addRestriction("au.id", "in", ids);
        }

        TypedQuery<AdminUser> query = tqb.toQuery(em);
        return query.getResultList();
    }

    public AdminUser saveAdminUser(AdminUser user) {
        if (em.contains(user) || user.getId() != null) {
            return em.merge(user);
        } else {
            em.persist(user);
            return user;
        }
    }

    public AdminUser readAdminUserByUserName(String userName) {
        TypedQuery<AdminUser> query = em.createNamedQuery("UC_READ_ADMIN_USER_BY_USERNAME", AdminUser.class);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "ucAdminSecurityVolatileQuery");
        query.setParameter("userName", userName);
        List<AdminUser> users = query.getResultList();
        //TODO rewrite on streams when upgraded to java 8
        Iterator<AdminUser> iterator = users.iterator();
        while (iterator.hasNext()){
            AdminUser user = iterator.next();
            if(Status.class.isAssignableFrom(user.getClass())) {
                if('Y' == ((Status)user).getArchived()) {
                    iterator.remove();
                }
            }
        }
        if (users != null && !users.isEmpty()) {
            return users.get(0);
        }
        return null;
    }

    public List<AdminUser> readAllAdminUsers() {
        TypedQuery<AdminUser> query = em.createNamedQuery("UC_READ_ALL_ADMIN_USERS", AdminUser.class);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "ucAdminSecurityVolatileQuery");
        return query.getResultList();
    }

    @Override
    public List<AdminUser> readAdminUserByEmail(String emailAddress) {
        TypedQuery<AdminUser> query = em.createNamedQuery("UC_READ_ADMIN_USER_BY_EMAIL", AdminUser.class);
        query.setParameter("email", emailAddress);
        return query.getResultList();
    }
}
