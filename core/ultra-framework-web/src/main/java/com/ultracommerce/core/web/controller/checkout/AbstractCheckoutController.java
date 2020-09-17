/*
 * #%L
 * UltraCommerce Framework Web
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
package com.ultracommerce.core.web.controller.checkout;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.i18n.service.ISOService;
import com.ultracommerce.common.payment.service.PaymentGatewayCheckoutService;
import com.ultracommerce.common.web.controller.UltraAbstractController;
import com.ultracommerce.core.checkout.service.CheckoutService;
import com.ultracommerce.core.order.service.FulfillmentGroupService;
import com.ultracommerce.core.order.service.FulfillmentOptionService;
import com.ultracommerce.core.order.service.OrderMultishipOptionService;
import com.ultracommerce.core.order.service.OrderService;
import com.ultracommerce.core.payment.service.OrderPaymentService;
import com.ultracommerce.core.payment.service.OrderToPaymentRequestDTOService;
import com.ultracommerce.core.web.checkout.validator.BillingInfoFormValidator;
import com.ultracommerce.core.web.checkout.validator.CheckoutPaymentInfoFormValidator;
import com.ultracommerce.core.web.checkout.validator.GiftCardInfoFormValidator;
import com.ultracommerce.core.web.checkout.validator.MultishipAddAddressFormValidator;
import com.ultracommerce.core.web.checkout.validator.OrderInfoFormValidator;
import com.ultracommerce.core.web.checkout.validator.ShippingInfoFormValidator;
import com.ultracommerce.core.web.order.service.CartStateService;
import com.ultracommerce.core.web.service.InitBinderService;
import com.ultracommerce.profile.core.service.AddressService;
import com.ultracommerce.profile.core.service.CountryService;
import com.ultracommerce.profile.core.service.CountrySubdivisionService;
import com.ultracommerce.profile.core.service.CustomerAddressService;
import com.ultracommerce.profile.core.service.CustomerPaymentService;
import com.ultracommerce.profile.core.service.CustomerService;
import com.ultracommerce.profile.core.service.PhoneService;
import com.ultracommerce.profile.core.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * An abstract controller that provides convenience methods and resource declarations for its
 * children. Operations that are shared between controllers that deal with checkout belong here.
 *
 * @author Andre Azzolini (apazzolini)
 * @author Elbert Bautista (elbertbautista)
 * @author Joshua Skorton (jskorton)
 */
public abstract class AbstractCheckoutController extends UltraAbstractController {

    private static final Log LOG = LogFactory.getLog(AbstractCheckoutController.class);

    protected static String ACTIVE_STAGE = "activeStage";

    protected static String cartPageRedirect = "redirect:/cart";
    protected static String checkoutView = "checkout/checkout";
    protected static String checkoutStagesPartial = "checkout/partials/checkoutStages";
    protected static String checkoutPageRedirect = "redirect:/checkout";
    protected static String baseConfirmationView = "ajaxredirect:/confirmation";

    /* Optional Service */
    @Autowired(required=false)
    @Qualifier("ucPaymentGatewayCheckoutService")
    protected PaymentGatewayCheckoutService paymentGatewayCheckoutService;

    /* Services */
    @Resource(name = "ucOrderService")
    protected OrderService orderService;

    @Resource(name = "ucOrderPaymentService")
    protected OrderPaymentService orderPaymentService;

    @Resource(name = "ucOrderToPaymentRequestDTOService")
    protected OrderToPaymentRequestDTOService dtoTranslationService;

    @Resource(name = "ucFulfillmentGroupService")
    protected FulfillmentGroupService fulfillmentGroupService;

    @Resource(name = "ucFulfillmentOptionService")
    protected FulfillmentOptionService fulfillmentOptionService;

    @Resource(name = "ucCheckoutService")
    protected CheckoutService checkoutService;
    
    @Resource(name = "ucCustomerService")
    protected CustomerService customerService;

    @Resource(name = "ucCustomerPaymentService")
    protected CustomerPaymentService customerPaymentService;

    @Resource(name = "ucStateService")
    protected StateService stateService;

    @Resource(name = "ucCountryService")
    protected CountryService countryService;

    @Resource(name = "ucCountrySubdivisionService")
    protected CountrySubdivisionService countrySubdivisionService;

    @Resource(name = "ucISOService")
    protected ISOService isoService;

    @Resource(name = "ucCustomerAddressService")
    protected CustomerAddressService customerAddressService;

    @Resource(name = "ucAddressService")
    protected AddressService addressService;

    @Resource(name = "ucPhoneService")
    protected PhoneService phoneService;

    @Resource(name = "ucOrderMultishipOptionService")
    protected OrderMultishipOptionService orderMultishipOptionService;

    /* Validators */
    @Resource(name = "ucShippingInfoFormValidator")
    protected ShippingInfoFormValidator shippingInfoFormValidator;

    @Resource(name = "ucBillingInfoFormValidator")
    protected BillingInfoFormValidator billingInfoFormValidator;

    @Resource(name = "ucCheckoutPaymentInfoFormValidator")
    protected CheckoutPaymentInfoFormValidator paymentInfoFormValidator;

    @Resource(name = "ucGiftCardInfoFormValidator")
    protected GiftCardInfoFormValidator giftCardInfoFormValidator;

    @Resource(name = "ucMultishipAddAddressFormValidator")
    protected MultishipAddAddressFormValidator multishipAddAddressFormValidator;

    @Resource(name = "ucOrderInfoFormValidator")
    protected OrderInfoFormValidator orderInfoFormValidator;

    @Resource(name = "ucCartStateService")
    protected CartStateService cartStateService;

    @Resource(name = "ucInitBinderService")
    protected InitBinderService initBinderService;

    /* Extension Managers */
    @Resource(name = "ucCheckoutControllerExtensionManager")
    protected UltraCheckoutControllerExtensionManager checkoutControllerExtensionManager;

    /* Views and Redirects */
    public String getCartPageRedirect() {
        return cartPageRedirect;
    }

    public String getCheckoutView() {
        return checkoutView;
    }

    public String getCheckoutStagesPartial() {
        return checkoutStagesPartial;
    }

    public String getCheckoutPageRedirect() {
        return checkoutPageRedirect;
    }

    public String getBaseConfirmationView() {
        return baseConfirmationView;
    }

    protected String getConfirmationView(String orderNumber) {
        return getBaseConfirmationView() + "/" + orderNumber;
    }

    protected void populateModelWithReferenceData(HttpServletRequest request, Model model) {
        //Add module specific model variables
        checkoutControllerExtensionManager.getProxy().addAdditionalModelVariables(model);
    }

    /**
     * Initializes some custom binding operations for the checkout flow.
     * More specifically, this method will attempt to bind state and country
     * abbreviations to actual State and Country objects when the String
     * representation of the abbreviation is submitted.
     *
     * @param request
     * @param binder
     * @throws Exception
     */
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        initBinderService.configAddressInitBinder(binder);
    }

}
