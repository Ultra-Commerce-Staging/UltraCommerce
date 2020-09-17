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
package com.ultracommerce.core.order.service;

import com.ultracommerce.core.order.domain.FulfillmentGroup;
import com.ultracommerce.core.order.domain.FulfillmentGroupFee;
import com.ultracommerce.core.order.domain.FulfillmentGroupItem;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.domain.OrderItem;
import com.ultracommerce.core.order.domain.OrderMultishipOption;
import com.ultracommerce.core.order.service.call.FulfillmentGroupItemRequest;
import com.ultracommerce.core.order.service.call.FulfillmentGroupRequest;
import com.ultracommerce.core.order.service.type.FulfillmentGroupStatusType;
import com.ultracommerce.core.order.service.type.FulfillmentType;
import com.ultracommerce.core.pricing.service.exception.PricingException;

import java.util.List;

public interface FulfillmentGroupService {

    public FulfillmentGroup save(FulfillmentGroup fulfillmentGroup);

    public FulfillmentGroup createEmptyFulfillmentGroup();

    public FulfillmentGroup findFulfillmentGroupById(Long fulfillmentGroupId);

    public void delete(FulfillmentGroup fulfillmentGroup);

    public FulfillmentGroup addFulfillmentGroupToOrder(FulfillmentGroupRequest fulfillmentGroupRequest, boolean priceOrder) throws PricingException;

    public FulfillmentGroup addItemToFulfillmentGroup(FulfillmentGroupItemRequest fulfillmentGroupItemRequest, boolean priceOrder) throws PricingException;

    public FulfillmentGroup addItemToFulfillmentGroup(FulfillmentGroupItemRequest fulfillmentGroupItemRequest, boolean priceOrder, boolean save) throws PricingException;

    public Order removeAllFulfillmentGroupsFromOrder(Order order, boolean priceOrder) throws PricingException;

    /**
     * Removes every fulfillment group item in every fulfillment group in the order
     * that is associated with the given orderItem. Note that it does not save the changes
     * made - instead, the caller is responsible for saving the order further down.
     *
     * @param order
     * @param orderItem
     */
    public void removeOrderItemFromFullfillmentGroups(Order order, OrderItem orderItem);

    public FulfillmentGroupFee createFulfillmentGroupFee();

    /**
     * Associates shippable FulfillmentGroupItems in the given Order such that they match the structure
     * of the OrderMultishipOptions associated with the given Order.
     *
     * @see OrderMultishipOption
     *
     * @param order
     * @return the saved order
     * @throws PricingException
     */
    public Order matchFulfillmentGroupsToMultishipOptions(Order order, boolean priceOrder) throws PricingException;

    /**
     * Collapses all of the shippable fulfillment groups in the given order to the first shippable fulfillment group
     * in the order.
     *
     * @see #matchFulfillmentGroupsToMultishipOptions(Order, boolean)
     *
     * @param order
     * @param priceOrder
     * @return the saved order
     * @throws PricingException
     */
    public Order collapseToOneShippableFulfillmentGroup(Order order, boolean priceOrder) throws PricingException;


    /**
     * Reads FulfillmentGroups whose status is not FULFILLED or DELIVERED.
     * @param start
     * @param maxResults
     * @return
     */
    public List<FulfillmentGroup> findUnfulfilledFulfillmentGroups(int start, int maxResults);

    /**
     * Reads FulfillmentGroups whose status is PARTIALLY_FULFILLED or PARTIALLY_DELIVERED.
     *
     * @param start
     * @param maxResults
     * @return
     */
    public List<FulfillmentGroup> findPartiallyFulfilledFulfillmentGroups(int start, int maxResults);

    /**
     * Returns FulfillmentGroups whose status is null, or where no processing has yet occured.
     * Default returns in ascending order according to date that the order was created.
     * @param start
     * @param maxResults
     * @return
     */
    public List<FulfillmentGroup> findUnprocessedFulfillmentGroups(int start, int maxResults);

    /**
     * Reads FulfillmentGroups by status, either ascending or descending according to the date that
     * the order was created.
     * @param status
     * @param start
     * @param maxResults
     * @param ascending
     * @return
     */
    public List<FulfillmentGroup> findFulfillmentGroupsByStatus(FulfillmentGroupStatusType status, int start, int maxResults, boolean ascending);

    /**
     * Reads FulfillmentGroups by status, ascending according to the date that
     * the order was created.
     * @param status
     * @param start
     * @param maxResults
     * @return
     */
    public List<FulfillmentGroup> findFulfillmentGroupsByStatus(FulfillmentGroupStatusType status, int start, int maxResults);

    /**
     * Determines if a fulfillment group is shippable based on its fulfillment type.
     *
     * @param fulfillmentType
     * @return
     */
    public boolean isShippable(FulfillmentType fulfillmentType);

    /**
     * Returns the first shippable fulfillment group from an order.
     *
     * @param order
     * @see #getAllShippableFulfillmentGroups(Order)
     * @see #isShippable(FulfillmentType)
     */
    public FulfillmentGroup getFirstShippableFulfillmentGroup(Order order);

    /**
     * Returns all of the shippable fulfillment groups for an order
     *
     * @see #isShippable(FulfillmentType)
     */
    public List<FulfillmentGroup> getAllShippableFulfillmentGroups(Order order);

    /**
     * Finds all FulfillmentGroupItems in the given Order that reference the given OrderItem.
     *
     * @param order
     * @param orderItem
     * @return the list of related FulfillmentGroupItems
     */
    public List<FulfillmentGroupItem> getFulfillmentGroupItemsForOrderItem(Order order, OrderItem orderItem);

    /**
     * @param order
     * @return
     */
    Integer calculateNumShippableFulfillmentGroups(Order order);


}
