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

import com.ultracommerce.core.order.domain.BundleOrderItem;
import com.ultracommerce.core.order.domain.DiscreteOrderItemFeePrice;

import java.util.ArrayList;
import java.util.List;
public class DiscreteOrderItemRequest extends AbstractOrderItemRequest {

    protected BundleOrderItem bundleOrderItem;
    
    protected List<DiscreteOrderItemFeePrice> discreteOrderItemFeePrices = new ArrayList<DiscreteOrderItemFeePrice>();

    public DiscreteOrderItemRequest() {
        super();
    }

    public DiscreteOrderItemRequest(AbstractOrderItemRequest request) {
        setCategory(request.getCategory());
        setItemAttributes(request.getItemAttributes());
        setPersonalMessage(request.getPersonalMessage());
        setProduct(request.getProduct());
        setQuantity(request.getQuantity());
        setSku(request.getSku());
        setOrder(request.getOrder());
        setSalePriceOverride(request.getSalePriceOverride());
        setRetailPriceOverride(request.getRetailPriceOverride());
    }


    @Override
    public DiscreteOrderItemRequest clone() {
        DiscreteOrderItemRequest returnRequest = new DiscreteOrderItemRequest();
        copyProperties(returnRequest);
        returnRequest.setDiscreteOrderItemFeePrices(discreteOrderItemFeePrices);
        return returnRequest;
    }

    public BundleOrderItem getBundleOrderItem() {
        return bundleOrderItem;
    }
    
    public void setBundleOrderItem(BundleOrderItem bundleOrderItem) {
        this.bundleOrderItem = bundleOrderItem;
    }

    public List<DiscreteOrderItemFeePrice> getDiscreteOrderItemFeePrices() {
        return discreteOrderItemFeePrices;
    }

    public void setDiscreteOrderItemFeePrices(
            List<DiscreteOrderItemFeePrice> discreteOrderItemFeePrices) {
        this.discreteOrderItemFeePrices = discreteOrderItemFeePrices;
    }
}
