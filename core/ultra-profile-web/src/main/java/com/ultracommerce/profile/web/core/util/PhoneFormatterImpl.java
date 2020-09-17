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
package com.ultracommerce.profile.web.core.util;

import org.apache.commons.lang.StringUtils;
import com.ultracommerce.profile.core.domain.Phone;
import org.springframework.stereotype.Service;

@Service("ucPhoneFormatter")
public class PhoneFormatterImpl implements PhoneFormatter {
    public void formatPhoneNumber(Phone phone) {
        if(phone != null && !StringUtils.isEmpty(phone.getPhoneNumber())){
            phone.setPhoneNumber(formatTelephoneNumber(phone.getPhoneNumber()));
        }
    }

    private String formatTelephoneNumber(String pNumber) {
        if (pNumber == null) {
            return null;
        }

        String number = pNumber.replaceAll("\\D", "");

        if (number.length() == 0) {
            return null;
        }

        if (number.length() > 10) {
            number = number.substring(0, 10);
        }

        StringBuffer newNumber = new StringBuffer(number);

        if (newNumber.length() == 10) {
            newNumber.insert(6, "-");
            newNumber.insert(3, "-");
        }

        return newNumber.toString();
    }
}
