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
package com.ultracommerce.core.order.service.workflow.service;

import com.ultracommerce.core.catalog.domain.Product;
import com.ultracommerce.core.catalog.domain.Sku;
import com.ultracommerce.core.order.service.call.OrderItemRequestDTO;
import com.ultracommerce.core.order.service.exception.RequiredAttributeNotProvidedException;
import com.ultracommerce.core.order.service.workflow.CartOperationRequest;
import com.ultracommerce.core.workflow.ActivityMessages;
import com.ultracommerce.core.workflow.ProcessContext;

import java.util.Map;

public interface OrderItemRequestValidationService {

    boolean satisfiesMinQuantityCondition(OrderItemRequestDTO orderItemRequestDTO, ProcessContext<CartOperationRequest> context);

    Integer getMinQuantity(OrderItemRequestDTO orderItemRequestDTO, ProcessContext<CartOperationRequest> context);

    Product determineProduct(OrderItemRequestDTO orderItemRequestDTO);

    Sku determineSku(OrderItemRequestDTO orderItemRequestDTO, ActivityMessages messages) throws RequiredAttributeNotProvidedException;

    Sku determineSku(Product product, Long skuId, Map<String, String> attributeValues, ActivityMessages messages) throws RequiredAttributeNotProvidedException;

}
