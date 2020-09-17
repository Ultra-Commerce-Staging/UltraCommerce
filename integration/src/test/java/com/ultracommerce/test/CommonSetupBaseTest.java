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
package com.ultracommerce.test;

import com.ultracommerce.common.i18n.domain.ISOCountry;
import com.ultracommerce.common.i18n.domain.ISOCountryImpl;
import com.ultracommerce.common.i18n.service.ISOService;
import com.ultracommerce.common.money.Money;
import com.ultracommerce.core.catalog.domain.Category;
import com.ultracommerce.core.catalog.domain.CategoryImpl;
import com.ultracommerce.core.catalog.domain.Product;
import com.ultracommerce.core.catalog.domain.ProductBundle;
import com.ultracommerce.core.catalog.domain.ProductImpl;
import com.ultracommerce.core.catalog.domain.Sku;
import com.ultracommerce.core.catalog.domain.SkuBundleItem;
import com.ultracommerce.core.catalog.domain.SkuBundleItemImpl;
import com.ultracommerce.core.catalog.domain.SkuImpl;
import com.ultracommerce.core.catalog.service.CatalogService;
import com.ultracommerce.core.catalog.service.type.ProductType;
import com.ultracommerce.core.order.dao.OrderDao;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.domain.OrderImpl;
import com.ultracommerce.core.order.service.OrderService;
import com.ultracommerce.core.order.service.type.OrderStatus;
import com.ultracommerce.core.pricing.domain.ShippingRate;
import com.ultracommerce.core.pricing.domain.ShippingRateImpl;
import com.ultracommerce.core.pricing.service.ShippingRateService;
import com.ultracommerce.profile.core.domain.Address;
import com.ultracommerce.profile.core.domain.AddressImpl;
import com.ultracommerce.profile.core.domain.Country;
import com.ultracommerce.profile.core.domain.CountryImpl;
import com.ultracommerce.profile.core.domain.Customer;
import com.ultracommerce.profile.core.domain.CustomerAddress;
import com.ultracommerce.profile.core.domain.CustomerAddressImpl;
import com.ultracommerce.profile.core.domain.State;
import com.ultracommerce.profile.core.domain.StateImpl;
import com.ultracommerce.profile.core.service.CountryService;
import com.ultracommerce.profile.core.service.CustomerAddressService;
import com.ultracommerce.profile.core.service.CustomerService;
import com.ultracommerce.profile.core.service.StateService;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.annotation.Resource;

public abstract class CommonSetupBaseTest extends TestNGSiteIntegrationSetup {

    @Resource
    protected ISOService isoService;

    @Resource
    protected CountryService countryService;
    
    @Resource
    protected StateService stateService;
    
    @Resource
    protected CustomerService customerService;
    
    @Resource
    protected CustomerAddressService customerAddressService;
    
    @Resource
    protected CatalogService catalogService;
    
    @Resource(name = "ucOrderService")
    protected OrderService orderService;
    
    @Resource
    protected ShippingRateService shippingRateService;

    @Resource
    private OrderDao orderDao;
    

    public void createCountry() {
        Country country = new CountryImpl();
        country.setAbbreviation("US");
        country.setName("United States");
        countryService.save(country);

        ISOCountry isoCountry = new ISOCountryImpl();
        isoCountry.setAlpha2("US");
        isoCountry.setName("UNITED STATES");
        isoService.save(isoCountry);
    }
    
    public void createState() {
        State state = new StateImpl();
        state.setAbbreviation("KY");
        state.setName("Kentucky");
        state.setCountry(countryService.findCountryByAbbreviation("US"));
        stateService.save(state);
    }
    
    public Customer createCustomer() {
        Customer customer = customerService.createCustomerFromId(null);
        return customer;
    }
    
    /**
     * Creates a country, state, and customer with some CustomerAddresses
     * @return customer created
     */
    public Customer createCustomerWithAddresses() {
        createCountry();
        createState();

        Customer customer = createCustomer();
        customer.setUsername(String.valueOf(customer.getId()));
        customer = customerService.saveCustomer(customer);

        CustomerAddress ca1 = new CustomerAddressImpl();
        Address address1 = new AddressImpl();
        address1.setAddressLine1("1234 Merit Drive");
        address1.setCity("Bozeman");
        address1.setPostalCode("75251");
        ca1.setAddress(address1);
        ca1.setAddressName("address1");
        ca1.setCustomer(customer);
        CustomerAddress addResult1 = customerAddressService.saveCustomerAddress(ca1);
        assert addResult1 != null;

        CustomerAddress ca2 = new CustomerAddressImpl();
        Address address2 = new AddressImpl();
        address2.setAddressLine1("12 Testing Drive");
        address2.setCity("Portland");
        address2.setPostalCode("75251");
        ca2.setAddress(address2);
        ca2.setAddressName("address2");
        ca2.setCustomer(customer);
        CustomerAddress addResult2 = customerAddressService.saveCustomerAddress(ca2);
        assert addResult2 != null;

        return customer;
    }
    
    /**
     * Creates a country, state, and customer with the supplied customerAddress
     * @param customerAddress
     * @return customer created
     */
    public CustomerAddress createCustomerWithAddress(CustomerAddress customerAddress) {
        createCountry();
        createState();
        Customer customer = createCustomer();
        customer.setUsername(String.valueOf(customer.getId()));
        customerAddress.setCustomer(customer);
        return saveCustomerAddress(customerAddress);
    }
    
    /**
     * Saves a customerAddress with state KY and country US.  Requires that createCountry() and createState() have been called
     * @param customerAddress
     */
    public CustomerAddress saveCustomerAddress(CustomerAddress customerAddress) {
        State state = stateService.findStateByAbbreviation("KY");
        customerAddress.getAddress().setState(state);
        Country country = countryService.findCountryByAbbreviation("US");
        customerAddress.getAddress().setCountry(country);

        customerAddress.getAddress().setIsoCountrySubdivision("US-KY");
        ISOCountry isoCountry = isoService.findISOCountryByAlpha2Code("US");
        customerAddress.getAddress().setIsoCountryAlpha2(isoCountry);

        return customerAddressService.saveCustomerAddress(customerAddress);
    }
    
    /**
     * Create a state, country, and customer with a basic order and some addresses
     */
    public Customer createCustomerWithBasicOrderAndAddresses() {
        Customer customer = createCustomerWithAddresses();
        Order order = new OrderImpl();
        order.setStatus(OrderStatus.IN_PROCESS);
        order.setTotal(new Money(BigDecimal.valueOf(1000)));
        
        assert order.getId() == null;
        order.setCustomer(customer);
        order = orderDao.save(order);
        assert order.getId() != null;
        
        return customer;
    }
    
    public Product addTestProduct(String productName, String categoryName) {
        return addTestProduct(productName, categoryName, true);
    }
    
    public Product addTestProduct(String productName, String categoryName, boolean active) {
        Calendar activeStartCal = Calendar.getInstance();
        activeStartCal.add(Calendar.DAY_OF_YEAR, -2);
        
        Calendar activeEndCal = Calendar.getInstance();
        activeEndCal.add(Calendar.DAY_OF_YEAR, -1);
        
        Category category = new CategoryImpl();
        category.setName(categoryName);
        category.setActiveStartDate(activeStartCal.getTime());
        category = catalogService.saveCategory(category);
        
        Sku newSku = new SkuImpl();
        newSku.setName(productName);
        newSku.setRetailPrice(new Money(44.99));
        newSku.setActiveStartDate(activeStartCal.getTime());  
        if (!active) {
            newSku.setActiveEndDate(activeEndCal.getTime());
        }
        newSku.setDiscountable(true);
        newSku = catalogService.saveSku(newSku);
        
        Product newProduct = new ProductImpl();
        newProduct.setDefaultCategory(category);
        newProduct.setDefaultSku(newSku);
        newProduct = catalogService.saveProduct(newProduct);

        return newProduct;
    }
    
    public ProductBundle addProductBundle() {
        // Create the product
        Product p = addTestProduct("bundleproduct1", "bundlecat1");
        
        // Create the sku for the ProductBundle object
        Sku bundleSku = catalogService.createSku();
        bundleSku.setName(p.getName());
        bundleSku.setRetailPrice(new Money(44.99));
        bundleSku.setActiveStartDate(p.getActiveStartDate());
        bundleSku.setActiveEndDate(p.getActiveEndDate());
        bundleSku.setDiscountable(true);
        
        // Create the ProductBundle and associate the sku
        ProductBundle bundle = (ProductBundle) catalogService.createProduct(ProductType.BUNDLE);
        bundle.setDefaultCategory(p.getDefaultCategory());
        bundle.setDefaultSku(bundleSku);
        bundle = (ProductBundle) catalogService.saveProduct(bundle);
        
        // Reverse-associate the ProductBundle to the sku (Must be done this way because it's a 
        // bidirectional OneToOne relationship
        //bundleSku.setDefaultProduct(bundle);
        //catalogService.saveSku(bundleSku);
        
        // Wrap the product/sku that is part of the bundle in a SkuBundleItem
        SkuBundleItem skuBundleItem = new SkuBundleItemImpl();
        skuBundleItem.setBundle(bundle);
        skuBundleItem.setQuantity(1);
        skuBundleItem.setSku(p.getDefaultSku());
        
        // Add the SkuBundleItem to the ProductBundle
        bundle.getSkuBundleItems().add(skuBundleItem);
        bundle = (ProductBundle) catalogService.saveProduct(bundle);
        
        return bundle;
    }
    

    public void createShippingRates() {
        ShippingRate sr = new ShippingRateImpl();
        sr.setFeeType("SHIPPING");
        sr.setFeeSubType("ALL");
        sr.setFeeBand(1);
        sr.setBandUnitQuantity(BigDecimal.valueOf(29.99));
        sr.setBandResultQuantity(BigDecimal.valueOf(8.5));
        sr.setBandResultPercent(0);
        ShippingRate sr2 = new ShippingRateImpl();
        
        sr2.setFeeType("SHIPPING");
        sr2.setFeeSubType("ALL");
        sr2.setFeeBand(2);
        sr2.setBandUnitQuantity(BigDecimal.valueOf(999999.99));
        sr2.setBandResultQuantity(BigDecimal.valueOf(8.5));
        sr2.setBandResultPercent(0);
        
        shippingRateService.save(sr);
        shippingRateService.save(sr2);
    }

}
