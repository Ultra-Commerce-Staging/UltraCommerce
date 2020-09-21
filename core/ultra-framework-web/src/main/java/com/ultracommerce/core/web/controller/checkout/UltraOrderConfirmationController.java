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

import com.ultracommerce.common.web.controller.UltraAbstractController;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.service.OrderService;
import com.ultracommerce.profile.core.domain.Customer;
import com.ultracommerce.profile.web.core.CustomerState;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UltraOrderConfirmationController extends UltraAbstractController {

    @Resource(name = "ucOrderService")
    protected OrderService orderService;
    
    @Resource(name = "ucConfirmationControllerExtensionManager")
    protected ConfirmationControllerExtensionManager extensionManager;
    
    protected static String orderConfirmationView = "checkout/confirmation";

    public String displayOrderConfirmationByOrderNumber(String orderNumber, Model model,
             HttpServletRequest request, HttpServletResponse response) {
        Customer customer = CustomerState.getCustomer();
        if (customer != null) {
            Order order = orderService.findOrderByOrderNumber(orderNumber);
            if (order != null && customer.equals(order.getCustomer())) {
                extensionManager.getProxy().processAdditionalConfirmationActions(order);

                model.addAttribute("order", order);
                model.addAttribute("analyticsOrder", order);
                return getOrderConfirmationView();
            }
        }
        return "redirect:/";
    }

    public String displayOrderConfirmationByOrderId(Long orderId, Model model,
             HttpServletRequest request, HttpServletResponse response) {

        Customer customer = CustomerState.getCustomer();
        if (customer != null) {
            Order order = orderService.findOrderById(orderId);
            if (order != null && customer.equals(order.getCustomer())) {
                extensionManager.getProxy().processAdditionalConfirmationActions(order);

                model.addAttribute("order", order);
                model.addAttribute("analyticsOrder", order);
                return getOrderConfirmationView();
            }
        }
        return "redirect:/";
    }

    public String getOrderConfirmationView() {
        return orderConfirmationView;
    }

}
