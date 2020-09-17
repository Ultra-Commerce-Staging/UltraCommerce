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
package com.ultracommerce.core.checkout.service;

import com.ultracommerce.common.currency.UltraCurrencyProvider;
import com.ultracommerce.common.currency.domain.UltraCurrency;
import com.ultracommerce.common.currency.service.UltraCurrencyService;
import com.ultracommerce.common.encryption.EncryptionModule;
import com.ultracommerce.common.i18n.domain.ISOCountry;
import com.ultracommerce.common.i18n.domain.ISOCountryImpl;
import com.ultracommerce.common.money.CurrencyConversionContext;
import com.ultracommerce.common.money.CurrencyConversionService;
import com.ultracommerce.common.money.Money;
import com.ultracommerce.common.payment.PaymentTransactionType;
import com.ultracommerce.common.payment.PaymentType;
import com.ultracommerce.common.time.SystemTime;
import com.ultracommerce.core.catalog.domain.Sku;
import com.ultracommerce.core.catalog.domain.SkuImpl;
import com.ultracommerce.core.catalog.service.CatalogService;
import com.ultracommerce.core.checkout.service.workflow.CheckoutResponse;
import com.ultracommerce.core.order.domain.DiscreteOrderItem;
import com.ultracommerce.core.order.domain.DiscreteOrderItemImpl;
import com.ultracommerce.core.order.domain.FulfillmentGroup;
import com.ultracommerce.core.order.domain.FulfillmentGroupImpl;
import com.ultracommerce.core.order.domain.FulfillmentGroupItem;
import com.ultracommerce.core.order.domain.FulfillmentGroupItemImpl;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.domain.OrderItem;
import com.ultracommerce.core.order.fulfillment.domain.FixedPriceFulfillmentOption;
import com.ultracommerce.core.order.fulfillment.domain.FixedPriceFulfillmentOptionImpl;
import com.ultracommerce.core.order.service.OrderItemService;
import com.ultracommerce.core.order.service.OrderService;
import com.ultracommerce.core.order.service.type.FulfillmentType;
import com.ultracommerce.core.payment.domain.OrderPayment;
import com.ultracommerce.core.payment.domain.OrderPaymentImpl;
import com.ultracommerce.core.payment.domain.PaymentTransaction;
import com.ultracommerce.core.payment.domain.PaymentTransactionImpl;
import com.ultracommerce.core.payment.domain.secure.CreditCardPayment;
import com.ultracommerce.core.payment.service.NullIntegrationGatewayType;
import com.ultracommerce.core.payment.service.SecureOrderPaymentService;
import com.ultracommerce.profile.core.domain.Address;
import com.ultracommerce.profile.core.domain.AddressImpl;
import com.ultracommerce.profile.core.domain.Country;
import com.ultracommerce.profile.core.domain.CountryImpl;
import com.ultracommerce.profile.core.domain.Customer;
import com.ultracommerce.profile.core.domain.State;
import com.ultracommerce.profile.core.domain.StateImpl;
import com.ultracommerce.profile.core.service.CustomerService;
import com.ultracommerce.test.TestNGSiteIntegrationSetup;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

public class CheckoutTest extends TestNGSiteIntegrationSetup {
    
    @Resource(name="ucCheckoutService")
    private CheckoutService checkoutService;
    
    @Resource(name="ucEncryptionModule")
    private EncryptionModule encryptionModule;

    @Resource(name = "ucCustomerService")
    private CustomerService customerService;
    
    @Resource(name = "ucOrderService")
    private OrderService orderService;

    @Resource(name = "ucCatalogService")
    private CatalogService catalogService;
    
    @Resource(name = "ucOrderItemService")
    private OrderItemService orderItemService;

    @Resource(name = "ucSecureOrderPaymentService")
    private SecureOrderPaymentService securePaymentInfoService;
    
    @Resource(name = "ucCurrencyService")
    protected UltraCurrencyService currencyService;

    @Test(groups = { "checkout" }, dependsOnGroups = { "createCartForCustomer", "testShippingInsert" }, dataProvider = "USCurrency", dataProviderClass = UltraCurrencyProvider.class)
    @Transactional
    public void testCheckout(UltraCurrency usCurrency) throws Exception {
        HashMap currencyConsiderationContext = new HashMap();
        currencyConsiderationContext.put("aa","bb");
        CurrencyConversionContext.setCurrencyConversionContext(currencyConsiderationContext);
        CurrencyConversionContext.setCurrencyConversionService(new CurrencyConversionService() {
            @Override
            public Money convertCurrency(Money source, Currency destinationCurrency, int destinationScale) {
                return source;
            }
        });
        String userName = "customer1";
        Customer customer = customerService.readCustomerByUsername(userName);
        Order order = orderService.createNewCartForCustomer(customer);
        usCurrency = currencyService.save(usCurrency);
        order.setCurrency(usCurrency);

        Address address = buildAddress();
        FulfillmentGroup group = buildFulfillmentGroup(order, address);
        addSampleItemToOrder(order, group);
        order.setTotalShipping(new Money(0D));
        addPaymentToOrder(order, address);
        
        //execute pricing for this order
        orderService.save(order, true);
        CheckoutResponse response = checkoutService.performCheckout(order);
        
        assert (order.getTotal().greaterThan(order.getSubTotal()));
    }



    private OrderPayment addPaymentToOrder(Order order, Address address) {
        OrderPayment payment = new OrderPaymentImpl();
        payment.setBillingAddress(address);
        payment.setAmount(new Money(15D + (15D * 0.05D)));
        payment.setReferenceNumber("1234");
        payment.setType(PaymentType.CREDIT_CARD);
        payment.setPaymentGatewayType(NullIntegrationGatewayType.NULL_INTEGRATION_GATEWAY);
        payment.setOrder(order);
        PaymentTransaction tx = new PaymentTransactionImpl();
        tx.setAmount(payment.getAmount());
        tx.setType(PaymentTransactionType.AUTHORIZE_AND_CAPTURE);
        tx.setOrderPayment(payment);
        payment.getTransactions().add(tx);

        CreditCardPayment cc = new CreditCardPayment() {

            private static final long serialVersionUID = 1L;
            private String referenceNumber = "1234";

            @Override
            public String getCvvCode() {
                return "123";
            }

            @Override
            public Integer getExpirationMonth() {
                return 11;
            }

            @Override
            public Integer getExpirationYear() {
                return 2011;
            }

            @Override
            public Long getId() {
                return null;
            }

            @Override
            public String getPan() {
                return "1111111111111111";
            }

            @Override
            public String getNameOnCard() {
                return "Cardholder Name";
            }

            @Override
            public void setCvvCode(String cvvCode) {
                //do nothing
            }

            @Override
            public void setExpirationMonth(Integer expirationMonth) {
                //do nothing
            }

            @Override
            public void setExpirationYear(Integer expirationYear) {
                //do nothing
            }

            @Override
            public void setId(Long id) {
                //do nothing
            }

            @Override
            public void setNameOnCard(String nameOnCard) {
                //do nothing
            }

            @Override
            public void setPan(String pan) {
                //do nothing
            }

            @Override
            public EncryptionModule getEncryptionModule() {
                return encryptionModule;
            }

            @Override
            public String getReferenceNumber() {
                return referenceNumber;
            }

            @Override
            public void setEncryptionModule(EncryptionModule encryptionModule) {
                //do nothing
            }

            @Override
            public void setReferenceNumber(String referenceNumber) {
                this.referenceNumber = referenceNumber;
            }

        };

        order.getPayments().add(payment);
        return payment;
    }

    private void addSampleItemToOrder(Order order, FulfillmentGroup group) {
        DiscreteOrderItem item = new DiscreteOrderItemImpl();
        item.setOrder(order);
        item.setQuantity(1);

        Sku newSku = new SkuImpl();
        newSku.setName("Under Armor T-Shirt -- Red");
        newSku.setRetailPrice(new Money(14.99));
        newSku.setActiveStartDate(SystemTime.asDate());
        newSku.setDiscountable(false);
        newSku = catalogService.saveSku(newSku);
        item.setSku(newSku);

        item = (DiscreteOrderItem) orderItemService.saveOrderItem(item);

        List<OrderItem> items = new ArrayList<>();
        items.add(item);
        order.setOrderItems(items);

        FulfillmentGroupItem fgItem = new FulfillmentGroupItemImpl();
        fgItem.setFulfillmentGroup(group);
        fgItem.setOrderItem(item);
        fgItem.setQuantity(1);
        //fgItem.setPrice(new Money(0D));
        group.addFulfillmentGroupItem(fgItem);
    }

    private FulfillmentGroup buildFulfillmentGroup(Order order, Address address) {
        FulfillmentGroup group = new FulfillmentGroupImpl();
        group.setIsShippingPriceTaxable(true);
        group.setOrder(order);
        group.setAddress(address);
        List<FulfillmentGroup> groups = new ArrayList<>();
        groups.add(group);
        order.setFulfillmentGroups(groups);
        Money total = new Money(5D);
        group.setShippingPrice(total);
        FixedPriceFulfillmentOption option = new FixedPriceFulfillmentOptionImpl();
        option.setPrice(new Money(0));
        option.setFulfillmentType(FulfillmentType.PHYSICAL_SHIP);
        group.setFulfillmentOption(option);
        return group;
    }

    private Address buildAddress() {
        Address address = new AddressImpl();
        address.setAddressLine1("123 Test Rd");
        address.setCity("Dallas");
        address.setFirstName("Jeff");
        address.setLastName("Fischer");
        address.setPostalCode("75240");
        address.setPrimaryPhone("972-978-9067");
        State state = new StateImpl();
        state.setAbbreviation("ALL");
        state.setName("ALL");
        address.setState(state);
        Country country = new CountryImpl();
        country.setAbbreviation("US");
        country.setName("United States");
        state.setCountry(country);
        address.setCountry(country);
        ISOCountry isoCountry = new ISOCountryImpl();
        isoCountry.setAlpha2("US");
        isoCountry.setName("UNITED STATES");
        address.setIsoCountryAlpha2(isoCountry);
        return address;
    }
}
