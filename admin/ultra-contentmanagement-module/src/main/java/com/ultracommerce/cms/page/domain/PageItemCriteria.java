/*
 * #%L
 * UltraCommerce CMS Module
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
package com.ultracommerce.cms.page.domain;

import com.ultracommerce.common.copy.MultiTenantCloneable;
import com.ultracommerce.common.rule.QuantityBasedRule;

import javax.annotation.Nonnull;

/**
 * Implementations of this interface contain item rule data that is used for targeting
 * <code>Page</code>s.
 * <br>
 * <br>
 * For example, a <code>Page</code>  could be setup to only show to user's
 * who have a particular product in their cart.
 *
 * @see com.ultracommerce.core.order.service.PageCartRuleProcessor
 * @author bpolster
 */
public interface PageItemCriteria extends QuantityBasedRule,MultiTenantCloneable<PageItemCriteria> {

    /**
     * Returns the parent <code>Page</code> to which this
     * field belongs.
     *
     * @return
     */
    @Nonnull
    public Page getPage();

    /**
     * Sets the parent <code>Page</code>.
     * @param page
     */
    public void setPage(@Nonnull Page page);

    /**
     * Builds a copy of this item.   Used by the content management system when an
     * item is edited.
     *
     * @return a copy of this item
     */
    @Nonnull
    public PageItemCriteria cloneEntity();
    
}
