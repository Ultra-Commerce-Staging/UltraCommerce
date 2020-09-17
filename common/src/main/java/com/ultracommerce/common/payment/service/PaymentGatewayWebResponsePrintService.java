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

import javax.servlet.http.HttpServletRequest;

/**
 * <p>This is a utility service that aids in translating the Request Attribute and
 * Request Parameters to a single String. This is useful when setting the Raw Response
 * fields on a PaymentResponseDTO. Primarily used in the PaymentGatewayWebResponseService
 * but can be injected anywhere you need to get the attributes or paraeters from an HTTPServletRequest
 * as a String.</p>
 *
 * @see {@link PaymentGatewayWebResponseService}
 * @see {@link com.ultracommerce.common.payment.dto.PaymentResponseDTO}
 *
 * @author Elbert Bautista (elbertbautista)
 */
public interface PaymentGatewayWebResponsePrintService {

    public String printRequest(HttpServletRequest request);

}
