/*
 * #%L
 * UltraCommerce Framework Web
 * %%
 * Copyright (C) 2009 - 2017 Ultra Commerce
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
package com.ultracommerce.core.web.checkout.service;

import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.web.checkout.model.BillingInfoForm;
import com.ultracommerce.core.web.checkout.model.OrderInfoForm;
import com.ultracommerce.core.web.checkout.model.PaymentInfoForm;
import com.ultracommerce.core.web.checkout.model.ShippingInfoForm;
import org.springframework.ui.Model;

public interface CheckoutFormService {

    OrderInfoForm prePopulateOrderInfoForm(OrderInfoForm orderInfoForm, Order cart);

    ShippingInfoForm prePopulateShippingInfoForm(ShippingInfoForm shippingInfoForm, Order cart);

    BillingInfoForm prePopulateBillingInfoForm(BillingInfoForm billingInfoForm, ShippingInfoForm shippingInfoForm, Order cart);

    PaymentInfoForm prePopulatePaymentInfoForm(PaymentInfoForm paymentInfoForm, ShippingInfoForm shippingInfoForm, Order cart);

    void prePopulateInfoForms(ShippingInfoForm shippingInfoForm, PaymentInfoForm paymentInfoForm);

    void determineIfSavedAddressIsSelected(Model model, ShippingInfoForm shippingInfoForm, PaymentInfoForm paymentInfoForm);
}
