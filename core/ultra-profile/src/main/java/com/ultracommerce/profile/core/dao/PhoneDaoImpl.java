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
import com.ultracommerce.profile.core.domain.Phone;
import com.ultracommerce.profile.core.domain.PhoneImpl;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository("ucPhoneDao")
public class PhoneDaoImpl implements PhoneDao {

    @PersistenceContext(unitName="ucPU")
    protected EntityManager em;

    @Resource(name="ucEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    public Phone save(Phone phone) {
        return em.merge(phone);
    }

    public Phone readPhoneById(Long phoneId) {
        return (Phone) em.find(PhoneImpl.class, phoneId);
    }

    public Phone create() {
        return (Phone) entityConfiguration.createEntityInstance(Phone.class.getName());
    }
}
