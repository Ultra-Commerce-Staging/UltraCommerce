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

import com.ultracommerce.common.payment.dto.AddressDTO;
import com.ultracommerce.common.payment.dto.PaymentResponseDTO;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.payment.domain.OrderPayment;
import com.ultracommerce.profile.core.domain.Address;
import com.ultracommerce.profile.core.domain.CustomerPayment;

/**
 * @author Elbert Bautista (elbertbautista)
 */
public interface PaymentResponseDTOToEntityService {

    public void populateBillingInfo(PaymentResponseDTO responseDTO, OrderPayment payment, Address tempBillingAddress, boolean isUseBillingAddressFromGateway);

    public void populateShippingInfo(PaymentResponseDTO responseDTO, Order order);

    public void populateAddressInfo(AddressDTO<PaymentResponseDTO> dto, Address address);

    /**
     * <p>
     * Will attempt to populate the {@link com.ultracommerce.profile.core.domain.CustomerPayment#setPaymentToken(String)}
     * by first looking at the response map for key {@link com.ultracommerce.common.payment.PaymentAdditionalFieldType#TOKEN}.
     * If not found, it will next look and see if a Credit Card is populated on the response and will attempt to get the
     * {@link com.ultracommerce.common.payment.dto.CreditCardDTO#getCreditCardNum()}
     *
     * <p>
     * Usually used during a tokenization flow when there is a direct response from the gateway (e.g. transparent redirect)
     * outside the scope of a checkout flow.
     * @param customerPayment
     * @param responseDTO
     * @see {@link com.ultracommerce.core.payment.service.DefaultPaymentGatewayCheckoutService}
     */
    public void populateCustomerPaymentToken(PaymentResponseDTO responseDTO, CustomerPayment customerPayment);

}
