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
package com.ultracommerce.core.order.service.workflow.add;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import com.ultracommerce.common.currency.domain.UltraCurrency;
import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.core.catalog.domain.Product;
import com.ultracommerce.core.catalog.domain.ProductOption;
import com.ultracommerce.core.catalog.domain.ProductOptionXref;
import com.ultracommerce.core.catalog.domain.Sku;
import com.ultracommerce.core.catalog.service.CatalogService;
import com.ultracommerce.core.order.domain.OrderItem;
import com.ultracommerce.core.order.service.OrderItemService;
import com.ultracommerce.core.order.service.OrderService;
import com.ultracommerce.core.order.service.ProductOptionValidationService;
import com.ultracommerce.core.order.service.call.ConfigurableOrderItemRequest;
import com.ultracommerce.core.order.service.call.NonDiscreteOrderItemRequestDTO;
import com.ultracommerce.core.order.service.call.OrderItemRequestDTO;
import com.ultracommerce.core.order.service.exception.MinQuantityNotFulfilledException;
import com.ultracommerce.core.order.service.exception.RequiredAttributeNotProvidedException;
import com.ultracommerce.core.order.service.workflow.CartOperationRequest;
import com.ultracommerce.core.order.service.workflow.add.extension.ValidateAddRequestActivityExtensionManager;
import com.ultracommerce.core.order.service.workflow.service.OrderItemRequestValidationService;
import com.ultracommerce.core.workflow.ActivityMessages;
import com.ultracommerce.core.workflow.BaseActivity;
import com.ultracommerce.core.workflow.ProcessContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

@Component("ucValidateAddRequestActivity")
public class ValidateAddRequestActivity extends BaseActivity<ProcessContext<CartOperationRequest>> {

    public static final int ORDER = 1000;

    @Resource(name = "ucOrderItemRequestValidationService")
    protected OrderItemRequestValidationService orderItemRequestValidationService;
    
    @Resource(name = "ucOrderService")
    protected OrderService orderService;
    
    @Resource(name = "ucCatalogService")
    protected CatalogService catalogService;

    @Resource(name = "ucProductOptionValidationService")
    protected ProductOptionValidationService productOptionValidationService;
    
    @Resource(name = "ucOrderItemService")
    protected OrderItemService orderItemService;

    @Resource(name = "ucValidateAddRequestActivityExtensionManager")
    protected ValidateAddRequestActivityExtensionManager extensionManager;

    public ValidateAddRequestActivity() {
        setOrder(ORDER);
    }
    
    @Override
    public ProcessContext<CartOperationRequest> execute(ProcessContext<CartOperationRequest> context) throws Exception {
        ExtensionResultHolder<Exception> resultHolder = new ExtensionResultHolder<>();
        resultHolder.setResult(null);
        if (extensionManager != null && extensionManager.getProxy() != null) {
            ExtensionResultStatusType result = extensionManager.getProxy().validate(context.getSeedData(), resultHolder);

            if (!ExtensionResultStatusType.NOT_HANDLED.equals(result)) {
                if (resultHolder.getResult() != null) {
                    throw resultHolder.getResult();
                }
            }
        }

        return validate(context);
    }

    protected ProcessContext<CartOperationRequest> validate(ProcessContext<CartOperationRequest> context) {
        CartOperationRequest request = context.getSeedData();
        OrderItemRequestDTO orderItemRequestDTO = request.getItemRequest();
        Integer orderItemQuantity = orderItemRequestDTO.getQuantity();

        if (!hasQuantity(orderItemQuantity)) {
            context.stopProcess();
        } else if (orderItemQuantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        } else if (!orderItemRequestValidationService.satisfiesMinQuantityCondition(orderItemRequestDTO, context)) {
            Integer minQuantity = orderItemRequestValidationService.getMinQuantity(orderItemRequestDTO, context);
            Long productId = orderItemRequestDTO.getProductId();

            throw new MinQuantityNotFulfilledException("This item requires a minimum quantity of " + minQuantity, productId);
        } else if (request.getOrder() == null) {
            throw new IllegalArgumentException("Order is required when adding item to order");
        } else {
            // TODO: In the next minor release, refactor this to leverage OrderItemRequestValidationService. Leaving as-is to maintain the API for now.
            Product product = determineProduct(orderItemRequestDTO);
            Sku sku;
            try {
                // TODO: In the next minor release, refactor this to leverage OrderItemRequestValidationService. Leaving as-is to maintain the API for now.
                sku = determineSku(product, orderItemRequestDTO.getSkuId(), orderItemRequestDTO.getItemAttributes(),
                    (ActivityMessages) context);
            } catch (RequiredAttributeNotProvidedException e) {
                if (orderItemRequestDTO instanceof ConfigurableOrderItemRequest) {
                    // Mark the request as a configuration error and proceed with the add.
                    orderItemRequestDTO.setHasConfigurationError(Boolean.TRUE);
                    return context;
                }
                throw e;
            }

            addSkuToCart(sku, orderItemRequestDTO, product, request);

            if (!hasSameCurrency(orderItemRequestDTO, request, sku)) {
                throw new IllegalArgumentException("Cannot have items with differing currencies in one cart");
            }

            validateIfParentOrderItemExists(orderItemRequestDTO);
        }
        
        return context;
    }

    protected boolean hasQuantity(Integer orderItemQuantity) {
        return orderItemQuantity != null && orderItemQuantity != 0;
    }

    protected Product determineProduct(OrderItemRequestDTO orderItemRequestDTO) {
        Product product = null;
        // Validate that if the user specified a productId, it is a legitimate productId
        if (orderItemRequestDTO.getProductId() != null) {
            product = catalogService.findProductById(orderItemRequestDTO.getProductId());

            if (product == null) {
                throw new IllegalArgumentException("Product was specified but no matching product was found with the productId ("
                                                   + orderItemRequestDTO.getProductId() + ")");
            }
        }

        return product;
    }
    
    protected Sku determineSku(Product product, Long skuId, Map<String, String> attributeValues, ActivityMessages messages) throws RequiredAttributeNotProvidedException {
        Sku sku = null;
        
        // Check whether the sku is correct given the product options.
        sku = findMatchingSku(product, attributeValues, messages);

        if (sku == null && skuId != null) {
            sku = catalogService.findSkuById(skuId);
        }

        if (sku == null && product != null) {
            // Set to the default sku
            if (canSellDefaultSku(product)) {
                sku = product.getDefaultSku();
            } else {
                throw new RequiredAttributeNotProvidedException("Unable to find non-default sku matching given options and cannot sell default sku", null);
            }
        }

        return sku;
    }

    protected boolean canSellDefaultSku(Product product) {
        return CollectionUtils.isEmpty(product.getAdditionalSkus()) || product.getCanSellWithoutOptions();
    }
    
    protected Sku findMatchingSku(Product product, Map<String, String> attributeValues, ActivityMessages messages) throws RequiredAttributeNotProvidedException {
        Map<String, String> attributesRelevantToFindMatchingSku = new HashMap<>();

        // Verify that required product-option values were set.
        if (product != null) {
            for (ProductOptionXref productOptionXref : ListUtils.emptyIfNull(product.getProductOptionXrefs())) {
                ProductOption productOption = productOptionXref.getProductOption();
                String attributeName = productOption.getAttributeName();
                String attributeValue = attributeValues.get(attributeName);
                boolean isRequired = productOption.getRequired();
                boolean hasStrategy = productOptionValidationService.hasProductOptionValidationStrategy(productOption);
                boolean isAddOrNoneTypes = productOptionValidationService.isAddOrNoneType(productOption);

                if (shouldValidateWithException(isRequired, isAddOrNoneTypes, attributeValue, hasStrategy)) {
                    productOptionValidationService.validate(productOption, attributeValue);
                }

                if (hasStrategy && !isAddOrNoneTypes) {
                    // we need to validate; however, we will not error out
                    productOptionValidationService.validateWithoutException(productOption, attributeValue, messages);
                }

                if (productOption.getUseInSkuGeneration()) {
                    attributesRelevantToFindMatchingSku.put(attributeName, attributeValue);
                }
            }

            return findMatchingSku(product, attributesRelevantToFindMatchingSku);
        }

        return null;
    }

    protected boolean shouldValidateWithException(boolean isRequired, boolean isAddOrNoneType, String attributeValue, boolean hasStrategy) {
        return (!hasStrategy || isAddOrNoneType) && (isRequired || StringUtils.isNotEmpty(attributeValue));
    }

    protected Sku findMatchingSku(Product product, Map<String, String> attributeValuesForSku) {
        Sku matchingSku = null;
        List<Long> possibleSkuIds = new ArrayList<>();

        for (Map.Entry<String, String> entry : MapUtils.emptyIfNull(attributeValuesForSku).entrySet()) {
            possibleSkuIds = productOptionValidationService.findSkuIdsForProductOptionValues(product.getId(), entry.getKey(), entry.getValue(), possibleSkuIds);

            if (CollectionUtils.isEmpty(possibleSkuIds)) {
                break;
            }
        }

        if (CollectionUtils.isNotEmpty(possibleSkuIds)) {
            matchingSku = catalogService.findSkuById(possibleSkuIds.iterator().next());
        }

        return matchingSku;
    }

    protected void addSkuToCart(Sku sku, OrderItemRequestDTO orderItemRequestDTO, Product product, CartOperationRequest request) {
        // If we couldn't find a sku, then we're unable to add to cart.
        if (!hasSkuOrIsNonDiscreteOI(sku, orderItemRequestDTO)) {
            handleIfNoSku(orderItemRequestDTO, product);
        } else if (sku == null) {
            handleIfNonDiscreteOI(orderItemRequestDTO);
        } else if (!sku.isActive()) {
            throw new IllegalArgumentException("The requested skuId (" + sku.getId() + ") is no longer active");
        } else {
            // We know which sku we're going to add, so we can add it
            request.getItemRequest().setSkuId(sku.getId());
        }
    }

    protected boolean hasSkuOrIsNonDiscreteOI(Sku sku, OrderItemRequestDTO orderItemRequestDTO) {
        return sku != null || orderItemRequestDTO instanceof NonDiscreteOrderItemRequestDTO;
    }

    protected void handleIfNoSku(OrderItemRequestDTO orderItemRequestDTO, Product product) {
        StringBuilder sb = new StringBuilder();

        for (Entry<String, String> entry : orderItemRequestDTO.getItemAttributes().entrySet()) {
            sb.append(entry.toString());
        }

        throw new IllegalArgumentException("Could not find SKU for :" +
                                           " productId: " + (product == null ? "null" : product.getId()) +
                                           " skuId: " + orderItemRequestDTO.getSkuId() +
                                           " attributes: " + sb.toString());
    }

    protected void handleIfNonDiscreteOI(OrderItemRequestDTO orderItemRequestDTO) {
        NonDiscreteOrderItemRequestDTO ndr = (NonDiscreteOrderItemRequestDTO) orderItemRequestDTO;

        if (StringUtils.isBlank(ndr.getItemName())) {
            throw new IllegalArgumentException("Item name is required for non discrete order item add requests");
        } else if (!hasPrice(ndr)) {
            throw new IllegalArgumentException("At least one override price is required for non discrete order item add requests");
        }
    }

    protected boolean hasPrice(NonDiscreteOrderItemRequestDTO ndr) {
        return ndr.getOverrideRetailPrice() != null || ndr.getOverrideSalePrice() != null;
    }

    protected boolean hasSameCurrency(OrderItemRequestDTO orderItemRequestDTO, CartOperationRequest request, Sku sku) {
        if (orderItemRequestDTO instanceof NonDiscreteOrderItemRequestDTO || sku == null || sku.getCurrency() == null || request.getOrder().getCurrency() == null) {
            return true;
        } else {
            UltraCurrency orderCurrency = request.getOrder().getCurrency();
            UltraCurrency skuCurrency = sku.getCurrency();

            return orderCurrency.equals(skuCurrency);
        }
    }

    protected void validateIfParentOrderItemExists(OrderItemRequestDTO orderItemRequestDTO) {
        // If the user has specified a parent order item to attach this to, it must exist in this cart
        if (orderItemRequestDTO.getParentOrderItemId() != null) {
            OrderItem parent = orderItemService.readOrderItemById(orderItemRequestDTO.getParentOrderItemId());
            if (parent == null) {
                throw new IllegalArgumentException("Could not find parent order item by the given id ("
                                                   + orderItemRequestDTO.getParentOrderItemId() + ")");
            }
        }
    }
}