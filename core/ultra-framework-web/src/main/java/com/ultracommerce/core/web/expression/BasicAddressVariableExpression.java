/*
 * #%L
 * UltraCommerce Framework Web
 * %%
 * Copyright (C) 2009 - 2017 Ultra Commerce
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
package com.ultracommerce.core.web.expression;

import com.ultracommerce.common.i18n.domain.ISOCountry;
import com.ultracommerce.common.web.expression.UltraVariableExpression;
import com.ultracommerce.presentation.condition.ConditionalOnTemplating;
import com.ultracommerce.profile.core.domain.Country;
import com.ultracommerce.profile.core.domain.CountrySubdivision;
import com.ultracommerce.profile.core.domain.State;
import com.ultracommerce.profile.core.service.CountryService;
import com.ultracommerce.profile.core.service.CountrySubdivisionService;
import com.ultracommerce.profile.core.service.StateService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

/**
 * @author Chris Kittrell (ckittrell)
 */
@Component("ucBasicAddressVariableExpression")
@ConditionalOnTemplating
public class BasicAddressVariableExpression implements UltraVariableExpression {

    @Resource(name = "ucStateService")
    protected StateService stateService;

    @Resource(name = "ucCountrySubdivisionService")
    protected CountrySubdivisionService countrySubdivisionService;

    @Resource(name = "ucCountryService")
    protected CountryService countryService;

    @Override
    public String getName() {
        return "address";
    }

    @Deprecated
    public List<State> getStateOptions() {
        return stateService.findStates();
    }

    public List<CountrySubdivision> getCountrySubOptionsByISOCountry(ISOCountry isoCountry) {
        if (isoCountry == null) {
            return new ArrayList<>();
        }

        return getCountrySubOptionsByCountryAbbrev(isoCountry.getAlpha2());
    }

    public List<CountrySubdivision> getCountrySubOptionsByCountryAbbrev(String countryAbbreviation) {
        return countrySubdivisionService.findSubdivisions(countryAbbreviation);
    }

    public List<Country> getCountryOptions() {
        return countryService.findCountries();
    }

}
