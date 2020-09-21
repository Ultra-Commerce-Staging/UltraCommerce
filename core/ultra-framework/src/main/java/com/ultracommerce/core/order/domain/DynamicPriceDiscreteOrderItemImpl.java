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
package com.ultracommerce.core.order.domain;

import com.ultracommerce.common.presentation.AdminPresentationClass;
import com.ultracommerce.core.catalog.domain.Sku;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

/**
 * 
 * @author jfischer
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "UC_DYN_DISCRETE_ORDER_ITEM")
@AdminPresentationClass(friendlyName = "DynamicPriceDiscreteOrderItemImpl_dynamicPriceOrderItem")
public class DynamicPriceDiscreteOrderItemImpl extends DiscreteOrderItemImpl implements DynamicPriceDiscreteOrderItem {

    private static final long serialVersionUID = 1L;

    @Override
    public void setSku(Sku sku) {
        this.sku = sku;
        this.name = sku.getName();
    }

    @Override
    public boolean updateSaleAndRetailPrices() {
        return false;
    }

}
