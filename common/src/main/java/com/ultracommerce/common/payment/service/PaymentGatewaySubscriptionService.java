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

package com.ultracommerce.common.payment.service;

import com.ultracommerce.common.payment.dto.PaymentRequestDTO;
import com.ultracommerce.common.payment.dto.PaymentResponseDTO;
import com.ultracommerce.common.vendor.service.exception.PaymentException;

/**
 * <p>Some gateways allow you to create a form of recurring billing by creating a subscription profile.
 * Note: Some Gateways charge an extra fee to enable this feature</p>
 *
 * @author Elbert Bautista (elbertbautista)
 */
public interface PaymentGatewaySubscriptionService {

    public PaymentResponseDTO createGatewaySubscription(PaymentRequestDTO requestDTO) throws PaymentException;

    public PaymentResponseDTO updateGatewaySubscription(PaymentRequestDTO requestDTO) throws PaymentException;

    public PaymentResponseDTO cancelGatewaySubscription(PaymentRequestDTO requestDTO) throws PaymentException;

}
