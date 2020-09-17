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
package com.ultracommerce.core.web.expression.checkout;

import com.ultracommerce.common.money.Money;
import com.ultracommerce.common.web.expression.UltraVariableExpression;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.service.FulfillmentGroupService;
import com.ultracommerce.core.web.checkout.service.CheckoutFormService;
import com.ultracommerce.core.web.order.CartState;
import com.ultracommerce.core.web.order.service.CartStateService;
import com.ultracommerce.presentation.condition.ConditionalOnTemplating;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Chris Kittrell (ckittrell)
 */
@Component("ucCheckoutFormVariableExpression")
@ConditionalOnTemplating
public class CheckoutFormVariableExpression implements UltraVariableExpression {

    @Resource(name = "ucCartStateService")
    protected CartStateService cartStateService;

    @Resource(name = "ucFulfillmentGroupService")
    protected FulfillmentGroupService fulfillmentGroupService;

    @Resource(name = "ucCheckoutFormService")
    protected CheckoutFormService checkoutFormService;

    @Override
    public String getName() {
        return "checkoutForm";
    }

    public boolean shouldShowShippingInfoStage() {
        Order cart = CartState.getCart();
        int numShippableFulfillmentGroups = fulfillmentGroupService.calculateNumShippableFulfillmentGroups(cart);

        return numShippableFulfillmentGroups > 0;
    }

    public boolean shouldShowBillingInfoStage() {
        return !cartStateService.cartHasThirdPartyPayment()
                && !cartStateService.cartHasUnconfirmedCreditCard();
    }

    /**
     * Toggle the Payment Info Section based on what payments were applied to the order
     *  (e.g. Third Party Account (i.e. PayPal Express) or Gift Cards/Customer Credit)
     */
    public boolean shouldShowAllPaymentMethods() {
        Money orderTotalAfterAppliedPayments = CartState.getCart().getTotalAfterAppliedPayments();
        boolean totalCoveredByAppliedPayments = (orderTotalAfterAppliedPayments != null && orderTotalAfterAppliedPayments.isZero());

        return !cartStateService.cartHasThirdPartyPayment()
                && !totalCoveredByAppliedPayments;
    }

}
