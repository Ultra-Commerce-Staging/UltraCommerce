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

import com.ultracommerce.common.payment.PaymentGatewayType;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * <p>This represents the main servic bus for grabbing configurations to configured payment gateways to execute service calls
 * programmatically. The main use for this in the framework is in
 * {@link com.ultracommerce.core.checkout.service.workflow.ValidateAndConfirmPaymentActivity} and its rollback handler
 * {@link com.ultracommerce.core.checkout.service.workflow.ConfirmPaymentsRollbackHandler}. Since multiple gateways
 * can be configured for a single implementation (like Paypal Express and Braintree, or Paypal Express, a credit card
 * module and a gift card module) this allows you to select between them to perform additional operations on a payment
 * transaction.</p>
 * 
 * <p>Once you obtain the correct gateway configuration bean, you can then obtain links to each service to perform individual
 * operations like {@link PaymentGatewayTransactionService} or {@link PaymentGatewayFraudService}.</p>
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
public interface PaymentGatewayConfigurationServiceProvider {
    
    /**
     * <p>Returns the first {@link PaymentGatewayConfigurationService} that matches the given {@link PaymentGatewayType}. Useful when
     * you need a particular {@link PaymentGatewayConfigurationService} to communicate in different ways to a payment gateway.</p>
     * 
     * @throws IllegalArgumentException if the given {@link PaymentGatewayType} is null or if there is no configuration for
     * the given {@link PaymentGatewayType}.
     */
    public PaymentGatewayConfigurationService getGatewayConfigurationService(@Nonnull PaymentGatewayType gatewayType);
    
    /*
     * All of the gateway configurations configured in the system.
     */
    public List<PaymentGatewayConfigurationService> getGatewayConfigurationServices();
    
    public void setGatewayConfigurationServices(List<PaymentGatewayConfigurationService> gatewayConfigurationServices);
    
}
