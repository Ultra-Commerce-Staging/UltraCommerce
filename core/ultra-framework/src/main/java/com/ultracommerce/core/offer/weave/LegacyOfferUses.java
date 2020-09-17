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
package com.ultracommerce.core.offer.weave;

import com.ultracommerce.core.offer.domain.Offer;
import com.ultracommerce.core.offer.domain.OfferAudit;

/**
 * This interface represents an Offer instance that has some additional, deprecated fields related to uses and maxUses
 * 
 * These fields may be showing up as required by the database as they were initially non-nullable.   If you don't have
 * custom logic using these fields, it is safe to remove the fields from your database.
 * 
 * If you do have these fields, this class supports weaving in the legacy fields back into the system.   This should 
 * be done as a last resort with a preference being to remove the columns (or at least the Non-Null restriction) from your
 * DB and refactor any code you have referencing these fields. 
 * 
 * These fields are not currently used by the codebase and have been removed in version 5.0
 * of the framework. However, for backwards compatibility reasons related to the non-nullable "uses" database field,
 * we are allowing these fields to be dynamically re-introduced via a application property. To enable
 * this behavior, add "enable.optional.offer.uses.fields=true" to the appropriate application property files.
 *
 * @author Jeff Fischer
 */
public interface LegacyOfferUses {

    /**
     * @deprecated replaced by the {@link OfferAudit} table
     */
    @Deprecated
    public int getUses() ;

    /**
     * @deprecated replaced by the {@link OfferAudit} table
     */
    @Deprecated
    public void setUses(int uses) ;

    /**
     * Returns the maximum number of times that this offer
     * can be used in the current order.
     *
     * 0 indicates unlimited usage.
     *
     * @deprecated use {@link #getMaxUsesPerOrder()} directly instead
     */
    @Deprecated
    public int getMaxUses() ;

    /**
     * Sets the maximum number of times that this offer
     * can be used in the current order.
     *
     * 0 indicates unlimited usage.
     *
     * @deprecated use {@link #setMaxUsesPerOrder(int)} directly instead
     */
    @Deprecated
    public void setMaxUses(int maxUses) ;
    
    
    @Deprecated
    /**
     * This field is not used by UC.
     * @return
     */
    public boolean isApplyDiscountToMarkedItems();

    @Deprecated
    /**
     * This field is not used by UC.
     * @return
     */
    public void setApplyDiscountToMarkedItems(boolean applyDiscountToMarkedItems);

}
