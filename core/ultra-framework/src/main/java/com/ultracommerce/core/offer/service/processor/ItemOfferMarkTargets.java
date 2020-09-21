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
package com.ultracommerce.core.offer.service.processor;

import com.ultracommerce.core.offer.service.discount.domain.PromotableCandidateItemOffer;
import com.ultracommerce.core.offer.service.discount.domain.PromotableOrder;
import com.ultracommerce.core.order.domain.OrderItem;


/**
 * This interface is used as a part of a template pattern in ItemOfferProcessor that allows reuse to other UC modules.
 * 
 * Changes here likely affect Subscription and AdvancedOffer modules.
 * @author bpolster
 *
 */
public interface ItemOfferMarkTargets {

    boolean markTargets(PromotableCandidateItemOffer itemOffer, PromotableOrder order, OrderItem relatedQualifier,
            boolean checkOnly);
}
