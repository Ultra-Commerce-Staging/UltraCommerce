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
package com.ultracommerce.core.pricing.service.fulfillment;

import com.ultracommerce.core.order.domain.FulfillmentGroup;
import com.ultracommerce.profile.core.domain.Address;

/**
 * Default implementation of {@link FulfillmentLocationResolver} that stores a
 * single Address. Useful for businesses that do not have a complicated warehouse solution
 * and fulfill from a single location.
 * 
 * @author Phillip Verheyden
 */
public class SimpleFulfillmentLocationResolver implements FulfillmentLocationResolver {

    protected Address address;

    @Override
    public Address resolveLocationForFulfillmentGroup(FulfillmentGroup group) {
        return address;
    }

    public Address getAddress() {
        return address;
    }
    
    public void setAddress(Address address) {
        this.address = address;
    }

}
