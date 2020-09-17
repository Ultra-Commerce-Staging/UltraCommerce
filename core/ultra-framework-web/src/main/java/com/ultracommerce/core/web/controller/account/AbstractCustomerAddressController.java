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
package com.ultracommerce.core.web.controller.account;

import com.ultracommerce.common.i18n.service.ISOService;
import com.ultracommerce.common.web.controller.UltraAbstractController;
import com.ultracommerce.core.web.controller.account.validator.CustomerAddressValidator;
import com.ultracommerce.core.web.service.InitBinderService;
import com.ultracommerce.profile.core.domain.Country;
import com.ultracommerce.profile.core.domain.CustomerAddress;
import com.ultracommerce.profile.core.domain.State;
import com.ultracommerce.profile.core.service.AddressService;
import com.ultracommerce.profile.core.service.CountryService;
import com.ultracommerce.profile.core.service.CustomerAddressService;
import com.ultracommerce.profile.core.service.StateService;
import com.ultracommerce.profile.web.core.CustomerState;
import org.springframework.web.bind.ServletRequestDataBinder;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * An abstract controller that provides convenience methods and resource declarations for its children.
 *
 * Operations that are shared between controllers that deal with Customer Addresses belong here.
 *
 * @author Elbert Bautista (elbertbautista)
 */
public class AbstractCustomerAddressController extends UltraAbstractController {


    protected static String customerAddressesView = "account/manageCustomerAddresses";
    protected static String customerAddressesRedirect = "redirect:/account/addresses";

    @Resource(name = "ucCustomerAddressService")
    protected CustomerAddressService customerAddressService;

    @Resource(name = "ucAddressService")
    protected AddressService addressService;

    @Resource(name = "ucCountryService")
    protected CountryService countryService;

    @Resource(name = "ucCustomerAddressValidator")
    protected CustomerAddressValidator customerAddressValidator;

    @Resource(name = "ucStateService")
    protected StateService stateService;

    @Resource(name = "ucISOService")
    protected ISOService isoService;

    @Resource(name = "ucInitBinderService")
    protected InitBinderService initBinderService;

    /**
     * Initializes some custom binding operations for the managing an address.
     * More specifically, this method will attempt to bind state and country
     * abbreviations to actual State and Country objects when the String
     * representation of the abbreviation is submitted.
     *
     * @param request
     * @param binder
     * @throws Exception
     */
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        initBinderService.configAddressInitBinder(binder);
    }

    protected List<State> populateStates() {
        return stateService.findStates();
    }

    protected List<Country> populateCountries() {
        return countryService.findCountries();
    }

    protected List<CustomerAddress> populateCustomerAddresses() {
        return customerAddressService.readActiveCustomerAddressesByCustomerId(CustomerState.getCustomer().getId());
    }

    public String getCustomerAddressesView() {
        return customerAddressesView;
    }

    public String getCustomerAddressesRedirect() {
        return customerAddressesRedirect;
    }

}
