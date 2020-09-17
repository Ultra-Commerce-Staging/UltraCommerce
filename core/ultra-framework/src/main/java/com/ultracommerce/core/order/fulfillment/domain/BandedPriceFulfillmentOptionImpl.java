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
package com.ultracommerce.core.order.fulfillment.domain;

import com.ultracommerce.common.copy.CreateResponse;
import com.ultracommerce.common.copy.MultiTenantCopyContext;
import com.ultracommerce.common.presentation.AdminPresentationClass;
import com.ultracommerce.common.presentation.AdminPresentationCollection;
import com.ultracommerce.core.order.domain.FulfillmentOptionImpl;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * @author Phillip Verheyden
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "UC_FULFILLMENT_OPT_BANDED_PRC")
@AdminPresentationClass(friendlyName = "Banded Price Fulfillment Option")
public class BandedPriceFulfillmentOptionImpl extends FulfillmentOptionImpl implements BandedPriceFulfillmentOption {

    private static final long serialVersionUID = 1L;
    
    @OneToMany(mappedBy="option", targetEntity=FulfillmentPriceBandImpl.class)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="ucStandardElements")
    @AdminPresentationCollection(friendlyName = "BandedPriceFulfillmentOptionBands", excluded = true)
    protected List<FulfillmentPriceBand> bands = new ArrayList<FulfillmentPriceBand>();

    @Override
    public List<FulfillmentPriceBand> getBands() {
        return bands;
    }

    @Override
    public void setBands(List<FulfillmentPriceBand> bands) {
        this.bands = bands;
    }

    @Override
    public CreateResponse<BandedPriceFulfillmentOption> createOrRetrieveCopyInstance(MultiTenantCopyContext context)
            throws CloneNotSupportedException {
        CreateResponse<BandedPriceFulfillmentOption> createResponse = super.createOrRetrieveCopyInstance(context);
        if (createResponse.isAlreadyPopulated()) {
            return createResponse;
        }
        BandedPriceFulfillmentOption myClone = createResponse.getClone();

        for (FulfillmentPriceBand band : bands) {
            myClone.getBands().add(band);
        }

        return createResponse;
    }

}
