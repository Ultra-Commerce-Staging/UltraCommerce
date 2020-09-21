/*
 * #%L
 * UltraCommerce Framework
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
package com.ultracommerce.core.pricing.service.workflow;

import com.ultracommerce.common.money.Money;
import com.ultracommerce.core.catalog.service.type.ProductBundlePricingModelType;
import com.ultracommerce.core.offer.domain.OrderAdjustment;
import com.ultracommerce.core.offer.domain.OrderAdjustmentImpl;
import com.ultracommerce.core.offer.service.OfferDataItemProvider;
import com.ultracommerce.core.order.domain.BundleOrderItem;
import com.ultracommerce.core.order.domain.FulfillmentGroup;
import com.ultracommerce.core.order.domain.FulfillmentGroupItem;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.domain.OrderItem;
import com.ultracommerce.core.workflow.DefaultProcessContextImpl;
import com.ultracommerce.core.workflow.ProcessContext;

import java.math.BigDecimal;

import junit.framework.TestCase;

public class FulfillmentItemPricingActivityTest extends TestCase {

    private OfferDataItemProvider dataProvider = new OfferDataItemProvider();
    private FulfillmentItemPricingActivity fulfillmentItemPricingActivity = new FulfillmentItemPricingActivity();

    protected Money sumProratedOfferAdjustments(Order order) {
        Money returnVal = new Money(order.getCurrency());
        for (FulfillmentGroup fulfillmentGroup : order.getFulfillmentGroups()) {
            for (FulfillmentGroupItem fulfillmentGroupItem : fulfillmentGroup.getFulfillmentGroupItems()) {
                if (fulfillmentGroupItem.getProratedOrderAdjustmentAmount() != null) {
                    returnVal = returnVal.add(fulfillmentGroupItem.getProratedOrderAdjustmentAmount());
                }
            }
        }
        return returnVal;
    }

    public void testZeroOrderSavings() throws Exception {
        Order order = dataProvider.createBasicOrder();
        ProcessContext<Order> context = new DefaultProcessContextImpl<Order>();
        context.setSeedData(order);

        fulfillmentItemPricingActivity.execute(context);
        assertTrue(sumProratedOfferAdjustments(order).getAmount().compareTo(BigDecimal.ZERO) == 0);
    }

    public void testDistributeOneDollarAcrossFiveEqualItems() throws Exception {
        Order order = dataProvider.createBasicOrder();
        Money subTotal = new Money(order.getCurrency());
        for (OrderItem orderItem : order.getOrderItems()) {
            orderItem.setSalePrice(new Money(10D));
            orderItem.getOrderItemPriceDetails().clear();
            subTotal = subTotal.add(orderItem.getTotalPrice());
        }

        OrderAdjustment adjustment = new OrderAdjustmentImpl();
        adjustment.setValue(new Money(new BigDecimal("1"), order.getCurrency()));
        order.getOrderAdjustments().add(adjustment);
        adjustment.setOrder(order);
        order.setSubTotal(subTotal);

        ProcessContext<Order> context = new DefaultProcessContextImpl<Order>();
        context.setSeedData(order);

        fulfillmentItemPricingActivity.execute(context);

        // Each item is equally priced, so the adjustment should be .20 per item.
        Money proratedAdjustment = new Money(".20");
        for (FulfillmentGroup fulfillmentGroup : order.getFulfillmentGroups()) {
            for (FulfillmentGroupItem fulfillmentGroupItem : fulfillmentGroup.getFulfillmentGroupItems()) {
                assertTrue(fulfillmentGroupItem.getProratedOrderAdjustmentAmount().compareTo(
                        proratedAdjustment.multiply(fulfillmentGroupItem.getQuantity())) == 0);
            }
        }
    }

    public void testDistributeOneDollarAcrossFiveItems() throws Exception {
        Order order = dataProvider.createBasicOrder();

        OrderAdjustment adjustment = new OrderAdjustmentImpl();
        adjustment.setValue(new Money(new BigDecimal("1"), order.getCurrency()));
        adjustment.setOrder(order);
        order.getOrderAdjustments().add(adjustment);

        ProcessContext<Order> context = new DefaultProcessContextImpl<Order>();
        context.setSeedData(order);

        fulfillmentItemPricingActivity.execute(context);

        Money adj1 = new Money(".31");
        Money adj2 = new Money(".69");

        for (FulfillmentGroup fulfillmentGroup : order.getFulfillmentGroups()) {
            for (FulfillmentGroupItem fulfillmentGroupItem : fulfillmentGroup.getFulfillmentGroupItems()) {
                if (fulfillmentGroupItem.getSalePrice().equals(new Money("19.99"))) {
                    assertTrue(fulfillmentGroupItem.getProratedOrderAdjustmentAmount().equals(adj1));
                } else {
                    assertTrue(fulfillmentGroupItem.getProratedOrderAdjustmentAmount().equals(adj2));
                }
            }
        }
    }

    public void testRoundingRequired() throws Exception {
        Order order = dataProvider.createBasicOrder();
        Money subTotal = new Money(order.getCurrency());
        for (OrderItem orderItem : order.getOrderItems()) {
            orderItem.getOrderItemPriceDetails().clear();
            orderItem.setQuantity(2);
            orderItem.setSalePrice(new Money(10D));
            subTotal = subTotal.add(orderItem.getTotalPrice());
        }
        order.setSubTotal(subTotal);

        OrderAdjustment adjustment = new OrderAdjustmentImpl();
        adjustment.setValue(new Money(new BigDecimal(".05"), order.getCurrency()));
        adjustment.setOrder(order);
        order.getOrderAdjustments().add(adjustment);

        ProcessContext<Order> context = new DefaultProcessContextImpl<Order>();
        context.setSeedData(order);

        fulfillmentItemPricingActivity.execute(context);

        assertTrue(sumProratedOfferAdjustments(order).equals(
                new Money(new BigDecimal(".05"), order.getCurrency())));
    }

    public void testBundleDistribution() throws Exception {
        Order order = dataProvider.createOrderWithBundle();
        Money subTotal = new Money(order.getCurrency());
        for (OrderItem orderItem : order.getOrderItems()) {
            subTotal = subTotal.add(orderItem.getTotalPrice());
        }
        order.setSubTotal(subTotal);

        OrderAdjustment adjustment = new OrderAdjustmentImpl();
        adjustment.setValue(new Money(new BigDecimal("1"), order.getCurrency()));
        adjustment.setOrder(order);
        order.getOrderAdjustments().add(adjustment);

        ProcessContext<Order> context = new DefaultProcessContextImpl<Order>();
        context.setSeedData(order);

        fulfillmentItemPricingActivity.execute(context);

        assertTrue(sumProratedOfferAdjustments(order).equals(
                new Money(new BigDecimal("1"), order.getCurrency())));
    }

    public void testBundleDistributionWithoutItemSum() throws Exception {
        Order order = dataProvider.createOrderWithBundle();

        Money subTotal = new Money(order.getCurrency());
        for (OrderItem orderItem : order.getOrderItems()) {
            if (orderItem instanceof BundleOrderItem) {
                BundleOrderItem bItem = (BundleOrderItem) orderItem;
                bItem.getProductBundle().setPricingModel(ProductBundlePricingModelType.BUNDLE);
            }
            subTotal = subTotal.add(orderItem.getTotalPrice());
        }
        order.setSubTotal(subTotal);

        OrderAdjustment adjustment = new OrderAdjustmentImpl();
        adjustment.setValue(new Money(new BigDecimal("1"), order.getCurrency()));
        adjustment.setOrder(order);
        order.getOrderAdjustments().add(adjustment);

        ProcessContext<Order> context = new DefaultProcessContextImpl<Order>();
        context.setSeedData(order);

        fulfillmentItemPricingActivity.execute(context);

        assertTrue(sumProratedOfferAdjustments(order).equals(
                new Money(new BigDecimal("1"), order.getCurrency())));
    }
}
