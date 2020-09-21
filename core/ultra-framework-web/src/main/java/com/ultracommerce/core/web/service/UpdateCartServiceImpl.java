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
package com.ultracommerce.core.web.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.currency.domain.UltraCurrency;
import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.web.UltraRequestContext;
import com.ultracommerce.core.catalog.domain.Sku;
import com.ultracommerce.core.order.domain.BundleOrderItem;
import com.ultracommerce.core.order.domain.DiscreteOrderItem;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.domain.OrderItem;
import com.ultracommerce.core.order.service.OrderService;
import com.ultracommerce.core.order.service.call.OrderItemRequestDTO;
import com.ultracommerce.core.order.service.call.UpdateCartResponse;
import com.ultracommerce.core.order.service.exception.AddToCartException;
import com.ultracommerce.core.order.service.exception.RemoveFromCartException;
import com.ultracommerce.core.pricing.service.exception.PricingException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

/**
 * Author: jerryocanas
 * Date: 9/26/12
 */
@Service("ucUpdateCartService")
public class UpdateCartServiceImpl implements UpdateCartService {
    protected static final Log LOG = LogFactory.getLog(UpdateCartServiceImpl.class);

    protected static UltraCurrency savedCurrency;

    @Resource(name="ucOrderService")
    protected OrderService orderService;
    
    @Resource(name = "ucUpdateCartServiceExtensionManager")
    protected UpdateCartServiceExtensionManager extensionManager;

    @Override
    public boolean currencyHasChanged() {
        UltraCurrency currency = findActiveCurrency();
        if (getSavedCurrency() == null) {
            setSavedCurrency(currency);
        } else if (getSavedCurrency() != currency){
            return true;
        }
        return false;
    }

    @Override
    public UpdateCartResponse copyCartToCurrentContext(Order currentCart) {
        if(currentCart.getOrderItems() == null){
            return null;
        }
        UltraCurrency currency = findActiveCurrency();
        if(currency == null){
            return null;
        }

        //Reprice order logic
        List<OrderItemRequestDTO> itemsToReprice = new ArrayList<>();
        List<OrderItem> itemsToRemove = new ArrayList<>();
        List<OrderItem> itemsToReset = new ArrayList<>();
        boolean repriceOrder = true;

        for(OrderItem orderItem: currentCart.getOrderItems()){
            //Lookup price in price list, if null, then add to itemsToRemove
            if (orderItem instanceof DiscreteOrderItem){
                DiscreteOrderItem doi = (DiscreteOrderItem) orderItem;
                if(checkAvailabilityInLocale(doi, currency)){
                    OrderItemRequestDTO itemRequest = new OrderItemRequestDTO();
                    itemRequest.setProductId(doi.getProduct().getId());
                    itemRequest.setQuantity(doi.getQuantity());
                    itemsToReprice.add(itemRequest);
                    itemsToReset.add(orderItem);
                } else {
                    itemsToRemove.add(orderItem);
                }
            } else if (orderItem instanceof BundleOrderItem) {
                BundleOrderItem boi = (BundleOrderItem) orderItem;
                for (DiscreteOrderItem doi : boi.getDiscreteOrderItems()) {
                    if(checkAvailabilityInLocale(doi, currency)){
                        OrderItemRequestDTO itemRequest = new OrderItemRequestDTO();
                        itemRequest.setProductId(doi.getProduct().getId());
                        itemRequest.setQuantity(doi.getQuantity());
                        itemsToReprice.add(itemRequest);
                        itemsToReset.add(orderItem);
                    } else {
                        itemsToRemove.add(orderItem);
                    }
                }
            }
        }

        for(OrderItem orderItem: itemsToReset){
            try {
                currentCart = orderService.removeItem(currentCart.getId(), orderItem.getId(), false);
            } catch (RemoveFromCartException e) {
                LOG.error("Could not remove from cart.", e);
            }
        }

        for(OrderItemRequestDTO itemRequest: itemsToReprice){
            try {
                currentCart = orderService.addItem(currentCart.getId(), itemRequest, false);
            } catch (AddToCartException e) {
                LOG.error("Could not add to cart.", e);
            }
        }

        // Reprice and save the cart
        try {
         currentCart = orderService.save(currentCart, repriceOrder);
        } catch (PricingException e) {
            LOG.error("Could not save cart.", e);
        }
        setSavedCurrency(currency);

        UpdateCartResponse updateCartResponse = new UpdateCartResponse();
        updateCartResponse.setRemovedItems(itemsToRemove);
        updateCartResponse.setOrder(currentCart);

        return updateCartResponse;
    }

    @Override
    public void validateAddToCartRequest(OrderItemRequestDTO itemRequest, Order cart) {
        if (extensionManager != null) {
            extensionManager.getProxy().validateAddToCartItem(itemRequest, cart);
        }
    }

    @Override
    public void updateAndValidateCart(Order cart) {
        if (extensionManager != null) {
            ExtensionResultHolder erh = new ExtensionResultHolder();
            extensionManager.getProxy().updateAndValidateCart(cart, erh);
            Boolean clearCart = (Boolean) erh.getContextMap().get("clearCart");
            Boolean repriceCart = (Boolean) erh.getContextMap().get("repriceCart");
            Boolean saveCart = (Boolean) erh.getContextMap().get("saveCart");
            if (clearCart != null && clearCart.booleanValue()) {
                orderService.cancelOrder(cart);
                cart = orderService.createNewCartForCustomer(cart.getCustomer());
            } else {
                try {
                    if (repriceCart != null && repriceCart.booleanValue()) {
                        orderService.save(cart, true, true);
                    } else if (saveCart != null && saveCart.booleanValue()) {
                        orderService.save(cart, false);
                    }
                } catch (PricingException pe) {
                    LOG.error("Pricing Exception while validating cart.   Clearing cart.", pe);
                    orderService.cancelOrder(cart);
                    cart = orderService.createNewCartForCustomer(cart.getCustomer());
                }
            }
        }
    }

    protected UltraCurrency findActiveCurrency(){
        if(UltraRequestContext.hasLocale()){
            return UltraRequestContext.getUltraRequestContext().getUltraCurrency();
        }
        return null;
    }

    protected boolean checkAvailabilityInLocale(DiscreteOrderItem doi, UltraCurrency currency) {
        if (doi.getSku() != null && extensionManager != null) {
            Sku sku = doi.getSku();
            return sku.isAvailable();
        }
        
        return false;
    }

    @Override
    public void setSavedCurrency(UltraCurrency savedCurrency) {
        this.savedCurrency = savedCurrency;
    }

    @Override
    public UltraCurrency getSavedCurrency() {
        return savedCurrency;
    }


}