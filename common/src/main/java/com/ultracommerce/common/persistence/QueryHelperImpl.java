/*
 * #%L
 * UltraCommerce Common Libraries
 * %%
 * Copyright (C) 2009 - 2018 Ultra Commerce
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
package com.ultracommerce.common.persistence;

import com.ultracommerce.common.web.UltraRequestContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 * Default implementation of {@link QueryHelper}. Utilizes the {@link UltraRequestContext} to store query hints
 * in a ThreadLocal context.
 *
 * @author Jeff Fischer
 */
@Component("ucQueryHelper")
public class QueryHelperImpl implements QueryHelper, ApplicationContextAware {

    private static ApplicationContext applicationContext;
    private static QueryHelper queryHelper;

    public static QueryHelper getSingleton() {
        if (applicationContext == null) {
            return null;
        }
        if (queryHelper == null) {
            queryHelper = (QueryHelper) applicationContext.getBean("ucQueryHelper");
        }
        return queryHelper;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        QueryHelperImpl.applicationContext = applicationContext;
    }

    @Override
    public <T> List<T> getResultListWithHint(TypedQuery<T> query, String hintKey, Object hintValue) {
        UltraRequestContext context = UltraRequestContext.getUltraRequestContext();
        try {
            context.getAdditionalProperties().put(hintKey, hintValue);
            return query.getResultList();
        } finally {
            context.getAdditionalProperties().remove(hintKey);
        }
    }

    @Override
    public <T> T getSingleResultWithHint(TypedQuery<T> query, String hintKey, Object hintValue) {
        UltraRequestContext context = UltraRequestContext.getUltraRequestContext();
        try {
            context.getAdditionalProperties().put(hintKey, hintValue);
            return query.getSingleResult();
        } finally {
            context.getAdditionalProperties().remove(hintKey);
        }
    }

    @Override
    public List getResultListWithHint(Query query, String hintKey, Object hintValue) {
        UltraRequestContext context = UltraRequestContext.getUltraRequestContext();
        try {
            context.getAdditionalProperties().put(hintKey, hintValue);
            return query.getResultList();
        } finally {
            context.getAdditionalProperties().remove(hintKey);
        }
    }

    @Override
    public Object getSingleResultWithHint(Query query, String hintKey, Object hintValue) {
        UltraRequestContext context = UltraRequestContext.getUltraRequestContext();
        try {
            context.getAdditionalProperties().put(hintKey, hintValue);
            return query.getSingleResult();
        } finally {
            context.getAdditionalProperties().remove(hintKey);
        }
    }

    @Override
    public Object getQueryHint(String hintKey) {
        UltraRequestContext context = UltraRequestContext.getUltraRequestContext();
        return context.getAdditionalProperties().get(hintKey);
    }

}
