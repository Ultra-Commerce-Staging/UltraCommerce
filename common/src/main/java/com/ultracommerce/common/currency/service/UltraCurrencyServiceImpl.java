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
package com.ultracommerce.common.currency.service;

import com.ultracommerce.common.currency.dao.UltraCurrencyDao;
import com.ultracommerce.common.currency.domain.UltraCurrency;
import com.ultracommerce.common.util.TransactionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;

/**
 * Author: jerryocanas
 * Date: 9/6/12
 */

@Service("ucCurrencyService")
public class UltraCurrencyServiceImpl implements UltraCurrencyService {

    @Resource(name="ucCurrencyDao")
    protected UltraCurrencyDao currencyDao;

    /**
     * Returns the default Ultra currency
     * @return The default currency
     */
    @Override
    public UltraCurrency findDefaultUltraCurrency() {
        return currencyDao.findDefaultUltraCurrency();
    }

    /**
     * @return The currency for the passed in code
     */
    @Override
    public UltraCurrency findCurrencyByCode(String currencyCode) {
        return currencyDao.findCurrencyByCode(currencyCode);
    }

    /**
     * Returns a list of all the Ultra Currencies
     *@return List of currencies
     */
    @Override
    public List<UltraCurrency> getAllCurrencies() {
        return currencyDao.getAllCurrencies();
    }

    @Override
    @Transactional(TransactionUtils.DEFAULT_TRANSACTION_MANAGER)
    public UltraCurrency save(UltraCurrency currency) {
        return currencyDao.save(currency);
    }
    
    @Override
    public UltraCurrency create() {
        return currencyDao.create();
    }    
}
