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
import com.ultracommerce.common.util.dao.TypedQueryBuilder;
import com.ultracommerce.openadmin.server.security.domain.ForgotPasswordSecurityToken;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * 
 * @author bpolster
 *
 */
@Repository("ucForgotPasswordSecurityTokenDao")
public class ForgotPasswordSecurityTokenDaoImpl implements ForgotPasswordSecurityTokenDao {

    @PersistenceContext(unitName = "ucPU")
    protected EntityManager em;

    @Resource(name="ucEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    @Override
    public ForgotPasswordSecurityToken readToken(String token) {
        return (ForgotPasswordSecurityToken) em.find(entityConfiguration.lookupEntityClass("com.ultracommerce.openadmin.server.security.domain.ForgotPasswordSecurityToken"), token);        
    }

    @Override
    public List<ForgotPasswordSecurityToken> readUnusedTokensByAdminUserId(Long adminUserId) {
        TypedQuery<ForgotPasswordSecurityToken> query = new TypedQueryBuilder<ForgotPasswordSecurityToken>(ForgotPasswordSecurityToken.class, "token")
                .addRestriction("token.adminUserId", "=", adminUserId)
                .addRestriction("token.tokenUsedFlag", "=", false)
                .toQuery(em);
        return query.getResultList();
    }

    @Override
    public ForgotPasswordSecurityToken saveToken(ForgotPasswordSecurityToken token) {
        return em.merge(token);
    }
}
