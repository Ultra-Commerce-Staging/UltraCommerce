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
package com.ultracommerce.core.payment;

import com.ultracommerce.common.money.Money;
import com.ultracommerce.common.payment.PaymentType;
import com.ultracommerce.core.payment.domain.OrderPayment;
import com.ultracommerce.core.payment.domain.OrderPaymentImpl;
import org.testng.annotations.DataProvider;

import java.math.BigDecimal;

public class PaymentInfoDataProvider {

    @DataProvider(name = "basicPaymentInfo")
    public static Object[][] provideBasicSalesPaymentInfo() {
        OrderPayment sop = new OrderPaymentImpl();
        sop.setAmount(new Money(BigDecimal.valueOf(10.99)));
        sop.setReferenceNumber("987654321");
        sop.setType(PaymentType.CREDIT_CARD);
        return new Object[][] { { sop } };
    }
}
