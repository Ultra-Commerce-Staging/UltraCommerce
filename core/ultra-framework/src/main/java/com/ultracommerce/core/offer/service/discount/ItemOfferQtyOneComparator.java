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

import com.ultracommerce.core.offer.service.discount.domain.PromotableCandidateItemOffer;

import java.util.Comparator;

/**
 * The same as {@link ItemOfferComparator} but uses the {@link PromotableCandidateItemOffer#getPotentialSavingsQtyOne()}
 * method instead.
 * 
 * @author Andre Azzolini (apazzolini)
 */
public class ItemOfferQtyOneComparator implements Comparator<PromotableCandidateItemOffer> {
    
    public static ItemOfferQtyOneComparator INSTANCE = new ItemOfferQtyOneComparator();

    public int compare(PromotableCandidateItemOffer p1, PromotableCandidateItemOffer p2) {
        
        Integer priority1 = p1.getPriority();
        Integer priority2 = p2.getPriority();
        
        int result = priority1.compareTo(priority2);
        
        if (result == 0) {
            // highest potential savings wins
            return p2.getPotentialSavingsQtyOne().compareTo(p1.getPotentialSavingsQtyOne());
        }
        return result;
    }

}
