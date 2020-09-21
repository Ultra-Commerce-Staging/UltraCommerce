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
package com.ultracommerce.core.order.service.call;


public class OrderItemRequest extends AbstractOrderItemRequest {

    protected String itemName;

    public OrderItemRequest() {
        super();
    }

    public OrderItemRequest(AbstractOrderItemRequest request) {
        setPersonalMessage(request.getPersonalMessage());
        setQuantity(request.getQuantity());
        setOrder(request.getOrder());
        setSalePriceOverride(request.getSalePriceOverride());
        setRetailPriceOverride(request.getRetailPriceOverride());
    }

    @Override
    public OrderItemRequest clone() {
        OrderItemRequest returnRequest = new OrderItemRequest();
        copyProperties(returnRequest);
        returnRequest.setItemName(itemName);
        return returnRequest;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

}
