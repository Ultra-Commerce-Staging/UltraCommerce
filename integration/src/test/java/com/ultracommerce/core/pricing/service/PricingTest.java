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
package com.ultracommerce.core.pricing.service;

import com.ultracommerce.common.i18n.domain.ISOCountry;
import com.ultracommerce.common.i18n.domain.ISOCountryImpl;
import com.ultracommerce.common.i18n.service.ISOService;
import com.ultracommerce.common.money.Money;
import com.ultracommerce.common.time.SystemTime;
import com.ultracommerce.core.catalog.domain.Sku;
import com.ultracommerce.core.catalog.domain.SkuFee;
import com.ultracommerce.core.catalog.domain.SkuFeeImpl;
import com.ultracommerce.core.catalog.domain.SkuImpl;
import com.ultracommerce.core.catalog.service.CatalogService;
import com.ultracommerce.core.catalog.service.type.SkuFeeType;
import com.ultracommerce.core.offer.domain.Offer;
import com.ultracommerce.core.offer.domain.OfferCode;
import com.ultracommerce.core.offer.domain.OfferCodeImpl;
import com.ultracommerce.core.offer.domain.OfferImpl;
import com.ultracommerce.core.offer.domain.OfferItemCriteria;
import com.ultracommerce.core.offer.domain.OfferItemCriteriaImpl;
import com.ultracommerce.core.offer.domain.OfferTargetCriteriaXref;
import com.ultracommerce.core.offer.domain.OfferTargetCriteriaXrefImpl;
import com.ultracommerce.core.offer.service.OfferService;
import com.ultracommerce.core.offer.service.type.OfferDiscountType;
import com.ultracommerce.core.offer.service.type.OfferType;
import com.ultracommerce.core.order.domain.DiscreteOrderItem;
import com.ultracommerce.core.order.domain.DiscreteOrderItemImpl;
import com.ultracommerce.core.order.domain.FulfillmentGroup;
import com.ultracommerce.core.order.domain.FulfillmentGroupFee;
import com.ultracommerce.core.order.domain.FulfillmentGroupImpl;
import com.ultracommerce.core.order.domain.FulfillmentGroupItem;
import com.ultracommerce.core.order.domain.FulfillmentGroupItemImpl;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.domain.OrderItem;
import com.ultracommerce.core.order.service.OrderItemService;
import com.ultracommerce.core.order.service.OrderService;
import com.ultracommerce.core.pricing.ShippingRateDataProvider;
import com.ultracommerce.core.pricing.domain.ShippingRate;
import com.ultracommerce.core.pricing.service.workflow.type.ShippingServiceType;
import com.ultracommerce.profile.core.domain.Address;
import com.ultracommerce.profile.core.domain.AddressImpl;
import com.ultracommerce.profile.core.domain.Country;
import com.ultracommerce.profile.core.domain.CountryImpl;
import com.ultracommerce.profile.core.domain.Customer;
import com.ultracommerce.profile.core.domain.State;
import com.ultracommerce.profile.core.domain.StateImpl;
import com.ultracommerce.profile.core.service.CountryService;
import com.ultracommerce.profile.core.service.CustomerService;
import com.ultracommerce.profile.core.service.StateService;
import com.ultracommerce.test.TestNGSiteIntegrationSetup;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

@SuppressWarnings("deprecation")
public class PricingTest extends TestNGSiteIntegrationSetup {

    @Resource
    private CustomerService customerService;

    @Resource(name = "ucOrderService")
    private OrderService orderService;

    @Resource
    private ShippingRateService shippingRateService;
    
    @Resource
    private CatalogService catalogService;
    
    @Resource(name = "ucOrderItemService")
    private OrderItemService orderItemService;
    
    @Resource
    private OfferService offerService;
    
    @Resource
    private CountryService countryService;
    
    @Resource
    private StateService stateService;

    @Resource
    private ISOService isoService;

    @Test(groups =  {"testShippingInsert"}, dataProvider = "basicShippingRates", dataProviderClass = ShippingRateDataProvider.class)
    @Rollback(false)
    public void testShippingInsert(ShippingRate shippingRate, ShippingRate sr2) throws Exception {
        shippingRate = shippingRateService.save(shippingRate);
        sr2 = shippingRateService.save(sr2);
    }

    @Test(groups = {"testPricing"}, dependsOnGroups = { "testShippingInsert", "createCustomerIdGeneration" })
    @Transactional
    public void testPricing() throws Exception {
        Order order = orderService.createNewCartForCustomer(createCustomer());
        
        customerService.saveCustomer(order.getCustomer());

        Country country = new CountryImpl();
        country.setAbbreviation("US");
        country.setName("United States");

        country = countryService.save(country);

        ISOCountry isoCountry = new ISOCountryImpl();
        isoCountry.setAlpha2("US");
        isoCountry.setName("UNITED STATES");

        isoCountry = isoService.save(isoCountry);

        State state = new StateImpl();
        state.setAbbreviation("TX");
        state.setName("Texas");
        state.setCountry(country);

        state = stateService.save(state);
        
        Address address = new AddressImpl();
        address.setAddressLine1("123 Test Rd");
        address.setCity("Dallas");
        address.setFirstName("Jeff");
        address.setLastName("Fischer");
        address.setPostalCode("75240");
        address.setPrimaryPhone("972-978-9067");
        address.setState(state);
        address.setCountry(country);
        address.setIsoCountrySubdivision("US-TX");
        address.setIsoCountryAlpha2(isoCountry);
        
        FulfillmentGroup group = new FulfillmentGroupImpl();
        group.setAddress(address);
        List<FulfillmentGroup> groups = new ArrayList<>();
        group.setMethod("standard");
        group.setService(ShippingServiceType.BANDED_SHIPPING.getType());
        group.setOrder(order);
        groups.add(group);
        order.setFulfillmentGroups(groups);
        Money total = new Money(8.5D);
        group.setShippingPrice(total);

        {
        DiscreteOrderItem item = new DiscreteOrderItemImpl();
        Sku sku = new SkuImpl();
        sku.setName("Test Sku");
        sku.setRetailPrice(new Money(10D));
        sku.setDiscountable(true);
           
        SkuFee fee = new SkuFeeImpl();
        fee.setFeeType(SkuFeeType.FULFILLMENT);
        fee.setName("fee test");
        fee.setAmount(new Money(10D));
        fee = catalogService.saveSkuFee(fee);
        List<SkuFee> fees = new ArrayList<>();
        fees.add(fee);
        
        sku.setFees(fees);
        sku = catalogService.saveSku(sku);
        
        item.setSku(sku);
        item.setQuantity(2);
        item.setOrder(order);
        
        item = (DiscreteOrderItem) orderItemService.saveOrderItem(item);
        
        order.addOrderItem(item);
        FulfillmentGroupItem fgItem = new FulfillmentGroupItemImpl();
        fgItem.setFulfillmentGroup(group);
        fgItem.setOrderItem(item);
        fgItem.setQuantity(2);
        //fgItem.setPrice(new Money(0D));
        group.addFulfillmentGroupItem(fgItem);
        }
        
        {
        DiscreteOrderItem item = new DiscreteOrderItemImpl();
        Sku sku = new SkuImpl();
        sku.setName("Test Product 2");
        sku.setRetailPrice(new Money(20D));
        sku.setDiscountable(true);
        
        sku = catalogService.saveSku(sku);
        
        item.setSku(sku);
        item.setQuantity(1);
        item.setOrder(order);
        
        item = (DiscreteOrderItem) orderItemService.saveOrderItem(item);
        
        order.addOrderItem(item);
        
        FulfillmentGroupItem fgItem = new FulfillmentGroupItemImpl();
        fgItem.setFulfillmentGroup(group);
        fgItem.setOrderItem(item);
        fgItem.setQuantity(1);
        //fgItem.setPrice(new Money(0D));
        group.addFulfillmentGroupItem(fgItem);
        }
        
        order.addOfferCode(createOfferCode("20 Percent Off Item Offer", OfferType.ORDER_ITEM, OfferDiscountType.PERCENT_OFF, 20, null, "discreteOrderItem.sku.name==\"Test Sku\""));
        order.addOfferCode(createOfferCode("3 Dollars Off Item Offer", OfferType.ORDER_ITEM, OfferDiscountType.AMOUNT_OFF, 3, null, "discreteOrderItem.sku.name!=\"Test Sku\""));
        order.addOfferCode(createOfferCode("1.20 Dollars Off Order Offer", OfferType.ORDER, OfferDiscountType.AMOUNT_OFF, 1.20, null, null));
        order.setTotalShipping(new Money(0D));
        
        orderService.save(order, true);

        assert order.getSubTotal().subtract(order.getOrderAdjustmentsValue()).equals(new Money(31.80D));
        assert (order.getTotal().greaterThan(order.getSubTotal()));
        assert (order.getTotalTax().equals(order.getSubTotal().subtract(order.getOrderAdjustmentsValue()).multiply(0.05D))); // Shipping is not taxable
        //determine the total cost of the fulfillment group fees
        Money fulfillmentGroupFeeTotal = getFulfillmentGroupFeeTotal(order);
        assert (order.getTotal().equals(order.getSubTotal().add(order.getTotalTax()).add(order.getTotalShipping()).add(fulfillmentGroupFeeTotal).subtract(order.getOrderAdjustmentsValue())));
    }

    public Money getFulfillmentGroupFeeTotal(Order order) {
        Money result = new Money(BigDecimal.ZERO);
        for (FulfillmentGroup group : order.getFulfillmentGroups()) {
            for (FulfillmentGroupFee fee : group.getFulfillmentGroupFees()) {
                result = result.add(fee.getAmount());
            }
        }
        return result;
    }

    @Test(groups = { "testShipping" }, dependsOnGroups = { "testShippingInsert", "createCustomerIdGeneration"})
    @Transactional
    public void testShipping() throws Exception {
        Order order = orderService.createNewCartForCustomer(createCustomer());
        
        customerService.saveCustomer(order.getCustomer());
        
        FulfillmentGroup group1 = new FulfillmentGroupImpl();
        FulfillmentGroup group2 = new FulfillmentGroupImpl();

        // setup group1 - standard
        group1.setMethod("standard");
        group1.setService(ShippingServiceType.BANDED_SHIPPING.getType());

        Country country = new CountryImpl();
        country.setAbbreviation("US");
        country.setName("United States");

        country = countryService.save(country);

        ISOCountry isoCountry = new ISOCountryImpl();
        isoCountry.setAlpha2("US");
        isoCountry.setName("UNITED STATES");

        isoCountry = isoService.save(isoCountry);

        State state = new StateImpl();
        state.setAbbreviation("TX");
        state.setName("Texas");
        state.setCountry(country);

        state = stateService.save(state);
        
        Address address = new AddressImpl();
        address.setAddressLine1("123 Test Rd");
        address.setCity("Dallas");
        address.setFirstName("Jeff");
        address.setLastName("Fischer");
        address.setPostalCode("75240");
        address.setPrimaryPhone("972-978-9067");

        address.setState(state);
        address.setCountry(country);
        address.setIsoCountrySubdivision("US-TX");
        address.setIsoCountryAlpha2(isoCountry);
        group1.setAddress(address);
        group1.setOrder(order);

        // setup group2 - truck
        group2.setMethod("truck");
        group2.setService(ShippingServiceType.BANDED_SHIPPING.getType());
        group2.setOrder(order);

        List<FulfillmentGroup> groups = new ArrayList<>();
        groups.add(group1);
        //groups.add(group2);
        order.setFulfillmentGroups(groups);
        Money total = new Money(8.5D);
        group1.setShippingPrice(total);
        group2.setShippingPrice(total);
        //group1.setTotalTax(new Money(1D));
        //group2.setTotalTax(new Money(1D));
        order.setSubTotal(total);
        order.setTotal(total);

        DiscreteOrderItem item = new DiscreteOrderItemImpl();
        Sku sku = new SkuImpl();
        sku.setRetailPrice(new Money(15D));
        sku.setDiscountable(true);
        sku.setName("Test Sku");
        
        sku = catalogService.saveSku(sku);
        
        item.setSku(sku);
        item.setQuantity(1);
        item.setOrder(order);
        
        item = (DiscreteOrderItem) orderItemService.saveOrderItem(item);
        
        List<OrderItem> items = new ArrayList<>();
        items.add(item);
        order.setOrderItems(items);
        for (OrderItem orderItem : items) {
            FulfillmentGroupItem fgi = new FulfillmentGroupItemImpl();
            fgi.setOrderItem(orderItem);
            fgi.setQuantity(orderItem.getQuantity());
            fgi.setFulfillmentGroup(group1);
            //fgi.setRetailPrice(new Money(15D));
            group1.addFulfillmentGroupItem(fgi);
        }
        order.setTotalShipping(new Money(0D));
        
        orderService.save(order, true);

        assert (order.getTotal().greaterThan(order.getSubTotal()));
        assert (order.getTotalTax().equals(order.getSubTotal().multiply(0.05D))); // Shipping price is not taxable
        assert (order.getTotal().equals(order.getSubTotal().add(order.getTotalTax().add(order.getTotalShipping()))));
    }
    
    public Customer createCustomer() {
        Customer customer = customerService.createCustomerFromId(null);
        return customer;
    }

    private OfferCode createOfferCode(String offerName, OfferType offerType, OfferDiscountType discountType, double value, String customerRule, String orderRule) {
        OfferCode offerCode = new OfferCodeImpl();
        Offer offer = createOffer(offerName, offerType, discountType, value, customerRule, orderRule);
        offerCode.setOffer(offer);
        offerCode.setOfferCode("OPRAH");
        offerCode = offerService.saveOfferCode(offerCode);
        return offerCode;
    }

    private Offer createOffer(String offerName, OfferType offerType, OfferDiscountType discountType, double value, String customerRule, String orderRule) {
        Offer offer = new OfferImpl();
        offer.setName(offerName);
        offer.setStartDate(SystemTime.asDate());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        offer.setStartDate(calendar.getTime());
        calendar.add(Calendar.DATE, 2);
        offer.setEndDate(calendar.getTime());
        offer.setType(offerType);
        offer.setDiscountType(discountType);
        offer.setValue(BigDecimal.valueOf(value));
        offer.setAutomaticallyAdded(true);

        OfferItemCriteria oic = new OfferItemCriteriaImpl();
        oic.setQuantity(1);
        oic.setMatchRule(orderRule);

        OfferTargetCriteriaXref targetXref = new OfferTargetCriteriaXrefImpl();
        targetXref.setOffer(offer);
        targetXref.setOfferItemCriteria(oic);

        offer.setTargetItemCriteriaXref(Collections.singleton(targetXref));
        offer.setCombinableWithOtherOffers(true);
        offer = offerService.save(offer);
        offer.setMaxUsesPerOrder(50);
        return offer;
    }
}
