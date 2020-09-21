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
package com.ultracommerce.core.store.domain;

import com.ultracommerce.common.persistence.Status;
import com.ultracommerce.profile.core.domain.Address;

import java.io.Serializable;

public interface Store extends Status, Serializable{

    public Long getId();
    public void setId(Long id);

    public String getName();
    public void setName(String name);

    public String getStoreNumber();
    public void setStoreNumber(String storeNumber);

    public Boolean getOpen();
    public void setOpen(Boolean open);

    public String getStoreHours();
    public void setStoreHours(String storeHours);
    
    public Address getAddress();
    public void setAddress(Address address);
    
    public Double getLongitude();
    public void setLongitude(Double longitude);

    public Double getLatitude();
    public void setLatitude(Double latitude);


}
