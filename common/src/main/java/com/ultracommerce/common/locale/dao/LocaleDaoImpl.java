/*
 * #%L
 * UltraCommerce Common Libraries
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
package com.ultracommerce.common.locale.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.locale.domain.Locale;
import com.ultracommerce.common.persistence.EntityConfiguration;
import com.ultracommerce.common.util.StringUtil;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by bpolster.
 */
@Repository("ucLocaleDao")
public class LocaleDaoImpl implements LocaleDao {
    private static final Log LOG = LogFactory.getLog(LocaleDaoImpl.class);

    @PersistenceContext(unitName = "ucPU")
    protected EntityManager em;

    @Resource(name="ucEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    /**
     * @return The locale for the passed in code
     */
    @Override
    public Locale findLocaleByCode(String localeCode) {
        Query query = em.createNamedQuery("UC_READ_LOCALE_BY_CODE");
        query.setParameter("localeCode", localeCode);
        query.setHint(org.hibernate.jpa.QueryHints.HINT_CACHEABLE, true);
        List<Locale> localeList = (List<Locale>) query.getResultList();
        if (localeList.size() >= 1) {
            if (localeList.size() > 1) {
                LOG.warn("Locale code " + StringUtil.sanitize(localeCode) + " exists for more than one locale");
            }
            return localeList.get(0);
        }
        return null;
    }

    /**
     * Returns the page template with the passed in id.
     *
     * @return The default locale
     */
    @Override
    public Locale findDefaultLocale() {
        Query query = em.createNamedQuery("UC_READ_DEFAULT_LOCALE");
        query.setHint(org.hibernate.jpa.QueryHints.HINT_CACHEABLE, true);
        List<Locale> localeList = (List<Locale>) query.getResultList();
        if (localeList.size() >= 1) {
            if (localeList.size() > 1) {
                LOG.warn("There is more than one default locale configured");
            }
            return localeList.get(0);
        }
        return null;
    }

    /**
     * Returns all supported UC locales.
     * @return
     */
    public List<Locale> findAllLocales() {
        Query query = em.createNamedQuery("UC_READ_ALL_LOCALES");
        query.setHint(org.hibernate.jpa.QueryHints.HINT_CACHEABLE, true);
        return (List<Locale>) query.getResultList();
    }
    
    @Override
    public Locale save(Locale locale){
        return em.merge(locale);
    }
    
}
