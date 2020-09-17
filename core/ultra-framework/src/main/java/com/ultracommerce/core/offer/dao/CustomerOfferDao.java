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
package com.ultracommerce.core.offer.dao;

import com.ultracommerce.core.offer.domain.CustomerOffer;
import com.ultracommerce.profile.core.domain.Customer;

import java.util.List;

public interface CustomerOfferDao {

    public CustomerOffer readCustomerOfferById(Long customerOfferId);

    public List<CustomerOffer> readCustomerOffersByCustomer(Customer customer);

    public CustomerOffer save(CustomerOffer customerOffer);

    public void delete(CustomerOffer customerOffer);

    public CustomerOffer create();
}
