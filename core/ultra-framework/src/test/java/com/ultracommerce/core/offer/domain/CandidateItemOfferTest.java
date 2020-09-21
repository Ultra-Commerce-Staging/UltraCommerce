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
package com.ultracommerce.core.offer.domain;

import junit.framework.TestCase;

import com.ultracommerce.common.money.Money;
import com.ultracommerce.core.catalog.domain.Category;
import com.ultracommerce.core.catalog.domain.CategoryImpl;
import com.ultracommerce.core.catalog.domain.CategoryProductXref;
import com.ultracommerce.core.catalog.domain.CategoryProductXrefImpl;
import com.ultracommerce.core.catalog.domain.Product;
import com.ultracommerce.core.catalog.domain.ProductImpl;
import com.ultracommerce.core.catalog.domain.Sku;
import com.ultracommerce.core.catalog.domain.SkuImpl;
import com.ultracommerce.core.offer.service.OfferDataItemProvider;
import com.ultracommerce.core.offer.service.discount.domain.PromotableCandidateItemOffer;
import com.ultracommerce.core.offer.service.discount.domain.PromotableCandidateItemOfferImpl;
import com.ultracommerce.core.offer.service.discount.domain.PromotableItemFactoryImpl;
import com.ultracommerce.core.offer.service.discount.domain.PromotableOfferUtility;
import com.ultracommerce.core.offer.service.discount.domain.PromotableOfferUtilityImpl;
import com.ultracommerce.core.offer.service.discount.domain.PromotableOrder;
import com.ultracommerce.core.offer.service.discount.domain.PromotableOrderImpl;
import com.ultracommerce.core.offer.service.discount.domain.PromotableOrderItem;
import com.ultracommerce.core.offer.service.discount.domain.PromotableOrderItemImpl;
import com.ultracommerce.core.offer.service.discount.domain.PromotableOrderItemPriceDetail;
import com.ultracommerce.core.offer.service.discount.domain.PromotableOrderItemPriceDetailImpl;
import com.ultracommerce.core.offer.service.type.OfferDiscountType;
import com.ultracommerce.core.order.domain.DiscreteOrderItemImpl;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.domain.OrderImpl;
import com.ultracommerce.core.order.domain.OrderItemPriceDetail;
import com.ultracommerce.core.order.domain.OrderItemPriceDetailImpl;
import com.ultracommerce.core.order.service.type.OrderItemType;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author jfischer
 *
 */
public class CandidateItemOfferTest extends TestCase {
    
    private PromotableCandidateItemOffer promotableCandidate;
    private Offer offer;
    private PromotableCandidateItemOffer candidateOffer;
    private PromotableOrderItem promotableOrderItem;
    private PromotableOrder promotableOrder;
    private PromotableOrderItemPriceDetail priceDetail;
    private PromotableOfferUtility promotableOfferUtility;

    @Override
    protected void setUp() throws Exception {
        promotableOfferUtility = new PromotableOfferUtilityImpl();
        OfferDataItemProvider dataProvider = new OfferDataItemProvider();
        
        CandidateItemOfferImpl candidate = new CandidateItemOfferImpl();
        
        Category category1 = new CategoryImpl();
        category1.setName("test1");
        category1.setId(1L);
        
        Product product1 = new ProductImpl();
        
        Sku sku1 = new SkuImpl();
        sku1.setName("test1");
        sku1.setDiscountable(true);
        sku1.setRetailPrice(new Money(19.99D));
        product1.setDefaultSku(sku1);

        CategoryProductXref xref1 = new CategoryProductXrefImpl();
        xref1.setProduct(product1);
        xref1.setCategory(category1);
        
        category1.getAllProductXrefs().add(xref1);

        Category category2 = new CategoryImpl();
        category2.setName("test2");
        category2.setId(2L);
        
        Product product2 = new ProductImpl();
        
        Sku sku2 = new SkuImpl();
        sku2.setName("test2");
        sku2.setDiscountable(true);
        sku2.setRetailPrice(new Money(29.99D));
        product2.setDefaultSku(sku2);

        CategoryProductXref xref2 = new CategoryProductXrefImpl();
        xref2.setProduct(product2);
        xref2.setCategory(category2);

        category2.getAllProductXrefs().add(xref2);
        
        DiscreteOrderItemImpl orderItem1 = new DiscreteOrderItemImpl();
        orderItem1.setCategory(category1);
        orderItem1.setName("test1");
        orderItem1.setOrderItemType(OrderItemType.DISCRETE);
        orderItem1.setProduct(product1);
        orderItem1.setQuantity(2);
        orderItem1.setSku(sku1);
        
        Order order = new OrderImpl();
        orderItem1.setOrder(order);
        
        promotableOrder = new PromotableOrderImpl(order, new PromotableItemFactoryImpl(promotableOfferUtility), false);
        offer = dataProvider.createItemBasedOfferWithItemCriteria(
                "order.subTotal.getAmount()>20",
                OfferDiscountType.PERCENT_OFF,
                "([MVEL.eval(\"toUpperCase()\",\"test1\"), MVEL.eval(\"toUpperCase()\",\"test2\")] contains MVEL.eval(\"toUpperCase()\", discreteOrderItem.category.name))",
                "([MVEL.eval(\"toUpperCase()\",\"test1\"), MVEL.eval(\"toUpperCase()\",\"test2\")] contains MVEL.eval(\"toUpperCase()\", discreteOrderItem.category.name))"
                ).get(0);
        candidateOffer = new PromotableCandidateItemOfferImpl(promotableOrder, offer);
        
        promotableOrderItem = new PromotableOrderItemImpl(orderItem1, null, new PromotableItemFactoryImpl(promotableOfferUtility), false);
        OrderItemPriceDetail pdetail = new OrderItemPriceDetailImpl();
        pdetail.setOrderItem(orderItem1);
        pdetail.setQuantity(2);
        priceDetail = new PromotableOrderItemPriceDetailImpl(promotableOrderItem, 2);
        
        List<PromotableOrderItem> items = new ArrayList<PromotableOrderItem>();
        items.add(promotableOrderItem);
        
        promotableCandidate = new PromotableCandidateItemOfferImpl(promotableOrder, offer);
        
        OfferTargetCriteriaXref xref = offer.getTargetItemCriteriaXref().iterator().next();
        promotableCandidate.getCandidateTargetsMap().put(xref.getOfferItemCriteria(), items);
    }
    
    public void testCalculateSavingsForOrderItem() throws Exception {
        Money savings = promotableOfferUtility.calculateSavingsForOrderItem(promotableCandidate, promotableOrderItem, 1);
        assertTrue(savings.equals(new Money(2D)));
        
        offer.setDiscountType(OfferDiscountType.AMOUNT_OFF);
        savings = promotableOfferUtility.calculateSavingsForOrderItem(promotableCandidate, promotableOrderItem, 1);
        assertTrue(savings.equals(new Money(10D)));
        
        offer.setDiscountType(OfferDiscountType.FIX_PRICE);
        savings = promotableOfferUtility.calculateSavingsForOrderItem(promotableCandidate, promotableOrderItem, 1);
        assertTrue(savings.equals(new Money(19.99D - 10D)));
    }
    
    public void testCalculateMaximumNumberOfUses() throws Exception {
        int maxOfferUses = promotableCandidate.calculateMaximumNumberOfUses();
        assertTrue(maxOfferUses == 2);
        
        offer.setMaxUsesPerOrder(1);
        maxOfferUses = promotableCandidate.calculateMaximumNumberOfUses();
        assertTrue(maxOfferUses == 1);
    }
    
    public void testCalculateMaxUsesForItemCriteria() throws Exception {
        int maxItemCriteriaUses = 9999;
        for (OfferTargetCriteriaXref targetXref : offer.getTargetItemCriteriaXref()) {
            int temp = promotableCandidate.calculateMaxUsesForItemCriteria(targetXref.getOfferItemCriteria(), offer);
            maxItemCriteriaUses = Math.min(maxItemCriteriaUses, temp);
        }
        assertTrue(maxItemCriteriaUses == 2);
    }
}
