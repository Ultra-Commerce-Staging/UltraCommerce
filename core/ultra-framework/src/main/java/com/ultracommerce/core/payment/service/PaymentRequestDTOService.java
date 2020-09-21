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

import com.ultracommerce.common.payment.dto.PaymentRequestDTO;
import com.ultracommerce.profile.core.domain.Customer;

/**
 * @author Chris Kittrell (ckittrell)
 */
public interface PaymentRequestDTOService {

    /**
     * Uses customer information to populate the {@link PaymentRequestDTO#customer()} object
     *  @param requestDTO the {@link PaymentRequestDTO} that should be populated
     * @param customer the {@link Customer} to get data from
     */
    PaymentRequestDTO populateCustomerInfo(PaymentRequestDTO requestDTO, Customer customer);

    /**
     * Uses customer information to populate the {@link PaymentRequestDTO#customer()} object
     *  @param requestDTO the {@link PaymentRequestDTO} that should be populated
     * @param customer the {@link Customer} to get data from
     * @param defaultEmailAddress the default email address to use if {@link Customer#getEmailAddress()} returns `null`
     */
    PaymentRequestDTO populateCustomerInfo(PaymentRequestDTO requestDTO, Customer customer, String defaultEmailAddress);

}
