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

import java.util.List;
import java.util.Map;

import com.ultracommerce.common.structure.dto.ItemCriteriaDTO;
import com.ultracommerce.common.structure.dto.StructuredContentDTO;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.profile.core.domain.Customer;

/**
 * Created by bpolster.
 */
public class StructuredContentCartRuleProcessor extends AbstractCartRuleProcessor<StructuredContentDTO> {

    /**
     * Expects to find a valid "Customer" in the valueMap.
     * Uses the customer to locate the cart and then loops through the items in the current
     * cart and checks to see if the cart items rules are met.
     *
     * @param sc
     */
    @Override
    public boolean checkForMatch(StructuredContentDTO sc, Map<String, Object> valueMap) {
        List<ItemCriteriaDTO> itemCriterias = sc.getItemCriteriaDTOList();

        if (itemCriterias != null && itemCriterias.size() > 0) {
            Order order = lookupOrderForCustomer((Customer) valueMap.get("customer"));

            if (order == null || order.getOrderItems() == null || order.getOrderItems().size() < 1) {
                return false;
            }

            for (ItemCriteriaDTO itemCriteria : itemCriterias) {
                if (! checkItemCriteria(itemCriteria, order.getOrderItems())) {
                    // Item criteria check failed.
                    return false;
                }
            }
        }

        return true;
    }

}
