/*
 * #%L
 * UltraCommerce Framework Web
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
package com.ultracommerce.core.web.checkout.model;

import com.ultracommerce.core.order.domain.PersonalMessage;
import com.ultracommerce.core.order.domain.PersonalMessageImpl;

import java.io.Serializable;

/**
 * This form is used to bind multiship options in a way that doesn't require
 * the actual objects to be instantiated -- we handle that at the controller
 * level.
 * 
 * 
 */
public class MultiShipInstructionForm implements Serializable {

    private static final long serialVersionUID = 1L;
    
    protected String deliveryMessage;
    protected PersonalMessage personalMessage = new PersonalMessageImpl();
    protected Long fulfillmentGroupId;
    
    public String getDeliveryMessage() {
        return deliveryMessage;
    }
    
    public void setDeliveryMessage(String deliveryMessage) {
        this.deliveryMessage = deliveryMessage;
    }
    
    public PersonalMessage getPersonalMessage() {
        return personalMessage;
    }
    
    public void setPersonalMessage(PersonalMessage personalMessage) {
        this.personalMessage = personalMessage;
    }

    public Long getFulfillmentGroupId() {
        return fulfillmentGroupId;
    }

    public void setFulfillmentGroupId(Long id) {
        this.fulfillmentGroupId = id;
    }
    
}
