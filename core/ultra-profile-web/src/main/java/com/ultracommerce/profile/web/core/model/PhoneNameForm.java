/*
 * #%L
 * UltraCommerce Profile Web
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
package com.ultracommerce.profile.web.core.model;

import com.ultracommerce.profile.core.domain.Phone;

/**
 * The Form Backing Bean used by the CustomerPhoneController.  This design was chosen instead
 * of placing multiple values on the request.  This is a smaller scenario since there are not many
 * properties for the CustomerPhoneController, but to be consistent, we did not put a large amount of
 * unnecessary parameters on the request.
 * 
 * @author sconlon
 *
 */
public class PhoneNameForm {
    private Phone phone;
    private String phoneName;

    public Phone getPhone() {
        return phone;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }
}
