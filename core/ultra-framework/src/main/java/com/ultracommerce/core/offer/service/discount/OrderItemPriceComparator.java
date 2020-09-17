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
package com.ultracommerce.core.offer.service.discount;

import com.ultracommerce.common.money.Money;
import com.ultracommerce.core.offer.service.discount.domain.PromotableOrderItem;

import java.util.Comparator;

/**
 * 
 * @author jfischer
 *
 */
public class OrderItemPriceComparator implements Comparator<PromotableOrderItem> {
    
    private boolean applyToSalePrice = false;
    
    public OrderItemPriceComparator(boolean applyToSalePrice) {
        this.applyToSalePrice = applyToSalePrice;
    }

    public int compare(PromotableOrderItem c1, PromotableOrderItem c2) {
        
        Money price = c1.getPriceBeforeAdjustments(applyToSalePrice);
        Money price2 = c2.getPriceBeforeAdjustments(applyToSalePrice);
        
        // highest amount first
        return price2.compareTo(price);
    }

}
