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
package com.ultracommerce.core.web.controller.account;


import org.apache.commons.lang.StringUtils;
import com.ultracommerce.common.web.controller.UltraAbstractController;
import com.ultracommerce.core.web.checkout.model.PaymentInfoForm;
import com.ultracommerce.core.web.controller.account.validator.AccountPaymentInfoFormValidator;
import com.ultracommerce.core.web.payment.service.SavedPaymentService;
import com.ultracommerce.core.web.service.InitBinderService;
import com.ultracommerce.profile.core.domain.Customer;
import com.ultracommerce.profile.core.domain.CustomerPayment;
import com.ultracommerce.profile.core.service.AddressService;
import com.ultracommerce.profile.core.service.CustomerAddressService;
import com.ultracommerce.profile.core.service.CustomerPaymentService;
import com.ultracommerce.profile.web.core.CustomerState;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * This is the page controller for adding, updating, and deleting a customer's saved payments.
 *
 * @author Chris Kittrell (ckittrell)
 * @author Jacob Mitash
 */
public class UltraManageCustomerPaymentsController extends UltraAbstractController {

    protected static String customerPaymentView = "account/manageCustomerPayments";
    protected static String customerPaymentRedirect = "redirect:/account/payments";

    @Resource(name = "ucSavedPaymentService")
    protected SavedPaymentService savedPaymentService;

    @Resource(name = "ucCustomerPaymentService")
    protected CustomerPaymentService customerPaymentService;

    @Resource(name = "ucCustomerAddressService")
    protected CustomerAddressService customerAddressService;

    @Resource(name = "ucAccountPaymentInfoFormValidator")
    protected AccountPaymentInfoFormValidator paymentInfoFormValidator;

    @Resource(name = "ucAddressService")
    protected AddressService addressService;

    @Resource(name = "ucInitBinderService")
    protected InitBinderService initBinderService;


    public String viewCustomerPayments(HttpServletRequest request, Model model, PaymentInfoForm paymentInfoForm) {
        Customer customer = CustomerState.getCustomer();

        if (customer == null) {
            throw new SecurityException("Customer is not found but tried to access account page");
        }

        return getCustomerPaymentView();
    }

    public String addCustomerPayment(HttpServletRequest request, Model model,
            PaymentInfoForm paymentInfoForm, BindingResult bindingResult) {
        Customer customer = CustomerState.getCustomer();

        addressService.populateAddressISOCountrySub(paymentInfoForm.getAddress());
        paymentInfoFormValidator.validate(paymentInfoForm, bindingResult);

        if (!bindingResult.hasErrors()) {
            if ((paymentInfoForm.getAddress().getPhonePrimary() != null) &&
                    (StringUtils.isEmpty(paymentInfoForm.getAddress().getPhonePrimary().getPhoneNumber()))) {
                paymentInfoForm.getAddress().setPhonePrimary(null);
            }
            if ((paymentInfoForm.getAddress().getPhoneSecondary() != null) &&
                    (StringUtils.isEmpty(paymentInfoForm.getAddress().getPhoneSecondary().getPhoneNumber()))) {
                paymentInfoForm.getAddress().setPhoneSecondary(null);
            }
            if ((paymentInfoForm.getAddress().getPhoneFax() != null) &&
                    (StringUtils.isEmpty(paymentInfoForm.getAddress().getPhoneFax().getPhoneNumber()))) {
                paymentInfoForm.getAddress().setPhoneFax(null);
            }

            savedPaymentService.addSavedPayment(customer, paymentInfoForm);

            return getCustomerPaymentRedirect();
        }

        return getCustomerPaymentView();
    }

    public String makeDefaultCustomerPayment(HttpServletRequest request, Model model, Long customerPaymentId) {
        CustomerPayment customerPayment = customerPaymentService.readCustomerPaymentById(customerPaymentId);

        if (customerPayment == null) {
            throw new IllegalArgumentException("Requested customer payment does not exist.");
        }

        customerPaymentService.setAsDefaultPayment(customerPayment);

        return getCustomerPaymentView();
    }

    public String removeCustomerPayment(HttpServletRequest request, Model model, Long customerPaymentId) {
        customerPaymentService.deleteCustomerPaymentById(customerPaymentId);

        return getCustomerPaymentView();
    }

    public String getCustomerPaymentView() {
        return customerPaymentView;
    }

    public String getCustomerPaymentRedirect() {
        return customerPaymentRedirect;
    }

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        initBinderService.configAddressInitBinder(binder);
    }
}
