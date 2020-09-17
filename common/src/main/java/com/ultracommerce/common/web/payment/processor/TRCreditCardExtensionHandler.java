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

package com.ultracommerce.common.web.payment.processor;

import com.ultracommerce.common.extension.ExtensionHandler;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.common.payment.dto.PaymentRequestDTO;
import com.ultracommerce.common.vendor.service.exception.PaymentException;

import java.util.Map;

/**
 * @author Elbert Bautista (elbertbautista)
 */
public interface TRCreditCardExtensionHandler extends ExtensionHandler {

    /**
     * <p>The implementing modules should take into consideration the passed in configuration settings map
     * and call their implementing TransparentRedirectService to generate either an Authorize
     * or Authorize and Capture Form. The decision should be based on the implementing
     * PaymentGatewayConfiguration.isPerformAuthorizeAndCapture();
     * </p>
     * <p>
     * This method accepts a RequestDTO that represents the order along with a map of
     * gateway-specific configuration settings.
     * The hidden values and the form action will be placed on the passed in formParameters
     * variable. The keys to that map can be retrieved by calling the following methods.
     * getFormActionKey, getFormHiddenParamsKey.
     * </p>
     *
     * @param formParameters
     * @param requestDTO
     * @param configurationSettings
     */
    public ExtensionResultStatusType createTransparentRedirectForm(
            Map<String, Map<String,String>> formParameters,
            PaymentRequestDTO requestDTO,
            Map<String, String> configurationSettings) throws PaymentException;

    public ExtensionResultStatusType setFormActionKey(StringBuilder key);

    public ExtensionResultStatusType setFormHiddenParamsKey(StringBuilder key);

}
