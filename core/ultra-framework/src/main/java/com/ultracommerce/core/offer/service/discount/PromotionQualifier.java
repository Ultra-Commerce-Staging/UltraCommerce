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
import com.ultracommerce.core.offer.domain.Offer;
import com.ultracommerce.core.offer.domain.OfferItemCriteria;

import java.io.Serializable;

/**
 * Records the usage of this item as qualifier or target of
 * the promotion.   The discount amount will be 0 if this
 * item was only used as a qualifier.
 * 
 * @author jfischer
 */
public class PromotionQualifier implements Serializable{ 
    private static final long serialVersionUID = 1L;
    
    private Offer promotion;
    private OfferItemCriteria itemCriteria;
    private int quantity;
    private int finalizedQuantity;
    private Money price;

    public Offer getPromotion() {
        return promotion;
    }
    public void setPromotion(Offer promotion) {
        this.promotion = promotion;
    }
    public OfferItemCriteria getItemCriteria() {
        return itemCriteria;
    }
    public void setItemCriteria(OfferItemCriteria itemCriteria) {
        this.itemCriteria = itemCriteria;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public int getFinalizedQuantity() {
        return finalizedQuantity;
    }
    public void setFinalizedQuantity(int finalizedQuantity) {
        this.finalizedQuantity = finalizedQuantity;
    }
    
    public void incrementQuantity(int quantity) {
        this.quantity = this.quantity + quantity;
    }
    
    public PromotionQualifier copy() {
        PromotionQualifier pq = new PromotionQualifier();
        pq.setItemCriteria(itemCriteria);
        pq.setPromotion(promotion);
        pq.setQuantity(quantity);
        pq.setFinalizedQuantity(finalizedQuantity);
        return pq;
    }
    
    public void resetQty(int qty) {
        quantity = qty;
        finalizedQuantity = qty;
    }

    public PromotionQualifier split(int splitItemQty) {
        PromotionQualifier returnQualifier = copy();
        int newQty = finalizedQuantity - splitItemQty;

        if (newQty <= 0) {
            throw new IllegalArgumentException("Splitting PromotionQualifier resulted in a negative quantity");
        }

        setFinalizedQuantity(newQty);
        setQuantity(newQty);

        returnQualifier.setQuantity(splitItemQty);
        returnQualifier.setFinalizedQuantity(splitItemQty);

        return returnQualifier;
    }

    public boolean isFinalized() {
        return quantity == finalizedQuantity;
    }

    public void setPrice(Money price) {
        this.price = price;
    }
    
    public Money getPrice() {
        return price;
    }
}
