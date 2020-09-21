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

package com.ultracommerce.profile.core.domain;

import com.ultracommerce.common.copy.MultiTenantCloneable;
import com.ultracommerce.common.domain.AdditionalFields;
import com.ultracommerce.common.payment.PaymentGatewayType;
import com.ultracommerce.common.payment.PaymentType;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>This entity is designed to deal with payments associated to an {@link Customer} and is used to refer to a saved 
 * payment that is stored at the Payment Gateway level. This entity can be used to represent any type of payment, 
 * such as credit cards, PayPal accounts, etc.</p>
 */
public interface CustomerPayment extends AdditionalFields, Serializable, MultiTenantCloneable<CustomerPayment> {

    public void setId(Long id);

    public Long getId();

    public Customer getCustomer();

    public void setCustomer(Customer customer);

    public Address getBillingAddress();

    public void setBillingAddress(Address billingAddress);

    public String getPaymentToken();

    public void setPaymentToken(String paymentToken);

    public PaymentType getPaymentType();

    public void setPaymentType(PaymentType paymentType);

    public PaymentGatewayType getPaymentGatewayType();

    public void setPaymentGatewayType(PaymentGatewayType paymentGatewayType);

    public boolean isDefault();

    public void setIsDefault(boolean isDefault);

    @Override
    public Map<String, String> getAdditionalFields();

    @Override
    public void setAdditionalFields(Map<String, String> additionalFields);

}
