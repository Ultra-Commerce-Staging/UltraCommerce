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
package com.ultracommerce.core.pricing.dao;

import com.ultracommerce.common.persistence.EntityConfiguration;
import com.ultracommerce.core.order.domain.FulfillmentOption;
import com.ultracommerce.core.order.fulfillment.domain.BandedPriceFulfillmentOption;
import com.ultracommerce.core.pricing.domain.ShippingRate;
import com.ultracommerce.core.pricing.domain.ShippingRateImpl;
import com.ultracommerce.core.pricing.service.FulfillmentPricingService;
import com.ultracommerce.core.pricing.service.fulfillment.provider.BandedFulfillmentPricingProvider;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;

/**
 * @deprecated Superceded in functionality by {@link BandedPriceFulfillmentOption} and {@link BandedFulfillmentPricingProvider}
 * @see {@link FulfillmentOption}, {@link FulfillmentPricingService}
 */
@Repository("ucShippingRatesDao")
@Deprecated
public class ShippingRateDaoImpl implements ShippingRateDao {

    @PersistenceContext(unitName = "ucPU")
    protected EntityManager em;

    @Resource(name = "ucEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    @Override
    public ShippingRate save(ShippingRate shippingRate) {
        return em.merge(shippingRate);
    }

    @Override
    public ShippingRate readShippingRateById(Long id) {
        return em.find(ShippingRateImpl.class, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ShippingRate readShippingRateByFeeTypesUnityQty(String feeType, String feeSubType, BigDecimal unitQuantity) {
        Query query = em.createNamedQuery("UC_READ_FIRST_SHIPPING_RATE_BY_FEE_TYPES");
        query.setParameter("feeType", feeType);
        query.setParameter("feeSubType", feeSubType);
        query.setParameter("bandUnitQuantity", unitQuantity);
        List<ShippingRate> returnedRates = query.getResultList();
        if (returnedRates.size() > 0) {
            return returnedRates.get(0);
        } else {
            return null;
        }
    }

    @Override
    public ShippingRate create() {
        return (ShippingRate) entityConfiguration.createEntityInstance(ShippingRate.class.getName());
    }
}
