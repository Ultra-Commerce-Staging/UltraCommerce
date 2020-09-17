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
package com.ultracommerce.core.payment.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.i18n.domain.ISOCountry;
import com.ultracommerce.common.i18n.service.ISOService;
import com.ultracommerce.common.payment.PaymentAdditionalFieldType;
import com.ultracommerce.common.payment.dto.AddressDTO;
import com.ultracommerce.common.payment.dto.PaymentResponseDTO;
import com.ultracommerce.common.util.StringUtil;
import com.ultracommerce.core.order.domain.FulfillmentGroup;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.service.FulfillmentGroupService;
import com.ultracommerce.core.payment.domain.OrderPayment;
import com.ultracommerce.profile.core.domain.Address;
import com.ultracommerce.profile.core.domain.Country;
import com.ultracommerce.profile.core.domain.CountrySubdivision;
import com.ultracommerce.profile.core.domain.CustomerPayment;
import com.ultracommerce.profile.core.domain.Phone;
import com.ultracommerce.profile.core.domain.State;
import com.ultracommerce.profile.core.service.AddressService;
import com.ultracommerce.profile.core.service.CountryService;
import com.ultracommerce.profile.core.service.CountrySubdivisionService;
import com.ultracommerce.profile.core.service.PhoneService;
import com.ultracommerce.profile.core.service.StateService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

/**
 * @author Elbert Bautista (elbertbautista)
 */
@Service("ucPaymentResponseDTOToEntityService")
public class PaymentResponseDTOToEntityServiceImpl implements PaymentResponseDTOToEntityService {

    private static final Log LOG = LogFactory.getLog(PaymentResponseDTOToEntityServiceImpl.class);

    @Resource(name = "ucAddressService")
    protected AddressService addressService;

    @Resource(name = "ucStateService")
    protected StateService stateService;

    @Resource(name = "ucCountryService")
    protected CountryService countryService;

    @Resource(name = "ucISOService")
    protected ISOService isoService;

    @Resource(name = "ucPhoneService")
    protected PhoneService phoneService;

    @Resource(name = "ucFulfillmentGroupService")
    protected FulfillmentGroupService fulfillmentGroupService;

    @Resource(name = "ucCountrySubdivisionService")
    protected CountrySubdivisionService countrySubdivisionService;

    @Override
    public void populateBillingInfo(PaymentResponseDTO responseDTO, OrderPayment payment, Address tempBillingAddress, boolean isUseBillingAddressFromGateway) {
        Address billingAddress = tempBillingAddress;
        if (responseDTO.getBillTo() != null && responseDTO.getBillTo().addressPopulated() && isUseBillingAddressFromGateway) {
            billingAddress = addressService.create();
            AddressDTO<PaymentResponseDTO> billToDTO = responseDTO.getBillTo();
            populateAddressInfo(billToDTO, billingAddress);
        }

        payment.setBillingAddress(billingAddress);
    }

    @Override
    public void populateShippingInfo(PaymentResponseDTO responseDTO, Order order) {
        FulfillmentGroup shippableFulfillmentGroup = fulfillmentGroupService.getFirstShippableFulfillmentGroup(order);
        if (responseDTO.getShipTo() != null && responseDTO.getShipTo().addressPopulated() && shippableFulfillmentGroup != null) {
            Address shippingAddress = addressService.create();
            AddressDTO<PaymentResponseDTO> shipToDTO = responseDTO.getShipTo();
            populateAddressInfo(shipToDTO, shippingAddress);

            shippableFulfillmentGroup = fulfillmentGroupService.findFulfillmentGroupById(shippableFulfillmentGroup.getId());
            if (shippableFulfillmentGroup != null) {
                shippableFulfillmentGroup.setAddress(shippingAddress);
                fulfillmentGroupService.save(shippableFulfillmentGroup);
            }
        }
    }

    @Override
    public void populateAddressInfo(AddressDTO<PaymentResponseDTO> dto, Address address) {
        address.setFirstName(dto.getAddressFirstName());
        address.setLastName(dto.getAddressLastName());
        address.setFullName(dto.getAddressFullName());
        address.setAddressLine1(dto.getAddressLine1());
        address.setAddressLine2(dto.getAddressLine2());
        address.setCity(dto.getAddressCityLocality());

        State state = null;
        if(dto.getAddressStateRegion() != null) {
            state = stateService.findStateByAbbreviation(dto.getAddressStateRegion());
        }
        if (state == null) {
            LOG.warn("The given state from the response: " + StringUtil.sanitize(dto.getAddressStateRegion()) + " could not be found"
                    + " as a state abbreviation in UC_STATE");
        }
        address.setState(state);
        
        CountrySubdivision isoCountrySub = countrySubdivisionService.findSubdivisionByAbbreviation(dto.getAddressStateRegion());
        if ( isoCountrySub != null) {
            address.setIsoCountrySubdivision(isoCountrySub.getAbbreviation());
            address.setStateProvinceRegion(isoCountrySub.getName());
        } else {
            //Integration does not conform to the ISO Code standard - just set the non-referential state province region
            address.setStateProvinceRegion(dto.getAddressStateRegion());
        }

        address.setPostalCode(dto.getAddressPostalCode());

        Country country = null;
        ISOCountry isoCountry = null;
        if (dto.getAddressCountryCode() != null) {
            country = countryService.findCountryByAbbreviation(dto.getAddressCountryCode());
            isoCountry = isoService.findISOCountryByAlpha2Code(dto.getAddressCountryCode());
        }
        if (country == null) {
            LOG.warn("The given country from the response: " + StringUtil.sanitize(dto.getAddressCountryCode()) + " could not be found"
                    + " as a country abbreviation in UC_COUNTRY");
        } else if (isoCountry == null) {
            LOG.error("The given country from the response: " + StringUtil.sanitize(dto.getAddressCountryCode()) + " could not be found"
                    + " as a country alpha-2 code in UC_ISO_COUNTRY");
        }

        address.setCountry(country);
        address.setIsoCountryAlpha2(isoCountry);

        if (dto.getAddressPhone() != null) {
            Phone billingPhone = phoneService.create();
            billingPhone.setPhoneNumber(dto.getAddressPhone());
            address.setPhonePrimary(billingPhone);
        }
        if (dto.getAddressEmail() != null) {
            address.setEmailAddress(dto.getAddressEmail());
        }
        if (dto.getAddressCompanyName() != null) {
            address.setCompanyName(dto.getAddressCompanyName());
        }

        addressService.populateAddressISOCountrySub(address);
    }

    @Override
    public void populateCustomerPaymentToken(PaymentResponseDTO responseDTO, CustomerPayment customerPayment) {
        if (responseDTO.getPaymentToken() != null) {
            customerPayment.setPaymentToken(responseDTO.getPaymentToken());
        } else if (responseDTO.getResponseMap().containsKey(PaymentAdditionalFieldType.TOKEN.getType())) {
            //handle legacy additional fields map
            customerPayment.setPaymentToken(responseDTO.getResponseMap().get(PaymentAdditionalFieldType.TOKEN.getType()));
        } else if (responseDTO.getCreditCard() != null) {
            //handle higher PCI level compliance scenarios
            customerPayment.setPaymentToken(responseDTO.getCreditCard().getCreditCardNum());
        }
    }

}
