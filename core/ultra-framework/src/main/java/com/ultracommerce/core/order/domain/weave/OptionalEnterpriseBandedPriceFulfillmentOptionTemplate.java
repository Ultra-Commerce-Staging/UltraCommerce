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
package com.ultracommerce.core.order.domain.weave;

import com.ultracommerce.common.presentation.AdminPresentationCollection;
import com.ultracommerce.core.order.fulfillment.domain.FulfillmentPriceBand;
import com.ultracommerce.core.order.fulfillment.domain.FulfillmentPriceBandImpl;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.OneToMany;

/**
 * This class is meant as a template to provide overriding of the annotations on fields in 
 * <code>com.ultracommerce.core.order.fulfillment.domain.BandedPriceFulfillmentOptionImpl</code>.  This provides a 
 * stop gap measure to allow someone to weave in the appropriate annotations in 4.0.x without forcing a schema change on those 
 * who prefer not to use it.  This should likely be removed in 4.1 for fixed annotations on the entity itself.
 * 
 * @author Kelly Tisdell
 *
 */
@Deprecated
public abstract class OptionalEnterpriseBandedPriceFulfillmentOptionTemplate {

    @OneToMany(mappedBy = "option", targetEntity = FulfillmentPriceBandImpl.class)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "ucStandardElements")
    @AdminPresentationCollection(friendlyName = "BandedPriceFulfillmentOptionBands", excluded = true)
    protected List<FulfillmentPriceBand> bands = new ArrayList<FulfillmentPriceBand>();

}
