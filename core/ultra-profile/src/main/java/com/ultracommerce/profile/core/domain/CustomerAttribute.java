/*
 * #%L
 * UltraCommerce Profile
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
package com.ultracommerce.profile.core.domain;

import com.ultracommerce.common.copy.MultiTenantCloneable;
import com.ultracommerce.common.value.ValueAssignable;


/**
 * Implementations of this interface are used to hold data about a Customers Attributes.
 * <br>
 * For high volume sites, you should consider extending the UC Customer entity instead of
 * relying on custom attributes as the extension mechanism is more performant under load.
 *
 * @see {@link CustomerAttributeImpl}, {@link Customer}
 * @author bpolster
 *
 */
public interface CustomerAttribute extends ValueAssignable<String>, MultiTenantCloneable<CustomerAttribute> {

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Long getId();

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId(Long id);

    /**
     * Gets the associated customer.
     *
     * @return the customer
     */
    public Customer getCustomer();

    /**
     * Sets the associated customer.
     *
     * @param customer
     */
    public void setCustomer(Customer customer);
}
