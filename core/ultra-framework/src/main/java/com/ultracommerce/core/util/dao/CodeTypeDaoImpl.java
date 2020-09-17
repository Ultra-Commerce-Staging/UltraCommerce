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
package com.ultracommerce.core.util.dao;

import com.ultracommerce.common.persistence.EntityConfiguration;
import com.ultracommerce.core.util.domain.CodeType;
import com.ultracommerce.core.util.domain.CodeTypeImpl;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;


@Repository("ucCodeTypeDao")
public class CodeTypeDaoImpl implements CodeTypeDao {

    @PersistenceContext(unitName="ucPU")
    protected EntityManager em;

    @Resource(name="ucEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    public CodeType create() {
        return ((CodeType) entityConfiguration.createEntityInstance(CodeType.class.getName()));
    }

    @SuppressWarnings("unchecked")
    public List<CodeType> readAllCodeTypes() {
        Query query = em.createNamedQuery("UC_READ_ALL_CODE_TYPES");
        return query.getResultList();
    }

    public void delete(CodeType codeType) {
        if (!em.contains(codeType)) {
            codeType = (CodeType) em.find(CodeTypeImpl.class, codeType.getId());
        }
        em.remove(codeType);
    }

    public CodeType readCodeTypeById(Long codeTypeId) {
        return (CodeType) em.find(entityConfiguration.lookupEntityClass(CodeType.class.getName()), codeTypeId);
    }

    @SuppressWarnings("unchecked")
    public List<CodeType> readCodeTypeByKey(String key) {
        Query query = em.createNamedQuery("UC_READ_CODE_TYPE_BY_KEY");
        query.setParameter("key", key);
        List<CodeType> result = query.getResultList();
        return result;
    }

    public CodeType save(CodeType codeType) {
        if(codeType.getId()==null) {
            em.persist(codeType);
        }else {
            codeType = em.merge(codeType);
        }
        return codeType;
    }

}
