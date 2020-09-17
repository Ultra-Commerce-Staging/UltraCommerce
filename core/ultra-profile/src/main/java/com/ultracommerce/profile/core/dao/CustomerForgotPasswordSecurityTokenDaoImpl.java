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
import com.ultracommerce.common.util.dao.TypedQueryBuilder;
import com.ultracommerce.profile.core.domain.CustomerForgotPasswordSecurityToken;
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
@Repository("ucCustomerForgotPasswordSecurityTokenDao")
public class CustomerForgotPasswordSecurityTokenDaoImpl implements CustomerForgotPasswordSecurityTokenDao {

    @PersistenceContext(unitName = "ucPU")
    protected EntityManager em;

    @Resource(name="ucEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    @Override
    public CustomerForgotPasswordSecurityToken readToken(String token) {
        return (CustomerForgotPasswordSecurityToken) em.find(entityConfiguration.lookupEntityClass("com.ultracommerce.profile.core.domain.CustomerForgotPasswordSecurityToken"), token);        
    }

    @Override
    public List<CustomerForgotPasswordSecurityToken> readUnusedTokensByCustomerId(Long customerId) {
        TypedQuery<CustomerForgotPasswordSecurityToken> query = new TypedQueryBuilder<CustomerForgotPasswordSecurityToken>(CustomerForgotPasswordSecurityToken.class, "token")
                .addRestriction("token.customerId", "=", customerId)
                .addRestriction("token.tokenUsedFlag", "=", false)
                .toQuery(em);
        return query.getResultList();
    }

    @Override
    public CustomerForgotPasswordSecurityToken saveToken(CustomerForgotPasswordSecurityToken token) {
        return em.merge(token);
    }
}
