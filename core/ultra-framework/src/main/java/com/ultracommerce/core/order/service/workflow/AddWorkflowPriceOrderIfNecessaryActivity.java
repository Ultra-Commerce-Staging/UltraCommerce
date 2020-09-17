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
package com.ultracommerce.core.order.service.workflow;

import org.apache.commons.collections.CollectionUtils;
import com.ultracommerce.core.order.dao.FulfillmentGroupItemDao;
import com.ultracommerce.core.order.domain.BundleOrderItem;
import com.ultracommerce.core.order.domain.DiscreteOrderItem;
import com.ultracommerce.core.order.domain.FulfillmentGroup;
import com.ultracommerce.core.order.domain.FulfillmentGroupItem;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.domain.OrderItem;
import com.ultracommerce.core.order.service.OrderItemService;
import com.ultracommerce.core.order.service.OrderMultishipOptionService;
import com.ultracommerce.core.order.service.OrderService;
import com.ultracommerce.core.workflow.BaseActivity;
import com.ultracommerce.core.workflow.ProcessContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

/**
 * As of Ultra version 3.1.0, saves of individual aspects of an Order (such as OrderItems and FulfillmentGroupItems) no
 * longer happen in their respective activities. Instead, we will now handle these saves in this activity exclusively.
 *
 * This provides the ability for an implementation to not require a transactional wrapper around the entire workflow and
 * instead only requires it around this particular activity. This is only recommended if there are long running steps in
 * the workflow, such as an external service call to check availability.
 *
 * @author Andre Azzolini (apazzolini)
 */
@Component("ucAddWorkflowPriceOrderIfNecessaryActivity")
public class AddWorkflowPriceOrderIfNecessaryActivity extends BaseActivity<ProcessContext<CartOperationRequest>> {

    public static final int ORDER = 5000;

    @Resource(name = "ucOrderService")
    protected OrderService orderService;

    @Resource(name = "ucOrderItemService")
    protected OrderItemService orderItemService;

    @Resource(name = "ucFulfillmentGroupItemDao")
    protected FulfillmentGroupItemDao fgItemDao;

    @Resource(name = "ucOrderMultishipOptionService")
    protected OrderMultishipOptionService orderMultishipOptionService;

    public AddWorkflowPriceOrderIfNecessaryActivity() {
        setOrder(ORDER);
    }

    @Override
    public ProcessContext<CartOperationRequest> execute(ProcessContext<CartOperationRequest> context) throws Exception {
        CartOperationRequest request = context.getSeedData();
        Order order = request.getOrder();

        // If the UpdateOrderMultishipOptionActivity identified that we should delete order item multiship options,
        // go ahead and carry out that delete here.
        if (CollectionUtils.isNotEmpty(request.getMultishipOptionsToDelete())) {
            for (Long[] pack : request.getMultishipOptionsToDelete()) {
                if (pack[1] == null) {
                    orderMultishipOptionService.deleteOrderItemOrderMultishipOptions(pack[0]);
                } else {
                    orderMultishipOptionService.deleteOrderItemOrderMultishipOptions(pack[0], pack[1].intValue());
                }
            }
        }

        // We potentially have some FulfillmentGroupItems that were identified in the FulfillmentGroupItemStrategy as
        // ones that should be deleted. Delete them here.
        if (CollectionUtils.isNotEmpty(request.getFgisToDelete())) {
            for (FulfillmentGroupItem fgi : request.getFgisToDelete()) {
                for (FulfillmentGroup fg : order.getFulfillmentGroups()) {
                    ListIterator<FulfillmentGroupItem> fgItemIter = fg.getFulfillmentGroupItems().listIterator();
                    while (fgItemIter.hasNext()) {
                        FulfillmentGroupItem fgi2 = fgItemIter.next();
                        if (fgi2 == fgi) {
                            fgItemIter.remove();
                            fgItemDao.delete(fgi2);
                        }
                    }
                }
            }
        }

        // We now need to delete any OrderItems that were marked as such, including their children, if any
        for (OrderItem oi : request.getOisToDelete()) {
            order.getOrderItems().remove(oi);

            if (oi.getParentOrderItem() != null) {
                OrderItem parentItem = oi.getParentOrderItem();
                parentItem.getChildOrderItems().remove(oi);
            }

            orderItemService.delete(oi);
        }

        // We need to build up a map of OrderItem to which FulfillmentGroupItems reference that particular OrderItem.
        // We'll also save the order item and build up a map of the unsaved items to their saved counterparts.
        Map<OrderItem, List<FulfillmentGroupItem>> oiFgiMap = new HashMap<>();
        Map<OrderItem, OrderItem> savedOrderItems = new HashMap<>();
        for (OrderItem oi : order.getOrderItems()) {
            if (oi instanceof BundleOrderItem) {
                // We first need to save the discrete order items that are part of this bundle. Once they're saved, we'll
                // mark them and remove them from this bundle.
                List<DiscreteOrderItem> doisToAdd = new ArrayList<>();
                ListIterator<DiscreteOrderItem> li = ((BundleOrderItem) oi ).getDiscreteOrderItems().listIterator();
                while (li.hasNext()) {
                    DiscreteOrderItem doi = li.next();
                    getOiFgiMap(order, oiFgiMap, doi);
                    DiscreteOrderItem savedDoi = (DiscreteOrderItem) orderItemService.saveOrderItem(doi);
                    savedOrderItems.put(doi, savedDoi);
                    li.remove();
                    doisToAdd.add(savedDoi);
                }

                // After the discrete order items are saved, we can re-add the saved versions to our bundle and then
                // save the bundle as well.
                ((BundleOrderItem) oi).getDiscreteOrderItems().addAll(doisToAdd);
                BundleOrderItem savedBoi = (BundleOrderItem) orderItemService.saveOrderItem(oi);
                savedOrderItems.put(oi, savedBoi);

                // Lastly, we'll want to go through our saved discrete order items and update the bundle that they relate
                // to to the saved version of the bundle.
                for (DiscreteOrderItem doi : savedBoi.getDiscreteOrderItems()) {
                    doi.setBundleOrderItem(savedBoi);
                }
            } else {
                getOiFgiMap(order, oiFgiMap, oi);
                savedOrderItems.put(oi, orderItemService.saveOrderItem(oi));
            }
        }

        // Now, we'll update the orderitems in the order to their saved counterparts
        ListIterator<OrderItem> li = order.getOrderItems().listIterator();
        List<OrderItem> oisToAdd = new ArrayList<>();
        while (li.hasNext()) {
            OrderItem oi = li.next();
            OrderItem savedOi = savedOrderItems.get(oi);
            oisToAdd.add(savedOi);
            li.remove();
        }
        order.getOrderItems().addAll(oisToAdd);

        for (Entry<OrderItem, List<FulfillmentGroupItem>> entry : oiFgiMap.entrySet()) {
            // Update any FulfillmentGroupItems that reference order items
            for (FulfillmentGroupItem fgi : entry.getValue()) {
                fgi.setOrderItem(savedOrderItems.get(entry.getKey()));
            }

            // We also need to update the orderItem on the request in case it's used by the caller of this workflow
            if (entry.getKey() == request.getOrderItem()) {
                request.setOrderItem(savedOrderItems.get(entry.getKey()));
            }
        }

        // We need to add the new item to the parent's child order items as well.
        updateChildOrderItem(request, order);

        // If a custom implementation needs to handle additional saves before the parent Order is saved, this method
        // can be overridden to provide that functionality.
        preSaveOperation(request);

        // Now that our collection items in our Order have been saved and the state of our Order is in a place where we
        // won't get a transient save exception, we are able to go ahead and save the order with optional pricing.
        order = orderService.save(order, request.isPriceOrder());
        request.setOrder(order);

        return context;
    }

    /**
     * Traverses the current OrderItem for a match to the parentOrderItemId.
     * If found, populates and returns true.
     *
     * @param request
     * @param orderItem
     * @return
     */
    protected void updateChildOrderItem(CartOperationRequest request, Order order) {
        boolean updated = false;
        for (OrderItem oi : order.getOrderItems()) {
            if (oi instanceof BundleOrderItem) {
                BundleOrderItem boi = (BundleOrderItem) oi;
                updated = checkAndUpdateChildren(request, boi); //check the bundle children
                if (!updated) {
                    for (OrderItem discreteOrderItem : boi.getDiscreteOrderItems()) { //check the bundle discrete items
                        if (checkAndUpdateChildren(request, discreteOrderItem)) {
                            updated = true;
                            break; //break out of the discrete items loop
                        }
                    }
                }
            } else {
                updated = checkAndUpdateChildren(request, oi);
            }
            if (updated) {
                break;
            }
        }
    }

    protected boolean checkAndUpdateChildren(CartOperationRequest request, OrderItem orderItem) {
        boolean parentUpdated = false;
        if (orderItem.getId().equals(request.getItemRequest().getParentOrderItemId())) {
            orderItem.getChildOrderItems().add(request.getOrderItem());
            parentUpdated = true;
        } else {
            if (CollectionUtils.isNotEmpty(orderItem.getChildOrderItems())) {
                for (OrderItem childOrderItem : orderItem.getChildOrderItems()) {
                    parentUpdated = checkAndUpdateChildren(request, childOrderItem);
                    if (parentUpdated) {
                        break;
                    }
                }
            }
        }
        return parentUpdated;
    }

    protected void getOiFgiMap(Order order, Map<OrderItem, List<FulfillmentGroupItem>> oiFgiMap, OrderItem oi) {
        List<FulfillmentGroupItem> fgis = new ArrayList<>();

        for (FulfillmentGroup fg : order.getFulfillmentGroups()) {
            for (FulfillmentGroupItem fgi : fg.getFulfillmentGroupItems()) {
                if (fgi.getOrderItem().equals(oi)) {
                    fgis.add(fgi);
                }
            }
        }

        oiFgiMap.put(oi, fgis);
    }

    /**
     * Intended to be overridden by a custom implementation if there is a requirement to perform additional logic or
     * saves before triggering the main Order save with pricing.
     *
     * @param request
     */
    protected void preSaveOperation(CartOperationRequest request) {
        // Ultra implementation does nothing here
    }

}
