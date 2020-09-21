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
package com.ultracommerce.core.checkout.service.gateway;

import com.ultracommerce.common.payment.service.AbstractPaymentGatewayConfigurationService;
import com.ultracommerce.common.payment.service.PaymentGatewayConfiguration;
import com.ultracommerce.common.payment.service.PaymentGatewayRollbackService;
import com.ultracommerce.common.payment.service.PaymentGatewayTransactionService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

/**
 * A Default Configuration to handle Passthrough Payments, for example COD payments.
 * This default implementation just supports a rollback service and transaction service.
 *
 * @author Elbert Bautista (elbertbautista)
 */
@Service("ucPassthroughPaymentConfigurationService")
public class PassthroughPaymentConfigurationServiceImpl extends AbstractPaymentGatewayConfigurationService {

    @Resource(name = "ucPassthroughPaymentConfiguration")
    protected PaymentGatewayConfiguration configuration;

    @Resource(name = "ucPassthroughPaymentRollbackService")
    protected PaymentGatewayRollbackService rollbackService;

    @Resource(name = "ucPassthroughPaymentTransactionService")
    protected PaymentGatewayTransactionService transactionService;

    @Override
    public PaymentGatewayConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public PaymentGatewayTransactionService getTransactionService() {
        return transactionService;
    }

}
