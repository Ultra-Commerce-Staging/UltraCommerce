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
package com.ultracommerce.core.offer.service.processor;

import junit.framework.TestCase;

import com.ultracommerce.core.offer.dao.CustomerOfferDao;
import com.ultracommerce.core.offer.dao.OfferCodeDao;
import com.ultracommerce.core.offer.dao.OfferDao;
import com.ultracommerce.core.offer.domain.CandidateFulfillmentGroupOffer;
import com.ultracommerce.core.offer.domain.CandidateFulfillmentGroupOfferImpl;
import com.ultracommerce.core.offer.domain.CandidateItemOffer;
import com.ultracommerce.core.offer.domain.CandidateItemOfferImpl;
import com.ultracommerce.core.offer.domain.FulfillmentGroupAdjustment;
import com.ultracommerce.core.offer.domain.FulfillmentGroupAdjustmentImpl;
import com.ultracommerce.core.offer.domain.Offer;
import com.ultracommerce.core.offer.domain.OfferImpl;
import com.ultracommerce.core.offer.domain.OfferQualifyingCriteriaXref;
import com.ultracommerce.core.offer.domain.OrderItemAdjustment;
import com.ultracommerce.core.offer.domain.OrderItemAdjustmentImpl;
import com.ultracommerce.core.offer.service.OfferDataItemProvider;
import com.ultracommerce.core.offer.service.OfferServiceImpl;
import com.ultracommerce.core.offer.service.OfferServiceUtilities;
import com.ultracommerce.core.offer.service.OfferServiceUtilitiesImpl;
import com.ultracommerce.core.offer.service.discount.CandidatePromotionItems;
import com.ultracommerce.core.offer.service.discount.domain.PromotableCandidateFulfillmentGroupOffer;
import com.ultracommerce.core.offer.service.discount.domain.PromotableFulfillmentGroup;
import com.ultracommerce.core.offer.service.discount.domain.PromotableItemFactoryImpl;
import com.ultracommerce.core.offer.service.discount.domain.PromotableOfferUtility;
import com.ultracommerce.core.offer.service.discount.domain.PromotableOfferUtilityImpl;
import com.ultracommerce.core.offer.service.discount.domain.PromotableOrder;
import com.ultracommerce.core.offer.service.discount.domain.PromotableOrderItem;
import com.ultracommerce.core.offer.service.type.OfferDiscountType;
import com.ultracommerce.core.order.dao.FulfillmentGroupItemDao;
import com.ultracommerce.core.order.dao.OrderItemDao;
import com.ultracommerce.core.order.domain.FulfillmentGroup;
import com.ultracommerce.core.order.domain.FulfillmentGroupItem;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.domain.OrderItem;
import com.ultracommerce.core.order.domain.OrderItemPriceDetail;
import com.ultracommerce.core.order.domain.OrderMultishipOption;
import com.ultracommerce.core.order.domain.OrderMultishipOptionImpl;
import com.ultracommerce.core.order.service.FulfillmentGroupService;
import com.ultracommerce.core.order.service.OrderItemService;
import com.ultracommerce.core.order.service.OrderMultishipOptionService;
import com.ultracommerce.core.order.service.OrderService;
import com.ultracommerce.core.order.service.call.FulfillmentGroupItemRequest;
import com.ultracommerce.profile.core.domain.Address;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

/**
 * 
 * @author jfischer
 *
 */
public class FulfillmentGroupOfferProcessorTest extends TestCase {

    protected OfferDao offerDaoMock;
    protected OrderItemDao orderItemDaoMock;
    protected OfferServiceImpl offerService;
    protected final OfferDataItemProvider dataProvider = new OfferDataItemProvider();
    protected OrderService orderServiceMock;
    protected OrderItemService orderItemServiceMock;
    protected FulfillmentGroupItemDao fgItemDaoMock;
    protected FulfillmentGroupService fgServiceMock;
    protected OrderMultishipOptionService multishipOptionServiceMock;
    protected OfferTimeZoneProcessor offerTimeZoneProcessorMock;
    protected OfferServiceUtilities offerServiceUtilitiesMock;
    protected PromotableOfferUtility promotableOfferUtility;

    protected FulfillmentGroupOfferProcessorImpl fgProcessor;

    /**
     * Created to work around a dependency in FulfillmentGroupOfferProcessorImpl to a live application context and
     * system properties service since it uses UCSystemProperty
     * 
     * @author Phillip Verheyden (phillipuniverse)
     */
    protected static class TestableFulfillmentGroupOfferProcessor extends FulfillmentGroupOfferProcessorImpl {
        public TestableFulfillmentGroupOfferProcessor(PromotableOfferUtility promotableOfferUtility) {
            super(promotableOfferUtility);
        }

        @Override
        protected boolean getQualifyGroupAcrossAllOrderItems(PromotableFulfillmentGroup fg) {
            return false;
        }
    }

    @Override
    protected void setUp() throws Exception {
        offerService = new OfferServiceImpl();
        CustomerOfferDao customerOfferDaoMock = EasyMock.createMock(CustomerOfferDao.class);
        OfferCodeDao offerCodeDaoMock = EasyMock.createMock(OfferCodeDao.class);
        orderServiceMock = EasyMock.createMock(OrderService.class);
        orderItemDaoMock = EasyMock.createMock(OrderItemDao.class);

        orderItemServiceMock = EasyMock.createMock(OrderItemService.class);
        fgItemDaoMock = EasyMock.createMock(FulfillmentGroupItemDao.class);
        offerDaoMock = EasyMock.createMock(OfferDao.class);
        fgServiceMock = EasyMock.createMock(FulfillmentGroupService.class);
        multishipOptionServiceMock = EasyMock.createMock(OrderMultishipOptionService.class);
        offerServiceUtilitiesMock = EasyMock.createMock(OfferServiceUtilities.class);
        offerTimeZoneProcessorMock = EasyMock.createMock(OfferTimeZoneProcessor.class);
        promotableOfferUtility = new PromotableOfferUtilityImpl();

        fgProcessor = new TestableFulfillmentGroupOfferProcessor(promotableOfferUtility);
        fgProcessor.setOfferDao(offerDaoMock);
        fgProcessor.setOrderItemDao(orderItemDaoMock);
        fgProcessor.setPromotableItemFactory(new PromotableItemFactoryImpl(promotableOfferUtility));
        fgProcessor.setOfferServiceUtilities(offerServiceUtilitiesMock);

        OfferServiceUtilitiesImpl offerServiceUtilities = new OfferServiceUtilitiesImpl(promotableOfferUtility);
        offerServiceUtilities.setOfferDao(offerDaoMock);
        offerServiceUtilities.setPromotableItemFactory(new PromotableItemFactoryImpl(promotableOfferUtility));

        OrderOfferProcessorImpl orderProcessor = new OrderOfferProcessorImpl(promotableOfferUtility);
        orderProcessor.setOfferDao(offerDaoMock);
        orderProcessor.setPromotableItemFactory(new PromotableItemFactoryImpl(promotableOfferUtility));
        orderProcessor.setOfferServiceUtilities(offerServiceUtilitiesMock);
        orderProcessor.setOfferTimeZoneProcessor(offerTimeZoneProcessorMock);
        orderProcessor.setOrderItemDao(orderItemDaoMock);
        orderProcessor.setOfferServiceUtilities(offerServiceUtilities);

        ItemOfferProcessorImpl itemProcessor = new ItemOfferProcessorImpl(promotableOfferUtility);
        itemProcessor.setOfferDao(offerDaoMock);
        itemProcessor.setPromotableItemFactory(new PromotableItemFactoryImpl(promotableOfferUtility));
        itemProcessor.setOrderItemDao(orderItemDaoMock);
        itemProcessor.setOfferServiceUtilities(offerServiceUtilities);

        offerService.setCustomerOfferDao(customerOfferDaoMock);
        offerService.setOfferCodeDao(offerCodeDaoMock);
        offerService.setOfferDao(offerDaoMock);
        offerService.setOrderOfferProcessor(orderProcessor);
        offerService.setItemOfferProcessor(itemProcessor);
        offerService.setFulfillmentGroupOfferProcessor(fgProcessor);
        offerService.setPromotableItemFactory(new PromotableItemFactoryImpl(promotableOfferUtility));
        offerService.setOrderService(orderServiceMock);
    }
    
    public void replay() {
        EasyMock.replay(offerDaoMock);
        EasyMock.replay(orderItemDaoMock);
        EasyMock.replay(orderServiceMock);
        EasyMock.replay(orderItemServiceMock);
        EasyMock.replay(fgItemDaoMock);
        EasyMock.replay(fgServiceMock);
        EasyMock.replay(multishipOptionServiceMock);
        EasyMock.replay(offerTimeZoneProcessorMock);
        EasyMock.replay(offerServiceUtilitiesMock);
    }

    public void verify() {
        EasyMock.verify(offerDaoMock);
        EasyMock.verify(orderItemDaoMock);
        EasyMock.verify(orderServiceMock);
        EasyMock.verify(orderItemServiceMock);
        EasyMock.verify(fgItemDaoMock);
        EasyMock.verify(fgServiceMock);
        EasyMock.verify(multishipOptionServiceMock);
        EasyMock.verify(offerTimeZoneProcessorMock);
        EasyMock.verify(offerServiceUtilitiesMock);
    }

    public void testApplyAllFulfillmentGroupOffersWithOrderItemOffers() throws Exception {
        final ThreadLocal<Order> myOrder = new ThreadLocal<Order>();
        EasyMock.expect(orderItemDaoMock.createOrderItemPriceDetail()).andAnswer(OfferDataItemProvider.getCreateOrderItemPriceDetailAnswer()).anyTimes();

        EasyMock.expect(orderItemDaoMock.createOrderItemQualifier()).andAnswer(OfferDataItemProvider.getCreateOrderItemQualifierAnswer()).atLeastOnce();

        EasyMock.expect(fgServiceMock.addItemToFulfillmentGroup(EasyMock.isA(FulfillmentGroupItemRequest.class), EasyMock.eq(false))).andAnswer(OfferDataItemProvider.getAddItemToFulfillmentGroupAnswer()).anyTimes();
        EasyMock.expect(orderServiceMock.removeItem(EasyMock.isA(Long.class), EasyMock.isA(Long.class), EasyMock.eq(false))).andAnswer(OfferDataItemProvider.getRemoveItemFromOrderAnswer()).anyTimes();
        EasyMock.expect(orderServiceMock.save(EasyMock.isA(Order.class), EasyMock.isA(Boolean.class))).andAnswer(OfferDataItemProvider.getSaveOrderAnswer()).anyTimes();
        
        EasyMock.expect(offerServiceUtilitiesMock.orderMeetsQualifyingSubtotalRequirements(EasyMock.isA(PromotableOrder.class), EasyMock.isA(Offer.class), EasyMock.isA(HashMap.class))).andReturn(true).anyTimes();
        EasyMock.expect(offerServiceUtilitiesMock.orderMeetsSubtotalRequirements(EasyMock.isA(PromotableOrder.class), EasyMock.isA(Offer.class))).andReturn(true).anyTimes();
        
        EasyMock.expect(orderServiceMock.getAutomaticallyMergeLikeItems()).andReturn(true).anyTimes();
        EasyMock.expect(orderItemServiceMock.saveOrderItem(EasyMock.isA(OrderItem.class))).andAnswer(OfferDataItemProvider.getSaveOrderItemAnswer()).anyTimes();
        EasyMock.expect(fgItemDaoMock.save(EasyMock.isA(FulfillmentGroupItem.class))).andAnswer(OfferDataItemProvider.getSaveFulfillmentGroupItemAnswer()).anyTimes();

        EasyMock.expect(offerDaoMock.createOrderItemPriceDetailAdjustment()).andAnswer(OfferDataItemProvider.getCreateOrderItemPriceDetailAdjustmentAnswer()).anyTimes();
        EasyMock.expect(offerDaoMock.createFulfillmentGroupAdjustment()).andAnswer(OfferDataItemProvider.getCreateFulfillmentGroupAdjustmentAnswer()).anyTimes();

        EasyMock.expect(orderServiceMock.findOrderById(EasyMock.isA(Long.class))).andAnswer(new IAnswer<Order>() {

            @Override
            public Order answer() throws Throwable {
                return myOrder.get();
            }
        }).anyTimes();

        EasyMock.expect(multishipOptionServiceMock.findOrderMultishipOptions(EasyMock.isA(Long.class))).andAnswer(new IAnswer<List<OrderMultishipOption>>() {

            @Override
            public List<OrderMultishipOption> answer() throws Throwable {
                List<OrderMultishipOption> options = new ArrayList<OrderMultishipOption>();
                PromotableOrder order = dataProvider.createBasicPromotableOrder(promotableOfferUtility);
                for (FulfillmentGroup fg : order.getOrder().getFulfillmentGroups()) {
                    Address address = fg.getAddress();
                    for (FulfillmentGroupItem fgItem : fg.getFulfillmentGroupItems()) {
                        for (int j = 0; j < fgItem.getQuantity(); j++) {
                            OrderMultishipOption option = new OrderMultishipOptionImpl();
                            option.setOrder(order.getOrder());
                            option.setAddress(address);
                            option.setOrderItem(fgItem.getOrderItem());
                            options.add(option);
                        }
                    }
                }

                return options;
            }
        }).anyTimes();

        multishipOptionServiceMock.deleteAllOrderMultishipOptions(EasyMock.isA(Order.class));
        EasyMock.expectLastCall().anyTimes();
        EasyMock.expect(fgServiceMock.collapseToOneShippableFulfillmentGroup(EasyMock.isA(Order.class), EasyMock.eq(false))).andAnswer(new IAnswer<Order>() {

            @Override
            public Order answer() throws Throwable {
                Order order = (Order) EasyMock.getCurrentArguments()[0];
                order.getFulfillmentGroups().get(0).getFulfillmentGroupItems().addAll(order.getFulfillmentGroups().get(1).getFulfillmentGroupItems());
                order.getFulfillmentGroups().remove(order.getFulfillmentGroups().get(1));

                return order;
            }
        }).anyTimes();
        EasyMock.expect(fgItemDaoMock.create()).andAnswer(OfferDataItemProvider.getCreateFulfillmentGroupItemAnswer()).anyTimes();
        fgItemDaoMock.delete(EasyMock.isA(FulfillmentGroupItem.class));
        EasyMock.expectLastCall().anyTimes();
        EasyMock.expect(offerTimeZoneProcessorMock.getTimeZone(EasyMock.isA(OfferImpl.class))).andReturn(TimeZone.getTimeZone("CST")).anyTimes();

        replay();

        PromotableOrder promotableOrder = dataProvider.createBasicPromotableOrder(promotableOfferUtility);
        Order order = promotableOrder.getOrder();
        myOrder.set(promotableOrder.getOrder());
        List<PromotableCandidateFulfillmentGroupOffer> qualifiedOffers = new ArrayList<PromotableCandidateFulfillmentGroupOffer>();
        List<Offer> offers = dataProvider.createFGBasedOffer("order.subTotal.getAmount()>20", "fulfillmentGroup.address.postalCode==75244", OfferDiscountType.PERCENT_OFF);
        offers.addAll(dataProvider.createFGBasedOfferWithItemCriteria("order.subTotal.getAmount()>20", "fulfillmentGroup.address.postalCode==75244", OfferDiscountType.PERCENT_OFF, "([MVEL.eval(\"toUpperCase()\",\"test1\")] contains MVEL.eval(\"toUpperCase()\", discreteOrderItem.category.name))"));
        offers.get(1).setName("secondOffer");

        offers.addAll(dataProvider.createItemBasedOfferWithItemCriteria(
                "order.subTotal.getAmount()>20",
                OfferDiscountType.PERCENT_OFF,
                "([MVEL.eval(\"toUpperCase()\",\"test1\"), MVEL.eval(\"toUpperCase()\",\"test2\")] contains MVEL.eval(\"toUpperCase()\", discreteOrderItem.category.name))",
                "([MVEL.eval(\"toUpperCase()\",\"test1\"), MVEL.eval(\"toUpperCase()\",\"test2\")] contains MVEL.eval(\"toUpperCase()\", discreteOrderItem.category.name))"
                ));
        offerService.applyAndSaveOffersToOrder(offers, promotableOrder.getOrder());

        offers.get(0).setTotalitarianOffer(true);
        offerService.applyAndSaveFulfillmentGroupOffersToOrder(offers, promotableOrder.getOrder());

        int fgAdjustmentCount = 0;
        for (FulfillmentGroup fg : order.getFulfillmentGroups()) {
            fgAdjustmentCount += fg.getFulfillmentGroupAdjustments().size();
        }
        //The totalitarian offer that applies to both fg's is not combinable and is a worse offer than the order item offers
        // - it is therefore ignored
        //However, the second combinable fg offer is allowed to be applied.
        assertTrue(fgAdjustmentCount == 1);

        promotableOrder = dataProvider.createBasicPromotableOrder(promotableOfferUtility);
        myOrder.set(promotableOrder.getOrder());
        offers.get(2).setValue(new BigDecimal("1"));

        offerService.applyAndSaveOffersToOrder(offers, promotableOrder.getOrder());
        offerService.applyAndSaveFulfillmentGroupOffersToOrder(offers, promotableOrder.getOrder());

        fgAdjustmentCount = 0;
        order = promotableOrder.getOrder();
        for (FulfillmentGroup fg : order.getFulfillmentGroups()) {
            fgAdjustmentCount += fg.getFulfillmentGroupAdjustments().size();
        }

        //The totalitarian fg offer is now a better deal than the order item offers, therefore the totalitarian fg offer is applied
        //and the order item offers are removed
        assertTrue(fgAdjustmentCount == 2);

        int itemAdjustmentCount = 0;
        for (OrderItem item : order.getOrderItems()) {
            for (OrderItemPriceDetail detail : item.getOrderItemPriceDetails()) {
                itemAdjustmentCount += detail.getOrderItemPriceDetailAdjustments().size();
            }
        }

        //Confirm that the order item offers are removed
        assertTrue(itemAdjustmentCount == 0);
        verify();
    }

    public void testApplyAllFulfillmentGroupOffers() {
        EasyMock.expect(offerServiceUtilitiesMock.orderMeetsQualifyingSubtotalRequirements(EasyMock.isA(PromotableOrder.class), EasyMock.isA(Offer.class), EasyMock.isA(HashMap.class))).andReturn(true).anyTimes();
        EasyMock.expect(offerServiceUtilitiesMock.orderMeetsSubtotalRequirements(EasyMock.isA(PromotableOrder.class), EasyMock.isA(Offer.class))).andReturn(true).anyTimes();
        replay();

        PromotableOrder order = dataProvider.createBasicPromotableOrder(promotableOfferUtility);

        List<PromotableCandidateFulfillmentGroupOffer> qualifiedOffers = new ArrayList<PromotableCandidateFulfillmentGroupOffer>();
        List<Offer> offers = dataProvider.createFGBasedOffer("order.subTotal.getAmount()>20", "fulfillmentGroup.address.postalCode==75244", OfferDiscountType.PERCENT_OFF);
        fgProcessor.filterFulfillmentGroupLevelOffer(order, qualifiedOffers, offers.get(0));

        boolean offerApplied = fgProcessor.applyAllFulfillmentGroupOffers(qualifiedOffers, order);

        assertTrue(offerApplied);

        order = dataProvider.createBasicPromotableOrder(promotableOfferUtility);

        qualifiedOffers = new ArrayList<PromotableCandidateFulfillmentGroupOffer>();
        offers = dataProvider.createFGBasedOffer("order.subTotal.getAmount()>20", "fulfillmentGroup.address.postalCode==75244", OfferDiscountType.PERCENT_OFF);
        offers.addAll(dataProvider.createFGBasedOfferWithItemCriteria("order.subTotal.getAmount()>20", "fulfillmentGroup.address.postalCode==75244", OfferDiscountType.PERCENT_OFF, "([MVEL.eval(\"toUpperCase()\",\"test1\")] contains MVEL.eval(\"toUpperCase()\", discreteOrderItem.category.name))"));
        offers.get(1).setName("secondOffer");
        fgProcessor.filterFulfillmentGroupLevelOffer(order, qualifiedOffers, offers.get(0));
        fgProcessor.filterFulfillmentGroupLevelOffer(order, qualifiedOffers, offers.get(1));

        offerApplied = fgProcessor.applyAllFulfillmentGroupOffers(qualifiedOffers, order);

        //the first offer applies to both fulfillment groups, but the second offer only applies to one of the fulfillment groups
        assertTrue(offerApplied);
        int fgAdjustmentCount = 0;
        for (PromotableFulfillmentGroup fg : order.getFulfillmentGroups()) {
            fgAdjustmentCount += fg.getCandidateFulfillmentGroupAdjustments().size();
        }
        assertTrue(fgAdjustmentCount == 3);

        verify();
    }

    public void testFilterFulfillmentGroupLevelOffer() {
        replay();

        PromotableOrder order = dataProvider.createBasicPromotableOrder(promotableOfferUtility);

        List<PromotableCandidateFulfillmentGroupOffer> qualifiedOffers = new ArrayList<PromotableCandidateFulfillmentGroupOffer>();
        List<Offer> offers = dataProvider.createFGBasedOffer("order.subTotal.getAmount()>20", "fulfillmentGroup.address.postalCode==75244", OfferDiscountType.PERCENT_OFF);
        fgProcessor.filterFulfillmentGroupLevelOffer(order, qualifiedOffers, offers.get(0));

        //test that the valid fg offer is included
        //No item criteria, so each fulfillment group applies
        assertTrue(qualifiedOffers.size() == 2 && qualifiedOffers.get(0).getOffer().equals(offers.get(0)));

        qualifiedOffers = new ArrayList<PromotableCandidateFulfillmentGroupOffer>();
        offers = dataProvider.createFGBasedOfferWithItemCriteria("order.subTotal.getAmount()>20", "fulfillmentGroup.address.postalCode==75244", OfferDiscountType.PERCENT_OFF, "([MVEL.eval(\"toUpperCase()\",\"test1\")] contains MVEL.eval(\"toUpperCase()\", discreteOrderItem.category.name))");
        fgProcessor.filterFulfillmentGroupLevelOffer(order, qualifiedOffers, offers.get(0));

        //test that the valid fg offer is included
        //only 1 fulfillment group has qualifying items
        assertTrue(qualifiedOffers.size() == 1 && qualifiedOffers.get(0).getOffer().equals(offers.get(0)));

        qualifiedOffers = new ArrayList<PromotableCandidateFulfillmentGroupOffer>();
        offers = dataProvider.createFGBasedOfferWithItemCriteria("order.subTotal.getAmount()>20", "fulfillmentGroup.address.postalCode==75240", OfferDiscountType.PERCENT_OFF, "([MVEL.eval(\"toUpperCase()\",\"test1\"),MVEL.eval(\"toUpperCase()\",\"test2\")] contains MVEL.eval(\"toUpperCase()\", discreteOrderItem.category.name))");
        fgProcessor.filterFulfillmentGroupLevelOffer(order, qualifiedOffers, offers.get(0));

        //test that the invalid fg offer is excluded - zipcode is wrong
        assertTrue(qualifiedOffers.size() == 0);

        qualifiedOffers = new ArrayList<PromotableCandidateFulfillmentGroupOffer>();
        offers = dataProvider.createFGBasedOfferWithItemCriteria("order.subTotal.getAmount()>20", "fulfillmentGroup.address.postalCode==75244", OfferDiscountType.PERCENT_OFF, "([MVEL.eval(\"toUpperCase()\",\"test5\"),MVEL.eval(\"toUpperCase()\",\"test6\")] contains MVEL.eval(\"toUpperCase()\", discreteOrderItem.category.name))");
        fgProcessor.filterFulfillmentGroupLevelOffer(order, qualifiedOffers, offers.get(0));

        //test that the invalid fg offer is excluded - no qualifying items
        assertTrue(qualifiedOffers.size() == 0);

        verify();
    }

    public void testCouldOfferApplyToFulfillmentGroup() {
        replay();

        PromotableOrder order = dataProvider.createBasicPromotableOrder(promotableOfferUtility);
        List<Offer> offers = dataProvider.createFGBasedOffer("order.subTotal.getAmount()>20", "fulfillmentGroup.address.postalCode==75244", OfferDiscountType.PERCENT_OFF);
        boolean couldApply = fgProcessor.couldOfferApplyToFulfillmentGroup(offers.get(0), order.getFulfillmentGroups().get(0));
        //test that the valid fg offer is included
        assertTrue(couldApply);

        offers = dataProvider.createFGBasedOffer("order.subTotal.getAmount()>20", "fulfillmentGroup.address.postalCode==75240", OfferDiscountType.PERCENT_OFF);
        couldApply = fgProcessor.couldOfferApplyToFulfillmentGroup(offers.get(0), order.getFulfillmentGroups().get(0));
        //test that the invalid fg offer is excluded
        assertFalse(couldApply);

        verify();
    }

    public void testCouldOrderItemMeetOfferRequirement() {
        replay();

        PromotableOrder order = dataProvider.createBasicPromotableOrder(promotableOfferUtility);
        List<Offer> offers = dataProvider.createFGBasedOfferWithItemCriteria("order.subTotal.getAmount()>20", "fulfillmentGroup.address.postalCode==75244", OfferDiscountType.PERCENT_OFF, "([MVEL.eval(\"toUpperCase()\",\"test1\"), MVEL.eval(\"toUpperCase()\",\"test2\")] contains MVEL.eval(\"toUpperCase()\", discreteOrderItem.category.name))");
        OfferQualifyingCriteriaXref xref = offers.get(0).getQualifyingItemCriteriaXref().iterator().next();
        boolean couldApply = fgProcessor.couldOrderItemMeetOfferRequirement(xref.getOfferItemCriteria(), order.getDiscountableOrderItems().get(0));
        //test that the valid fg offer is included
        assertTrue(couldApply);

        offers = dataProvider.createFGBasedOfferWithItemCriteria("order.subTotal.getAmount()>20", "fulfillmentGroup.address.postalCode==75244", OfferDiscountType.PERCENT_OFF, "([MVEL.eval(\"toUpperCase()\",\"test5\"), MVEL.eval(\"toUpperCase()\",\"test6\")] contains MVEL.eval(\"toUpperCase()\", discreteOrderItem.category.name))");
        xref = offers.get(0).getQualifyingItemCriteriaXref().iterator().next();
        couldApply = fgProcessor.couldOrderItemMeetOfferRequirement(xref.getOfferItemCriteria(), order.getDiscountableOrderItems().get(0));
        //test that the invalid fg offer is excluded
        assertFalse(couldApply);

        verify();
    }

    public void testCouldOfferApplyToOrderItems() {
        replay();

        PromotableOrder order = dataProvider.createBasicPromotableOrder(promotableOfferUtility);

        List<PromotableOrderItem> orderItems = new ArrayList<PromotableOrderItem>();
        for (PromotableOrderItem orderItem : order.getDiscountableOrderItems()) {
            orderItems.add(orderItem);
        }

        List<Offer> offers = dataProvider.createFGBasedOfferWithItemCriteria("order.subTotal.getAmount()>20", "fulfillmentGroup.address.postalCode==75244", OfferDiscountType.PERCENT_OFF, "([MVEL.eval(\"toUpperCase()\",\"test1\"), MVEL.eval(\"toUpperCase()\",\"test2\")] contains MVEL.eval(\"toUpperCase()\", discreteOrderItem.category.name))");
        CandidatePromotionItems candidates = fgProcessor.couldOfferApplyToOrderItems(offers.get(0), orderItems);
        //test that the valid fg offer is included
        assertTrue(candidates.isMatchedQualifier() && candidates.getCandidateQualifiersMap().size() == 1);

        offers = dataProvider.createFGBasedOfferWithItemCriteria("order.subTotal.getAmount()>20", "fulfillmentGroup.address.postalCode==75244", OfferDiscountType.PERCENT_OFF, "([MVEL.eval(\"toUpperCase()\",\"test5\"), MVEL.eval(\"toUpperCase()\",\"test6\")] contains MVEL.eval(\"toUpperCase()\", discreteOrderItem.category.name))");
        candidates = fgProcessor.couldOfferApplyToOrderItems(offers.get(0), orderItems);
        //test that the invalid fg offer is excluded because there are no qualifying items
        assertFalse(candidates.isMatchedQualifier() && candidates.getCandidateQualifiersMap().size() == 1);

        verify();
    }

    public class CandidateFulfillmentGroupOfferAnswer implements IAnswer<CandidateFulfillmentGroupOffer> {

        @Override
        public CandidateFulfillmentGroupOffer answer() throws Throwable {
            return new CandidateFulfillmentGroupOfferImpl();
        }

    }

    public class FulfillmentGroupAdjustmentAnswer implements IAnswer<FulfillmentGroupAdjustment> {

        @Override
        public FulfillmentGroupAdjustment answer() throws Throwable {
            return new FulfillmentGroupAdjustmentImpl();
        }

    }

    public class CandidateItemOfferAnswer implements IAnswer<CandidateItemOffer> {

        @Override
        public CandidateItemOffer answer() throws Throwable {
            return new CandidateItemOfferImpl();
        }

    }

    public class OrderItemAdjustmentAnswer implements IAnswer<OrderItemAdjustment> {

        @Override
        public OrderItemAdjustment answer() throws Throwable {
            return new OrderItemAdjustmentImpl();
        }

    }
}
