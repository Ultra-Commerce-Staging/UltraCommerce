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
package com.ultracommerce.common.locale.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.locale.dao.LocaleDao;
import com.ultracommerce.common.locale.domain.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by bpolster.
 */
@Service("ucLocaleService")
public class LocaleServiceImpl implements LocaleService {
    private static final Log LOG = LogFactory.getLog(LocaleServiceImpl.class);

    @Resource(name="ucLocaleDao")
    protected LocaleDao localeDao;

    @Override
    public Locale findLocaleByCode(String localeCode) {
        return localeDao.findLocaleByCode(localeCode);
    }
    
    @Override
    public Locale findDefaultLocale() {
        return localeDao.findDefaultLocale();
    }

    @Override
    public List<Locale> findAllLocales() {
        return localeDao.findAllLocales();
    }
    
    @Override
    @Transactional("ucTransactionManager")
    public Locale save(Locale locale) {
        return localeDao.save(locale);
    }
    
}
