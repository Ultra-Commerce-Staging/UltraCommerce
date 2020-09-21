/*
 * #%L
 * UltraCommerce Integration
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

import javax.annotation.Resource;

import com.ultracommerce.common.payment.service.AbstractPaymentGatewayConfigurationService;
import com.ultracommerce.common.payment.service.PaymentGatewayConfiguration;
import com.ultracommerce.common.payment.service.PaymentGatewayRollbackService;
import com.ultracommerce.common.payment.service.PaymentGatewayTransactionConfirmationService;
import com.ultracommerce.common.payment.service.PaymentGatewayTransactionService;
import org.springframework.stereotype.Service;

/**
 * Copied from mycompany.sample.payment.service
 * We need it to be picked up by the  siteintegration setup superlasses of groovy, which already scans "org" packages.
 * @author gdiaz
 */
@Service("ucNullIntegrationGatewayConfigurationService")
public class NullIntegrationGatewayConfigurationServiceImpl extends AbstractPaymentGatewayConfigurationService {

    @Resource(name = "ucNullIntegrationGatewayConfiguration")
    protected NullIntegrationGatewayConfiguration configuration;

    @Resource(name = "ucNullIntegrationGatewayRollbackService")
    protected PaymentGatewayRollbackService rollbackService;

    @Resource(name = "ucNullIntegrationGatewayHostedTransactionConfirmationService")
    protected NullIntegrationGatewayTransactionConfirmationServiceImpl transactionConfirmationServiceImpl;

    @Resource(name = "ucNullIntegrationGatewayTransactionService")
    protected NullIntegrationGatewayTransactionServiceImpl transactionService;

    public PaymentGatewayConfiguration getConfiguration() {
        return configuration;
    }

    public PaymentGatewayTransactionService getTransactionService() {
        return transactionService;
    }

    public PaymentGatewayTransactionConfirmationService getTransactionConfirmationService() {
        return transactionConfirmationServiceImpl;
    }

    public PaymentGatewayRollbackService getRollbackService() {
        return rollbackService;
    }
}
