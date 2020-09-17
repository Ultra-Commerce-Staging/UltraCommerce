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
import com.ultracommerce.common.currency.domain.UltraCurrencyImpl;
import com.ultracommerce.common.currency.util.UltraCurrencyUtils;
import com.ultracommerce.common.money.Money;
import com.ultracommerce.common.payment.PaymentLogEventType;
import com.ultracommerce.common.payment.PaymentTransactionType;
import com.ultracommerce.common.presentation.AdminPresentation;
import com.ultracommerce.profile.core.domain.Customer;
import com.ultracommerce.profile.core.domain.CustomerImpl;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Parameter;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @deprecated - payment logs should now be captured as raw responses in Payment Transaction line items
 */
@Deprecated
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "UC_PAYMENT_LOG")
public class PaymentLogImpl implements PaymentLog {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "PaymentLogId")
    @GenericGenerator(
        name="PaymentLogId",
        strategy="com.ultracommerce.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="PaymentLogImpl"),
            @Parameter(name="entity_name", value="com.ultracommerce.core.payment.domain.PaymentLogImpl")
        }
    )
    @Column(name = "PAYMENT_LOG_ID")
    protected Long id;

    @Column(name = "USER_NAME", nullable=false)
    @Index(name="PAYMENTLOG_USER_INDEX", columnNames={"USER_NAME"})
    @AdminPresentation(friendlyName = "PaymentLogImpl_User_Name", order = 1, group = "PaymentLogImpl_Payment_Log", readOnly = true)
    protected String userName;

    @Column(name = "TRANSACTION_TIMESTAMP", nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    @AdminPresentation(friendlyName = "PaymentLogImpl_Transaction_Time", order = 3, group = "PaymentLogImpl_Payment_Log", readOnly = true)
    protected Date transactionTimestamp;

    @Column(name = "ORDER_PAYMENT_ID")
    @Index(name="PAYMENTLOG_ORDERPAYMENT_INDEX", columnNames={"ORDER_PAYMENT_ID"})
    @AdminPresentation(excluded = true, readOnly = true)
    protected Long orderPaymentId;

    @ManyToOne(targetEntity = CustomerImpl.class)
    @JoinColumn(name = "CUSTOMER_ID")
    @Index(name="PAYMENTLOG_CUSTOMER_INDEX", columnNames={"CUSTOMER_ID"})
    protected Customer customer;

    @Column(name = "ORDER_PAYMENT_REF_NUM")
    @Index(name="PAYMENTLOG_REFERENCE_INDEX", columnNames={"ORDER_PAYMENT_REFERENCE_NUMBER"})
    @AdminPresentation(friendlyName = "PaymentLogImpl_Payment_Ref_Number", order = 4, group = "PaymentLogImpl_Payment_Log", readOnly = true)
    protected String orderPaymentReferenceNumber;

    @Column(name = "TRANSACTION_TYPE", nullable=false)
    @Index(name="PAYMENTLOG_TRANTYPE_INDEX", columnNames={"TRANSACTION_TYPE"})
    @AdminPresentation(friendlyName = "PaymentLogImpl_Transaction_Type", order = 5, group = "PaymentLogImpl_Payment_Log", readOnly = true)
    protected String transactionType;

    @Column(name = "TRANSACTION_SUCCESS")
    @AdminPresentation(friendlyName = "PaymentLogImpl_Transaction_Successfule", order = 6, group = "PaymentLogImpl_Payment_Log", readOnly = true)
    protected Boolean transactionSuccess = false;

    @Column(name = "EXCEPTION_MESSAGE")
    @AdminPresentation(friendlyName = "PaymentLogImpl_Exception_Message", order = 7, group = "PaymentLogImpl_Payment_Log", readOnly = true)
    protected String exceptionMessage;

    @Column(name = "LOG_TYPE", nullable=false)
    @Index(name="PAYMENTLOG_LOGTYPE_INDEX", columnNames={"LOG_TYPE"})
    @AdminPresentation(friendlyName = "PaymentLogImpl_Type", order = 8, group = "PaymentLogImpl_Payment_Log", readOnly = true)
    protected String logType;

    @Column(name = "AMOUNT_PAID", precision=19, scale=5)
    @AdminPresentation(friendlyName = "PaymentLogImpl_Amount", order = 2, group = "PaymentLogImpl_Payment_Log", readOnly = true)
    protected BigDecimal amountPaid;

    @ManyToOne(targetEntity = UltraCurrencyImpl.class)
    @JoinColumn(name = "CURRENCY_CODE")
    @AdminPresentation(friendlyName = "PaymentLogImpl_currency", order = 2, group = "PaymentLogImpl_Payment_Log", readOnly = true)
    protected UltraCurrency currency;
    
    
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public Date getTransactionTimestamp() {
        return transactionTimestamp;
    }

    @Override
    public void setTransactionTimestamp(Date transactionTimestamp) {
        this.transactionTimestamp = transactionTimestamp;
    }

    @Override
    public Long getPaymentInfoId() {
        return orderPaymentId;
    }

    @Override
    public void setPaymentInfoId(Long paymentInfoId) {
        this.orderPaymentId = paymentInfoId;
    }

    @Override
    public Customer getCustomer() {
        return customer;
    }

    @Override
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String getPaymentInfoReferenceNumber() {
        return orderPaymentReferenceNumber;
    }

    @Override
    public void setPaymentInfoReferenceNumber(String paymentInfoReferenceNumber) {
        this.orderPaymentReferenceNumber = paymentInfoReferenceNumber;
    }

    @Override
    public PaymentTransactionType getTransactionType() {
        return PaymentTransactionType.getInstance(transactionType);
    }

    @Override
    public void setTransactionType(PaymentTransactionType transactionType) {
        this.transactionType = transactionType.getType();
    }

    @Override
    public PaymentLogEventType getLogType() {
        return PaymentLogEventType.getInstance(logType);
    }

    @Override
    public void setLogType(PaymentLogEventType logType) {
        this.logType = logType.getType();
    }

    @Override
    public Boolean getTransactionSuccess() {
        if (transactionSuccess == null) {
            return Boolean.FALSE;
        } else {
            return transactionSuccess;
        }
    }

    @Override
    public void setTransactionSuccess(Boolean transactionSuccess) {
        this.transactionSuccess = transactionSuccess;
    }

    @Override
    public String getExceptionMessage() {
        return exceptionMessage;
    }

    @Override
    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    @Override
    public Money getAmountPaid() {
        return UltraCurrencyUtils.getMoney(amountPaid, currency);
    }

    @Override
    public void setAmountPaid(Money amountPaid) {
        this.amountPaid = Money.toAmount(amountPaid);
    }

    @Override
    public UltraCurrency getCurrency() {
        return currency;
    }

    @Override
    public void setCurrency(UltraCurrency currency) {
        this.currency = currency;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((customer == null) ? 0 : customer.hashCode());
        result = prime * result + ((orderPaymentId == null) ? 0 : orderPaymentId.hashCode());
        result = prime * result + ((orderPaymentReferenceNumber == null) ? 0 : orderPaymentReferenceNumber.hashCode());
        result = prime * result + ((transactionTimestamp == null) ? 0 : transactionTimestamp.hashCode());
        result = prime * result + ((userName == null) ? 0 : userName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!getClass().isAssignableFrom(obj.getClass())) {
            return false;
        }
        PaymentLogImpl other = (PaymentLogImpl) obj;

        if (id != null && other.id != null) {
            return id.equals(other.id);
        }

        if (customer == null) {
            if (other.customer != null) {
                return false;
            }
        } else if (!customer.equals(other.customer)) {
            return false;
        }
        if (orderPaymentId == null) {
            if (other.orderPaymentId != null) {
                return false;
            }
        } else if (!orderPaymentId.equals(other.orderPaymentId)) {
            return false;
        }
        if (orderPaymentReferenceNumber == null) {
            if (other.orderPaymentReferenceNumber != null) {
                return false;
            }
        } else if (!orderPaymentReferenceNumber.equals(other.orderPaymentReferenceNumber)) {
            return false;
        }
        if (transactionTimestamp == null) {
            if (other.transactionTimestamp != null) {
                return false;
            }
        } else if (!transactionTimestamp.equals(other.transactionTimestamp)) {
            return false;
        }
        if (userName == null) {
            if (other.userName != null) {
                return false;
            }
        } else if (!userName.equals(other.userName)) {
            return false;
        }
        return true;
    }
}
