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
package com.ultracommerce.core.pricing.service.module;

import com.ultracommerce.common.vendor.service.exception.FulfillmentPriceException;
import com.ultracommerce.core.order.domain.FulfillmentGroup;
import com.ultracommerce.core.order.domain.FulfillmentOption;
import com.ultracommerce.core.pricing.service.fulfillment.provider.FulfillmentPricingProvider;

/**
 * @deprecated Superceded by functionality given by {@link FulfillmentOption} and {@link FulfillmentPricingProvider}
 * @see {@link FulfillmentPricingProvider}, {@link FulfillmentOption}
 */
@Deprecated
public interface ShippingModule {

    public String getName();

    public void setName(String name);

    public FulfillmentGroup calculateShippingForFulfillmentGroup(FulfillmentGroup fulfillmentGroup) throws FulfillmentPriceException;
    
    public String getServiceName();
    
    public Boolean isValidModuleForService(String serviceName);
    
    public void setDefaultModule(Boolean isDefaultModule);
    
    public Boolean isDefaultModule();

}
