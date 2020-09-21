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
package com.ultracommerce.profile.core.service;

import com.ultracommerce.profile.core.dao.StateDao;
import com.ultracommerce.profile.core.domain.State;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @deprecated - use {@link com.ultracommerce.profile.core.service.CountrySubdivisionServiceImpl} instead.
 */
@Deprecated
@Service("ucStateService")
public class StateServiceImpl implements StateService {

    @Resource(name="ucStateDao")
    protected StateDao stateDao;

    public List<State> findStates() {
        return stateDao.findStates();
    }

    public List<State> findStates(String countryAbbreviation) {
        return stateDao.findStates(countryAbbreviation);
    }

    public State findStateByAbbreviation(String abbreviation) {
        return stateDao.findStateByAbbreviation(abbreviation);
    }

    @Transactional("ucTransactionManager")
    public State save(State state) {
        return stateDao.save(state);
    }
}

