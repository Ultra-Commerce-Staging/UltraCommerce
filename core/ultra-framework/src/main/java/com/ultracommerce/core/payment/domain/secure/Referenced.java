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
package com.ultracommerce.core.payment.domain.secure;

import com.ultracommerce.common.encryption.EncryptionModule;
import com.ultracommerce.core.payment.domain.OrderPayment;
import com.ultracommerce.core.payment.service.SecureOrderPaymentService;

import java.io.Serializable;

import javax.annotation.Nonnull;

/**
 * <p>The main interface used to store extra-secure data such as credit card, bank accounts and gift card data. All entities
 * that implement this interface should be stored in a completely separate database under strict PCI compliance. Ultra
 * provides the ability for this in the ucSecurePU persistence unit, which all implementing entities are members of.</p>
 *
 * <p>Entities that implement this {@link Referenced} interface should not be instantiated directly but rather be instaniated
 * via {@link SecureOrderPaymentService#create(com.ultracommerce.core.payment.service.type.PaymentType)}</p>
 * 
 * <p>In the common case, this is rarely used as most implementors will NOT want to deal with the liability and extra PCI
 * requirements associated with storing sensitive payment data. Consider integrating with a payment provider that takes
 * care of PCI-sensitive data instead.</p>
 *
 * @see {@link CreditCardPayment}
 * @see {@link GiftCardPayment}
 * @see {@link BankAccountPayment}
 * @author Phillip Verheyden (phillipuniverse)
 */
public interface Referenced extends Serializable {

    public Long getId();
    
    public void setId(Long id);
    
    /**
     * <p>The indirect link between non-secure data and the secure data represented here. Since implementing entities
     * should be in a separate persistence unit (ucSecurePU), this is the only avenue to show a relationship between the two.</p>
     * 
     * <p>From the {@link Order} side of the domain, this is linked by {@link OrderPayment#getReferenceNumber()} on the
     * {@link OrderPayment} entity.</p>
     * 
     * @see {@link OrderPayment#getReferenceNumber()}
     */
    public String getReferenceNumber();

    /**
     * Set the link between this secure entity and the {@link OrderPayment}. This should not be null as this is required
     * @param referenceNumber
     */
    public void setReferenceNumber(@Nonnull String referenceNumber);

    /**
     * @return the {@link EncryptionModule} used to encrypt and decrypt this secure information back and forth
     */
    public EncryptionModule getEncryptionModule();

    /**
     * Sets the encryption module used by to encrypt and decrypt the data saved in the ucSecurePU persistence unit. This
     * normally corresponds to the ucEncryptionModule Spring bean which should be automatically set after invoking
     * {@link SecureOrderPaymentService#findSecurePaymentInfo(String, com.ultracommerce.core.payment.service.type.PaymentType)}
     * and {@link SecureOrderPaymentService#create(com.ultracommerce.core.payment.service.type.PaymentType)}.
     * 
     * @see {@link SecureOrderPaymentService#findSecurePaymentInfo(String, com.ultracommerce.core.payment.service.type.PaymentType)}
     * @see {@link SecureOrderPaymentService#create(com.ultracommerce.core.payment.service.type.PaymentType)}
     */
    public void setEncryptionModule(EncryptionModule encryptionModule);
    
}
