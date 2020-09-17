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
package com.ultracommerce.common.currency.domain;


import com.ultracommerce.common.admin.domain.AdminMainEntity;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransform;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransformMember;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import com.ultracommerce.common.presentation.AdminPresentation;
import com.ultracommerce.common.presentation.AdminPresentationClass;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Author: jerryocanas Date: 9/6/12
 */

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "UC_CURRENCY")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "ucCMSElements")
@AdminPresentationClass(friendlyName = "UltraCurrencyImpl_baseCurrency")
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_SITE),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.AUDITABLE_ONLY)
})
public class UltraCurrencyImpl implements UltraCurrency, AdminMainEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "CURRENCY_CODE")
    @AdminPresentation(friendlyName = "UltraCurrencyImpl_Currency_Code",
            order = 1, group = "UltraCurrencyImpl_Details", prominent = true, gridOrder = 2000)
    protected String currencyCode;

    @Column(name = "FRIENDLY_NAME")
    @AdminPresentation(friendlyName = "UltraCurrencyImpl_Name", order = 2, group = "UltraCurrencyImpl_Details",
            prominent = true, gridOrder = 1000)
    protected String friendlyName;

    @Column(name = "DEFAULT_FLAG")
    @AdminPresentation(friendlyName = "UltraCurrencyImpl_Is_Default", group = "UltraCurrencyImpl_Details", excluded = true)
    protected Boolean defaultFlag = false;
    
    @Transient
    protected java.util.Currency javaCurrency;

    @Override
    public String getCurrencyCode() {
        return currencyCode;
    }
    
    public java.util.Currency getJavaCurrency() {
        if (javaCurrency == null && getCurrencyCode() != null) {
            javaCurrency = java.util.Currency.getInstance(getCurrencyCode());
        }
        return javaCurrency;
    }

    @Override
    public void setCurrencyCode(String code) {
        this.currencyCode = code;
    }

    @Override
    public String getFriendlyName() {
        return friendlyName;
    }

    @Override
    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    @Override
    public boolean getDefaultFlag() {
        if (defaultFlag == null) {
            return false;
        }
        return defaultFlag.booleanValue();
    }

    @Override
    public void setDefaultFlag(boolean defaultFlag) {
        this.defaultFlag = new Boolean(defaultFlag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) return false;
        if (!getClass().isAssignableFrom(o.getClass())) {
            return false;
        }

        UltraCurrencyImpl currency = (UltraCurrencyImpl) o;

        if (currencyCode != null ? !currencyCode.equals(currency.currencyCode) : currency.currencyCode != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = currencyCode != null ? currencyCode.hashCode() : 0;
        return result;
    }

    @Override
    public String getMainEntityName() {
        if (getFriendlyName() != null) {
            return getFriendlyName() + " (" + getCurrencyCode() + ")";
        } else {
            return getCurrencyCode();
        }
    }
}
