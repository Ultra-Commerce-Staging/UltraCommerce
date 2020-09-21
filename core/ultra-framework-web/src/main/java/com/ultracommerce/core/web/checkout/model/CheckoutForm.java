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
package com.ultracommerce.core.web.checkout.model;

import com.ultracommerce.common.i18n.domain.ISOCountryImpl;
import com.ultracommerce.common.payment.CreditCardType;
import com.ultracommerce.profile.core.domain.Address;
import com.ultracommerce.profile.core.domain.AddressImpl;
import com.ultracommerce.profile.core.domain.CountryImpl;
import com.ultracommerce.profile.core.domain.PhoneImpl;
import com.ultracommerce.profile.core.domain.StateImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CheckoutForm implements Serializable {
    
    private static final long serialVersionUID = 8866879738364589339L;
    
    private String emailAddress;
    private Address shippingAddress = new AddressImpl();
    private Address billingAddress = new AddressImpl();
    private String creditCardNumber;
    private String creditCardCvvCode;
    private String creditCardExpMonth;
    private String creditCardExpYear;
    private String selectedCreditCardType;
    private boolean isSameAddress;

    public CheckoutForm() {
        shippingAddress = new AddressImpl();
        billingAddress = new AddressImpl();
        shippingAddress.setPhonePrimary(new PhoneImpl());
        billingAddress.setPhonePrimary(new PhoneImpl());
        shippingAddress.setPhoneSecondary(new PhoneImpl());
        billingAddress.setPhoneSecondary(new PhoneImpl());
        shippingAddress.setPhoneFax(new PhoneImpl());
        billingAddress.setPhoneFax(new PhoneImpl());
        shippingAddress.setIsoCountryAlpha2(new ISOCountryImpl());
        billingAddress.setIsoCountryAlpha2(new ISOCountryImpl());
        isSameAddress = true;

        /**
         * @deprecated - setCountry() and setState() on address have been deprecated in favor of ISO standardization.
         * Leaving here for legacy implementations.
         */
        shippingAddress.setCountry(new CountryImpl());
        billingAddress.setCountry(new CountryImpl());
        shippingAddress.setState(new StateImpl());
        billingAddress.setState(new StateImpl());
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getSelectedCreditCardType() {
        return selectedCreditCardType;
    }

    public void setSelectedCreditCardType(String selectedCreditCardType) {
        this.selectedCreditCardType = selectedCreditCardType;
    }

    public List<CreditCardType> getApprovedCreditCardTypes() {
        List<CreditCardType> approvedCCTypes = new ArrayList<CreditCardType>();
        approvedCCTypes.add(CreditCardType.VISA);
        approvedCCTypes.add(CreditCardType.MASTERCARD);
        approvedCCTypes.add(CreditCardType.AMEX);
        return approvedCCTypes;
    }

    public Address getShippingAddress() {
        return shippingAddress == null ? new AddressImpl() : shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Address getBillingAddress() {
        return billingAddress == null ? new AddressImpl() : billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
       this.billingAddress = billingAddress;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getCreditCardCvvCode() {
        return creditCardCvvCode;
    }

    public void setCreditCardCvvCode(String creditCardCvvCode) {
        this.creditCardCvvCode = creditCardCvvCode;
    }

    public String getCreditCardExpMonth() {
        return creditCardExpMonth;
    }

    public void setCreditCardExpMonth(String creditCardExpMonth) {
        this.creditCardExpMonth = creditCardExpMonth;
    }

    public String getCreditCardExpYear() {
        return creditCardExpYear;
    }

    public void setCreditCardExpYear(String creditCardExpYear) {
        this.creditCardExpYear = creditCardExpYear;
    }

    public boolean getIsSameAddress() {
        return isSameAddress;
    }

    public void setIsSameAddress(boolean isSameAddress) {
        this.isSameAddress = isSameAddress;
    }

}
