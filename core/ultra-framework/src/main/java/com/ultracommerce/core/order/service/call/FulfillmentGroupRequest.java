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
package com.ultracommerce.core.order.service.call;

import com.ultracommerce.core.order.domain.FulfillmentOption;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.service.type.FulfillmentType;
import com.ultracommerce.profile.core.domain.Address;
import com.ultracommerce.profile.core.domain.Phone;

import java.util.ArrayList;
import java.util.List;

public class FulfillmentGroupRequest {

    protected Address address;
    protected Order order;
    protected Phone phone;
    
    /**
     * Both of these fields uses are superceded by the FulfillmentOption paradigm
     */
    @Deprecated
    protected String method;
    @Deprecated
    protected String service;
    
    protected FulfillmentOption option;
    
    protected List<FulfillmentGroupItemRequest> fulfillmentGroupItemRequests = new ArrayList<FulfillmentGroupItemRequest>();

    protected FulfillmentType fulfillmentType;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }
    
    public FulfillmentOption getOption() {
        return option;
    }
    
    public void setOption(FulfillmentOption option) {
        this.option = option;
    }

    public List<FulfillmentGroupItemRequest> getFulfillmentGroupItemRequests() {
        return fulfillmentGroupItemRequests;
    }

    public void setFulfillmentGroupItemRequests(List<FulfillmentGroupItemRequest> fulfillmentGroupItemRequests) {
        this.fulfillmentGroupItemRequests = fulfillmentGroupItemRequests;
    }

    public FulfillmentType getFulfillmentType() {
        return fulfillmentType;
    }

    public void setFulfillmentType(FulfillmentType fulfillmentType) {
        this.fulfillmentType = fulfillmentType;
    }

    /**
     * Deprecated in favor of {@link #getOption()}
     * @see {@link FulfillmentOption}
     */
    @Deprecated
    public String getMethod() {
        return method;
    }

    /**
     * Deprecated in favor of {@link #setOption(FulfillmentOption)}
     * @see {@link FulfillmentOption}
     */    
    @Deprecated
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * Deprecated in favor of {@link #getOption()}
     * @see {@link FulfillmentOption}
     */
    @Deprecated
    public String getService() {
        return service;
    }

    /**
     * Deprecated in favor of {@link #setOption(FulfillmentOption)}
     * @see {@link FulfillmentOption}
     */    
    @Deprecated
    public void setService(String service) {
        this.service = service;
    }

}
