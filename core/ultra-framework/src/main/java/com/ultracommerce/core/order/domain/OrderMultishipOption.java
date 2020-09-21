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

import com.ultracommerce.common.copy.MultiTenantCloneable;
import com.ultracommerce.profile.core.domain.Address;

/**
 * Represents a given set of options for an OrderItem in an Order in the 
 * multiship context. This class is used to store current multiship settings
 * for an Order without having to generate the necessary FulfillmentGroups and
 * FulfillmentGroupItems. It also can be used to re-create the multiship set
 * should the Order change
 * 
 * @author Andre Azzolini (apazzolini)
 */
public interface OrderMultishipOption extends MultiTenantCloneable<OrderMultishipOption>{

    /**
     * Returns the internal id of this OrderMultishipOption
     * 
     * @return the internal id
     */
    public Long getId();

    /**
     * Sets the internal id of this OrderMultishipOption
     * 
     * @param id the internal id
     */
    public void setId(Long id);

    /**
     * Returns the Order associated with this OrderMultishipOption
     * 
     * @return the associated Order
     */
    public Order getOrder();

    /**
     * Sets the associated Order with this OrderMultishipOption
     * 
     * @param order the associated order
     */
    public void setOrder(Order order);

    /**
     * Gets the OrderItem associated with this OrderMultishipOption.
     * Note that the default Ultra implementation will produce
     * an equal number of instances of OrderMultishipOption to the
     * quantity of the OrderItem
     * 
     * @return the associated OrderItem
     */
    public OrderItem getOrderItem();

    /**
     * Sets the associated OrderItem with this OrderMultishipOption
     * 
     * @see OrderMultishipOption#getOrderItem()
     * 
     * @param orderItem the associated OrderItem
     */
    public void setOrderItem(OrderItem orderItem);

    /**
     * Gets the associated Address with this OrderMultishipOption
     * 
     * @return the associated Address
     */
    public Address getAddress();

    /**
     * Sets the associated Address with this OrderMultishipOption
     * 
     * @param address the associated Address
     */
    public void setAddress(Address address);

    /**
     * Gets the associated FulfillmentOption with this OrderMultishipOption
     * 
     * @return the associated FulfillmentOption
     */
    public FulfillmentOption getFulfillmentOption();

    /**
     * Sets the associated FulfillmentOption with this OrderMultishipOption
     * 
     * @param fulfillmentOption the associated FulfillmentOption
     */
    public void setFulfillmentOption(FulfillmentOption fulfillmentOption);

}
