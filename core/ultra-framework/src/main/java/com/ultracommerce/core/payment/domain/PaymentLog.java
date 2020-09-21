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
package com.ultracommerce.core.payment.domain;

import com.ultracommerce.common.currency.domain.UltraCurrency;
import com.ultracommerce.common.money.Money;
import com.ultracommerce.common.payment.PaymentLogEventType;
import com.ultracommerce.common.payment.PaymentTransactionType;
import com.ultracommerce.profile.core.domain.Customer;

import java.io.Serializable;
import java.util.Date;

/**
 * @deprecated - payment logs should now be captured as raw responses in Payment Transaction line items
 */
@Deprecated
public interface PaymentLog extends Serializable {

    public Long getId();

    public void setId(Long id);

    public String getUserName();

    public void setUserName(String userName);

    public Date getTransactionTimestamp();

    public void setTransactionTimestamp(Date transactionTimestamp);

    public Long getPaymentInfoId();

    public void setPaymentInfoId(Long paymentInfoId);

    public Customer getCustomer();

    public void setCustomer(Customer customer);

    public String getPaymentInfoReferenceNumber();

    public void setPaymentInfoReferenceNumber(String paymentInfoReferenceNumber);

    public PaymentTransactionType getTransactionType();

    public void setTransactionType(PaymentTransactionType transactionType);

    public Boolean getTransactionSuccess();

    public void setTransactionSuccess(Boolean transactionSuccess);

    public String getExceptionMessage();

    public void setExceptionMessage(String exceptionMessage);

    public PaymentLogEventType getLogType();

    public void setLogType(PaymentLogEventType logType);

    public Money getAmountPaid();

    public void setAmountPaid(Money amountPaid);

    void setCurrency(UltraCurrency currency);

    UltraCurrency getCurrency();

}
