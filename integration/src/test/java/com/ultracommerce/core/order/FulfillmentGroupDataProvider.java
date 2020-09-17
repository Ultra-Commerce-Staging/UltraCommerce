/*
 * #%L
 * UltraCommerce Integration
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
package com.ultracommerce.core.order;

import com.ultracommerce.common.money.Money;
import com.ultracommerce.core.order.domain.FulfillmentGroupImpl;
import com.ultracommerce.core.order.fulfillment.domain.FixedPriceFulfillmentOption;
import com.ultracommerce.core.order.fulfillment.domain.FixedPriceFulfillmentOptionImpl;
import com.ultracommerce.core.pricing.service.workflow.type.ShippingServiceType;
import org.testng.annotations.DataProvider;

public class FulfillmentGroupDataProvider {

    @DataProvider(name = "basicFulfillmentGroup")
    public static Object[][] provideBasicSalesFulfillmentGroup() {
        FulfillmentGroupImpl sos = new FulfillmentGroupImpl();
        sos.setReferenceNumber("123456789");
        FixedPriceFulfillmentOption option = new FixedPriceFulfillmentOptionImpl();
        option.setPrice(new Money(0));
        sos.setFulfillmentOption(option);
        return new Object[][] { { sos } };
    }
    
    @DataProvider(name = "basicFulfillmentGroupLegacy")
    public static Object[][] provideBasicSalesFulfillmentGroupLegacy() {
        FulfillmentGroupImpl sos = new FulfillmentGroupImpl();
        sos.setReferenceNumber("123456789");
        sos.setMethod("standard");
        sos.setService(ShippingServiceType.BANDED_SHIPPING.getType());
        return new Object[][] { { sos } };
    }
}
