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

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.currency.util.UltraCurrencyUtils;
import com.ultracommerce.core.order.domain.FulfillmentGroup;
import com.ultracommerce.core.order.domain.FulfillmentGroupItem;
import com.ultracommerce.core.order.fulfillment.domain.BandedPriceFulfillmentOption;
import com.ultracommerce.core.pricing.domain.ShippingRate;
import com.ultracommerce.core.pricing.service.ShippingRateService;
import com.ultracommerce.core.pricing.service.fulfillment.provider.BandedFulfillmentPricingProvider;
import com.ultracommerce.core.pricing.service.workflow.type.ShippingServiceType;
import com.ultracommerce.profile.core.domain.Address;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @deprecated Superceded by functionality given by {@link BandedPriceFulfillmentOption} and {@link BandedFulfillmentPricingProvider}
 * @see {@link BandedPriceFulfillmentOption}, {@link BandedPriceFulfillmentOption}
 */
@Deprecated
public class BandedShippingModule implements ShippingModule {
    
    private static final Log LOG = LogFactory.getLog(BandedShippingModule.class);

    public static final String MODULENAME = "bandedShippingModule";

    protected String name = MODULENAME;
    protected Boolean isDefaultModule = false;

    @Resource(name = "ucShippingRateService")
    private ShippingRateService shippingRateService;

    private Map<String, String> feeTypeMapping;
    private Map<String, String> feeSubTypeMapping;

    // this will need to calculate shipping on each fulfilmentGroup in an order
    @Override
    public FulfillmentGroup calculateShippingForFulfillmentGroup(FulfillmentGroup fulfillmentGroup) {
        calculateShipping(fulfillmentGroup);
        return fulfillmentGroup;
    }

    private void calculateShipping(FulfillmentGroup fulfillmentGroup) {
        if (!isValidModuleForService(fulfillmentGroup.getService()) && !isDefaultModule()) {
            LOG.info("fulfillment group (" + fulfillmentGroup.getId() + ") with a service type of (" + fulfillmentGroup.getService() + ") is not valid for this module service type (" + getServiceName() + ")");
            return;
        }
        if (fulfillmentGroup.getFulfillmentGroupItems().size() == 0) {
            LOG.warn("fulfillment group (" + fulfillmentGroup.getId() + ") does not contain any fulfillment group items. Unable to price banded shipping");
            fulfillmentGroup.setShippingPrice(UltraCurrencyUtils.getMoney(BigDecimal.ZERO, fulfillmentGroup.getOrder().getCurrency()));
            fulfillmentGroup.setSaleShippingPrice(UltraCurrencyUtils.getMoney(BigDecimal.ZERO, fulfillmentGroup.getOrder().getCurrency()));
            fulfillmentGroup.setRetailShippingPrice(UltraCurrencyUtils.getMoney(BigDecimal.ZERO, fulfillmentGroup.getOrder().getCurrency()));
            return;
        }
        Address address = fulfillmentGroup.getAddress();
        String state = null;
        if (StringUtils.isNotBlank(address.getStateProvinceRegion())) {
            state = address.getStateProvinceRegion();
        } else if (address.getState() != null) {
            state = address.getState().getAbbreviation();
        }

        BigDecimal retailTotal = new BigDecimal(0);
        String feeType = feeTypeMapping.get(fulfillmentGroup.getMethod());
        String feeSubType = ((feeSubTypeMapping.get(state) == null) ? feeSubTypeMapping.get("ALL") : feeSubTypeMapping.get(state));

        for (FulfillmentGroupItem fulfillmentGroupItem : fulfillmentGroup.getFulfillmentGroupItems()) {
            BigDecimal price = (fulfillmentGroupItem.getRetailPrice() != null) ? fulfillmentGroupItem.getRetailPrice().getAmount().multiply(BigDecimal.valueOf(fulfillmentGroupItem.getQuantity())) : null;
            if (price == null) {
                price = fulfillmentGroupItem.getOrderItem().getRetailPrice().getAmount().multiply(BigDecimal.valueOf(fulfillmentGroupItem.getQuantity()));
            }
            retailTotal = retailTotal.add(price);
        }

        ShippingRate sr = shippingRateService.readShippingRateByFeeTypesUnityQty(feeType, feeSubType, retailTotal);
        if (sr == null) {
            throw new NotImplementedException("Shipping rate " + fulfillmentGroup.getMethod() + " not supported");
        }
        BigDecimal shippingPrice = new BigDecimal(0);
        if (sr.getBandResultPercent().compareTo(0) > 0) {
            BigDecimal percent = new BigDecimal(sr.getBandResultPercent() / 100);
            shippingPrice = retailTotal.multiply(percent);
        } else {
            shippingPrice = sr.getBandResultQuantity();
        }
        fulfillmentGroup.setShippingPrice(UltraCurrencyUtils.getMoney(shippingPrice, fulfillmentGroup.getOrder().getCurrency()));
        fulfillmentGroup.setSaleShippingPrice(fulfillmentGroup.getShippingPrice());
        fulfillmentGroup.setRetailShippingPrice(fulfillmentGroup.getSaleShippingPrice());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getFeeTypeMapping() {
        return feeTypeMapping;
    }

    public void setFeeTypeMapping(Map<String, String> feeTypeMapping) {
        this.feeTypeMapping = feeTypeMapping;
    }

    public Map<String, String> getFeeSubTypeMapping() {
        return feeSubTypeMapping;
    }

    public void setFeeSubTypeMapping(Map<String, String> feeSubTypeMapping) {
        this.feeSubTypeMapping = feeSubTypeMapping;
    }

    @Override
    public String getServiceName() {
        return ShippingServiceType.BANDED_SHIPPING.getType();
    }

    @Override
    public Boolean isValidModuleForService(String serviceName) {
        return getServiceName().equals(serviceName);
    }

    @Override
    public Boolean isDefaultModule() {
        return isDefaultModule;
    }

    @Override
    public void setDefaultModule(Boolean isDefaultModule) {
        this.isDefaultModule = isDefaultModule;
    }

}
