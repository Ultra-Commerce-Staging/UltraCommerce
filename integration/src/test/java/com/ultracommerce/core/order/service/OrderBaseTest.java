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
package com.ultracommerce.core.order.service;

import org.apache.commons.lang.time.DateUtils;
import com.ultracommerce.core.catalog.domain.Category;
import com.ultracommerce.core.catalog.domain.Product;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.service.call.OrderItemRequestDTO;
import com.ultracommerce.core.order.service.exception.AddToCartException;
import com.ultracommerce.profile.core.domain.Customer;
import com.ultracommerce.test.CommonSetupBaseTest;

import java.util.Date;

public class OrderBaseTest extends CommonSetupBaseTest {

    protected Customer createNamedCustomer() {
        Customer customer = customerService.createCustomerFromId(null);
        customer.setUsername(String.valueOf(customer.getId()));
        return customer;
    }
    
    public Order setUpNamedOrder() throws AddToCartException {
        Customer customer = customerService.saveCustomer(createNamedCustomer());

        Order order = orderService.createNamedOrderForCustomer("Boxes Named Order", customer);
        
        Product newProduct = addTestProduct("Cube Box", "Boxes");        
        Category newCategory = newProduct.getDefaultCategory();
        
        order = orderService.addItem(order.getId(), 
                new OrderItemRequestDTO(newProduct.getId(), newProduct.getDefaultSku().getId(), newCategory.getId(), 2), 
                true);

        return order;
    }
    
    public Order setUpCartWithActiveSku() throws AddToCartException {
        Customer customer = customerService.saveCustomer(createNamedCustomer());

        Order order = orderService.createNewCartForCustomer(customer);

        Product newProduct = addTestProduct("Plastic Crate Active", "Crates");
        Category newCategory = newProduct.getDefaultCategory();
        
        order = orderService.addItem(order.getId(), 
                new OrderItemRequestDTO(newProduct.getId(), newProduct.getDefaultSku().getId(), newCategory.getId(), 1), 
                true);

        return order;
    }
    
    public Order setUpCartWithInactiveSku() throws AddToCartException {
        Customer customer = customerService.saveCustomer(createNamedCustomer());

        Order order = orderService.createNewCartForCustomer(customer);

        Product newProduct = addTestProduct("Plastic Crate Should Be Inactive", "Crates");
        Category newCategory = newProduct.getDefaultCategory();
        
        order = orderService.addItem(order.getId(), 
                new OrderItemRequestDTO(newProduct.getId(), newProduct.getDefaultSku().getId(), newCategory.getId(), 1), 
                true);
        
        // Make the SKU inactive
        newProduct.getDefaultSku().setActiveEndDate(DateUtils.addDays(new Date(), -1));
        catalogService.saveSku(newProduct.getDefaultSku());

        return order;
    }
    
}
