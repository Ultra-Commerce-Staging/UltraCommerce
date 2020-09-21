/*
 * #%L
 * UltraCommerce Framework Web
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
package com.ultracommerce.core.web.service;

import org.apache.commons.lang.StringUtils;
import com.ultracommerce.common.i18n.domain.ISOCountry;
import com.ultracommerce.common.i18n.service.ISOService;
import com.ultracommerce.profile.core.domain.Country;
import com.ultracommerce.profile.core.domain.Phone;
import com.ultracommerce.profile.core.domain.PhoneImpl;
import com.ultracommerce.profile.core.domain.State;
import com.ultracommerce.profile.core.service.CountryService;
import com.ultracommerce.profile.core.service.StateService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.ServletRequestDataBinder;

import java.beans.PropertyEditorSupport;

import javax.annotation.Resource;

/**
 * @author Chris Kittrell (ckittrell)
 */
@Service("ucInitBinderService")
public class InitBinderServiceImpl implements InitBinderService {

    @Resource(name = "ucCountryService")
    protected CountryService countryService;

    @Resource(name = "ucStateService")
    protected StateService stateService;

    @Resource(name = "ucISOService")
    protected ISOService isoService;

    @Override
    public void configAddressInitBinder(ServletRequestDataBinder binder) {

        /**
         * @deprecated - address.setState() is deprecated in favor of ISO standardization
         * This is here for legacy compatibility
         */
        binder.registerCustomEditor(State.class, "address.state", new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (StringUtils.isNotEmpty(text)) {
                    State state = stateService.findStateByAbbreviation(text);
                    setValue(state);
                } else {
                    setValue(null);
                }
            }
        });

        /**
         * @deprecated - address.setCountry() is deprecated in favor of ISO standardization
         * This is here for legacy compatibility
         */
        binder.registerCustomEditor(Country.class, "address.country", new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (StringUtils.isNotEmpty(text)) {
                    Country country = countryService.findCountryByAbbreviation(text);
                    setValue(country);
                } else {
                    setValue(null);
                }
            }
        });

        binder.registerCustomEditor(ISOCountry.class, "address.isoCountryAlpha2", new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (StringUtils.isNotEmpty(text)) {
                    ISOCountry isoCountry = isoService.findISOCountryByAlpha2Code(text);
                    setValue(isoCountry);
                }else {
                    setValue(null);
                }
            }
        });

        binder.registerCustomEditor(Phone.class, "address.phonePrimary", new PropertyEditorSupport() {

            @Override
            public void setAsText(String text) {
                Phone phone = new PhoneImpl();
                phone.setPhoneNumber(text);
                setValue(phone);
            }

        });

        binder.registerCustomEditor(Phone.class, "address.phoneSecondary", new PropertyEditorSupport() {

            @Override
            public void setAsText(String text) {
                Phone phone = new PhoneImpl();
                phone.setPhoneNumber(text);
                setValue(phone);
            }

        });

        binder.registerCustomEditor(Phone.class, "address.phoneFax", new PropertyEditorSupport() {

            @Override
            public void setAsText(String text) {
                Phone phone = new PhoneImpl();
                phone.setPhoneNumber(text);
                setValue(phone);
            }

        });
    }
}
