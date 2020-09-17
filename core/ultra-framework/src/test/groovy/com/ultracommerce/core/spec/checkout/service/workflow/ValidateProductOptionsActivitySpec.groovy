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
package com.ultracommerce.core.spec.checkout.service.workflow

import com.ultracommerce.core.catalog.domain.Product
import com.ultracommerce.core.catalog.domain.ProductImpl
import com.ultracommerce.core.catalog.domain.ProductOption
import com.ultracommerce.core.catalog.domain.ProductOptionImpl
import com.ultracommerce.core.catalog.domain.ProductOptionXref
import com.ultracommerce.core.catalog.domain.ProductOptionXrefImpl
import com.ultracommerce.core.catalog.service.type.ProductOptionValidationStrategyType
import com.ultracommerce.core.catalog.service.type.ProductOptionValidationType
import com.ultracommerce.core.checkout.service.workflow.ValidateProductOptionsActivity
import com.ultracommerce.core.order.domain.DiscreteOrderItem
import com.ultracommerce.core.order.domain.DiscreteOrderItemImpl
import com.ultracommerce.core.order.domain.OrderItem
import com.ultracommerce.core.order.domain.OrderItemAttributeImpl
import com.ultracommerce.core.order.service.ProductOptionValidationService
import com.ultracommerce.core.order.service.ProductOptionValidationServiceImpl
import com.ultracommerce.core.order.service.exception.ProductOptionValidationException
import com.ultracommerce.core.order.service.exception.RequiredAttributeNotProvidedException
import com.ultracommerce.core.workflow.ActivityMessages
import org.springframework.core.env.Environment


/**
 * 
 * 
 * @author Austin Rooke
 *
 */

class ValidateProductOptionsActivitySpec extends BaseCheckoutActivitySpec {

    ProductOptionValidationService mockProductOptionValidationService
    DiscreteOrderItem orderItem
    ProductOption productOption
    ProductOptionXref productOptionXref
    Product product
    List<ProductOptionXref> productOptions
    List<OrderItem> orderItems
    Environment mockEnvironment = Mock()

    def setup() {
        orderItem = new DiscreteOrderItemImpl()
        productOption = new ProductOptionImpl()
        productOptionXref = new ProductOptionXrefImpl()
        product = new ProductImpl()
        productOptions = new ArrayList()
        orderItems = new ArrayList()

        mockProductOptionValidationService = Spy(ProductOptionValidationServiceImpl)
        mockProductOptionValidationService.environment=mockEnvironment
    }

    def "Test that exception is thrown when attributeValues for a DiscreteOrder Item when ProductOptions are required are not provided"() {
        setup:

        productOption.setRequired(true)
        productOption.setProductOptionValidationStrategyType(null)
        productOption.setAttributeName('name')
        productOptionXref.setProductOption(productOption)
        productOptions << productOptionXref

        product.setProductOptionXrefs(productOptions)
        orderItem.setProduct(product)
        orderItem.setOrderItemAttributes(new HashMap())
        orderItems << orderItem
        context.seedData.order.setOrderItems(orderItems)

        activity = new ValidateProductOptionsActivity().with {
            productOptionValidationService = mockProductOptionValidationService
            it
        }

        when: "I execute the ValidateProductOptionsActivity"
        context = activity.execute(context)

        then: "RequiredAttributeNotProvidedException is thrown"
        thrown(RequiredAttributeNotProvidedException)
    }

    def "Test that ProductOptionValidationService.validate is called when ProductOptionValidationType is provided but the ProductOptionValidationStrategy is either not provided or lower than SUBMIT_ORDER in rank"() {
        setup:

        productOption.setRequired(false)
        productOption.setAttributeName("blah")
        productOption.setProductOptionValidationStrategyType(null)
        productOption.setProductOptionValidationType(new ProductOptionValidationType("1","1"))
        productOptionXref.setProductOption(productOption)
        productOptions << productOptionXref

        product.setProductOptionXrefs(productOptions)
        orderItem.setProduct(product)
        Map orderItemAttributes = new HashMap()
        orderItemAttributes.put("blah",new OrderItemAttributeImpl())
        orderItem.setOrderItemAttributes(orderItemAttributes)
        orderItems << orderItem

        mockProductOptionValidationService.validate(_, _) >> true

        context.seedData.order.setOrderItems(orderItems)

        activity = new ValidateProductOptionsActivity().with {
            productOptionValidationService = mockProductOptionValidationService
            it
        }

        when: "I execute the ValidateProductOptionsActivity"
        context = activity.execute(context)

        then: "ProductOptionValidationService.validate() method is called once"
        1 * mockProductOptionValidationService.validate(*_)
    }

    def "Test that an ActivityMessageDTO is added to the context when a ProductOption has been provided a ProductOptionValidationType and a ProductOptionValidationStrategyType rank greater than SUBMIT_ORDER"() {
        setup:

        productOption.setRequired(false)
        productOption.setProductOptionValidationStrategyType(new ProductOptionValidationStrategyType("1", 4000, "1"))
        productOption.setProductOptionValidationType(null)
        productOptionXref.setProductOption(productOption)
        productOptions << productOptionXref

        product.setProductOptionXrefs(productOptions)
        orderItem.setProduct(product)
        orderItem.setOrderItemAttributes(new HashMap())
        orderItems << orderItem

        context.seedData.order.setOrderItems(orderItems)

        mockProductOptionValidationService.validate(*_) >> { throw new ProductOptionValidationException() }


        activity = new ValidateProductOptionsActivity().with {
            productOptionValidationService = mockProductOptionValidationService
            it
        }

        when: "I execute the ValidationProductOptionsActivity"
        context = activity.execute(context)

        then: "Context will have a new message in its ActivityMessages"
        ((ActivityMessages) context).getActivityMessages().size() == 1
    }
}
