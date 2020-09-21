/*
 * #%L
 * UltraCommerce Profile
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
package com.ultracommerce.profile.core.domain;

import com.ultracommerce.common.copy.MultiTenantCloneable;

import java.io.Serializable;

public interface Phone extends Serializable {

    public Long getId();

    public void setId(Long id);

    public String getCountryCode();

    public void setCountryCode(String countryCode);

    public String getPhoneNumber();

    public void setPhoneNumber(String phoneNumber);

    public String getExtension();

    public void setExtension(String extension);

    public boolean isDefault();

    public void setDefault(boolean isDefault);

    public boolean isActive();

    public void setActive(boolean isActive);
}
